package serialize.compress;

import com.hadoop.compression.lzo.*;
import com.hadoop.compression.lzo.LzoCodec;
import com.hadoop.mapreduce.LzoTextInputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.List;

/*
 * 读取hdfs上未压缩的文件，进行lzo压缩并且增加索引，使得符合lzop格式，保存到hdfs；
 * 开启一个MapReduce任务，input是上面产生的lzop压缩文件，output通过lzop的压缩
 * 
 * 命令行参数：
 * input.txt lzoutput
 * 
 * 
 * yarn jar compress.jar  serialize.compress.LzopMapReduce input.txt lzopout
 * 
 */

public class LzopMapReduce extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		int res = ToolRunner.run(conf, new LzopMapReduce(), args);
		if (res != 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("LzopMapReduce is done !");
		}
		System.exit(res);
	}

	public static Path compressAndIndex(Path file, Configuration conf) throws IOException {

		Configuration tmpConfig = new Configuration(conf);
		// tmpConfig.setLong("dfs.block.size", 1280000);
		tmpConfig.setInt(LzoCodec.LZO_BUFFER_SIZE_KEY, 512);

		Path compressedFile = LzopFileReadWrite.compress(file, tmpConfig);

		compressedFile.getFileSystem(tmpConfig).delete(new Path(compressedFile.toString() + LzoIndex.LZO_INDEX_SUFFIX),
				false);
		// 生成lzop的index文件
		new LzoIndexer(tmpConfig).index(compressedFile);

		LzoIndex index = LzoIndex.readIndex(compressedFile.getFileSystem(tmpConfig), compressedFile);
		for (int i = 0; i < index.getNumberOfBlocks(); i++) {
			System.out.println("block[" + i + "] = " + index.getPosition(i));
		}

		// Job job = new Job(conf);
		Job job = Job.getInstance(conf, "compressAndIndex");
		job.setInputFormatClass(LzoTextInputFormat.class);
		LzoTextInputFormat inputFormat = new LzoTextInputFormat();
		TextInputFormat.setInputPaths(job, compressedFile);

		List<InputSplit> is = inputFormat.getSplits(job);

		System.out.println("input splits = " + is.size());

		return compressedFile;
	}

	@Override
	public int run(String[] args) throws Exception {
		// Configuration conf = new Configuration();
		
		if (args.length != 2) {
			System.err.println("Usage: LzopMapReduce <in> <out>");
			ToolRunner.printGenericCommandUsage(System.err);
			System.exit(2);
		}

		Path inputFile = new Path(args[0]);
		Path compressedInputFile = compressAndIndex(inputFile, getConf());
		Path outputFile = new Path(args[1]);

		FileSystem hdfs = outputFile.getFileSystem(getConf());

		hdfs.delete(outputFile, true);

		Job job = Job.getInstance(getConf(), "LzopMapReduce");
		job.setJarByClass(LzopMapReduce.class);

		// MapReduce2 ( YARN )
		getConf().setBoolean("mapreduce.map.output.compress", true);
		getConf().setClass("mapreduce.map.output.compress.codec", LzopCodec.class, CompressionCodec.class);

		getConf().setBoolean("mapreduce.output.fileoutputformat.compress", true);
		getConf().setClass("mapreduce.output.fileoutputformat.compress.codec", LzopCodec.class, CompressionCodec.class);
		getConf().set("mapreduce.output.fileoutputformat.compress.type", "BLOCK");

		job.setMapperClass(Mapper.class);
		job.setReducerClass(Reducer.class);

		job.setInputFormatClass(LzoTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, compressedInputFile);
		FileOutputFormat.setOutputPath(job, outputFile);

		return job.waitForCompletion(true) ? 0 : 1;
	}

}

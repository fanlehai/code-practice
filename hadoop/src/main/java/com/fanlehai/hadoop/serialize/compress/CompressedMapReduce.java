package com.fanlehai.hadoop.serialize.compress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



/*
 * 
 * 使用MapReduce默认流程来用指定压缩算法，压缩map输出数据和输出文件；
 * 
 * 命令行参数：
 * compress mrcompress org.apache.hadoop.io.compress.SnappyCodec
 * 
 * compress mrcompress com.hadoop.compression.lzo.LzopCodec
 * 
 */


public class CompressedMapReduce extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		int res = ToolRunner.run(conf, new CompressedMapReduce(), args);
		if (res != 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("CompressedMapReduce is done !");
		}
		System.exit(res);
		
	}

	@Override
	public int run(String[] args) throws Exception {
		Path inputFile = new Path(args[0]);
		Path outputFile = new Path(args[1]);
		
		FileSystem.get(getConf()).delete(outputFile, true);

		FileSystem hdfs = outputFile.getFileSystem(getConf());

		hdfs.delete(outputFile, true);

		Class<?> codecClass = Class.forName(args[2]);

		// MapReduce1
		/*getConf().setBoolean("mapred.output.compress", true);
		getConf().setClass("mapred.output.compression.codec", codecClass, CompressionCodec.class);

		getConf().setBoolean("mapred.compress.map.output", true);
		getConf().setClass("mapred.map.output.compression.codec", codecClass, CompressionCodec.class);
		getConf().set("mapred.output.compression.type", "BLOCK");*/

		// MapReduce2 ( YARN )
		getConf().setBoolean("mapreduce.map.output.compress", true);
		getConf().setClass("mapreduce.map.output.compress.codec", codecClass, CompressionCodec.class);
		
		
		getConf().setBoolean("mapreduce.output.fileoutputformat.compress", true);
		getConf().setClass("mapreduce.output.fileoutputformat.compress.codec",codecClass,CompressionCodec.class);
		getConf().set("mapreduce.output.fileoutputformat.compress.type", "BLOCK");
		
		
		// Job job = new Job(conf);
		Job job = Job.getInstance(getConf(), "CompressedMapReduce");
		job.setJarByClass(CompressedMapReduce.class);

		job.setMapperClass(Mapper.class);
		job.setReducerClass(Reducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, inputFile);
		FileOutputFormat.setOutputPath(job, outputFile);

		return job.waitForCompletion(true) ? 0 : 1;
	}

}

package serialize.sequencefile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
 * 读取压缩的sequencefile(FileWriteCompressDriver输出的文件)，输出为文本文件
 * 编译此文件并且打包成jar : write.jar
 * yarn jar write.jar serialize.sequencefile.FileReadCompressDriver test1.snappy sequence/read
 * 注意输入文件一定要有后缀，此处是snappy后缀，这样hadoop根据后缀名就可以调用相应的解码，不然hadoop是不解码的
 */


public class FileReadCompressDriver extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new FileWriteCompressDriver(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("SequenceFileRead is done !");
		}
	}

	public int run(String[] args) throws Exception {
		
		if (args.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		Job job = Job.getInstance(super.getConf(), "FileReadCompressDriver");

		job.setJarByClass(serialize.sequencefile.FileReadCompressDriver.class);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		
		job.setMapperClass(FileMap.class);
		
		job.setNumReduceTasks(0);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		// 注意此处要用SequenceFileOutputFormat，不然textoutput会乱码，用hdfs dfs -text 查看
		LazyOutputFormat.setOutputFormatClass(job, SequenceFileOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}
	
	private void printUsage() {
		System.err.println("Usage: FileReadCompressDriver <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

}

package muiltioutput;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import smallfile.WordCountCombine;
import smallfile.WordCountMap;
import smallfile.WordCountReduce;
import smallfile.WourdCount;


/*
 * 默认一个reduce的数据都写到一个文件中，此例子可以使得一个reduce写到多个文件中
 * 
 * 使用案例：当一个文件中有各个年份的数据，可以用此方式把相同年份的数据写到同一个文件中，充分发挥分布式性能
 */


public class FileWriteDirver extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		System.setProperty("hadoop.home.dir", "/Users/liuhai/lib/hadoop/hadoop-2.7.2");
		int res = ToolRunner.run(new Configuration(), new FileWriteDirver(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("FileWriteDirver is done !");
		}
	}

	public int run(String[] args) throws Exception {
		Configuration conf = new Configuration();
		GenericOptionsParser parser = new GenericOptionsParser(conf, args);
		String[] otherArgs = parser.getRemainingArgs();
		if (otherArgs.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(otherArgs[1]), true);
		
		Job job = Job.getInstance(conf, "FileWriteDirver");
		job.setJarByClass(FileWriteDirver.class);
		

		job.setReducerClass(FileWriteReduce.class);
		job.setOutputValueClass(Text.class);
		LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}
	
	private void printUsage() {
		System.err.println("Usage: FileWriteDirver <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

}

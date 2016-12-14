package muiltioutput;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


/*
 * 默认一个reduce的数据都写到一个文件中，此例子可以使得一个reduce写到多个文件中
 * 
 * 使用案例：当一个文件中有各个年份的数据，可以用此方式把相同年份的数据写到同一个文件中，充分发挥分布式性能
 */

public class FileWriteDirver extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		
		int res = ToolRunner.run(conf, new FileWriteDirver(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("FileWriteDirver is done !");
		}

		System.exit(res);
	}

	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		Job job = Job.getInstance(super.getConf(), "FileWriteDirver");

		job.setJarByClass(FileWriteDirver.class);

		job.setReducerClass(FileWriteReduce.class);
		job.setOutputValueClass(Text.class);
		LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	private void printUsage() {
		System.err.println("Usage: FileWriteDirver <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

}

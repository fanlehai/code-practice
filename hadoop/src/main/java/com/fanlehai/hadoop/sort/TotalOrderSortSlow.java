package com.fanlehai.hadoop.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class TotalOrderSortSlow extends Configured implements Tool {

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		int res = ToolRunner.run(conf, new TotalOrderSortSlow(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("TotalOrderSortSlow is done !");
		}

		System.exit(res);
	}

	public int run(String[] args) throws Exception {
		// yarn jar sort.jar sort.TotalOrderSort /stackover/Users.xml
		// /user/liuhai/stackover-sort-users 0.1
		// yarn jar sort.jar sort.TotalOrderSortSlow /stackover/Users.xml
		// /user/liuhai/stackover-sort-users-slow

		if (args.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		Job job = Job.getInstance(super.getConf(), "TotalOrderSortSlow");

		job.setJarByClass(com.fanlehai.hadoop.sort.TotalOrderSortSlow.class);
		// TODO: specify a mapper
		job.setMapperClass(LastAccessDateMap.class);
		// TODO: specify a reducer
		job.setReducerClass(ValueReduce.class);

		job.setNumReduceTasks(1);

		// TODO: specify output types
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// TODO: specify input and output DIRECTORIES (not files)
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	private void printUsage() {
		System.err.println("Usage: TotalOrderSortSlow <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

}

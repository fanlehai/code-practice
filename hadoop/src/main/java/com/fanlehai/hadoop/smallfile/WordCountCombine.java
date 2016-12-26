package com.fanlehai.hadoop.smallfile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
 * 合并小文件，减少map数量，减少job作业时间
 * 
 * yarn jar word.jar smallfile.WordCountCombine sequence wordcombine
 */

public class WordCountCombine extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new WordCountCombine(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("WordCountCombine is done !");
		}
	}

	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		Job job = Job.getInstance(super.getConf(), "WordCountCombine");

		job.setJarByClass(WourdCount.class);

		// 多个小文件用这个格式，可以减少map数量，减少job作业时间
		// CombineSequenceFileInputFormat这个是针对sequencefile的
		job.setInputFormatClass(CombineTextInputFormat.class);

		job.setMapperClass(WordCountMap.class);

		job.setReducerClass(WordCountReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// job.setOutputFormatClass(SequenceFileOutputFormat.class);
		// LazyOutputFormat.setOutputFormatClass(job,
		// SequenceFileOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	private void printUsage() {
		System.err.println("Usage: WordCountCombine <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

}

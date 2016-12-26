package com.fanlehai.hadoop.sort.secondary;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


/*
 * mapreduce对key进行排序，但是value是不保证有序的。
 * 
 * 二次排序，对value也进行排序
 * 
 * yarn jar sort.jar sort.secondary.SecondarySort -D mapreduce.job.reduces=1 InputResource/sort/input.txt outsort
 * 
 */

public class SecondarySort extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		int res = ToolRunner.run(conf, new SecondarySort(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("SecondarySort is done !");
		}

		System.exit(res);
	}

	@Override
	public int run(String[] args) throws Exception {
		
		if (args.length != 2) {
			System.err.println("Usage: SecondarySort <in> <out>");
			ToolRunner.printGenericCommandUsage(System.err);
			System.exit(2);
		}

		
		Job job = Job.getInstance(getConf(), "SecondarySort");
		job.setJarByClass(com.fanlehai.hadoop.sort.secondary.SecondarySort.class);

		job.setMapperClass(MapperSort.class);
		
		job.setReducerClass(ReducerSort.class);

		job.setPartitionerClass(FirstPartitioner.class);
		job.setGroupingComparatorClass(GroupComparator.class);
		
		job.setOutputKeyClass(KeyComparator.class);
		job.setOutputValueClass(Text.class);
		
		job.setInputFormatClass(TextInputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

}

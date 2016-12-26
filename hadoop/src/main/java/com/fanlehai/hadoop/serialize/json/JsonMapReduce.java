package com.fanlehai.hadoop.serialize.json;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/*
 * 
 * 针对hadoop输入输入文件格式是json，并且每个object是一行
 * 
 * 
 * 
 */


public final class JsonMapReduce extends Configured implements Tool {

	public static class Map extends Mapper<LongWritable, MapWritable, Text, Text> {

		@Override
		protected void map(LongWritable key, MapWritable value, Context context)
				throws IOException, InterruptedException {

			for (java.util.Map.Entry<Writable, Writable> entry : value.entrySet()) {
				context.write((Text) entry.getKey(), (Text) entry.getValue());
			}
		}
	}

	public static void main(String... args) throws Exception {
		Configuration conf = new Configuration();

		int res = ToolRunner.run(conf, new JsonMapReduce(), args);
		
		if (res != 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("JsonMapReduce is done !");
		}
		
		System.exit(res);
	}

	@Override
	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println("Usage: JsonMapReduce <in> <out>");
			ToolRunner.printGenericCommandUsage(System.err);
			System.exit(2);
		}

		String input = args[0];
		String output = args[1];

		Job job = Job.getInstance(getConf(), "JsonMapReduce");
		job.setJarByClass(JsonMapReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapperClass(Map.class);
		
		// json输入格式，未压缩，一行一个json的object
		job.setInputFormatClass(JsonInputFormat.class);
		
		// json输入格式，lzo压缩，一行一个json的object
		//job.setInputFormatClass(LzoJsonInputFormat.class);

		job.setNumReduceTasks(0);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		Path outPath = new Path(output);
		FileOutputFormat.setOutputPath(job, outPath);
		outPath.getFileSystem(getConf()).delete(outPath, true);

		return job.waitForCompletion(true) ? 0 : 1;
	}
}

package com.fanlehai.hadoop.serialize.thrift;

import java.io.IOException;
import java.util.StringTokenizer;

import com.twitter.elephantbird.mapreduce.io.ThriftConverter;
import com.twitter.elephantbird.util.HadoopCompat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.twitter.elephantbird.mapreduce.input.MultiInputFormat;
import com.twitter.elephantbird.mapreduce.io.ThriftWritable;
import com.twitter.elephantbird.mapreduce.output.LzoThriftB64LineOutputFormat;
import com.twitter.elephantbird.mapreduce.output.LzoThriftBlockOutputFormat;

/*
 * 
 * 生成simple_age的thrift的java类(Age.java)：
 * thrift -out src/main/java/ --gen java src/main/resources/thrift/simple_age.thrift
 * 
 * yarn jar hadoop.jar com.fanlehai.hadoop.serialize.thrift.ThriftMRExample -Dthrift.test=lzoOut thi tho
 * 
 * 
 * yarn jar hadoop.jar com.fanlehai.hadoop.serialize.thrift.ThriftMRExample -Dthrift.test=lzoIn tho/part-m-00000.lzo pbtextout
 * 
 * 
 * yarn jar hadoop.jar com.fanlehai.hadoop.serialize.thrift.ThriftMRExample -Dthrift.test=sort thi thsorto
 * 
 * 
 */



/**
 * -Dthrift.test=lzoOut : takes text files with name and age on each line as
 * input and writes to lzo file with Thrift serilized data. <br>
 * -Dthrift.test=lzoIn : does the reverse. <br>
 * <br>
 *
 * -Dthrift.test.format=Block (or B64Line) to test different formats. <br>
 * 
 */

public class ThriftMRExample extends Configured implements Tool {

	private ThriftMRExample() {
	}

	public static class TextMapper extends Mapper<LongWritable, Text, NullWritable, ThriftWritable<Age>> {
		ThriftWritable<Age> tWritable = ThriftWritable.newInstance(Age.class);
		Age age = new Age();

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer line = new StringTokenizer(value.toString(), " ");
			
			if (line.hasMoreTokens() && age.setName(line.nextToken()) != null 
					&& line.hasMoreTokens() && age.setAge(Integer.parseInt(line.nextToken())) != null) {
				tWritable.set(age);
				context.write(null, tWritable);
			}
		}
	}

	public int runTextToLzo(String[] args, Configuration conf) throws Exception {
		Job job = Job.getInstance(conf);
		job.setJobName("Thrift Example : Text to LzoB64Line");

		job.setJarByClass(getClass());
		job.setMapperClass(TextMapper.class);
		job.setNumReduceTasks(0);

		job.setInputFormatClass(TextInputFormat.class);
		if (conf.get("thrift.test.format", "B64Line").equals("Block")) {
			LzoThriftBlockOutputFormat.setClassConf(Age.class, HadoopCompat.getConfiguration(job));
			job.setOutputFormatClass(LzoThriftBlockOutputFormat.class);
		} else { // assume B64Line
			LzoThriftB64LineOutputFormat.setClassConf(Age.class, HadoopCompat.getConfiguration(job));
			job.setOutputFormatClass(LzoThriftB64LineOutputFormat.class);
		}

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static class LzoMapper extends Mapper<LongWritable, ThriftWritable<Age>, Text, Text> {
		@Override
		protected void map(LongWritable key, ThriftWritable<Age> value, Context context)
				throws IOException, InterruptedException {
			Age age = value.get();
			context.write(null, new Text(age.getName() + "\t" + age.getAge()));
		}
	}

	public int runLzoToText(String[] args, Configuration conf) throws Exception {
		Job job = Job.getInstance(conf);
		job.setJobName("Thrift Example : LzoB64Line to Text");

		job.setJarByClass(getClass());
		job.setMapperClass(LzoMapper.class);
		job.setNumReduceTasks(0);

		// input format is same for both B64Line and Block formats
		MultiInputFormat.setInputFormatClass(Age.class, job);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static class SortMapper extends Mapper<LongWritable, Text, Text, ThriftWritable<Age>> {
		ThriftWritable<Age> tWritable = ThriftWritable.newInstance(Age.class);
		Age age = new Age();

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer line = new StringTokenizer(value.toString(), " ");

			if (line.hasMoreTokens() && age.setName(line.nextToken()) != null && line.hasMoreTokens()
					&& age.setAge(Integer.parseInt(line.nextToken())) != null) {
				tWritable.set(age);
				context.write(new Text(age.getName()), tWritable);
			}
		}
	}

	public static class SortReducer extends Reducer<Text, ThriftWritable<Age>, Text, Text> {
		ThriftConverter<Age> converter = ThriftConverter.newInstance(Age.class);

		@Override
		protected void reduce(Text key, Iterable<ThriftWritable<Age>> values, Context context)
				throws IOException, InterruptedException {
			for (ThriftWritable<Age> value : values) {
				/*
				 * setConverter() before get() is required since 'value' object
				 * was created by MR with default ThriftWritable's default
				 * constructor, as result object does not know its runtime
				 * Thrift class.
				 */
				value.setConverter(converter);
				context.write(null, new Text(value.get().getName() + "\t" + value.get().getAge()));
			}
		}
	}

	int runSorter(String[] args, Configuration conf) throws Exception {
		// A more complete example with reducers. Tests ThriftWritable as
		// map output value class.
		Job job = Job.getInstance(conf);
		job.setJobName("Thift Example : ThriftWritable as Map output class");

		job.setJarByClass(getClass());
		job.setMapperClass(SortMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(ThriftWritable.class);

		job.setReducerClass(SortReducer.class);
		job.setNumReduceTasks(1);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		int res = ToolRunner.run(conf, new ThriftMRExample(), args);
		if (res != 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("ThriftMRExample is done !");
		}
		System.exit(res);

	}

	@Override
	public int run(String[] args) throws Exception {

		ThriftMRExample runner = new ThriftMRExample();

		if (args.length != 2) {
			System.out.println("Usage: hadoop jar path/to/this.jar " + runner.getClass() + " <input dir> <output dir>");
			System.exit(1);
		}
		
		FileSystem.get(getConf()).delete(new Path(args[1]), true);

		String test = getConf().get("thrift.test", "lzoIn");

		int ret = 0;
		if (test.equals("lzoIn")) {
			ret = runner.runLzoToText(args, getConf());
		} else if (test.equals("lzoOut")) {
			ret = runner.runTextToLzo(args, getConf());
		} else if (test.equals("sort")) {
			ret = runner.runSorter(args, getConf());
		} else {
			throw new Exception("thrift.test  is null");
		}
		
		return ret;
	}
}

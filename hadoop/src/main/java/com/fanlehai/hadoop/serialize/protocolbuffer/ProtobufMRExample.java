package com.fanlehai.hadoop.serialize.protocolbuffer;

import java.io.IOException;
import java.util.StringTokenizer;

import com.twitter.elephantbird.mapreduce.io.ProtobufConverter;
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

import com.fanlehai.hadoop.serialize.protocolbuffer.NameAge.Age;
import com.twitter.elephantbird.mapreduce.input.MultiInputFormat;
import com.twitter.elephantbird.mapreduce.io.ProtobufWritable;
import com.twitter.elephantbird.mapreduce.output.LzoProtobufB64LineOutputFormat;
import com.twitter.elephantbird.mapreduce.output.LzoProtobufBlockOutputFormat;

/*
 * 生成NameAge的protobuf的java类(NameAge.java)：
 * protoc --java_out=src/main/java/  src/main/resources/protobuf/age.proto
 * 
 * yarn jar hadoop.jar com.fanlehai.hadoop.serialize.protocolbuffer.ProtobufMRExample -Dproto.test=lzoOut \
 *  -Dproto.test.format=[Block,B64Line] age.txt pbo
 * 
 *  yarn jar hadoop.jar com.fanlehai.hadoop.serialize.protocolbuffer.ProtobufMRExample -Dproto.test=lzoIn \
 *   pbo/part-m-00000.lzo pbtextout
 * 
 * 
 * yarn jar hadoop.jar com.fanlehai.hadoop.serialize.protocolbuffer.ProtobufMRExample -Dproto.test=sort \
 *   age.txt pbsorto
 *   
 *   
 */

/**
 * -Dproto.test=lzoOut : takes text files with name and age on each line as
 * input and writes to lzo file with Protobuf serilized data.
 * 
 * -Dproto.test=lzoIn : does the reverse.
 *
 * -Dproto.test.format=Block (or B64Line) to test different formats.
 */

public class ProtobufMRExample extends Configured implements Tool {
	// This is intentionally very similar to ThriftMRExample.

	private ProtobufMRExample() {
	}

	public static class TextMapper extends Mapper<LongWritable, Text, NullWritable, ProtobufWritable<Age>> {
		ProtobufWritable<Age> protoWritable = ProtobufWritable.newInstance(Age.class);

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			// StringTokenizer line = new StringTokenizer(value.toString(),
			// "\t\r\n");
			StringTokenizer line = new StringTokenizer(value.toString(), " ");
			String name;

			if (line.hasMoreTokens() && (name = line.nextToken()) != null && line.hasMoreTokens()) {
				protoWritable.set(Age.newBuilder().setName(name).setAge(Integer.parseInt(line.nextToken())).build());
				context.write(null, protoWritable);
			}
		}
	}

	public int runTextToLzo(String[] args, Configuration conf) throws Exception {
		Job job = Job.getInstance(conf);
		job.setJobName("Protobuf Example : Text to LzoB64Line");

		job.setJarByClass(getClass());
		job.setMapperClass(TextMapper.class);
		job.setNumReduceTasks(0);

		job.setInputFormatClass(TextInputFormat.class);
		if (conf.get("proto.test.format", "B64Line").equals("Block")) {
			LzoProtobufBlockOutputFormat.setClassConf(Age.class, HadoopCompat.getConfiguration(job));
			job.setOutputFormatClass(LzoProtobufBlockOutputFormat.class);
		} else { // assume B64Line
			LzoProtobufB64LineOutputFormat.setClassConf(Age.class, HadoopCompat.getConfiguration(job));
			job.setOutputFormatClass(LzoProtobufB64LineOutputFormat.class);
		}

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static class LzoMapper extends Mapper<LongWritable, ProtobufWritable<Age>, Text, Text> {
		@Override
		protected void map(LongWritable key, ProtobufWritable<Age> value, Context context)
				throws IOException, InterruptedException {
			Age age = value.get();
			context.write(null, new Text(age.getName() + "\t" + age.getAge()));
		}
	}

	int runLzoToText(String[] args, Configuration conf) throws Exception {
		Job job = Job.getInstance(conf);
		job.setJobName("Protobuf Example : LzoB64Line to Text");

		job.setJarByClass(getClass());
		job.setMapperClass(LzoMapper.class);
		job.setNumReduceTasks(0);

		// input format is same for both B64Line or block:
		MultiInputFormat.setInputFormatClass(Age.class, job);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static class SortMapper extends Mapper<LongWritable, Text, Text, ProtobufWritable<Age>> {
		ProtobufWritable<Age> protoWritable = ProtobufWritable.newInstance(Age.class);

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer line = new StringTokenizer(value.toString(), " ");
			String name;

			if (line.hasMoreTokens() && (name = line.nextToken()) != null && line.hasMoreTokens()) {
				protoWritable.set(Age.newBuilder().setName(name).setAge(Integer.parseInt(line.nextToken())).build());
				context.write(new Text(name), protoWritable);
			}
		}
	}

	public static class SortReducer extends Reducer<Text, ProtobufWritable<Age>, Text, Text> {
		ProtobufConverter<Age> converter = ProtobufConverter.newInstance(Age.class);

		@Override
		protected void reduce(Text key, Iterable<ProtobufWritable<Age>> values, Context context)
				throws IOException, InterruptedException {
			for (ProtobufWritable<Age> value : values) {
				/*
				 * setConverter() before value.get() is invoked. It is required
				 * since 'value' object was created by MR with
				 * ProtobufWritable's default constructor, as result object does
				 * not know its runtime Protobuf class. The 'value' object just
				 * contains serialized bytes for the protobuf. It can create the
				 * actual protobuf only after it knows which protobuf class to
				 * use.
				 */
				value.setConverter(converter);
				context.write(null, new Text(value.get().getName() + "\t" + value.get().getAge()));
			}
		}
	}

	int runSorter(String[] args, Configuration conf) throws Exception {

		Job job = Job.getInstance(conf);
		job.setJobName("Protobuf Example : ProtobufWritable as Map output class");

		job.setJarByClass(getClass());
		job.setMapperClass(SortMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(ProtobufWritable.class);

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

		int res = ToolRunner.run(conf, new ProtobufMRExample(), args);
		if (res != 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("ProtobufMRExample is done !");
		}
		System.exit(res);

	}

	@Override
	public int run(String[] args) throws Exception {
		ProtobufMRExample runner = new ProtobufMRExample();
		if (args.length != 2) {
			System.out.println("Usage: yarn jar path/to/this.jar " + runner.getClass() + " <input dir> <output dir>");
			ToolRunner.printGenericCommandUsage(System.err);
			System.exit(2);
		}

		FileSystem.get(getConf()).delete(new Path(args[1]), true);

		String test = getConf().get("proto.test", "lzoIn");

		System.out.println("**************************");
		System.out.println(test);
		System.out.println("**************************");
		int ret = 0;
		if (test.equals("lzoIn")) {
			ret = runner.runLzoToText(args, getConf());
		} else if (test.equals("lzoOut")) {
			ret = runner.runTextToLzo(args, getConf());
		} else if (test.equals("sort")) {
			ret = runner.runSorter(args, getConf());
		} else {
			throw new Exception("proto.test  is null");
		}

		return ret;

	}
}

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fanlehai.hadoop.serialize.avro;

import java.io.IOException;
import java.util.*;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.mapred.AvroWrapper;
import org.apache.avro.mapred.Pair;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * The classic WordCount example modified to output Avro Pair<CharSequence,
 * Integer> records instead of text.
 */

/*
 * 1.用GenericReadWrite生成一个users.avro 2.上传users.avro到hdfs hdfs dfs -put
 * users.avro .
 * 
 * 3.打包本项目为avrowc.jar
 * 
 * 查看avro文件 java -jar /Users/liuhai/lib/AVRO/1.8.1/avro-tools-1.8.1.jar tojson
 * part-00000.avro
 * 
 */

public class MapReduceAvroWordCount extends Configured implements Tool {

	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			StringTokenizer tokenizer = new StringTokenizer(line);
			while (tokenizer.hasMoreTokens()) {
				word.set(tokenizer.nextToken());
				context.write(word, one);
			}
		}
	}

	public static class Reduce
			extends Reducer<Text, IntWritable, AvroWrapper<Pair<CharSequence, Integer>>, NullWritable> {

		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			context.write(
					new AvroWrapper<Pair<CharSequence, Integer>>(new Pair<CharSequence, Integer>(key.toString(), sum)),
					NullWritable.get());
		}
	}

	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		Job job = Job.getInstance(super.getConf(), "AvroWordCount");

		job.setJarByClass(MapReduceAvroWordCount.class);
		job.setJobName("AvroWordCount");

		// We call setOutputSchema first so we can override the configuration
		// parameters it sets
		AvroJob.setOutputKeySchema(job, Pair.getPairSchema(Schema.create(Type.STRING), Schema.create(Type.INT)));
		job.setOutputValueClass(NullWritable.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setSortComparatorClass(Text.Comparator.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	public static void main(String[] args) throws Exception {
		

		int res = ToolRunner.run(new Configuration(), new MapReduceAvroWordCount(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("AvroWordCount is done !");
		}
		System.exit(res);
	}

	private void printUsage() {
		System.err.println("Usage: AvroWordCount <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}
}

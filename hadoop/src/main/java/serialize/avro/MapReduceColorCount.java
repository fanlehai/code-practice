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

package serialize.avro;

import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.avro.mapreduce.AvroKeyValueOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
 * 1.先生成user.java文件
 * 通过avro的tool，根据user.avsc生成User.java文件，对avro文件写入和读取
 * java -jar /Users/liuhai/lib/AVRO/1.8.1/avro-tools-1.8.1.jar compile schema user.avsc .
 * 2.用GenericReadWrite生成一个users.avro
 * 3.上传users.avro到hdfs
 * hdfs dfs -put users.avro .
 * 
 * 4.运行程序
 * export LIBJARS=/Users/liuhai/lib/AVRO/1.8.1/avro-mapred-1.8.1.jar,/Users/liuhai/lib/AVRO/1.8.1/avro-1.8.1.jar
 * export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:/Users/liuhai/lib/AVRO/1.8.1/avro-mapred-1.8.1.jar:/Users/liuhai/lib/AVRO/1.8.1/avro-1.8.1.jar
 * hadoop jar avro.jar serialize.avro.MapReduceColorCount -libjars $LIBJARS user.avro avrocolor
 * 
 */

public class MapReduceColorCount extends Configured implements Tool {

	public static class ColorCountMapper extends Mapper<AvroKey<User>, NullWritable, Text, IntWritable> {

		@Override
		public void map(AvroKey<User> key, NullWritable value, Context context)
				throws IOException, InterruptedException {

			CharSequence color = key.datum().getFavoriteColor();
			if (color == null) {
				color = "none";
			}
			context.write(new Text(color.toString()), new IntWritable(1));
		}
	}

	public static class ColorCountReducer
			extends Reducer<Text, IntWritable, AvroKey<CharSequence>, AvroValue<Integer>> {

		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {

			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			context.write(new AvroKey<CharSequence>(key.toString()), new AvroValue<Integer>(sum));
		}
	}

	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		Job job = Job.getInstance(super.getConf(), "MapReduceAvroWordCount");

		job.setJarByClass(MapReduceColorCount.class);
		job.setJobName("Color Count");

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setInputFormatClass(AvroKeyInputFormat.class);
		job.setMapperClass(ColorCountMapper.class);
		AvroJob.setInputKeySchema(job, User.getClassSchema());
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setOutputFormatClass(AvroKeyValueOutputFormat.class);
		job.setReducerClass(ColorCountReducer.class);
		AvroJob.setOutputKeySchema(job, Schema.create(Schema.Type.STRING));
		AvroJob.setOutputValueSchema(job, Schema.create(Schema.Type.INT));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		System.setProperty("hadoop.home.dir", "/Users/liuhai/lib/hadoop/hadoop-2.7.2");
		int res = ToolRunner.run(conf, new MapReduceColorCount(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("MapReduceColorCount is done !");
		}

		System.exit(res);
	}

	private void printUsage() {
		System.err.println("Usage: MapReduceColorCount <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}
}

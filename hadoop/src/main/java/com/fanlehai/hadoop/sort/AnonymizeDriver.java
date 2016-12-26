package com.fanlehai.hadoop.sort;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.fanlehai.hadoop.utils.MRDPUtils;

/*
 * 
 * 对数据进行抽取随机排序，用途：
 * 1. 为了匿名的目的对数据混排；
 * 2. 在随机的数据集中获得可重复的随机采样；
 * 
 */

public class AnonymizeDriver extends Configured implements Tool {

	public static class AnonymizeMapper extends Mapper<Object, Text, IntWritable, Text> {

		private IntWritable outkey = new IntWritable();
		private Random rndm = new Random();
		private Text outvalue = new Text();

		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

			// Parse the input string into a nice map
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());

			if (parsed.size() > 0) {
				StringBuilder bldr = new StringBuilder();
				bldr.append("<row ");
				for (Entry<String, String> entry : parsed.entrySet()) {

					if (entry.getKey().equals("UserId") || entry.getKey().equals("Id")) {
						// ignore these fields
					} else if (entry.getKey().equals("CreationDate")) {
						// Strip out the time, anything after the 'T' in the
						// value
						bldr.append(entry.getKey() + "=\""
								+ entry.getValue().substring(0, entry.getValue().indexOf('T')) + "\" ");
					} else {
						// Otherwise, output this.
						bldr.append(entry.getKey() + "=\"" + entry.getValue() + "\" ");
					}

				}
				bldr.append(">");
				outkey.set(rndm.nextInt());
				outvalue.set(bldr.toString());
				context.write(outkey, outvalue);
			}
		}
	}

	public static class ValueReducer extends Reducer<IntWritable, Text, Text, NullWritable> {
		@Override
		protected void reduce(IntWritable key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			for (Text t : values) {
				context.write(t, NullWritable.get());
			}
		}
	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		int res = ToolRunner.run(conf, new AnonymizeDriver(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("Anonymize is done !");
		}

		System.exit(res);
	}

	@Override
	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println("Usage: Anonymize <user data> <out>");
			System.exit(1);
		}

		// Configure the join type
		Job job = Job.getInstance(getConf(), "Anonymize");
		job.setJarByClass(AnonymizeDriver.class);

		job.setMapperClass(AnonymizeMapper.class);
		job.setReducerClass(ValueReducer.class);

		TextInputFormat.setInputPaths(job, new Path(args[0]));
		TextOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);

		return job.waitForCompletion(true) ? 1 : 0;
	}
}

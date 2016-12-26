package com.fanlehai.hadoop.filter;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

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
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.fanlehai.hadoop.utils.*;

/*
 * 获得users.xml表中reputation值的前十名user的信息
 * 
 * 【注意:】
 * 1. 前10中，相同reputation的处理，这里使用了list存储相同reputation
 * 2. TopTenData中使用ArrayList，如果要用Text需要考虑深拷贝的问题
 * 
 * yarn jar topten.jar /stackover/Users.xml topten
 * 
 * yarn jar topten.jar stackover-users-sample topten
 */

public class TopTen extends Configured implements Tool {

	public static void addValue(TreeMap<Integer, TopTenData> repToRecordMap, String strKey, String value) {
		Integer iReputation = Integer.parseInt(strKey);
		// TopTenData data = new TopTenData();
		// 判断top10里面相同reputation的id，放到list里面
		if (repToRecordMap.containsKey(iReputation)) {
			TopTenData data = repToRecordMap.get(iReputation);
			// data.listData.add(value);
			repToRecordMap.put(iReputation, new TopTenData(data, value));
		} else {
			repToRecordMap.put(iReputation, new TopTenData(value));
		}

		if (repToRecordMap.size() > 10) {
			repToRecordMap.remove(repToRecordMap.firstKey());
		}
	}

	public static class TopTenMap extends Mapper<LongWritable, Text, NullWritable, Text> {

		private TreeMap<Integer, TopTenData> repToRecordMap = new TreeMap<Integer, TopTenData>();

		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			Map<String, String> parse = MRDPUtils.transformXmlToMap(value.toString());
			String reputation = parse.get("Reputation");
			if (reputation == null || reputation.isEmpty()) {
				return;
			}
			addValue(repToRecordMap, reputation, value.toString());
		}

		protected void cleanup(Context context) throws IOException, InterruptedException {
			for (TopTenData data : repToRecordMap.values()) {
				for (String text : data.listData) {
					System.out.println("map : write :" + text);
					context.write(NullWritable.get(), new Text(text));
				}
			}
			repToRecordMap.clear();
		}
	}

	public static class TopTenRecord extends Reducer<NullWritable, Text, NullWritable, Text> {

		private static TreeMap<Integer, TopTenData> repToRecordMap = new TreeMap<Integer, TopTenData>();

		protected void reduce(NullWritable key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			for (Text value : values) {
				Map<String, String> parse = MRDPUtils.transformXmlToMap(value.toString());
				String reputation = parse.get("Reputation");
				if (reputation == null || reputation.isEmpty()) {
					return;
				}
				addValue(repToRecordMap, reputation, value.toString());
			}
		}

		protected void cleanup(Context context) throws IOException, InterruptedException {
			for (TopTenData data : repToRecordMap.descendingMap().values()) {
				for (String text : data.listData) {
					context.write(NullWritable.get(), new Text(text));
				}
			}
		}
	}

	private void printUsage() {
		System.err.println("Usage: TopTen <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			printUsage();
			return 0;
		}

		FileSystem.get(getConf()).delete(new Path(args[1]), true);

		Job job = Job.getInstance(getConf(), "TopTen");
		job.setJarByClass(com.fanlehai.hadoop.filter.TopTen.class);

		job.setMapperClass(TopTenMap.class);
		job.setReducerClass(TopTenRecord.class);

		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);

		// TODO: specify input and output DIRECTORIES (not files)
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		int res = ToolRunner.run(conf, new TopTen(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("TopTen is done !");
		}
		System.exit(res);

	}
}

package com.fanlehai.hadoop.join;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.fanlehai.hadoop.utils.*;

/*
 * 连接Comment.xml和user.xml通过Id
 * 1. ReplicatedJoinPrepare通过这个程序生成user.xml的id和name的对应表
 * 2. 在本程序中把1中的文件加入到chache：job.addCacheFile(new Path(args[0]).toUri());
 * 3. 在map的setup中加载2中的文件
 * 4. 在map的map方法中连接commnetj.xml数据和user.xml数据
 * yarn jar test.jar join.ReplicatedJoinDriver id/part-r-00000 stackover-comments-sample/part-r-00000 replicatedjoin inner
 * 
 */

public class ReplicatedJoinDriver extends Configured implements Tool {

	public static class ReplicatedJoinMapper extends Mapper<Object, Text, Text, Text> {

		private Text outvalue = new Text();
		private String joinType = null;

		private Map<String, String> stationIdToName = new HashMap<String, String>();

		public void initialize(File file) throws IOException {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				String line;
				while ((line = in.readLine()) != null) {
					String[] string = line.split("\\t");
					// Integer len = string.length;
					// System.out.println(line + " : "+len.toString());
					if (string.length == 2) {
						stationIdToName.put(string[0], string[1]);
					}
				}
			} finally {
				IOUtils.closeStream(in);
			}
		}

		@Override
		public void setup(Context context) throws IOException, InterruptedException {

			String string = context.getConfiguration().get("join.user.data");
			int index = string.lastIndexOf('/');
			if (index != -1) {
				string = string.substring(index + 1, string.length());
			}
			initialize(new File(string));

			// Get the join type
			joinType = context.getConfiguration().get("join.type");
		}

		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

			// Parse the input string into a nice map
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());

			String userId = parsed.get("Id");

			if (userId == null) {
				return;
			}

			String userInformation = stationIdToName.get(userId);

			// If the user information is not null, then output
			if (userInformation != null) {
				outvalue.set(userInformation);
				context.write(value, outvalue);
			} else if (joinType.equalsIgnoreCase("leftouter")) {
				// If we are doing a left outer join, output the record with an
				// empty value
				context.write(value, new Text(""));
			}
		}
	}

	public static void main(String[] args) throws Exception {
		

		Configuration conf = new Configuration();
		int res = ToolRunner.run(conf, new ReplicatedJoinDriver(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("ReplicatedJoin  is done !");
		}
		System.exit(res);
	}

	public int run(String[] args) throws Exception {

		if (args.length != 4) {
			System.err.println("Usage: ReplicatedJoin <user data> <comment data> <out> [inner|leftouter]");
			System.exit(1);
		}

		String joinType = args[3];
		if (!(joinType.equalsIgnoreCase("inner") || joinType.equalsIgnoreCase("leftouter"))) {
			System.err.println("Join type not set to inner or leftouter");
			System.exit(2);
		}

		FileSystem.get(getConf()).delete(new Path(args[2]), true);

		// Configure the join type
		// Job job = new Job(conf, "Replicated Join");
		Job job = Job.getInstance(getConf(), "ReplicatedJoin");
		job.getConfiguration().set("join.type", joinType);
		job.getConfiguration().set("join.user.data", args[0]);
		job.setJarByClass(ReplicatedJoinDriver.class);

		job.setMapperClass(ReplicatedJoinMapper.class);
		job.setNumReduceTasks(0);

		TextInputFormat.setInputPaths(job, new Path(args[1]));
		TextOutputFormat.setOutputPath(job, new Path(args[2]));

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// 数据加入到缓存区，文件分发到每个map
		job.addCacheFile(new Path(args[0]).toUri());

		return job.waitForCompletion(true) ? 1 : 0;
	}
}

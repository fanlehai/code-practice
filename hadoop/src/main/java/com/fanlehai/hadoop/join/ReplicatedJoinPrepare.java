package com.fanlehai.hadoop.join;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.fanlehai.hadoop.utils.*;


/*
 * 取得stackover的user表中的Id对应的DisplayName
 * 
 * * 【测试数据】：StackOver的Users.xml，格式如下：
 * <?xml version="1.0" encoding="utf-8"?>
	<users>
	  <row Id="3615528" Reputation="1" CreationDate="2014-05-08T08:29:59.600" DisplayName="Mike" LastAccessDate="2016-07-25T18:57:54.603" WebsiteUrl="http://stolmet-zywiec.pl/" Location="Poland" AboutMe="" Views="1" UpVotes="0" DownVotes="0" ProfileImageUrl="https://www.gravatar.com/avatar/?s=128&d=identicon&r=PG&f=1" AccountId="4440990" />
	  <row Id="3615103" Reputation="15" CreationDate="2014-05-08T06:28:57.240" DisplayName="Ferragam" LastAccessDate="2016-07-11T12:27:28.937" WebsiteUrl="" AboutMe="" Views="0" UpVotes="1" DownVotes="0" ProfileImageUrl="https://www.gravatar.com/avatar/9b1e2803df438055a11f0035e3780cc1?s=128&d=identicon&r=PG&f=1" AccountId="4440465" />
	  <row Id="3614148" Reputation="1" CreationDate="2014-05-07T22:27:22.250" DisplayName="ocornejo77" LastAccessDate="2016-04-22T14:53:43.063" WebsiteUrl="" Location="Milán, Italia" AboutMe="<p>I'm a Computer Science Ph.D. student at Milano-Bicocca.</p>" Views="0" UpVotes="0" DownVotes="0" ProfileImageUrl="https://i.stack.imgur.com/objxt.jpg" AccountId="4439156" />
	  <row Id="3614019" Reputation="1" CreationDate="2014-05-07T21:30:17.430" DisplayName="user3614019" LastAccessDate="2016-04-19T16:25:53.750" Views="0" UpVotes="0" DownVotes="0" ProfileImageUrl="https://www.gravatar.com/avatar/?s=128&d=identicon&r=PG&f=1" AccountId="4438967" />
	  <row Id="3613414" Reputation="3" CreationDate="2014-05-07T18:08:15.627" DisplayName="Lestat" LastAccessDate="2016-09-02T21:14:59.063" WebsiteUrl="" AboutMe="" Views="1" UpVotes="0" DownVotes="0" ProfileImageUrl="https://www.gravatar.com/avatar/?s=128&d=identicon&r=PG&f=1" AccountId="4438152" />
	  ...
	  ...
	</users>
 * 命令：
 * yarn jar prepare.jar join.ReplicatedJoinPrepare -D mapreduce.job.reduces=5 /stackover/Users.xml id
 * 
 * 生成的文件是：id/part-r-00000
 */

public class ReplicatedJoinPrepare extends Configured implements Tool {

	public static class ReplicatedJoinMapper extends Mapper<Object, Text, Text, Text> {

		private Text outkey = new Text();

		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

			// Parse the input string into a nice map
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());

			String userId = parsed.get("Id");
			String userName = parsed.get("DisplayName");

			if (userId == null || userId.isEmpty() || userName == null || userName.isEmpty()) {
				return;
			}

			outkey.set(userId);
			context.write(outkey, new Text(userName));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		int res = ToolRunner.run(conf, new ReplicatedJoinPrepare(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("ReplicatedJoinPrepare  is done !");
		}
		System.exit(res);
	}

	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println("Usage: ReplicatedJoinPrepare <user data> <out>");
			System.exit(1);
		}
		
		FileSystem.get(getConf()).delete(new Path(args[1]), true);

		// Configure the join type
		Job job = Job.getInstance(getConf(), "ReplicatedJoinPrepare");
		job.setJarByClass(ReplicatedJoinPrepare.class);

		job.setMapperClass(ReplicatedJoinMapper.class);

		TextInputFormat.setInputPaths(job, new Path(args[0]));
		TextOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		return job.waitForCompletion(true) ? 1 : 0;
	}
}

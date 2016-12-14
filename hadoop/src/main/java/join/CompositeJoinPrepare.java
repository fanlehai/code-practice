package join;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import utils.MRDPUtils;


/*
 * 运行两个job：
 * 一个job取得user表的userid和name；
 * 一个job取得comment表的userid和text(评论内容)
 * 
 * yarn jar compositejoinprepare.jar join.CompositeJoinPrepare -D mapreduce.job.reduces=10 /stackover/Users.xml join/user /stackover/Comments.xml join/comment

 * 
 */

public class CompositeJoinPrepare extends Configured implements Tool {

	public static class MapUser extends Mapper<LongWritable, Text, Text, Text> {
		
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());

			String strName = parsed.get("DisplayName");

			// Grab the “UserID” since it is what we are grouping by
			String userId = parsed.get("Id");

			// .get will return null if the key is not there
			if (strName == null || strName.isEmpty() || userId == null || userId.isEmpty()) {
				// skip this record
				return;
			}
			context.write(new Text(userId), new Text(strName));
		}
	}

	public static class MapComment extends Mapper<LongWritable, Text, Text, Text> {
		
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());

			String strText = parsed.get("Text");

			// Grab the “UserID” since it is what we are grouping by
			String userId = parsed.get("UserId");

			// .get will return null if the key is not there
			if (strText == null || strText.isEmpty() || userId == null || userId.isEmpty()) {
				// skip this record
				return;
			}
			if (strText.length()>20) {
				strText = strText.substring(0, 16);
				strText += "...";
			}
			context.write(new Text(userId), new Text(strText));
		}
	}

	public int run(String[] args) throws Exception {
		if (args.length != 4) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		FileSystem.get(new Configuration()).delete(new Path(args[3]), true);
		
		String userInputPath = args[0];
		String userOutputPath = args[1];
		String commentInputPath = args[2];
		String commentOutputPath = args[3];
		int  res = 0;
		Job job = Job.getInstance(super.getConf(), "CompositeJoinPrepareUser");
		job.setJarByClass(join.CompositeJoinPrepare.class);
		job.setMapperClass(MapUser.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		// 输出的文件，以gzip压缩，防止compositejoin时被split
		TextOutputFormat.setCompressOutput(job, true);
		TextOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
		FileInputFormat.setInputPaths(job, new Path(userInputPath));
		FileOutputFormat.setOutputPath(job, new Path(userOutputPath));
		if (job.waitForCompletion(true)) {
			System.out.println("CompositeJoinPrepareUser is done!");
			
			Job jobComment = Job.getInstance(super.getConf(), "CompositeJoinPrepareComment");
			jobComment.setJarByClass(join.CompositeJoinPrepare.class);
			jobComment.setMapperClass(MapComment.class);
			jobComment.setOutputKeyClass(Text.class);
			jobComment.setOutputValueClass(Text.class);
			// 输出的文件，以gzip压缩，防止compositejoin时被split
			TextOutputFormat.setCompressOutput(jobComment, true);
			TextOutputFormat.setOutputCompressorClass(jobComment, GzipCodec.class);
			
			FileInputFormat.setInputPaths(jobComment, new Path(commentInputPath));
			FileOutputFormat.setOutputPath(jobComment, new Path(commentOutputPath));
			if (jobComment.waitForCompletion(true)) {
				res = 1;
				System.out.println("CompositeJoinPrepareComment is done!");
			}
			else {
				System.out.println("CompositeJoinPrepareComment : something bad hadppend!");
			}
		}else {
			System.out.println("CompositeJoinPrepareUser : something bad hadppend!");
		}
		
		return  res;
	}

	private void printUsage() {
		System.err.println("Usage: CompositeJoinPrepare <in1> <out1> <in2> <out2>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

	public static void main(String[] args) throws Exception {

		

		Configuration conf = new Configuration();
		int res = ToolRunner.run(conf, new CompositeJoinPrepare(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("CompositeJoinPrepare is done !");
		}
		System.exit(res);

	}

}

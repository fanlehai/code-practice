package filter;

import java.io.IOException;
import java.util.Map;

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

import utils.MRDPUtils;

/*
 * 对评论表中评论过滤，去除重复userid，得到哪些用户进行了评论。
 * 实现方式很简单，用id作为key在map中进行输出，到reduce之前已经分区分组了，
 * 所以每一个key已经对应的值（这里设置值为nullwritable）都是一条记录给到reduce
 * 
 * yarn jar distinct.jar filter.DistinctUser /stackover/Comment.xml distinct
 * 
 * yarn jar distinct.jar filter.DistinctUser stackover-comments-sample distinct
 */

public class DistinctUser extends Configured implements Tool {

	public static class DistinctUserMap extends Mapper<LongWritable, Text, Text, NullWritable> {

		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			Map<String, String> parse = MRDPUtils.transformXmlToMap(value.toString());
			String userId = parse.get("UserId");
			if (userId == null || userId.isEmpty()) {
				return;
			}
			context.write(new Text(userId), NullWritable.get());
		}

	}

	public static class DistinctUserReduce extends Reducer<Text, NullWritable, Text, NullWritable> {

		protected void reduce(Text key, Iterable<NullWritable> values, Context context)
				throws IOException, InterruptedException {

			context.write(key, NullWritable.get());
		}
	}

	private void printUsage() {
		System.err.println("Usage: DistinctUser <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			printUsage();
			return 0;
		}

		FileSystem.get(getConf()).delete(new Path(args[1]), true);

		Job job = Job.getInstance(getConf(), "DistinctUser");
		job.setJarByClass(filter.TopTen.class);

		job.setMapperClass(DistinctUserMap.class);
		job.setReducerClass(DistinctUserReduce.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		int res = ToolRunner.run(conf, new DistinctUser(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("DistinctUser is done !");
		}
		System.exit(res);
	}

}

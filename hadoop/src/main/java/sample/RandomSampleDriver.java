package sample;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class RandomSampleDriver extends Configured implements Tool {

	public static final String FILTER_PERCENTAGE_KEY = "com.fanlehai.hadoop.sample.filter_percentage";

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		System.setProperty("hadoop.home.dir", "/Users/liuhai/lib/hadoop/hadoop-2.7.2");
		int res = ToolRunner.run(conf, new RandomSampleDriver(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("RandomSample is done !");
		}

		System.exit(res);

	}

	/**
	 * @param args需要三个参数
	 *            percentage：1-100的整数，表示要取得数据集的百分之多少数据 in：输入数据路径 out：输出数据目录
	 */
	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		Job job = Job.getInstance(super.getConf(), "Simple Random Sampling");

		Float filterPercentage = 0.0f;
		try {
			filterPercentage = Float.parseFloat(args[0]) / 100.0f;
		} catch (NumberFormatException nfe) {
			printUsage();
		}

		job.setJarByClass(RandomSampleDriver.class);
		job.setMapperClass(RandomSampleMap.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		job.setNumReduceTasks(1); // prevent lots of small files
		job.getConfiguration().setFloat(FILTER_PERCENTAGE_KEY, filterPercentage);
		FileInputFormat.addInputPath(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	private void printUsage() {
		System.err.println("Usage: RandomSample <percentage> <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

}

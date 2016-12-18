package job.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import utils.MRDPUtils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.chain.ChainReducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


/*
 * 一个mapreduce程序[map+reduce] = 多个map阶段 + 一个reduce阶段 + 多个map阶段
 * 
 * 多个map阶段使用ChainMapper实现
 * 一个reduce阶段用ChainReduce实现
 * 
 * 同一时间只能有一个map或一个reduce在运行，不能并行；
 * 
 * 上一个map的输出数据作为下一个map或reduce的输入，这是个线性执行的过程
 * 
 */





public class ChainMapperDriver extends Configured implements Tool {

	public static final String AVERAGE_CALC_GROUP = "AverageCalculation";
	public static final String MULTIPLE_OUTPUTS_BELOW_5000 = "below5000";
	public static final String MULTIPLE_OUTPUTS_ABOVE_5000 = "above5000";

	public static class UserIdCountMapper extends Mapper<Object, Text, Text, LongWritable> {

		public static final String RECORDS_COUNTER_NAME = "Records";

		private static final LongWritable ONE = new LongWritable(1);
		private Text outkey = new Text();

		protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {

			// Parse the input into a nice map.
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());

			// Get the value for the OwnerUserId attribute
			String userId = parsed.get("OwnerUserId");

			if (userId != null) {
				outkey.set(userId);
				context.write(outkey, ONE);
			}

		}

	}

	public static class UserIdReputationEnrichmentMapper extends Mapper<Text, LongWritable, Text, LongWritable> {

		private Text outkey = new Text();
		private HashMap<String, String> userIdToReputation = new HashMap<String, String>();

		@SuppressWarnings("resource")
		protected void setup(Context context) throws IOException, InterruptedException {

			try {
				userIdToReputation.clear();
				// Path[] files = DistributedCache.getLocalCacheFiles(job);

				URI[] files = context.getCacheFiles();

				if (files == null || files.length == 0) {
					throw new RuntimeException("User information is not set in DistributedCache");
				}

				// Read all files in the DistributedCache
				for (URI p : files) {
					BufferedReader rdr = new BufferedReader(
							new InputStreamReader(new GZIPInputStream(new FileInputStream(new File(p.toString())))));

					String line;
					// For each record in the user file
					while ((line = rdr.readLine()) != null) {

						// Get the user ID and reputation
						Map<String, String> parsed = MRDPUtils.transformXmlToMap(line);
						String userId = parsed.get("Id");
						String reputation = parsed.get("Reputation");

						if (userId != null && reputation != null) {
							// Map the user ID to the reputation
							userIdToReputation.put(userId, reputation);
						}
					}
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		protected void map(Text key, LongWritable value, Context context) throws IOException, InterruptedException {

			String reputation = userIdToReputation.get(key.toString());
			if (reputation != null) {
				outkey.set(value.get() + "\t" + reputation);
				context.write(outkey, value);
			}
		}

	}

	public static class LongSumReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

		private LongWritable outvalue = new LongWritable();

		protected void reduce(Text key, Iterable<LongWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (LongWritable value : values) {
				sum += Integer.parseInt(value.toString());
			}
			outvalue.set(sum);
			context.write(key, outvalue);
		}

	}

	public static class UserIdBinningMapper extends Mapper<Text, LongWritable, Text, LongWritable> {

		@SuppressWarnings("rawtypes")
		private MultipleOutputs mos = null;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected void setup(Context context) throws IOException, InterruptedException {
			mos = new MultipleOutputs(context);
		}

		@SuppressWarnings("unchecked")
		protected void map(Text key, LongWritable value, Context context) throws IOException, InterruptedException {

			if (Integer.parseInt(key.toString().split("\t")[1]) < 5000) {
				mos.write(MULTIPLE_OUTPUTS_BELOW_5000, key, value);
			} else {
				mos.write(MULTIPLE_OUTPUTS_ABOVE_5000, key, value);
			}

		}

		protected void cleanup(Context context) throws IOException, InterruptedException {
			try {
				mos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		int res = ToolRunner.run(conf, new ChainMapperDriver(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("ChainMapperReducer is done !");
		}
		System.exit(res);

	}

	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("Usage: ChainMapperReducer <posts> <users> <out>");
			System.exit(2);
		}

		Path postInput = new Path(args[0]);
		Path userInput = new Path(args[1]);
		Path outputDir = new Path(args[2]);

		Job job = Job.getInstance(getConf(), "ChainMapperReducer");
		job.setJarByClass(ChainMapperDriver.class);

		// 此map的输出数据给到下面的mapper
		ChainMapper.addMapper(job, UserIdCountMapper.class, LongWritable.class, Text.class, Text.class,
				LongWritable.class, getConf());

		// 此map的输出数据给到下面的reduce
		ChainMapper.addMapper(job, UserIdReputationEnrichmentMapper.class, Text.class, LongWritable.class, Text.class,
				LongWritable.class, getConf());
		// 此reduce的输出数据给到下面的mapper
		ChainReducer.setReducer(job, LongSumReducer.class, Text.class, LongWritable.class, Text.class,
				LongWritable.class, getConf());

		ChainReducer.addMapper(job, UserIdBinningMapper.class, Text.class, LongWritable.class, Text.class,
				LongWritable.class, getConf());

		job.setCombinerClass(LongSumReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		TextInputFormat.setInputPaths(job, postInput);

		// Configure multiple outputs
		job.setOutputFormatClass(NullOutputFormat.class);
		FileOutputFormat.setOutputPath(job, outputDir);
		MultipleOutputs.addNamedOutput(job, MULTIPLE_OUTPUTS_ABOVE_5000, TextOutputFormat.class, Text.class,
				LongWritable.class);
		MultipleOutputs.addNamedOutput(job, MULTIPLE_OUTPUTS_BELOW_5000, TextOutputFormat.class, Text.class,
				LongWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		// Add the user files to the DistributedCache
		FileStatus[] userFiles = FileSystem.get(getConf()).listStatus(userInput);
		for (FileStatus status : userFiles) {
			// DistributedCache.addCacheFile(status.getPath().toUri(), conf);
			job.addCacheFile(new URI(status.getPath().toString()));
		}

		return job.waitForCompletion(true) ? 1 : 0;
	}
}

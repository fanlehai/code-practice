package join;

import static java.lang.Math.ceil;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.round;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataOutputStream;
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
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.util.bloom.BloomFilter;
import org.apache.hadoop.util.bloom.Key;
import org.apache.hadoop.util.hash.Hash;

import utils.MRDPUtils;

/*
 * 
 * 测试数据：（解压后大小1.7g，实际威望值大于1500有73622）
 * 用户数据：https://archive.org/download/stackexchange/stackoverflow.com-Users.7z
 * 
 * 这里要计算bloom的几个参数：
 * 
 * /stackover/Users.xml bloomfilter
 * 
 * 
 */
public class ReduceSideJoinWithBloomPrepare extends Configured implements Tool {

	public static final String BLOOM_M = "bloom_m";
	public static final String BLOOM_K = "bloom_k";
	private static String FILTER_OUTPUT_FILE_CONF = "bloomfilter.output.file";

	public static class UserMapper extends Mapper<LongWritable, Text, NullWritable, BloomFilter> {

		private BloomFilter filter = null;// new BloomFilter(80000, 1,
											// Hash.MURMUR_HASH);
		private int counter = 0, counter_1500 = 0;

		/**
		 * Called once at the beginning of the task.
		 */
		protected void setup(Context context) throws IOException, InterruptedException {
			// NOTHING
			String bloom_m = context.getConfiguration().get(BLOOM_M);
			if (bloom_m == null || bloom_m.isEmpty()) {
				System.out.println("请设置bloom_m值！");
			}
			String bloom_k = context.getConfiguration().get(BLOOM_K);
			if (bloom_k == null || bloom_k.isEmpty()) {
				System.out.println("请设置bloom_k值！");
			}
			filter = new BloomFilter(Integer.parseInt(bloom_m), Integer.parseInt(bloom_k), Hash.MURMUR_HASH);
		}

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			// Parse the input string into a nice map
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());

			String userId = parsed.get("Id");
			String reputation = parsed.get("Reputation");
			

			if (userId == null || userId.isEmpty() || reputation == null || reputation.isEmpty()) {
				return;
			}
			counter = counter + 1;

			if (Integer.parseInt(reputation) > 1500) {
				Key filterKey = new Key(userId.getBytes());
				filter.add(filterKey);
				counter_1500 = counter_1500 + 1;
			}
		}

		/**
		 * Called once at the end of the task.
		 */
		protected void cleanup(Context context) throws IOException, InterruptedException {
			// NOTHING
			
			  System.out.println("********************************************************");
			  System.out.println("********************************************************");
			  System.out.println("总数据记录条数："+Integer.toString(counter));
			  System.out.println("威望大于1500数据记录条数："+Integer.toString(counter_1500)); 
			  System.out.println("********************************************************");
			  System.out.println("********************************************************");
			 

			// Write the filter to HDFS once all maps are finished
			context.write(NullWritable.get(), filter);

		}
	}

	public static class UserReduce extends Reducer<NullWritable, BloomFilter, NullWritable, NullWritable> {

		// 记录输出有多少行数据
		int nCount = 0;

		private BloomFilter filter = null;// new BloomFilter(2_000_000, 7,
											// Hash.MURMUR_HASH);

		protected void setup(Context context) throws IOException, InterruptedException {// NOTHING
			String bloom_m = context.getConfiguration().get(BLOOM_M);
			if (bloom_m == null || bloom_m.isEmpty()) {
				System.out.println("请设置bloom_m值！");
			}
			String bloom_k = context.getConfiguration().get(BLOOM_K);
			if (bloom_k == null || bloom_k.isEmpty()) {
				System.out.println("请设置bloom_k值！");
			}
			filter = new BloomFilter(Integer.parseInt(bloom_m), Integer.parseInt(bloom_k), Hash.MURMUR_HASH);
		}

		protected void reduce(NullWritable key, Iterable<BloomFilter> values, Context context)
				throws IOException, InterruptedException {

			System.out.println("******【reduce】*****");
			
			// Merge all filters by logical OR
			for (BloomFilter value : values) {
				filter.or(value);
				nCount = nCount + 1;
				System.out.println("******【reduce】*****ncount : "+Integer.toString(nCount));
			}
		}

		protected void cleanup(Context context) throws IOException, InterruptedException {
			// NOTHING
			System.out.println("********************************************************");
			System.out.println("********************************************************");
			System.out.println("【reduce】威望大于1500数据记录条数：" + Integer.toString(nCount));
			System.out.println("********************************************************");
			System.out.println("********************************************************");

			Path outputFilePath = new Path(context.getConfiguration().get(FILTER_OUTPUT_FILE_CONF));
			FileSystem fs = FileSystem.get(context.getConfiguration());

			try (FSDataOutputStream fsdos = fs.create(outputFilePath)) {
				filter.write(fsdos);
			} catch (Exception e) {
				throw new IOException("Error while writing bloom filter to file system.", e);
			}
		}
	}

	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println("Usage: ReduceSideJoinWithBloomPrepare <in> <out>");
			ToolRunner.printGenericCommandUsage(System.err);
			System.exit(2);
		}

		FileSystem.get(getConf()).delete(new Path(args[1]), true);

		// 这里待测试的数据的威望值大于1500的有73622条，所以设置bloom的n为80000；p(flase概率)为0.01(百分之一)
		// 开始计算m和k
		int m = ReduceSideJoinWithBloomPrepare.calculateM(80000, (float) 0.01);
		int k = ReduceSideJoinWithBloomPrepare.calculateK(80000, m);

		// Configuration conf = new Configuration();
		Job job = Job.getInstance(getConf(), "ReduceSideJoinWithBloomPrepare");
		job.setJarByClass(join.ReduceSideJoinWithBloomPrepare.class);

		job.getConfiguration().setInt(BLOOM_M, m);
		job.getConfiguration().setInt(BLOOM_K, k);
		job.getConfiguration().set(FILTER_OUTPUT_FILE_CONF, args[1]);

		job.setMapperClass(UserMapper.class);
		job.setReducerClass(UserReduce.class);
		job.setNumReduceTasks(1);

		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(BloomFilter.class);
		job.setOutputFormatClass(NullOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	private static int calculateM(int n, float p) {
		return (int) ceil((n * log(p)) / log(1.0f / (pow(2.0f, log(2.0f)))));
	}

	private static int calculateK(int n, int m) {
		return (int) round(log(2.0f) * m / n);
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		int res = ToolRunner.run(conf, new ReduceSideJoinWithBloomPrepare(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("ReduceSideJoinWithBloomPrepare is done !");
		}
		System.exit(res);
	}

}

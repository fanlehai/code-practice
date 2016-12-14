package minmaxcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
 * 
 * 运行参数：-D mapreduce.job.reduces=5 /stackover/Comments.xml /user/liuhai/stackover
 * 
 * 
 */
public class MinMaxCountDriver extends Configured implements Tool {

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		
		int res = ToolRunner.run(conf, new MinMaxCountDriver(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("MinMaxCount is done !");
		}
		System.exit(res);
	}

	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		Job job = Job.getInstance(super.getConf(), "MinMaxCount");

		job.setJarByClass(MinMaxCountDriver.class);

		// 1. input，hadoop的默认input数据格式就是TextInputFormat，所以这里不用设置
		// 2.设置具体mapper
		job.setMapperClass(MinMaxCountMapper.class);
		// 3. partitioner不需要，略过
		// 4. combiner,设置成reducer的处理即可
		job.setCombinerClass(MinMaxCountReducer.class);
		// 5. comparator，不需要，略过
		// 6. 设置reducer
		job.setReducerClass(MinMaxCountReducer.class);

		// 7. 设置K2,V2,K3,V3的类型，因为他们对应类型相同，用下面方式设置这4个类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MinMaxCountTuple.class);

		// 8. 设置输入文件和输出的文件路径
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	private void printUsage() {
		System.err.println("Usage: MinMaxCount <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

}

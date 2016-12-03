package sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;
import org.apache.hadoop.util.GenericOptionsParser;

public class TotalOrderSort {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 3) {
			System.err
					.println("Usage: TotalOrderSorting <user data> <out> <sample rate>");
			System.exit(1);
		}

		// 获取并设置input、output目录
		Path inputPath = new Path(otherArgs[0]);
		Path partitionFile = new Path(otherArgs[1] + "_partitions.lst");
		Path outputStage = new Path(otherArgs[1] + "_staging");
		Path outputOrder = new Path(otherArgs[1]);
		double sampleRate = Double.parseDouble(otherArgs[2]);

		// 删除中间目录
		FileSystem.get(new Configuration()).delete(outputOrder, true);
		FileSystem.get(new Configuration()).delete(outputStage, true);
		FileSystem.get(new Configuration()).delete(partitionFile, true);

		// 开始第一个mapreduce任务：待排序文件分区
		Job sampleJob = Job.getInstance(conf, "TotalOrderSortingStage1");
		sampleJob.setJarByClass(TotalOrderSort.class);
		// 1. 设置输入文件路径（待排序文件）
		TextInputFormat.setInputPaths(sampleJob, inputPath);
		// 2. 设置map
		sampleJob.setMapperClass(LastAccessDateMap.class);
		// 3. 不需要reduce
		sampleJob.setNumReduceTasks(0);
		// 4. 设置K2,V2的值类型
		sampleJob.setOutputKeyClass(Text.class);
		sampleJob.setOutputValueClass(Text.class);
		// 5. 设置输出文件格式为SequenceFileOutputFormat，采用Gzip压缩
		sampleJob.setOutputFormatClass(SequenceFileOutputFormat.class);
	    SequenceFileOutputFormat.setCompressOutput(sampleJob, true);
	    SequenceFileOutputFormat.setOutputCompressorClass(sampleJob, GzipCodec.class);
	    SequenceFileOutputFormat.setOutputCompressionType(sampleJob, CompressionType.BLOCK);
	    // 6. 设置输出文件路径
	    SequenceFileOutputFormat.setOutputPath(sampleJob, outputStage);

		// 提交第一个任务
		int code = sampleJob.waitForCompletion(true) ? 0 : 1;

		if (code == 0) {
			System.err.println("任务1：成功");
			
			// 开始第二个mapreduce任务：排序
			Job orderJob = Job.getInstance(conf, "TotalOrderSortingStage2");
			orderJob.setJarByClass(TotalOrderSort.class);

			// 设置input文件类型(K1,V1)
			orderJob.setInputFormatClass(SequenceFileInputFormat.class);
			// 设置input路径
			SequenceFileInputFormat.setInputPaths(orderJob, outputStage);
				
			// 设置分区
			orderJob.setPartitionerClass(TotalOrderPartitioner.class);
			// 设置分区文件
			TotalOrderPartitioner.setPartitionFile(orderJob.getConfiguration(),partitionFile);

			// 设置reduce
			orderJob.setReducerClass(ValueReduce.class);

			// 设置reduce的任务数量（默认是1个）
			orderJob.setNumReduceTasks(5);

			// 设置K2,V2的值类型
			orderJob.setOutputKeyClass(Text.class);
			orderJob.setOutputValueClass(Text.class);

			// 设置输出目录
			TextOutputFormat.setOutputPath(orderJob, outputOrder);

			// 输出文件格式没有设定，就默认为TextOutputFormat，K3与V3的分隔符默认是Tab
			// 这里设置K3,V3之间的分隔符为空
			//orderJob.getConfiguration().set("mapreduce.output.textoutputformat.separator", "");

			// 第一个mapreduce任务产生的文件对其key进行随机抽样
			// sampleRate（采样率）有用户设置;采样最大样本数这里设置为10000;采样最大分区为5
			InputSampler.writePartitionFile(orderJob,new InputSampler.RandomSampler(sampleRate, 10000, 5));
						
			// 提交第二个排序任务
			code = orderJob.waitForCompletion(true) ? 0 : 2;
			if (code == 0) {
				System.err.println("任务2：成功");
			} else {
				System.err.println("任务2：失败");
			}
		}else {
			System.err.println("任务1：失败");
		}

		// 删除中间临时目录
		//FileSystem.get(new Configuration()).delete(partitionFile, false);
		//FileSystem.get(new Configuration()).delete(outputStage, true);

		System.exit(code);
		
	}

}

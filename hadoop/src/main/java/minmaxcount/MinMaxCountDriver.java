package minmaxcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import io.*;
import minmaxcount.MinMaxCountMapper;

public class MinMaxCountDriver {

	public static void main(String[] args) throws Exception {
		
		if (args.length != 2) {
            System.err.println("Usage: ExampleDriver <in> <out>");
            System.exit(2);
        }
		
		
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "MinMaxCount");
		job.setJarByClass(MinMaxCountDriver.class);
		
		job.setNumReduceTasks(5);

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

		if (!job.waitForCompletion(true)) {
			System.out.println("MinMaxCount : something badly happened !");
			return;
		} else {
			System.out.println("MinMaxCount is done!");
		}
	}

}

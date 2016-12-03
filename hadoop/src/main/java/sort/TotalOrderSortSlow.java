package sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class TotalOrderSortSlow {

	public static void main(String[] args) throws Exception {
		
		
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err
					.println("Usage: TotalOrderSortSlow <user data> <out>");
			System.exit(1);
		}
		
		Job job = Job.getInstance(conf, "TotalOrderSortSlow");
		job.setJarByClass(sort.TotalOrderSortSlow.class);
		// TODO: specify a mapper
		job.setMapperClass(LastAccessDateMap.class);
		// TODO: specify a reducer
		job.setReducerClass(ValueReduce.class);
		
		job.setNumReduceTasks(1);

		// TODO: specify output types
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// TODO: specify input and output DIRECTORIES (not files)
		FileInputFormat.setInputPaths(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		

		if (!job.waitForCompletion(true))
			return;
	}

}

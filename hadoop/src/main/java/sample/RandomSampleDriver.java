package sample;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class RandomSampleDriver extends Configured implements Tool {

	public static final String FILTER_PERCENTAGE_KEY = "com.fanlehai.hadoop.sample.filter_percentage";
	
	public static void main(String[] args) throws Exception {

		int res = ToolRunner.run(new Configuration(), new RandomSampleDriver(), args);
		if (res == 0) {
			System.err.println("something badlly happened !");
		} else {
			System.out.println("RandomSample is done !");
		}

	}

	public int run(String[] args) throws Exception {

		Configuration conf = new Configuration();
	    GenericOptionsParser parser = new GenericOptionsParser(conf, args);
	    String[] otherArgs = parser.getRemainingArgs();
	    if (otherArgs.length != 3) {
	      printUsage();
	    }
	    Float filterPercentage = 0.0f;
	    try {
	      filterPercentage = Float.parseFloat(otherArgs[0]) / 100.0f;
	    } catch (NumberFormatException nfe) {
	      printUsage();
	    }

	    Job job = Job.getInstance(conf, "Simple Random Sampling");
	    job.setJarByClass(RandomSampleDriver.class);
	    job.setMapperClass(RandomSampleMap.class);
	    job.setOutputKeyClass(NullWritable.class);
	    job.setOutputValueClass(Text.class);
	    job.setNumReduceTasks(1); // prevent lots of small files
	    job.getConfiguration().setFloat(FILTER_PERCENTAGE_KEY, filterPercentage);
	    FileInputFormat.addInputPath(job, new Path(otherArgs[1]));
	    FileOutputFormat.setOutputPath(job, new Path(otherArgs[2]));
	    

		return job.waitForCompletion(true) ? 0 : 1;
	}

	private void printUsage() {
		System.err.println("Usage: SampleData <percentage> <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

}

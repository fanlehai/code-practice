package serialize.xml;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


import java.io.IOException;

public final class SimpleXmlOutputMapReduce extends Configured implements Tool {

	public static class Reduce extends Reducer<Text, Text, Text, Text> {

		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			context.write(new Text("<configuration>"), null);
		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			context.write(new Text("</configuration>"), null);
		}

		private Text outputKey = new Text();

		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			for (Text value : values) {
				outputKey.set(constructPropertyXml(key, value));
				context.write(outputKey, null);
			}
		}

		public static String constructPropertyXml(Text name, Text value) {
			StringBuilder sb = new StringBuilder();
			sb.append("<property><name>").append(name).append("</name><value>").append(value)
					.append("</value></property>");
			return sb.toString();
		}
	}

	public static void main(String... args) throws Exception {
		Configuration conf = new Configuration();

		int res = ToolRunner.run(conf, new SimpleXmlOutputMapReduce(), args);
		if (res != 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("SimpleXmlOutputMapReduce is done !");
		}
		System.exit(res);
	}

	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: SimpleXmlOutputMapReduce <in> <out>");
			ToolRunner.printGenericCommandUsage(System.err);
			System.exit(2);
		}

		String input = args[0];
		String output = args[1];

		Job job = Job.getInstance(getConf(), "SimpleXmlOutputMapReduce");
		job.setJarByClass(SimpleXmlOutputMapReduce.class);
		job.setReducerClass(Reduce.class);
		job.setInputFormatClass(KeyValueTextInputFormat.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		Path outPath = new Path(output);
		FileOutputFormat.setOutputPath(job, outPath);

		outPath.getFileSystem(getConf()).delete(outPath, true);

		return job.waitForCompletion(true) ? 0 : 1;
	}
}

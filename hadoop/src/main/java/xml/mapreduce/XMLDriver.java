package xml.mapreduce;

import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 
 * 代码来源：https://github.com/ildac/xmlInputFormat.git
 * 
 * 解析的xml如下格式：
 * <property>
 * 		<name>property Name</name>
 * 		<value>property value</value>
 * </property>
 */

public class XMLDriver extends Configured implements Tool {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(XMLDriver.class);

	public int run(String[] args) throws Exception {

		
		if (args.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		
		Configuration conf = super.getConf();
		conf.set("key.value.separator.in.input.line", " ");
		conf.set(XMLRecordReader.START_TAG, "<property>");
		conf.set(XMLRecordReader.END_TAG, "</property>");

		Job job = Job.getInstance(conf, "XMLDriver");
		job.setJarByClass(XMLDriver.class);

		job.setInputFormatClass(XMLInputFormat.class);
		
		job.setMapperClass(XMLMap.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setNumReduceTasks(0);

		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		if (job.waitForCompletion(true)) {
			return 0;
		}
		return 1;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(new Date());
		int res = ToolRunner.run(new Configuration(), new XMLDriver(), args);
		System.out.println(new Date());
		System.exit(res);
	}
	
	private void printUsage() {
		System.err.println("Usage: XMLDriver <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

}

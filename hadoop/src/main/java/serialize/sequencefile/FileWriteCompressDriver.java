package serialize.sequencefile;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.fs.FileSystem;

/*
 * 通过（TextInputFormat）读取文件，然后输出到sequencefile中，并且对sequencefile进行snappy压缩。
 * 本地hdfs使用：
 * 编译此文件并且打包成jar : write.jar
 * yarn jar write.jar serialize.sequencefile.FileWriteCompressDriver sequence sequenceout
 * yarn jar write.jar serialize.sequencefile.FileWriteCompressDriver sequence/a.txt sequenceout
 * 集群hdfs使用：
 * 需要输入正确的输入和输出路径
 */

public class FileWriteCompressDriver extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new FileWriteCompressDriver(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("SequenceFileWrite is done !");
		}
		
		Properties props = System.getProperties();
		 Enumeration<?> e = props.propertyNames();  
       while (e.hasMoreElements()) {  
           String key = (String) e.nextElement();  
           String value = props.getProperty(key);  
           System.out.println("Key:" + key + ",Value:" + value);  
       }  
       
	}

	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		Job job = Job.getInstance(super.getConf(), "FileWriteCompressDriver");

		job.setJarByClass(serialize.sequencefile.FileWriteCompressDriver.class);

		job.setInputFormatClass(KeyValueTextInputFormat.class);

		job.setMapperClass(FileMap.class);
		job.setNumReduceTasks(1);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// job.setOutputFormatClass(SequenceFileOutputFormat.class);
		LazyOutputFormat.setOutputFormatClass(job, SequenceFileOutputFormat.class);
		// 开启压缩
		SequenceFileOutputFormat.setCompressOutput(job, true);
		// 选用Snappy格式压缩，当然还可以选择Gzip压缩等
		SequenceFileOutputFormat.setOutputCompressorClass(job, SnappyCodec.class);
		// SequenceFileOutputFormat.setOutputCompressorClass(job,GzipCodec.class);
		// 压缩以块的方式，不以记录，这样效率更高
		SequenceFileOutputFormat.setOutputCompressionType(job, CompressionType.BLOCK);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	private void printUsage() {
		System.err.println("Usage: FileWriteCompressDriver <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

}

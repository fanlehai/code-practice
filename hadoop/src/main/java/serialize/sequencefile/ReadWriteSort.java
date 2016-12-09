package serialize.sequencefile;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.SequenceFile.Metadata;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
 * 1. 对于key是IntWritable；value是Text；的sequencefile文件，提供读和写的方法；
 * 2. 用mapreduce对sequencefile进行排序；
 */

public class ReadWriteSort extends Configured implements Tool {

	private static final String[] DATA = { "One, two, buckle my shoe", "Three, four, shut the door",
			"Five, six, pick up sticks", "Seven, eight, lay them straight", "Nine, ten, a big fat hen" };

	public static void Read(String filePath) {
		Configuration conf = new Configuration();
		Path path = new Path(filePath);
		SequenceFile.Reader reader = null;
		try {
			reader = new SequenceFile.Reader(conf, Reader.file(path), Reader.bufferSize(4096), Reader.start(0));
			Writable key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
			Writable value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
			// long position = reader.getPosition();
			// reader.seek(position);
			while (reader.next(key, value)) {
				String syncSeen = reader.syncSeen() ? "*" : "";
				System.out.printf("[%s]\t%s\t%s\n", syncSeen, key, value);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(reader);
		}
	}

	public static void Write(String filePath) {
		Configuration conf = new Configuration();
		FileSystem fs;
		Path path = new Path(filePath);

		IntWritable key = new IntWritable();
		Text value = new Text();

		SequenceFile.Writer writer = null;
		try {

			fs = FileSystem.get(URI.create(filePath), conf);
			writer = SequenceFile.createWriter(conf, Writer.file(path), Writer.keyClass(key.getClass()),
					Writer.valueClass(value.getClass()),
					Writer.bufferSize(fs.getConf().getInt("io.file.buffer.size", 4096)),
					Writer.replication(fs.getDefaultReplication(path)), Writer.blockSize(1073741824),
					Writer.compression(SequenceFile.CompressionType.BLOCK, new DefaultCodec()),
					Writer.progressable(null), Writer.metadata(new Metadata()));

			for (int i = 0; i < 100; i++) {
				if (i < 50) {
					key.set(100 - i);
				} else {
					key.set(i - 50);
				}

				value.set(DATA[i % DATA.length]);
				System.out.printf("[%s]\t%s\t%s\n", writer.getLength(), key, value);
				writer.append(key, value);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(writer);
		}
	}

	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		Job job = Job.getInstance(super.getConf(), "sequencefilesort");

		job.setJarByClass(ReadWriteSort.class);

		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	private void printUsage() {
		System.err.println("Usage: WordCountCombine <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("hadoop.home.dir", "/Users/liuhai/lib/hadoop/hadoop-2.7.2");
		int res = ToolRunner.run(new Configuration(), new ReadWriteSort(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("sequencefilesort is done !");
		}
	}

}

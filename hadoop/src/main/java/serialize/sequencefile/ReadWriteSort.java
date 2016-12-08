package serialize.sequencefile;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
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
import org.apache.hadoop.util.ReflectionUtils;

public class ReadWriteSort {

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

}

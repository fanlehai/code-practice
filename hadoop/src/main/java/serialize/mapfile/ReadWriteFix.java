package serialize.mapfile;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

/*
 * 提供三个功能：
 * 1. 读取mapfile
 * 2. 写mapfile
 * 3. 修复mapfile的index
 * 
 * sequencefile转换成mapfile的方法：
 * 1. 对sequencefile进行排序，用serialize.sequencefile.ReadWriteSort方法；
 * 2. 用本文件的fix进行修复索引即可
 */

public class ReadWriteFix {

	private static final String[] DATA = { "One, two, buckle my shoe", "Three, four, shut the door",
			"Five, six, pick up sticks", "Seven, eight, lay them straight", "Nine, ten, a big fat hen" };

	// 读取一个mapfile文件
	public static void Read(String mapfilePath) {

		Configuration conf = new Configuration();
		IntWritable key = new IntWritable();
		Text value = new Text();
		MapFile.Reader reader;
		try {

			reader = new MapFile.Reader(new Path(mapfilePath), conf);
			while (reader.next(key, value)) {
				System.out.println(key.toString() + "    " + value.toString());
			}
			reader.close();

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 创建一个mapfile并且写入数据
	public static void Write(String mapfilePath) {
		// String uri = args[0];
		Configuration conf = new Configuration();

		FileSystem fs;

		IntWritable key = new IntWritable();
		Text value = new Text();
		MapFile.Writer writer = null;

		try {
			fs = FileSystem.get(URI.create(mapfilePath), conf);

			writer = new MapFile.Writer(fs.getConf(), new Path(mapfilePath), MapFile.Writer.keyClass(IntWritable.class),
					MapFile.Writer.valueClass(Text.class), MapFile.Writer.compression(CompressionType.NONE));
			for (int i = 0; i < 1024; i++) {
				key.set(i + 1);
				value.set(DATA[i % DATA.length]);
				writer.append(key, value);
			}
			writer.close();

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void fix(String mapfilePath) {
		Configuration conf = new Configuration();
		FileSystem fs;

		Path map = new Path(mapfilePath);
		Path mapData = new Path(map, MapFile.DATA_FILE_NAME);

		// Get key and value types from data sequence file
		SequenceFile.Reader reader;
		Writable key, value;

		try {
			fs = FileSystem.get(URI.create(mapfilePath), conf);
			reader = new SequenceFile.Reader(conf, Reader.file(mapData), Reader.bufferSize(4096), Reader.start(0));
			key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
			value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
			// long position = reader.getPosition();
			reader.close();

			// Create the map file index file
			long entries;
			entries = MapFile.fix(fs, map, key.getClass(), value.getClass(), false, conf);
			System.out.printf("Created MapFile %s with %d entries\n", map, entries);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}

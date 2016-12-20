package serialize.compress;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

/*
 * 从标准输入输入数据，压缩后，从标准输出输出数据；
 * 
 * 打包（export）成compress.jar
 * export HADOOP_CLASSPATH=compress.jar:$HADOOP_CLASSPATH
 * echo "test" | hadoop serialize.compress.StreamCompressor org.apache.hadoop.io.compress.GzipCodec  | gunzip
 * 输出压缩数据到文件
 * echo "test" | hadoop serialize.compress.StreamCompressor org.apache.hadoop.io.compress.SnappyCodec
 */
public class StreamCompressor {

	public static void main(String[] args) throws Exception {
		String codecClassname = args[0];
		Class<?> codecClass = Class.forName(codecClassname);
		Configuration conf = new Configuration();

		CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);

		// 1. 压缩数据后保存到文件
		File file = new File("out.snappy");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		CompressionOutputStream out = codec.createOutputStream(fileOutputStream);
		IOUtils.copyBytes(System.in, out, 4096, true);
		/* 2. 输出压缩数据到标准输出
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		CompressionOutputStream out = codec.createOutputStream(System.out);
		IOUtils.copyBytes(System.in, out, 4096, false);
		out.finish();
		*/
		
	}
}
// ^^ StreamCompressor

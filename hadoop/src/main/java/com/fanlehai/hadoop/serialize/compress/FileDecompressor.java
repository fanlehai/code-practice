
package com.fanlehai.hadoop.serialize.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;

/*
 * 根据文件扩展名选取相应的codec解压文件
 * 
 * export HADOOP_CLASSPATH=compress.jar
 * hadoop serialize.compress.FileDecompressor out.snappy
 * 
 */
public class FileDecompressor {

	public static void main(String[] args) throws Exception {
		String uri = args[0];
		Configuration conf = new Configuration();

		Path inputPath = new Path(uri);
		CompressionCodecFactory factory = new CompressionCodecFactory(conf);
		CompressionCodec codec = factory.getCodec(inputPath);
		if (codec == null) {
			System.err.println("No codec found for " + uri);
			System.exit(1);
		}
		
		String outputUri = CompressionCodecFactory.removeSuffix(uri, codec.getDefaultExtension());

		FileSystem.get(conf).delete(new Path(outputUri), true);
		InputStream in = null;
		FileOutputStream out = null;
		try {
			File file = new File(uri);
			FileInputStream fileInputStream = new FileInputStream(file);
			in = codec.createInputStream(fileInputStream);
			File fileOut = new File(outputUri);
			out = new FileOutputStream(fileOut);
			IOUtils.copyBytes(in, out, conf);
		} finally {
			IOUtils.closeStream(in);
			IOUtils.closeStream(out);
		}
		
		
		System.out.println("******* done! *******");
	}
}
// ^^ FileDecompressor

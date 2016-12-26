package com.fanlehai.hadoop.serialize.compress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.InputStream;
import java.io.OutputStream;

/*
 * 读取本地文件，压缩后存储到hdfs上
 * 
 * 命令行参数：
 *  org.apache.hadoop.io.compress.SnappyCodec InputResource/sort/input.txt lcom
 */


public class CompressedFileWriteFromLocal {
	public static void main(String... args) throws Exception {
		
		
		Configuration config = new Configuration();
		FileSystem hdfs = FileSystem.get(config);

		Class<?> codecClass = Class.forName(args[0]);
		CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, config);

		InputStream is = FileSystem.getLocal(config).open(new Path(args[1]));
		OutputStream os = hdfs.create(new Path(args[2] + codec.getDefaultExtension()));

		OutputStream cos = codec.createOutputStream(os);

		IOUtils.copyBytes(is, cos, config, true);

		IOUtils.closeStream(os);
		IOUtils.closeStream(is);
	}
}

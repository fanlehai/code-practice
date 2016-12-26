package com.fanlehai.hadoop.serialize.compress;

import com.hadoop.compression.lzo.LzopCodec;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.*;

/*
 * 
 * 读取hdfs上文件，进行lzo压缩保存到hdfs；
 * 读取hdfs上用lzo压缩的文件；
 * 
 * 命令行参数：
 * compress
 * 
 * 
 */

public class LzopFileReadWrite {

	public static void main(String... args) throws Exception {
		Configuration config = new Configuration();

		LzopCodec codec = new LzopCodec();
		codec.setConf(config);

		Path srcFile = new Path(args[0]);
		Path restoredFile = new Path(args[0] + ".restored");

		Path destFile = compress(srcFile, config);
		decompress(destFile, restoredFile, config);
	}

	public static Path compress(Path src, Configuration config) throws IOException {
		Path destFile = new Path(src.toString() + new LzopCodec().getDefaultExtension());

		LzopCodec codec = new LzopCodec();
		codec.setConf(config);

		FileSystem hdfs = FileSystem.get(config);
		InputStream is = null;
		OutputStream os = null;
		try {
			is = hdfs.open(src);
			os = codec.createOutputStream(hdfs.create(destFile));

			IOUtils.copyBytes(is, os, config);
		} finally {
			IOUtils.closeStream(os);
			IOUtils.closeStream(is);
		}
		return destFile;
	}

	public static void decompress(Path src, Path dest, Configuration config) throws IOException {
		LzopCodec codec = new LzopCodec();
		codec.setConf(config);

		FileSystem hdfs = FileSystem.get(config);
		InputStream is = null;
		OutputStream os = null;
		try {
			is = codec.createInputStream(hdfs.open(src));
			os = hdfs.create(dest);

			IOUtils.copyBytes(is, os, config);
		} finally {
			IOUtils.closeStream(os);
			IOUtils.closeStream(is);
		}
	}
}

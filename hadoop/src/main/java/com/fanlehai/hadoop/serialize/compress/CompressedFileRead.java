package com.fanlehai.hadoop.serialize.compress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;

import java.io.InputStream;

/*
 * 从hdfs中读取压缩文件(根据文件后缀名自动识别Codec)，标准输出
 * 
 * 命令行参数：
 * compress.snappy
 * 
 */

public class CompressedFileRead {
	public static void main(String... args) throws Exception {
		
		if (args.length != 1) {
			System.err.println("Usage: CompressedFileRead <in>");
			
			return ;
		}
		
		String inputPath = args[0];
		
		Configuration config = new Configuration();
		FileSystem hdfs = FileSystem.get(config);

		InputStream is = hdfs.open(new Path(inputPath));
		
		CompressionCodecFactory factory = new CompressionCodecFactory(config);
		CompressionCodec codec = factory.getCodec(new Path(inputPath));
		if (codec == null) {
			System.err.println("No codec found for " + inputPath);
			System.exit(1);
		}

		InputStream cis = codec.createInputStream(is);

		IOUtils.copyBytes(cis, System.out, config, true);
		
		System.out.flush();

		IOUtils.closeStream(is);
	}
}

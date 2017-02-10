package com.fanlehai.java.nio;

// Creating a very large file using mapping.
// {RunByHand}
import java.nio.*;
import java.nio.channels.*;
import java.io.*;
import static com.fanlehai.java.util.Print.*;

public class LargeMappedFiles {
	static int length = 0x8FFFFFF; // 128 MB

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		MappedByteBuffer out = new RandomAccessFile("test.dat", "rw").getChannel().map(FileChannel.MapMode.READ_WRITE,
				0, length);
		for (int i = 0; i < length; i++)
			out.put((byte) 'x');
		print("Finished writing");
		for (int i = length / 2; i < length / 2 + 6; i++)
			printnb((char) out.get(i));
	}
}

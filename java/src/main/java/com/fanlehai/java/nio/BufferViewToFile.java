package com.fanlehai.java.nio;

import static com.fanlehai.java.util.Print.print;
import static com.fanlehai.java.util.Print.printnb;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

public class BufferViewToFile {

	static int BSIZE = 1024;

	public static void main(String[] args) throws Exception {

		// 写
		ByteBuffer bb = ByteBuffer.allocate(BSIZE);
		IntBuffer ib = bb.asIntBuffer();
		// Store an array of int:
		ib.put(new int[] { 11, 42, 47, 99, 143, 811, 1016 });
		
		FileChannel fc;
		fc = new FileOutputStream("data2.txt").getChannel();
		fc.write(bb);
		fc.close();

		
		// 读
		fc = new FileInputStream("data2.txt").getChannel();
		IntBuffer ibr = ((ByteBuffer) bb.rewind()).asIntBuffer();
		printnb("Int Buffer ");
		while (ibr.hasRemaining())
			print(ibr.position() + " -> " + ibr.get() + ", ");
		//print();
		
		
	}

}

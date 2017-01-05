package com.fanlehai.java.io;

import java.io.*;

public class FormattedMemoryInput {
	public static void main(String[] args) throws IOException {
		try {
			DataInputStream in = new DataInputStream(
					new ByteArrayInputStream(BufferedInputFile.read("FormattedMemoryInput.java").getBytes()));
			while (true)
				System.out.print((char) in.readByte());
			
			//下面这个方法可以返还还有多少字节可以读取
			//in.available();
			
		} catch (EOFException e) {
			System.err.println("End of stream");
		}
	}
} /* (Execute to see output) */// :~

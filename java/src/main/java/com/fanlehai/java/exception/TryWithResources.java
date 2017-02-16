package com.fanlehai.java.exception;

import java.io.BufferedReader;
import java.io.FileReader;

public class TryWithResources {

	public static void main(String[] args) {
		try (final FileReader reader = new FileReader(
				"src/main/java/com/fanlehai/java/exception/TryWithResources.java");
				BufferedReader bufferedReader = new BufferedReader(reader);) {
			// 1. 读取第一种方式：
			// int i = 0;
			// while((i = reader.read()) != -1){
			// System.out.write(i);
			// }

			// 2. 用BufferedReader更好读取字符集
			char[] cbuf = new char[10];
			while (bufferedReader.read(cbuf) != -1) {
				System.out.print(new String(cbuf));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

package com.fanlehai.java.memoryleak;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * 命令行参数：-XX:+HeapDumpOnOutOfMemoryError -Xmx100m
 */

public class CreateOom {
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		List<String> list = new ArrayList<String>();
		while (1 < 2) {
			list.add("OutOfMemoryError soon".intern());
		}

	}
}

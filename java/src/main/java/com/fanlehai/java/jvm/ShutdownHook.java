package com.fanlehai.java.jvm;

import java.util.Date;

public class ShutdownHook {

	public static void main(String[] args) {

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("shutting down JVM at time : " + new Date());
			}
		});
	}
}

package com.fanlehai.java.thread;

public class VolatileTest {

	public static volatile int race = 0;

	public static void increase() {
		race++;
	}

	private static final int THREADS_COUNT = 20;

	public static void main(String[] args) {

		Thread[] threads = new Thread[THREADS_COUNT];
		for (int i = 0; i < threads.length; i++) {
			// Thread thread = threads[i];
			threads[i] = new Thread(new Runnable() {
				public void run() {
					for (int j = 0; j < 10000; j++) {
						increase();
					}
				}
			});
			threads[i].start();
		}

		// 等待所有线程都运行结束
		while (Thread.activeCount() > 1) {
			Thread.yield();
		}
		System.out.println("test volatile race : " + Integer.toString(race));
		// 每次运行得到的结果都是不同的
	}

}

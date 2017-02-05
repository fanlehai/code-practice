package com.fanlehai.java.thread;

import java.util.concurrent.atomic.AtomicLong;

public final class FalseSharing implements Runnable {

	public final static int NUM_THREADS = 8; // change
	public final static long ITERATIONS = 500L * 1000L * 1000L;
	private final int arrayIndex;

	private static PaddedAtomicLong[] longs = new PaddedAtomicLong[NUM_THREADS];
	static {
		for (int i = 0; i < longs.length; i++) {
			longs[i] = new PaddedAtomicLong();
		}
	}

	public FalseSharing(final int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}

	public static void main(final String[] args) throws Exception {
		final long start = System.nanoTime();
		runTest();
		System.out.println("duration = " + (System.nanoTime() - start));
	}

	private static void runTest() throws InterruptedException {
		Thread[] threads = new Thread[NUM_THREADS];

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new FalseSharing(i));
		}

		for (Thread t : threads) {
			t.start();
		}

		for (Thread t : threads) {
			t.join();
		}
	}

	public void run() {
		long i = ITERATIONS + 1;
		while (0 != --i) {
			longs[arrayIndex].set(i);
		}
	}

	// 这段代码用于使用PaddedAtomicLong中的成员，方式jvm对没有使用的变量进行优化处理，
	// 使得补齐缓存行解决伪共享的行为失效
	public static long sumPaddingToPreventOptimisation(final int index) {
		PaddedAtomicLong v = longs[index];
		return v.p1 + v.p2 + v.p3 + v.p4 + v.p5 + v.p6;
	}

	public static class PaddedAtomicLong extends AtomicLong {
		// 下面这些变量只是为扩充PaddedAtomicLong的类大小，
		// 使得缓存行的64个字节不能同时放下两个PaddedAtomicLong对象
		public volatile long p1, p2, p3, p4, p5, p6 = 7L;
	}

}

// 8线程
// 没有伪共享运行时间： duration = 20126566694
// 伪共享运行时间： duration = 33444313154


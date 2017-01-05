package com.fanlehai.java.thread;

// Interrupting a blocked thread.
import java.util.concurrent.*;
import java.io.*;

class SleepBlocked implements Runnable {
	public void run() {
		try {
			System.out.println("Sleeping :");
			TimeUnit.SECONDS.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("InterruptedException");
		}
		System.out.println("Exiting SleepBlocked.run()");
	}
}

class IOBlocked implements Runnable {
	private InputStream in;

	public IOBlocked(InputStream is) {
		in = is;
	}

	public void run() {
		try {
			System.out.println("Waiting for read():");
			in.read();
		} catch (IOException e) {
			if (Thread.currentThread().isInterrupted()) {
				System.out.println("Interrupted from blocked I/O");
			} else {
				throw new RuntimeException(e);
			}
		}
		System.out.println("Exiting IOBlocked.run()");
	}
}

class SynchronizedBlocked implements Runnable {
	public synchronized void f() {
		while (true) // Never releases lock
			Thread.yield();
	}

	public SynchronizedBlocked() {
		new Thread() {
			public void run() {
				f(); // Lock acquired by this thread
			}
		}.start();
	}

	public void run() {
		System.out.println("Trying to call f()");
		f();
		System.out.println("Exiting SynchronizedBlocked.run()");
	}
}

public class Interrupting {
	private static ExecutorService exec = Executors.newCachedThreadPool();

	static void test(Runnable r) throws InterruptedException {
		Future<?> f = exec.submit(r);
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("Interrupting " + r.getClass().getName());
		f.cancel(true); // Interrupts if running
		System.out.println("Interrupt sent to " + r.getClass().getName());
	}

	public static void main(String[] args) throws Exception {
		
		/*
		 * 单个线程单独中断
		 * 
		 * 
		 */
//		test(new SleepBlocked());
//		test(new IOBlocked(System.in));
//		test(new SynchronizedBlocked());
		
		
		/*
		 * 所有线程统一中断
		 * 
		 * 
		 */
		exec.execute(new SleepBlocked());
		exec.execute(new IOBlocked(System.in));
		exec.execute(new SynchronizedBlocked());
		
		exec.shutdownNow();
		TimeUnit.SECONDS.sleep(1);
		System.in.close();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Aborting with System.exit(0)");
		System.exit(0); // ... since last 2 interrupts failed
	}
} /*
	 * Output: (95% match) Interrupting SleepBlocked InterruptedException
	 * Exiting SleepBlocked.run() Interrupt sent to SleepBlocked Waiting for
	 * read(): Interrupting IOBlocked Interrupt sent to IOBlocked Trying to call
	 * f() Interrupting SynchronizedBlocked Interrupt sent to
	 * SynchronizedBlocked Aborting with System.exit(0)
	 */// :~

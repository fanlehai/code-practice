package com.fanlehai.java.thread;

/**
 * 同步线程
 */
public class SyncThread implements Runnable {
	private int count;
	private volatile  String name ;

	public SyncThread(String str) {
		count = 1;
		this.name = str;
	}

	public void run() {
		
		// 1. 下面这句只对当前实例对象互斥，多个线程同时使用这个对象是互斥的（但是如果有两个此实例对象，他们之间并不互斥，相互不干扰）
		//synchronized (this) {
		// 2. 下面这句表示对于所有SyncThread实例对象都是互斥的
		synchronized (SyncThread.class) {
			for (int i = 0; i < 5; i++) {
				try {
					System.out.println("ThreadName : " + Thread.currentThread().getName() + "  ClassName : " +
										name + "  :   " + (count++));
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int getCount() {
		return count;
	}
}
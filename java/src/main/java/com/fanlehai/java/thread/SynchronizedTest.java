package com.fanlehai.java.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;



public class SynchronizedTest {

	public static void main(String[] args) {

		/*
		 * 
		 * 	synchronized
		 * 
		 */
//		System.out.println("修饰一个代码块:synchronized (this), 一个实例对象");
//		// 修饰一个代码块:synchronized (this), 只有一个对象syncThread，只有一个代码块，2个线程
//		SyncThread syncThread = new SyncThread("syncThread");
//		Thread thread1 = new Thread(syncThread, "SyncThread1");
//		Thread thread2 = new Thread(syncThread, "SyncThread2");
//		thread1.start();
//		thread2.start();
//
//		// 等待所有线程都运行结束
//		while (Thread.activeCount() > 1) {
//			Thread.yield();
//		}
//
//		System.out.println("修饰一个代码块:synchronized (this), 两个实例对象");
//		// 修饰一个代码块:synchronized (this), 这里有两个对象，meger对象对应一个互斥代码块，2个线程访问对象不同，互补干扰
//		SyncThread syncThread3 = new SyncThread("syncThread3");
//		SyncThread syncThread4 = new SyncThread("syncThread4");
//		Thread thread3 = new Thread(syncThread3, "SyncThread3");
//		Thread thread4 = new Thread(syncThread4, "SyncThread4");
//		thread3.start();
//		thread4.start();
		
		
		
		ExecutorService exec = Executors.newCachedThreadPool();
		//for (int i = 0; i < 5; i++){
//			exec.execute(new SyncThreadMethod());
//		}
		Data data = new Data();
		exec.execute(new SyncThreadMethod(data));
		exec.execute(new SyncThreadMethod2(data));
		
		
		
		
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		for(int j = 0; j <5; j++){
//			
//			try {
//				Thread.sleep(5000);
//				SyncThreadMethod.data.drop();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
	

	}

}

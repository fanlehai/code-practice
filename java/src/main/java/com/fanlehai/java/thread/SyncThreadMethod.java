package com.fanlehai.java.thread;

class Data {

	synchronized void Test() {
		System.out.println(Thread.currentThread() + "Test in");
		// for (int i = 0; i < 5; i++) {
		// System.out.println(Thread.currentThread() + " : " +
		// Integer.toString(i));
		try {
			Thread.sleep(1000);
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }
		System.out.println(Thread.currentThread() + "Test out");
	}

	synchronized void drop() {

		System.out.println(Thread.currentThread() + "drop");// 
		notifyAll();
	}
}

class SyncThreadMethod2 implements Runnable {

	Data data2;

	SyncThreadMethod2(Data data) {
		data2 = data;
	}
	// static Data data = new Data();

	public void run() {
		while (!Thread.interrupted()) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data2.drop();

		}

	}

}

public class SyncThreadMethod implements Runnable {

	Data data;

	SyncThreadMethod(Data da) {
		data = da;
	}

	public void run() {

		while (!Thread.interrupted()) {

			 try {
			 Thread.sleep(100);
			 } catch (InterruptedException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
			 }
			data.Test();

		}
	}

}

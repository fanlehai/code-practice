package com.fanlehai.java.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class ProducerQueue implements Runnable {

	BlockingQueue<Integer> queue;

	ProducerQueue(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i = 0;
		while (true) {

			try {
				System.out.println(Thread.currentThread().getName()+" : " + Integer.toString(i));
				queue.put(i++);
				TimeUnit.MILLISECONDS.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}

class ConsumerQueue implements Runnable {

	BlockingQueue<Integer> queue;

	ConsumerQueue(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {

			try {
				System.out.println(Thread.currentThread().getName() +" : "+ queue.take().toString());
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}

public class ProducerConsumer {

	public static void main(String[] args) {

		BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
		Thread proThread = new Thread(new ProducerQueue(queue));
		Thread conThread = new Thread(new ConsumerQueue(queue));
		Thread conThread1 = new Thread(new ConsumerQueue(queue));
		proThread.start();
		conThread.start();
		conThread1.start();
	}

}

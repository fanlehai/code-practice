package com.fanlehai.java.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.tools.ant.taskdefs.PathConvert.MapEntry;

class MyThread extends Thread {
	private Object object;

	public MyThread(Object object) {
		this.object = object;
	}

	public void run() {
		System.out.println("before unpark");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 获取blocker
		System.out.println("Blocker info " + LockSupport.getBlocker((Thread) object));
		// 释放许可
		LockSupport.unpark((Thread) object);
		// 再次获取blocker
		System.out.println("Blocker info " + LockSupport.getBlocker((Thread) object));
		System.out.println("after unpark");
	}
}

public class ParkAndUnparkDemo {
	public static void main(String[] args) {
		MyThread myThread = new MyThread(Thread.currentThread());
		myThread.start();
		System.out.println("before park");
		// 获取许可
		LockSupport.park("ParkAndUnparkDemo");
		System.out.println("after park");
	}
}
//  输出
/*
before park
before unpark
Blocker info ParkAndUnparkDemo
Blocker info ParkAndUnparkDemo
after unpark
after park
 */

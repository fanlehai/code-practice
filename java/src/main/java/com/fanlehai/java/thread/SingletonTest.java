package com.fanlehai.java.thread;

public class SingletonTest {
	
	private volatile static SingletonTest sigtionTest = null;
	public SingletonTest getInstance() {
		if (sigtionTest == null) {
			synchronized (SingletonTest.class) {
				if (sigtionTest == null) {
					sigtionTest = new SingletonTest();
				}
			}
		}
		return sigtionTest;
	}

}

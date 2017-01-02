package com.fanlehai.java.thread;

import java.util.concurrent.*;

public class DaemonThreadFactory implements ThreadFactory {
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setDaemon(true);
		return t;
	}
} /// :~

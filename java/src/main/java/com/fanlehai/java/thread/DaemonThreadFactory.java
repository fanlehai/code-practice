package com.fanlehai.java.thread;

import java.util.concurrent.*;

public class DaemonThreadFactory implements ThreadFactory {
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		// 这里设置线程相关属性：是否是后台线程、优先级等
		t.setDaemon(true);
		return t;
	}
}

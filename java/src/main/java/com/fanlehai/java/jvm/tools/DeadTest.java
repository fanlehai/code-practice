package com.fanlehai.java.jvm.tools;

public class DeadTest {

	/**
	 * 线程死锁等待演示
	 */
	static class SynAddRunalbe implements Runnable {
		int a, b;

		public SynAddRunalbe(int a, int b) {
			this.a = a;
			this.b = b;
		}

		public void run() {
			// [-128, 127]之间的数字会被缓存
			// valueof返回a的Integer对象
			synchronized (Integer.valueOf(a)) {
				synchronized (Integer.valueOf(b)) {
					System.out.println(a + b);
				}
			}
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(new SynAddRunalbe(1, 2)).start();
			new Thread(new SynAddRunalbe(2, 1)).start();
		}
	}

}

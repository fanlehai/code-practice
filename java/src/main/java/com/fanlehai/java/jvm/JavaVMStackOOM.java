package com.fanlehai.java.jvm;


/*
 * VM Args: -Xss2M
 * 
 */

public class JavaVMStackOOM {
	
	private void dontSTOP() {
		while (true) {
			
		}
	}
	
	public void stackLeakByThread(){
		while (true) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					dontSTOP();
				}
			});
			thread.start();
		}
	}
	
	public static void main(String[] args) {
		JavaVMStackOOM oom = new JavaVMStackOOM();
		oom.stackLeakByThread();
	}

}

package com.fanlehai.java.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;


class CallableStatement implements Callable<Throwable> {
    private final CountDownLatch startLatch = new CountDownLatch(1);

    public Throwable call() throws Exception {
    	System.out.println("Throwable call()");
    	TimeUnit.MILLISECONDS.sleep(1000);
    	System.out.println("Throwable call() wake");
        try {
            startLatch.countDown();
            //originalStatement.evaluate();
            System.out.println("22222222222");
        } catch (Exception e) {
            throw e;
        } catch (Throwable e) {
            return e;
        }
        return null;
    }

    public void awaitStarted() throws InterruptedException {
        startLatch.await();
    }
}



public class ThreadGroupTest {
	
	
	
	public static void main(String[] args) {
		
		
		CallableStatement callable = new CallableStatement();
        FutureTask<Throwable> task = new FutureTask<Throwable>(callable);
        ThreadGroup threadGroup = new ThreadGroup("FailOnTimeoutGroup");
        Thread thread = new Thread(threadGroup, task, "Time-limited test");
        thread.setDaemon(true);
        thread.start();
        
        try {
        	callable.awaitStarted();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("end");
        
	}

}

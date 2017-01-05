package com.fanlehai.java.thread;

// Interrupting a blocked NIO channel.
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.concurrent.*;
import java.io.*;

class NIOBlocked implements Runnable {
	private final SocketChannel sc;

	public NIOBlocked(SocketChannel sc) {
		this.sc = sc;
	}

	public void run() {
		try {
			System.out.println("Waiting for read() in " + this);
			sc.read(ByteBuffer.allocate(1));
		} catch (ClosedByInterruptException e) {
			System.out.println("ClosedByInterruptException");
		} catch (AsynchronousCloseException e) {
			System.out.println("AsynchronousCloseException");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Exiting NIOBlocked.run() " + this);
	}
}

public class NIOInterruption {
	public static void main(String[] args) throws Exception {
		ExecutorService exec = Executors.newCachedThreadPool();
		ServerSocket server = new ServerSocket(3080);
		InetSocketAddress isa = new InetSocketAddress("localhost", 3080);
		SocketChannel sc1 = SocketChannel.open(isa);
		SocketChannel sc2 = SocketChannel.open(isa);
		
		// 方法一：通过分开启动两个线程
//		Future<?> f = exec.submit(new NIOBlocked(sc1));
//		exec.execute(new NIOBlocked(sc2));
//		exec.shutdown();
//		TimeUnit.SECONDS.sleep(1);
//		// Produce an interrupt via cancel:
//		f.cancel(true);
//		TimeUnit.SECONDS.sleep(1);
//		// Release the block by closing the channel:
//		sc2.close();
		
		// 方法二：一起启动管理
		exec.execute(new NIOBlocked(sc1));
		exec.execute(new NIOBlocked(sc2));
		TimeUnit.SECONDS.sleep(1);
		exec.shutdownNow();
	}
} /*
	 * Output: (Sample) 
	 * Waiting for read() in NIOBlocked@7a84e4 
	 * Waiting for read() in NIOBlocked@15c7850 
	 * ClosedByInterruptException 
	 * Exiting NIOBlocked.run() NIOBlocked@15c7850 
	 * AsynchronousCloseException 
	 * Exiting NIOBlocked.run() NIOBlocked@7a84e4
	 */// :~

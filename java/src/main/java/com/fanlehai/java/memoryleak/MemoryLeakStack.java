package com.fanlehai.java.memoryleak;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MemoryLeakStack<T> {

	private List<T> list = new ArrayList<>(10);
	private int nSize = 0;
	
//	public T pop() {
//		if (nSize<1) {
//			return null;
//		}
//		nSize--;
//		return list.get(nSize);
//	}
	
	public void push(T value){
//		list.add(value);
//		nSize++;
		//list.set(0, value);
		list.add(0, value);
	}
	
	public static void main(String[] args) throws InterruptedException {
		MemoryLeakStack<Integer> slist = new MemoryLeakStack<>();
		while(true){
			slist.push(new Integer(1000));
			TimeUnit.MICROSECONDS.sleep(1000);
		}
	}
}

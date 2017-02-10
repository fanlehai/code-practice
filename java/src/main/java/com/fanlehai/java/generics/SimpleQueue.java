package com.fanlehai.java.generics;

// A different kind of container that is Iterable
import java.util.*;

public class SimpleQueue<T> implements Iterable<T> {
	private LinkedList<T> storage = new LinkedList<T>();

	public void add(T t) {
		storage.offer(t);
		System.out.println(this.getClass().getName() + " : add(T t)");
	}

	public T get() {
		return storage.poll();
	}

	public Iterator<T> iterator() {
		return storage.iterator();
	}
} /// :~

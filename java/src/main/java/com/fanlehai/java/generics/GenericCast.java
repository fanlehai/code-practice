package com.fanlehai.java.generics;

import java.util.ArrayList;

class FixedSizeStack<T> {
	private int index = 0;
	private Object[] storage;

	public FixedSizeStack(int size) {
		storage = new Object[size];
	}

	public void push(T item) {
		storage[index++] = item;
	}

	@SuppressWarnings("unchecked")
	public T pop() {
		if (index-1<0) {
			return null;
		}
		return  (T) storage[--index];
	}
}

public class GenericCast {
	public static final int SIZE = 10;

	public static void main(String[] args) {
		FixedSizeStack<String> strings = new FixedSizeStack<String>(SIZE);
		for (String s : "A B C D E F G H I J".split(" "))
			strings.push(s);
		for (int i = 0; i < SIZE; i++) {
			String s = strings.pop();
			System.out.print(s + " ");
			System.out.println(strings.pop().getClass().getName());
		}
	}
} 
/* Output:
J java.lang.String
H java.lang.String
F java.lang.String
D java.lang.String
B java.lang.String
*/

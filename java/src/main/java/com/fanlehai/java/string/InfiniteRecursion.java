package com.fanlehai.java.string;

// Accidental recursion.
// {RunByHand}
import java.util.*;

public class InfiniteRecursion {
	public String toString() {
		// 这里的this也会调用此toStrng方法，导致递归循环栈溢出
		// 把this换成super.toString即可；
		return " InfiniteRecursion address: " + this + "\n";
	}

	public static void main(String[] args) {
		List<InfiniteRecursion> v = new ArrayList<InfiniteRecursion>();
		for (int i = 0; i < 10; i++)
			v.add(new InfiniteRecursion());
		System.out.println(v);
		
	}
} /// :~

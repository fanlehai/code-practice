// Utilities to simplify generic container creation
// by using type argument inference.
package com.fanlehai.java.util;

import java.util.*;

public class CreateObject {
	public static <K, V> Map<K, V> map() {
		return new HashMap<K, V>();
	}

	public static <T> List<T> list() {
		return new ArrayList<T>();
	}

	public static <T> LinkedList<T> lList() {
		return new LinkedList<T>();
	}

	public static <T> Set<T> set() {
		return new HashSet<T>();
	}

	public static <T> Queue<T> queue() {
		return new LinkedList<T>();
	}

	// Examples:
	public static void main(String[] args) {
		Map<String, List<String>> sls = CreateObject.map();
		List<String> ls = CreateObject.list();
		LinkedList<String> lls = CreateObject.lList();
		Set<String> ss = CreateObject.set();
		Queue<String> qs = CreateObject.queue();
	}
} /// :~

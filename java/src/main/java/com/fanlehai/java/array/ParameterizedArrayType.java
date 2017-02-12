package com.fanlehai.java.array;

import java.util.Arrays;

class ClassParameter<T> {
	public T[] f(T[] arg) {
		return arg;
	}
}

class MethodParameter {
	public static <T> T[] f(T[] arg) {
		return arg;
	}
}

public class ParameterizedArrayType {
	public static void main(String[] args) {
		Integer[] ints = { 1, 2, 3, 4, 5 };
		Double[] doubles = { 1.1, 2.2, 3.3, 4.4, 5.5 };
		Integer[] ints2 = new ClassParameter<Integer>().f(ints);
		System.out.println(Arrays.deepToString(ints2));
		Double[] doubles2 = new ClassParameter<Double>().f(doubles);
		System.out.println(Arrays.deepToString(doubles2));
		ints2 = MethodParameter.f(ints);
		System.out.println(Arrays.deepToString(ints2));
		doubles2 = MethodParameter.f(doubles);
		System.out.println(Arrays.deepToString(doubles2));
	}
} /// :~

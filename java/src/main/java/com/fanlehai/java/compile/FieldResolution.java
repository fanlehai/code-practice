package com.fanlehai.java.compile;

public class FieldResolution {

	interface Interface0 {
		int A = 0;
	}

	interface Interface1 extends Interface0 {
		int A = 1;
	}

	interface Interface2 {
		int A = 2;
	}

	static class Parent implements Interface1 {
		public static int A = 3;
	}

	static class Sub extends Parent implements Interface2 {
		// 如果注释了这一句，那么Parent和Interface2同时出现A字段名，编译器拒绝编译
		public static int A = 4;
	}

	public static void main(String[] args) {
		System.out.println(Sub.A);
	}
}

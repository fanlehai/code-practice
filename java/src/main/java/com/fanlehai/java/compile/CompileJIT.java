package com.fanlehai.java.compile;


/*
 * -XX:+PrintCompilation
 * 要求虚拟机在即是编译时把编译成本地代码的方法名称打印出来：-XX:+PrintCompilation
 * 要求虚拟机输出方法内联信息；-XX:+PrintInlining
 * 
 */


public class CompileJIT {

	public static final int NUM = 15000;

	public static int doubleValue(int i) {
		// 这个空循环用于后面演示JIT代码优化过程
		for (int j = 0; j < 100000; j++)
			;
		return i * 2;
	}

	public static long calcSum() {
		long sum = 0;
		for (int i = 1; i <= 100; i++) {
			sum += doubleValue(i);
		}
		return sum;
	}

	public static void main(String[] args) {
		for (int i = 0; i < NUM; i++) {
			calcSum();
		}
	}

}

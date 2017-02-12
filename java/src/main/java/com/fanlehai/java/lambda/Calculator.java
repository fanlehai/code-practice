package com.fanlehai.java.lambda;

interface pra<T>{
	void test();
}

class Ex{
	<T> Ex(pra<T> p){
		p.test();
	};
}

public class Calculator {
	interface IntegerMath {
		int operation(int a, int b);
	}

	public int operateBinary(int a, int b, IntegerMath op) {
		return op.operation(a, b);
	}

	public static void main(String... args) {

		Calculator myApp = new Calculator();
		IntegerMath addition = (a, b) -> a + b;
		IntegerMath subtraction = (a, b) -> a - b;
		System.out.println("40 + 2 = " + myApp.operateBinary(40, 2, addition));
		System.out.println("20 - 10 = " + myApp.operateBinary(20, 10, subtraction));
		
		// 用于构造函数中
		Ex ex = new Ex(()->System.out.println("aaa"));
	}
}

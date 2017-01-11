package com.fanlehai.java.grammar;
// Direct field access is determined at compile time.

class Super {
	public int field = 0;

	public int getField() {
		return field;
	}
}

class Sub extends Super {
	public int field = 1;

	public int getField() {
		return field;
	}

	public int getSuperField() {
		return super.field;
	}
	
	public String Test(){
		return "test";
	}
}

public class FieldAccess {
	public static void main(String[] args) {
		Super sup = new Sub(); // Upcast
		// sup.test(); 出错
		System.out.println("sup.field = " + sup.field + ", sup.getField() = " + sup.getField());
		Sub sub = new Sub();
		System.out.println("sub.field = " + sub.field + ", sub.getField() = " + sub.getField()
				+ ", sub.getSuperField() = " + sub.getSuperField());
		sub.Test();
	}
} /* Output:
sup.field = 0, sup.getField() = 1
sub.field = 1, sub.getField() = 1, sub.getSuperField() = 0
*///:~

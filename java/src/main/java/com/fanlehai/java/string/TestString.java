package com.fanlehai.java.string;

public class TestString {

	public static void main(String[] args) {
		// 1
		String a = "hello2";
		String b = "hello" + 2;
		System.out.println((a == b));
		System.out.println("--------------------");
		// 2
		String a1 = "hello2";
		String b1 = "hello";
		String c1 = b1 + 2;
		System.out.println((a1 == c1));
		System.out.println("--------------------");
		// 3
		String a2 = "hello2";
		final String b2 = "hello";
		String c2 = b2 + 2;
		System.out.println((a2 == c2));
		System.out.println("--------------------");
		// 4
		String a3 = "hello2";
        final String b3 = getHello();
        String c3 = b3 + 2;
        System.out.println((a3 == c3));
        System.out.println("--------------------");
        // 5
        String a4 = "hello";
        String b4 =  new String("hello");
        String c4 =  new String("hello");
        String d4 = b4.intern();
 
        System.out.println(a4==b4);
        System.out.println(b4==c4);
        System.out.println(b4==d4);
        System.out.println(a4==d4);
        System.out.println("--------------------");
	}
	
	public static String getHello() {
        return "hello";
    }
}
/*
true
--------------------
false
--------------------
true
--------------------
false
--------------------
false
false
false
true
--------------------
 */

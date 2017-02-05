package com.fanlehai.java.string;

public class ReverseStringTest {

	public static void main(String[] args) {

		String string = "HelloWorld";

		System.out.println(new StringBuffer(string).reverse());

		System.out.println(new StringBuilder(string).reverse());

		System.out.println(reverse(string));
		
		System.out.println(reverseRecursively(string));

		reverseCharbuf();

	}

	public static void reverseCharbuf() {
		char[] text = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		// char[] d = new char[]{'1','2','3'};
		System.out.println(text);
		for (int i = 0; i < text.length; ++i) {
			int mid = text.length / 2;
			if (i < mid) {
				char tmp = text[i];
				text[i] = text[text.length - 1 - i];
				text[text.length - 1 - i] = tmp;
			}
		}

		System.out.println(text);
	}

	public static String reverse(String source) {
		if (source == null || source.isEmpty()) {
			return source;
		}
		StringBuffer reverse = new StringBuffer();
		for (int i = source.length() - 1; i >= 0; i--) {
			reverse.append(source.charAt(i));
		}

		return reverse.toString();
	}

	public static String reverseRecursively(String str) {

		// base case to handle one char string and empty string
		if (str.length() < 2) {
			return str;
		}

		return reverseRecursively(str.substring(1)) + str.charAt(0);

	}

}

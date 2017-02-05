package com.fanlehai.java.string;

import java.util.regex.Pattern;

public class RegularExpressionDemo {
	public static void main(String[] args) {

		String[] strInput = new String[] { "+11", "-11", "+1r", "-1r", "+rr", "-rr", "rr", "234.33", "+234.33",
				"-234.33", "-23.", "+23.", "-23.a", "+23.a" };

		Pattern pattern = Pattern.compile("^[+-]?\\d+\\.?\\d*$");
		for (String string : strInput) {
			if (pattern.matcher(string).matches()) {
				System.out.println(string + ": 是数字");
			} else {
				System.out.println(string + ": 不是数字");
			}
		}
	}
}

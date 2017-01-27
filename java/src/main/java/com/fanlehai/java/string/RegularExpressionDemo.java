package com.fanlehai.java.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionDemo {

	public static void main(String[] args) {
		
		String s1 = "sadf", s2="23adsfds",s3="232342";
		String regex = "(.)*(\\d)(.)*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s1);
		if (matcher.matches()) {
			System.out.println("s1有数字");
		}else {
			System.out.println("s1没有数字");
		}
		matcher = pattern.matcher(s2);
		if (matcher.matches()) {
			System.out.println("s2有数字");
		}else {
			System.out.println("s2没有数字");
		}
		matcher = pattern.matcher(s3);
		if (matcher.matches()) {
			System.out.println("s3有数字");
		}else {
			System.out.println("s3没有数字");
		}
	}
}

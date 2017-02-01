package com.fanlehai.java.junit;

public class BatchCheckUtil {
	private static boolean validInt(String p) {
		try {
			//Integer.parseInt(p);
			System.out.println(Integer.parseInt(p));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}

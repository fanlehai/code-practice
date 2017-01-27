package com.fanlehai.java.string;

public class StringPrintAll {

	private static void permutation(String perm, String word) {
		if (word.isEmpty()) {
			System.err.println(perm + word);
		} else {
			for (int i = 0; i < word.length(); i++) {
				permutation(perm + word.charAt(i), word.substring(0, i) + word.substring(i + 1, word.length()));
			}
		}
	}

	public static void main(String[] args) {
		permutation("", "123");
	}

}

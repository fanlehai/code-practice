package com.fanlehai.java.string;

// 如果两个字符串含有相同字符，只是顺序不同，我们称之为：Anagram
public class AnagramTest {

	public static boolean isAnagram(String s1, String s2) {
		boolean bReturn = false;
		if (s1.length() == s2.length()) {
			int iRet = 0;
			for (int i = 0; i < s1.length(); ++i) {
				iRet ^= s1.charAt(i);
			}

			for (int j = 0; j < s2.length(); ++j) {
				iRet ^= s2.charAt(j);
			}
			if (iRet == 0) {
				bReturn = true;
			}
		}
		return bReturn;
	}

	public static void main(String[] args) {
		System.out.println("word dwor = " + isAnagram("word", "dwor"));
		System.out.println("word dw2r = " + isAnagram("word", "dw2r"));
	}

}

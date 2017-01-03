package com.fanlehai.java.jvm;


/**
 * VM Args：-XX:PermSize=10M -XX:MaxPermSize=10M
 * 
 * 此实例在jdk8中无法实现，因为上面两个属性已经被移除
 */
public class RuntimeConstantPoolOOM {

	public static void main(String[] args) {
		String str1 = new StringBuilder("中国").append("钓鱼岛").toString();
		System.out.println(str1.intern() == str1);

		String str2 = new StringBuilder("ja").append("va").toString();
		System.out.println(str2.intern() == str2);
	}
}

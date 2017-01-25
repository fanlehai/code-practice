package com.fanlehai.java.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ConcurrentModExceptionDemo {
	public static void main(String args[]) { 
		List<String> listOfPhones = new ArrayList<String>(
				Arrays.asList( "iPhone 6S", "iPhone 6", "iPhone 5", "Samsung Galaxy 4", "Lumia Nokia")); 
		System.out.println("list of phones: " + listOfPhones);
		
		for (String string : listOfPhones) {
			if (string.startsWith("iPhone")) {
				// listOfPhones.remove(string);  // 会有java.util.ConcurrentModificationException异常
			}
		}
		
		// 这个是正确的做法
		for (Iterator<String> iterator = listOfPhones.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			if (string.startsWith("iPhone")) {
				iterator.remove();
			}
			
		}
		
		System.out.println("list of phones: " + listOfPhones);

	}
}

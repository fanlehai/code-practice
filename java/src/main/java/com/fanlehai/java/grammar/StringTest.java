package com.fanlehai.java.grammar;

import java.util.HashMap;

public class StringTest {
	
	public static void main(String[] args){
		
		String s1 = "Java";
		String s2 = "Java";
		System.out.println("s1 : " + s1);
		System.out.println("s2 : " + s2);
		
		System.out.println("===========");
		s1 = "C++";
		System.out.println("s1 : " + s1);
		System.out.println("s2 : " + s2);
		
		System.out.println("===========");
		String str1=new String("hello");
		String str2=new String("hello");
		
		if(str1==str2) {
		     System.out.println("str1==str2 is TRUE");
		} else{
		     System.out.println("str1==str2 is FALSE");
		}
		
		if(str1.equals(str2)) {
		      System.out.println("str1.equals(str2) is TRUE");
		} else { 
		      System.out.println("str1.equals(str2) is FALSE");
		}
		HashMap<String, String> map = new HashMap<String,String>();
		map.put("1", "va");
		//map.equals(o)


	}

}

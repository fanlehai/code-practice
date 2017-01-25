package com.fanlehai.java.grammar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

class BaseCast{
	public void name() {
		System.out.println("name");
	}
}


public class ClassCast extends BaseCast {
	public static void main(String[] args) {
		
		BaseCast base = new BaseCast();
		//BaseCast base = new ClassCast();
		ClassCast cast = (ClassCast)base;
		cast.name();
		try {
			cast.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> list = new ArrayList<String>();
		LinkedList<String> strings = new LinkedList<String>();
		strings.poll();
		strings.remove();
		
	}
}

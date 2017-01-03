package com.fanlehai.java.jvm;

import java.util.ArrayList;
import java.util.List;



/*
 * 
 * AM Args : -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+HeapDumpOnOutOfMemoryError
 * 
 */



public class HeapOOM {
	
	static class OOMObject{
		
	}
	
	
	public static void main(String[] args){
		
		List<OOMObject> list = new ArrayList<OOMObject>();
		
		while (true) {
			list.add(new OOMObject());
		}
		
		
	}

}

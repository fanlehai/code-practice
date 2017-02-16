package com.fanlehai.java.algorithm;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class Bubble {

	public void bubble(LinkedList<Integer> list) {
		if (list == null) {
			throw new NullPointerException();
		}
		Boolean bChange = false;
		for(int i =0; i<list.size()-1; ++i){
			if (list.get(i) > list.get(i+1)) {
				list.set(i, list.get(i) ^ list.get(i+1));
				list.set(i+1, list.get(i) ^ list.get(i+1));
				list.set(i, list.get(i) ^ list.get(i+1));
				bChange = true;
			}
		}
		if (bChange) {
			bubble(list);
		}
	}
	
	@Test
	public void bubbleTest(){
		List<Integer> list = Arrays.asList(8,9,4,5,7,6,3,2,1);
		LinkedList<Integer> linkedList = new LinkedList<>(list);
		bubble(linkedList);
		Collections.sort(list);
		Assert.assertArrayEquals(list.toArray(), linkedList.toArray());
	}
	
}

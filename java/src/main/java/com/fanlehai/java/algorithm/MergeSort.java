package com.fanlehai.java.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class MergeSort {

	public List<Integer> mergeSort(List<Integer> list) {
		if (list.size() < 2) {
			return list;
		}

		int nMid = list.size() >>> 1;
		List<Integer> left = list.subList(0, nMid);
		List<Integer> right = list.subList(nMid, list.size());

		return merge(mergeSort(left), mergeSort(right));
	}

	public List<Integer> merge(List<Integer> left, List<Integer> right) {

		List<Integer> merge = new LinkedList<>();
		int nLeft = 0, nRight = 0;

		while (nLeft < left.size() && nRight < right.size()) {
			if (left.get(nLeft) <= right.get(nRight)) {
				merge.add(left.get(nLeft));
				nLeft++;
			} else {
				merge.add(right.get(nRight));
				nRight++;
			}
		}
		while (nLeft < left.size()) {
			merge.add(left.get(nLeft));
			nLeft++;
		}
		while (nRight < right.size()) {
			merge.add(right.get(nRight));
			nRight++;
		}

		return merge;
	}

	@Test
	public void mergeSortTest() {
		List<Integer> list = Arrays.asList(8, 9, 4, 5, 7, 6, 3, 2, 1);
		LinkedList<Integer> linkedList = new LinkedList<>(list);
		List<Integer> sorted = mergeSort(linkedList);
		Collections.sort(list);

		Assert.assertArrayEquals(list.toArray(), sorted.toArray());
		//System.out.println(list);
		//System.out.println(sorted);
	}
}

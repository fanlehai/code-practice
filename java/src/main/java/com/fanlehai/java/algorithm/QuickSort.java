package com.fanlehai.java.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class QuickSort {

	public List<Integer> quickSort(List<Integer> list) {

		if (list.size() < 2) {
			return list;
		}

		List<Integer> listLow = new ArrayList<Integer>();
		List<Integer> listHigh = new ArrayList<Integer>();

		int nKeyValue = list.get(0);

		for (int i = 1; i < list.size(); ++i) {
			if (list.get(i) < nKeyValue) {
				listLow.add(list.get(i));
			} else {
				listHigh.add(list.get(i));
			}
		}

		List<Integer> listSort = quickSort(listLow);
		listSort.add(nKeyValue);
		listSort.addAll(quickSort(listHigh));
		return listSort;
	}

	@Test
	public void quickSortTest() {
		List<Integer> list = Arrays.asList(8, 9, 4, 5, 7, 6, 3, 2, 1);
		List<Integer> sorted = quickSort(new LinkedList<>(list));
		Collections.sort(list);

		Assert.assertArrayEquals(list.toArray(), sorted.toArray());
		// System.out.println(list);
		// System.out.println(sorted);
	}
}

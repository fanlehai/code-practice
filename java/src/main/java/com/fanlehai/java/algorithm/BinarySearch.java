package com.fanlehai.java.algorithm;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class BinarySearch {

	public Boolean binarySearch(List<Integer> list, Integer nValue) {

		if (list == null || list.isEmpty()) {
			return false;
		}

		int nMid = list.size() >>> 1;

		if (list.get(nMid) == nValue) {
			return true;
		}

		if (list.get(nMid) > nValue) {
			return binarySearch(list.subList(0, nMid), nValue);
		} else {
			return binarySearch(list.subList(nMid + 1, list.size()), nValue);
		}
	}

	@Test
	public void binarySearchTest() {
		Assert.assertTrue(binarySearch(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 4));
		Assert.assertFalse(binarySearch(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 11));
	}
}

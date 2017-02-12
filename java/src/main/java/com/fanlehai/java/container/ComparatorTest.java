package com.fanlehai.java.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class TV {
	private int size;
	private String brand;

	public TV(int size, String brand) {
		this.size = size;
		this.brand = brand;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
}

class SizeComparator implements Comparator<TV> {
	@Override
	public int compare(TV tv1, TV tv2) {
		int tv1Size = tv1.getSize();
		int tv2Size = tv2.getSize();

		if (tv1Size > tv2Size) {
			return 1;
		} else if (tv1Size < tv2Size) {
			return -1;
		} else {
			return 0;
		}
	}
}

public class ComparatorTest {
	public static void main(String[] args) {
		TV tv1 = new TV(55, "Samsung");
		TV tv2 = new TV(60, "Sony");
		TV tv3 = new TV(42, "Panasonic");

		ArrayList<TV> al = new ArrayList<TV>();
		al.add(tv1);
		al.add(tv2);
		al.add(tv3);

		Collections.sort(al, new SizeComparator());
		for (TV a : al) {
			System.out.println(a.getBrand());
		}
	}
}

package com.fanlehai.java.container;

import java.util.Comparator;
import java.util.TreeSet;

class Dogor {
	int size;

	Dogor(int s) {
		size = s;
	}
}

class DogSizeComparator implements Comparator<Dogor> {
	@Override
	public int compare(Dogor d1, Dogor d2) {
		return d1.size - d2.size;
	}
}

class Dog implements Comparable<Dog> {
	int size;

	Dog(int s) {
		size = s;
	}

	@Override
	public int compareTo(Dog o) {
		return o.size - this.size;
	}
}

public class ComparatorAndComparable {
	public static void main(String[] args) {
		// comparator
		TreeSet<Dogor> d = new TreeSet<Dogor>(new DogSizeComparator()); // pass
																		// comparator
		d.add(new Dogor(1));
		d.add(new Dogor(2));
		d.add(new Dogor(1));

		// Comparable
		TreeSet<Dog> d1 = new TreeSet<Dog>();
		d1.add(new Dog(1));
		d1.add(new Dog(2));
		d1.add(new Dog(1));
	}
}

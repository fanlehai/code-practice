package com.fanlehai.java.generics;

// Using Collection.checkedList().
import java.util.*;

class Cat extends Pet {
}

class Dog extends Pet {
}

public class CheckedList {
	@SuppressWarnings("unchecked")
	static void oldStyleMethod(List probablyDogs) {
		probablyDogs.add(new Cat());
	}

	public static void main(String[] args) {
		List<Dog> dogs1 = new ArrayList<Dog>();
		// Quietly accepts a Cat
		oldStyleMethod(dogs1); 
		List<Dog> dogs2 = Collections.checkedList(new ArrayList<Dog>(), Dog.class);
		try {
			// Throws an exception
			oldStyleMethod(dogs2); 
		} catch (Exception e) {
			//System.out.println(e);
			e.printStackTrace();
		}
		// Derived types work fine:
		List<Pet> pets = Collections.checkedList(new ArrayList<Pet>(), Pet.class);
		pets.add(new Dog());
		pets.add(new Cat());
		
	}
} 
/*
java.lang.ClassCastException: Attempt to insert class com.fanlehai.java.generics.Cat element into collection with element type class com.fanlehai.java.generics.Dog
	at java.util.Collections$CheckedCollection.typeCheck(Collections.java:3037)
	at java.util.Collections$CheckedCollection.add(Collections.java:3080)
	at com.fanlehai.java.generics.CheckedList.oldStyleMethod(CheckedList.java:16)
	at com.fanlehai.java.generics.CheckedList.main(CheckedList.java:24)	 
*/

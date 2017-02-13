package com.fanlehai.java.generics;

import java.util.*;
import com.fanlehai.java.util.*;

class Person {

}

class Pet {

}

public class SimplerPets {
	public static void main(String[] args) {
		Map<Person, List<? extends Pet>> petPeople = CreateObject.map();
		
		Map<Person, List<? extends Pet>> petPeople2 = new HashMap<Person, List<? extends Pet>>();
		// Rest of the code is the same...
	}
} /// :~

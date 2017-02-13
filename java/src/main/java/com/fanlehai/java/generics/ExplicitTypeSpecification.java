package com.fanlehai.java.generics;

import java.util.*;
import com.fanlehai.java.util.*;

public class ExplicitTypeSpecification {
	static void f(Map<Person, List<Pet>> petPeople) {
	}

	public static void main(String[] args) {
		f(CreateObject.<Person, List<Pet>>map());
	}
} /// :~

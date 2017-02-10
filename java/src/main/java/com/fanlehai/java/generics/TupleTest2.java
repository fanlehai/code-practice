package com.fanlehai.java.generics;
import com.fanlehai.java.util.*;
import static com.fanlehai.java.util.Tuple.*;

public class TupleTest2 {
	static TwoTuple<String, Integer> f() {
		return tuple("hi", 47);
	}

	// 此处TwoTuple被向上转型，变成一个非参数化的TwoTuple；
	static TwoTuple f2() {
		return tuple("hi", 47);
	}

	static ThreeTuple<Amphibian, String, Integer> g() {
		return tuple(new Amphibian(), "hi", 47);
	}

	static FourTuple<Vehicle, Amphibian, String, Integer> h() {
		return tuple(new Vehicle(), new Amphibian(), "hi", 47);
	}

	static FiveTuple<Vehicle, Amphibian, String, Integer, Double> k() {
		return tuple(new Vehicle(), new Amphibian(), "hi", 47, 11.1);
	}

	public static void main(String[] args) {
		TwoTuple<String, Integer> ttsi = f();
		System.out.println(ttsi);
		System.out.println(f2());
		System.out.println(g());
		System.out.println(h());
		System.out.println(k());
	}
} 
/* Output: (80% match)
(hi, 47)
(hi, 47)
(com.fanlehai.java.generics.Amphibian@7852e922, hi, 47)
(com.fanlehai.java.generics.Vehicle@4e25154f, com.fanlehai.java.generics.Amphibian@70dea4e, hi, 47)
(com.fanlehai.java.generics.Vehicle@5c647e05, com.fanlehai.java.generics.Amphibian@33909752, hi, 47, 11.1)
*///:~

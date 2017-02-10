package com.fanlehai.java.generics;

class SelfBoundSetter<T extends SelfBoundSetter<T>> {
	void set(T arg){
		System.out.println(arg.getClass().getName());
	};
}

class Setter extends SelfBoundSetter<Setter> {
	
}

public class SelfBoundingAndCovariantArguments {
	static void testA(Setter s1, Setter s2, SelfBoundSetter<Setter> sbs) {
		s1.set(s2);
		// s1.set(sbs); // Error:
		// set(Setter) in SelfBoundSetter<Setter>
		// cannot be applied to (SelfBoundSetter)
	}
	
	public static void main(String[] args){
		testA(new Setter(), new Setter(), new SelfBoundSetter());
	}
}
/* output:
 * com.fanlehai.java.generics.Setter
 */

package com.fanlehai.java.enumtest;

// Capabilities of the Enum class
import static com.fanlehai.java.util.Print.*;

enum Shrubbery {
	GROUND, CRAWLING, HANGING
}

public class EnumClass {
	public static void main(String[] args) {
		for (Shrubbery s : Shrubbery.values()) {
			print(s + " ordinal: " + s.ordinal());
			printnb(s.compareTo(Shrubbery.CRAWLING) + " ");
			printnb(s.equals(Shrubbery.CRAWLING) + " ");
			print(s == Shrubbery.CRAWLING);
			print(s.getDeclaringClass());
			print(s.name());
			print("----------------------");
		}
		// Produce an enum value from a string name:
		for (String s : "HANGING CRAWLING GROUND".split(" ")) {
			Shrubbery shrub = Enum.valueOf(Shrubbery.class, s);
			print(shrub);
		}
	}
} /* Output:
GROUND ordinal: 0
-1 false false
class com.fanlehai.java.enumtest.Shrubbery
GROUND
----------------------
CRAWLING ordinal: 1
0 true true
class com.fanlehai.java.enumtest.Shrubbery
CRAWLING
----------------------
HANGING ordinal: 2
1 false false
class com.fanlehai.java.enumtest.Shrubbery
HANGING
----------------------
HANGING
CRAWLING
GROUND
*///:~

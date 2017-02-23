package com.fanlehai.java.string;

/*
 * 命令行参数："A String"
 */

public class InternTest {

	private static char[] chars = { 'A', ' ', 'S', 't', 'r', 'i', 'n', 'g' };

	public static void main(String[] args) {
		// (0) For the base case, we just use a String literal
		String aString = "A String";

		// (1) For the first test case, we construct a String by
		// concatenating several literals. Note, however,
		// that all parts of the string are known at compile time.
		String aConcatentatedString = "A" + " " + "String";

		printResults("(1)", "aString", aString, "aConcatentatedString", aConcatentatedString);

		// (2) For the second case, construct the same String, but
		// in a way such that it's contents cannot be known
		// until runtime.
		String aRuntimeString = new String(chars);

		// Verify that (0) and (2) are the same according
		// to equals(...), but not ==
		printResults("(2)", "aString", aString, "aRuntimeString", aRuntimeString);

		// (3) For the third case, create a String object by
		// invoking the intern() method on (3).
		String anInternedString = aRuntimeString.intern();

		// Verify that (0) and (3) now reference the same
		// object.
		printResults("(3)", "aString", aString, "anInternedString", anInternedString);
		
		printResults("(3-1)", "aRuntimeString", aRuntimeString, "anInternedString", anInternedString);

		// (4) For the forth case, we explicitly construct
		// String object around a literal.
		String anExplicitString = new String("A String");

		// Verify that (0) and (4) are different objects.
		// Interning would solve this, but it would be
		// better to simply avoid constructing a new object
		// around a literal.
		printResults("(4)", "aString", aString, "anExplicitString", anExplicitString);

		// (5) For a more realistic test, compare (0) to
		// the first argument. This illustrates that unless
		// intern()'d, Strings that originate externally
		// will not be ==, even when they contain the
		// same values.
		if (args.length > 0) {
			String firstArg = args[0];
			printResults("(5)", "aString", aString, "firstArg", firstArg);

			// (6) Verify that interning works in this case
			String firstArgInterned = firstArg.intern();
			printResults("(6)", "aString", aString, "firstArgInterned", firstArgInterned);
		}
	}

	/**
	 * Utility method to print the results of equals(...) and ==
	 */
	private static void printResults(String tag, String s1Name, String s1, String s2Name, String s2) {
		System.out.println(tag);
		System.out.println("  " + s1Name + " == " + s2Name + " : " + (s1 == s2));
		System.out.println("  " + s1Name + ".equals(" + s2Name + ") : " + s1.equals(s2));
		System.out.println();
	}

}

/*
(1)
  aString == aConcatentatedString : true
  aString.equals(aConcatentatedString) : true

(2)
  aString == aRuntimeString : false
  aString.equals(aRuntimeString) : true

(3)
  aString == anInternedString : true
  aString.equals(anInternedString) : true

(4)
  aString == anExplicitString : false
  aString.equals(anExplicitString) : true

(5)
  aString == firstArg : false
  aString.equals(firstArg) : true

(6)
  aString == firstArgInterned : true
  aString.equals(firstArgInterned) : true
*/

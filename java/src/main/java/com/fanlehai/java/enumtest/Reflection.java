package com.fanlehai.java.enumtest;
// Analyzing enums using reflection.
import java.lang.reflect.*;
import java.util.*;
import com.fanlehai.java.util.*;
import static com.fanlehai.java.util.Print.*;

/*
 * 在eclipse中运行，需要配置下面信息:
 * Run Configurations --> Arguments --> Working directory --> Other  -->  ${workspace_loc:java/target/classes}
 */

enum Explore {
	HERE, THERE
}

public class Reflection {
	
	public static Set<String> analyze(Class<?> enumClass) {
		print("----- Analyzing " + enumClass + " -----");
		print("Interfaces:");
		for (Type t : enumClass.getGenericInterfaces())
			print(t);
		print("Base: " + enumClass.getSuperclass());
		print("Methods: ");
		Set<String> methods = new TreeSet<String>();
		for (Method m : enumClass.getMethods())
			methods.add(m.getName());
		print(methods);
		return methods;
	}

	public static void main(String[] args) {
		Set<String> exploreMethods = analyze(Explore.class);
		Set<String> enumMethods = analyze(Enum.class);
		print("Explore.containsAll(Enum)? " + exploreMethods.containsAll(enumMethods));
		printnb("Explore.removeAll(Enum): ");
		exploreMethods.removeAll(enumMethods);
		print(exploreMethods);
		// Decompile the code for the enum:
		OSExecute.command("javap com.fanlehai.java.enumtest.Explore");
	}
} 
/* Output:
----- Analyzing class com.fanlehai.java.enumtest.Explore -----
Interfaces:
Base: class java.lang.Enum
Methods: 
[compareTo, equals, getClass, getDeclaringClass, hashCode, name, notify, notifyAll, ordinal, toString, valueOf, values, wait]
----- Analyzing class java.lang.Enum -----
Interfaces:
java.lang.Comparable<E>
interface java.io.Serializable
Base: class java.lang.Object
Methods: 
[compareTo, equals, getClass, getDeclaringClass, hashCode, name, notify, notifyAll, ordinal, toString, valueOf, wait]
Explore.containsAll(Enum)? true
Explore.removeAll(Enum): [values]
Compiled from "Reflection.java"
final class com.fanlehai.java.enumtest.Explore extends java.lang.Enum<com.fanlehai.java.enumtest.Explore> {
  public static final com.fanlehai.java.enumtest.Explore HERE;
  public static final com.fanlehai.java.enumtest.Explore THERE;
  static {};
  public static com.fanlehai.java.enumtest.Explore[] values();
  public static com.fanlehai.java.enumtest.Explore valueOf(java.lang.String);
}
*///:~

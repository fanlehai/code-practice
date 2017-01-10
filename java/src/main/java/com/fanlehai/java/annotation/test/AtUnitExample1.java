package com.fanlehai.java.annotation.test;

import com.fanlehai.java.atunit.*;
import com.fanlehai.java.util.*;


/*
 * 第一种方法：
 * 在terminal中运行步骤（前提是这个文件已经编译过了）：
 * 1. cd  /Users/liuhai/git/code-practice/java/target/classes
 * 2.java com.fanlehai.java.atunit.AtUnit com/fanlehai/java/annotation/test/AtUnitExample1
 * 
 * 第二种方法：
 * 在eclipse中运行，需要配置下面信息:
 * Run Configurations --> Arguments --> Working directory --> Other  -->  ${workspace_loc:java/target/classes}
 * 
 * 
 */



public class AtUnitExample1 {
	
	public String methodOne() {
		return "This is methodOne";
	}

	public int methodTwo() {
		System.out.println("This is methodTwo");
		return 2;
	}

	@Test
	boolean methodOneTest() {
		return methodOne().equals("This is methodOne");
	}

	@Test
	boolean m2() {
		return methodTwo() == 2;
	}

	@Test
	private boolean m3() {
		return true;
	}

	// Shows output for failure:
	@Test
	boolean failureTest() {
		return false;
	}

	@Test
	boolean anotherDisappointment() {
		return false;
	}

	public static void main(String[] args) throws Exception {
		OSExecute.command("java com.fanlehai.java.atunit.AtUnit com/fanlehai/java/annotation/test/AtUnitExample1");
	}
} /* Output:
annotations.AtUnitExample1
  . methodOneTest
  . m2 This is methodTwo

  . m3
  . failureTest (failed)
  . anotherDisappointment (failed)
(5 tests)

>>> 2 FAILURES <<<
  annotations.AtUnitExample1: failureTest
  annotations.AtUnitExample1: anotherDisappointment
*///:~

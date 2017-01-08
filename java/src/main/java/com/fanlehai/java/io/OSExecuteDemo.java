package com.fanlehai.java.io;

// Demonstrates standard I/O redirection.
import com.fanlehai.java.util.OSExecute;;



/*
 * 第一种方法：
 * 在terminal中运行步骤（前提是这个文件已经编译过了）：
 * 1. cd  /Users/liuhai/git/code-practice/java/target/classes
 * 2.java  com.fanlehai.java.io.OSExecuteDemo
 * 
 * 第二种方法：
 * 在eclipse中运行，需要配置下面信息:
 * Run Configurations --> Arguments --> Working directory --> Other  -->  ${workspace_loc:java/target/classes}
 * 
 * 
 */


public class OSExecuteDemo {
	public static void main(String[] args) {
		OSExecute.command("javap com.fanlehai.java.io.OSExecuteDemo");
	}
} 

/* Output:
Compiled from "OSExecuteDemo.java"
public class OSExecuteDemo extends java.lang.Object{
    public OSExecuteDemo();
    public static void main(java.lang.String[]);
}
*///:~

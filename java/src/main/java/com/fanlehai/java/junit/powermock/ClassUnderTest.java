package com.fanlehai.java.junit.powermock;

import java.io.File;

public class ClassUnderTest {

	public boolean callArgumentInstance(File file) {
		return file.exists();
	}

	public boolean callInternalInstance(String path) {
		File file = new File(path);
		return file.exists();
	}

	public boolean callFinalMethod(ClassDependency refer) {
		return refer.isAlive();
	}

	public boolean callStaticMethod() {
		return ClassDependency.isExist();
	}

	public boolean callPrivateMethod() {
		return isExist();
	}

	private boolean isExist() {
		return false;
	}

	public boolean callSystemFinalMethod(String str) {
		return str.isEmpty();
	}

	public String callSystemStaticMethod(String str) {
		return System.getProperty(str);
	}

	public boolean order1() {
		return true;
	}

	public boolean order2() {
		return true;
	}

	public boolean order3() {
		return true;
	}

}

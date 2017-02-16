package com.fanlehai.java.grammar;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;

class FinalPrivate {
	final private String id;
	final private String name;
	public static int INT_SIZE = 10;
	public final static int INT_SIZE_FINAL = 10; // 这个值是无法通过反射修改的，异常抛出

	public FinalPrivate(String idString, String nameString) {
		this.id = idString;
		this.name = nameString;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}

public class FinalPrivateTest {

	public FinalPrivateTest() {

	}

	@Test
	public void TestPrivate() {
		FinalPrivate finalPrivateTest = new FinalPrivate("001", "john");
		try {
			Field field = finalPrivateTest.getClass().getDeclaredField("name");
			// 使得字段的访问控制字段失效
			field.setAccessible(true);
			try {
				field.set(finalPrivateTest, "john-modify");

				Assert.assertEquals("john-modify", finalPrivateTest.getName());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test(expected = IllegalAccessException.class)
	public void TestPrivateStatic() {
		Field field;
		try {
			field = FinalPrivate.class.getDeclaredField("INT_SIZE_FINAL");
			// 使得字段的访问控制字段失效
			field.setAccessible(true);
			try {
				field.setInt(null, 11);

				Assert.assertEquals(11, FinalPrivate.INT_SIZE_FINAL);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}

package com.fanlehai.java.junit;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class BatchCheckUtilTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
	}

	@Test
	public void validInt_when_input_not_int() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		String input = "test";
		Method validInt = BatchCheckUtil.class.getDeclaredMethod("validInt", String.class);
		validInt.setAccessible(true);
		// 把BatchCheckUtil.class换成对象的实例就是调用普通方法
		Object object = validInt.invoke(BatchCheckUtil.class, input);
		assertFalse((Boolean) object);
	}

}

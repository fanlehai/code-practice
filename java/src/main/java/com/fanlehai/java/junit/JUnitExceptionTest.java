package com.fanlehai.java.junit;

import static org.junit.Assert.*;

import org.junit.Test;

public class JUnitExceptionTest {
	/**
	 * Test of speed method, of class JUnit4ExceptionTest.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSpeed() {
		int distance = 0;
		int time = 0;
		SpeedUtils instance = new SpeedUtils();
		int expResult = 0;
		int result = instance.speed(distance, time); // shold throw exception
		assertEquals(expResult, result);
	}
	
	/*
	 *	上面可以检查异常，但是更推荐使用下面这种显式测试异常的方式： 
	 */
	@Test
	public void testSpeed2() {
		int distance = 0;
		int time = 10;
		SpeedUtils instance = new SpeedUtils();
		try {
			instance.speed(distance, time); // shold throw exception
			fail("没有抛出任何异常！");
		} catch (Exception e) {
			// 测试通过！
		}
	}

}

package com.fanlehai.java.junit;

import static org.junit.Assert.*;

import org.junit.Test;

public class JUnitExceptionTest {
	/**
	 * Test of speed method, of class JUnit4ExceptionTest.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSpeed() {
		System.out.println("speed");
		int distance = 0;
		int time = 0;
		SpeedUtils instance = new SpeedUtils();
		int expResult = 0;
		int result = instance.speed(distance, time); // shold throw exception
		assertEquals(expResult, result);
	}

}

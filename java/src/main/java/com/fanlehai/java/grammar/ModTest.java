package com.fanlehai.java.grammar;

import org.junit.Assert;
import org.junit.Test;

public class ModTest {
	@Test
	public void testMod() {
		int m = 5;
		int n = 3;
		Assert.assertEquals(2, m % n);

		n = -3;
		Assert.assertEquals(2, m % n);

		m = -5;
		Assert.assertEquals(-2, m % n);

		n = 3;
		Assert.assertEquals(-2, m % n);

		double a = 5.3;
		Assert.assertEquals(2.3, (double) a % n, 0.0000000000000001);

		double b = 3.3;
		Assert.assertEquals(2.0, a % b, 0.0000000000000001);
	}
}

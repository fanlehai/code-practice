package com.fanlehai.java.junit;

import org.junit.Assert;
import org.junit.Test;

public class JustTest {
	@Test
	public void isEqual() {
		Integer i1 = Integer.valueOf("2");
		Integer i2 = Integer.valueOf("2");
		Assert.assertTrue(i1 == i2);
	}
}

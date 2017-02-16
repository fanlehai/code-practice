package com.fanlehai.java.junit;

import org.junit.Test;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;

public class HamcrestExample {

	@Test
	public void useHamcrest() {
		Integer aInteger = 400;
		MatcherAssert.assertThat(aInteger, Is.is(400));
		MatcherAssert.assertThat(aInteger, IsEqual.equalTo(400));
	}
}

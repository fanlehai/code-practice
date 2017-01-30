package com.fanlehai.java.junit;

import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


public class TimeoutRuleTest {

	/**
	 * Rule is applied to all methods of class time is specified in milliseconds
	 */
	@Rule
	public Timeout timeout = new Timeout(1000, TimeUnit.MILLISECONDS);

	/**
	 * Example of timeout test. Test will fail if it takes more than 1 sec to
	 * execute
	 */
	@Test
	public void testTimeout1() {
		while (true)
			;
	}

	/**
	 * Example of timeout test. Test will fail if it takes more than 1 sec to
	 * execute
	 */
	@Test
	public void testTimeout2() {
		while (true)
			;
	}
}

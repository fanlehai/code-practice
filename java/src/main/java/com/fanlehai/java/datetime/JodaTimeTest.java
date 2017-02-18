package com.fanlehai.java.datetime;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;

public class JodaTimeTest {
	public static void main(String[] args) {
		DateTime dt = new DateTime();
		System.out.println(dt);
		
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
		System.out.println(fmt.print(dt));
	}
	
	@Test
	public void formatTime(){
		DateTime dTime = new DateTime()
				.withMonthOfYear(2)
				.withDayOfMonth(18);
		
		DateTimeFormatter fmt = DateTimeFormat.forPattern("ddMM");
		Assert.assertEquals("1802", fmt.print(dTime));
	}
}

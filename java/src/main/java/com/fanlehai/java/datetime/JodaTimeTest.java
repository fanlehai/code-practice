package com.fanlehai.java.datetime;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaTimeTest {
	public static void main(String[] args) {
		DateTime dt = new DateTime();
		System.out.println(dt);
		
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
		System.out.println(fmt.print(dt));
	}
}

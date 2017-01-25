package com.fanlehai.java.datetime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class String2Date {

	public static class DateUtils {
		private static final DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		public static synchronized Date toDate(String date) throws ParseException {
			return format.parse(date);
		}
	}

	public static void main(String args[]) {
		// One example, long dates
		String string = "February 6, 2014";
		Date date;
		try {
			date = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(string);
			System.out.println(date);
			// Thu Feb 06 00:00:00 IST 2014
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// Another example, date in MM/dd/yyyy format
		String source = "01/09/2014";
		try {
			date = new SimpleDateFormat("MM/dd/yyyy").parse(source);
			System.out.println(date);
			// Thu Jan 09 00:00:00 IST 2014
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// One more String to Date Example in 04 JAN 2014 20:30
		String birthday = "04 JAN 2014 20:30";
		try {
			date = new SimpleDateFormat("dd MMM yyyy HH:mm").parse(birthday);
			System.out.println(date);
			// Sat Jan 04 20:30:00 IST 2014
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// Remember "m" and "M" are different
		String weddingday = "30-01-2011";
		try {
			date = new SimpleDateFormat("dd-mm-yyyy").parse(weddingday);
			System.out.println(date);
			// Sun Jan 30 00:01:00 IST 2011
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// calling synchronized method to convert String to date
		try {
			Date mydate = DateUtils.toDate("04/02/2014");
			System.out.println(mydate);
			// Tue Feb 04 00:00:00 IST 2014
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package com.tahona.js.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Logger {
	private final static SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

	public static void d(final String string) {
		final Date time = Calendar.getInstance().getTime();
		System.out.println(formatter.format(time) + " " + string);
	}
}

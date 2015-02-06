package com.ma.zerui.weatherdemo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	
	public static String getDate(long time) {
	       TimeZone tz = TimeZone.getDefault();
	       SimpleDateFormat sdf = new SimpleDateFormat("EE, MMM/dd");
	       sdf.setTimeZone(tz);//set time zone.
	       String date = sdf.format(new Date(time*1000));
	       
	      return date;
	}

}

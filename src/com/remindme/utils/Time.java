package com.remindme.utils;

import java.text.SimpleDateFormat;
import java.util.Date;



public abstract class Time {

	public static String parseTime(Date date){
		String strTime;
		SimpleDateFormat format= new SimpleDateFormat("HH:mm");
		strTime =format.format(date);
		return strTime;
	}
}

package com.remindme.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public abstract class Time {
	
	private static Locale locale = new Locale("es ES");
	
	public static String parseTime(Date date){
		String strTime;
		SimpleDateFormat format= new SimpleDateFormat("HH:mm", locale);
		strTime =format.format(date);
		return strTime;
	}
	public static String parseLongDate(Date date){
		SimpleDateFormat format= new SimpleDateFormat("E dd/MM/yyyy HH:mm", locale);
		return format.format(date);
	}
	
	public static String parseDateDay(Date date){
		 SimpleDateFormat format = new SimpleDateFormat("d 'de' MMM yyyy", locale);
		 return format.format(date);
	}
	
	public static Integer dayOfWeek(Date date){
		Calendar cal = dateToCalendar(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public static Integer dayOfMonth(Date date){
		Calendar cal = dateToCalendar(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	public static Integer dayOfYear(Date date){
		Calendar cal = dateToCalendar(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}
	
	private static Calendar dateToCalendar(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
}

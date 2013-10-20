package com.remindme.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public abstract class Time {
	
	private static Locale locale = new Locale("es ES");
	
	public static String formatTime(Date date){
		String strTime;
		SimpleDateFormat format= new SimpleDateFormat("HH:mm", locale);
		strTime =format.format(date);
		return strTime;
	}
	public static String formatLongDate(Date date){
		SimpleDateFormat format= new SimpleDateFormat("E dd/MM/yyyy HH:mm", locale);
		return format.format(date);
	}
	
	public static String formatDayDate(Date date){
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
	public static Date updateDate(Date dateToUpdate, Date day) {
		Calendar cal = Calendar.getInstance();
		Calendar calAux = Calendar.getInstance();
		calAux.setTime(day);
		cal.setTime(dateToUpdate);
		
		cal.set(Calendar.DAY_OF_YEAR, calAux.get(Calendar.DAY_OF_YEAR));
		cal.set(Calendar.MONTH, calAux.get(Calendar.MONTH));
		cal.set(Calendar.YEAR, calAux.get(Calendar.YEAR));
		return cal.getTime();
	}
	public static Date retainMainDate(Date dateOfRecord) {
		Calendar calAux = Calendar.getInstance();
		calAux.setTime(dateOfRecord);
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.DAY_OF_YEAR, calAux.get(Calendar.DAY_OF_YEAR));
		cal.set(Calendar.MONTH, calAux.get(Calendar.MONTH));
		cal.set(Calendar.YEAR, calAux.get(Calendar.YEAR));
		return cal.getTime();
	}
	
}

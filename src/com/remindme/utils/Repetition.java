package com.remindme.utils;

import java.util.Calendar;
import java.util.Date;

import com.remindme.ui.R;
import android.content.Context;
import android.util.Log;

public enum Repetition {
		
	SINGLE,
	
	DAILY,
	
	WEEKLY,
	
	MONTHLY,
	
	ANNUAL;
	
	private static final Long LONG_DAY = Long.valueOf(86400000);
	private static final Long LONG_WEEK	= Long.valueOf(604800000);
	private static final Long LONG_MONTH = Long.valueOf(14400000);
	private static final Long LONG_YEAR = Long.valueOf(28800000);
	
	
	public static Long getAsLong(Repetition repeat){
		Long result;
		switch (repeat) {
		case DAILY:
			result = LONG_DAY;
			break;
		case WEEKLY:
			result =LONG_WEEK;
			break;
		case MONTHLY:
			result =LONG_MONTH;
			break;
		case ANNUAL:
			result =LONG_YEAR;
			break;
		default:
			result=Long.valueOf(-1);
			break;
		}
		return result;
	}
	
	
	public static Repetition getRepetition(String repString, Context ctx){
		
		String[] array  = ctx.getResources().getStringArray(R.array.new_array_spinnerRepetition);
		int position = 0;
		
		for(String string: array){
			if (repString.contentEquals(string))
				
				break;
			else
				position++;
		}
		return Repetition.values()[position];
		
		
	}
	
	/**
	 * Devuelve la cadena traducida al idioma que se este usando el usuario en la aplicacion
	 * @param enString
	 * @param ctx
	 * @return
	 */
	public static String translateStringToLocal(String enString, Context ctx){
		Repetition rep = Repetition.valueOf(enString);
		
		String[] array  = ctx.getResources().getStringArray(R.array.new_array_spinnerRepetition);
		
		return array[rep.ordinal()];
	}
	
	public static Date getNextDate(Date date, String enRepStr)
	{
		Calendar calendar = Calendar.getInstance();
		
		calendar = Calendar.getInstance();
	    calendar.clear();
		calendar.setTime(date);
	    
	    
	    switch (valueOf(enRepStr)) 
	    {
	    case SINGLE:
	    	calendar.clear();
	    	break;
		case DAILY:
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			break;
	
		case WEEKLY:
			calendar.add(Calendar.DAY_OF_YEAR, 7);
			break;
	
		case MONTHLY:
			calendar.add(Calendar.MONTH, 1);
			break;
			
		case ANNUAL:
			calendar.add(Calendar.YEAR, 1);
			break;
		default:
			break;
			
	    }
		
		return calendar.getTime();
	}
	
	private static Boolean isSingle(String repStr){
		return repStr.contentEquals(SINGLE.toString());
	}
	
	public static Boolean changedFromSingle(String old, String recent){
		return isSingle(old) && !Repetition.isSingle(recent);
	}
	
	public static Boolean changedToSingle(String old, String recent){
		return !isSingle(old) && Repetition.isSingle(recent);
	}
	
	public static Integer dayOfRepetition(Date date, String repetition) {
		Integer day= 0;
		switch (Repetition.valueOf(repetition)) {
		
		case WEEKLY:
			day = Time.dayOfWeek(date);
			break;
		case MONTHLY:
			day = Time.dayOfMonth(date);
			break;
		case ANNUAL:
			day = Time.dayOfYear(date);
			break;
		default:
			break;
		}
		
		return day;
	}
}



package com.remindme.utils;

import java.util.Calendar;
import java.util.Date;

import remind.me.R;
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
		//String[] array = Resources.getSystem().getStringArray(R.array.new_spinnerNotice);
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
	public static String translateString(String enString, Context ctx){
		Repetition rep = Repetition.valueOf(enString);
		
		String[] array  = ctx.getResources().getStringArray(R.array.new_array_spinnerRepetition);
		
		return array[rep.ordinal()];
	}
	
	public static Date getNextDate(Date date, Repetition rep){
		
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    
	    switch (rep) {
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
	
		//uchcalendar.set(year, month, day);
		return calendar.getTime();
	}

}



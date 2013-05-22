package com.utils;

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
	    int year = calendar.get(Calendar.YEAR);
	    int month = calendar.get(Calendar.MONTH)+1;
	    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
	    int day = calendar.get(Calendar.DAY_OF_YEAR);
	    
		switch (rep) {
		case DAILY:
			dayOfMonth++;
			day++;
			if(calendar.getActualMaximum(Calendar.MONTH)<dayOfMonth){
				day = 0;
				month++;
				if(month> Calendar.DECEMBER){
					year++;
				}
			}
			
			break;
		case WEEKLY:
			dayOfMonth+=7;
			day+=7;
			int dayMaxMonth = calendar.getActualMaximum(Calendar.MONTH);
			if(dayMaxMonth<dayOfMonth){
				int remainder = dayOfMonth - dayMaxMonth;
				day += 7 - remainder;
				month++;
				if(month> Calendar.DECEMBER){
					year++;
				}
			}
				
			break;
		case MONTHLY:
			month++;
			if(month> Calendar.DECEMBER)
				month = 1;
			break;
		case ANNUAL:
			year++;
			break;
		default:
			break;
		}
		calendar.set(year, month, day);
		return calendar.getTime();
	}

}



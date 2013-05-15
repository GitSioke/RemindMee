package com.utils;

import remind.me.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

/**
 * Clase enumerada representa los valores elegibles para la repetición que se da en una tarea.
 * 
 * @author pick
 *
 */
public enum Notice {
	
	HOUR,
	
	TWOHOURS,
	
	FOURHOURS,
	
	EIGHTHOURS,
	
	TWELVEHOURS,
	
	DAY,
	
	TWODAYS;
	
	private static final Long LONG_HOUR = Long.valueOf(3600000);
	private static final Long LONG_TWOHOURS		 = Long.valueOf(7200000);
	private static final Long LONG_FOURHOURS = Long.valueOf(14400000);
	private static final Long LONG_EIGTHHOURS = Long.valueOf(28800000);
	private static final Long LONG_TWELVEHOURS = Long.valueOf(43200000);
	private static final Long LONG_DAY = Long.valueOf(86400000);
	private static final Long LONG_TWODAYS = Long.valueOf(172800000);
	
	
	public static Long getAsLong(Notice notice){
		Long result;
		switch (notice) {
		case HOUR:
			result =LONG_HOUR;
			break;
		case TWOHOURS:
			result =LONG_TWOHOURS;
			break;
		case FOURHOURS:
			result =LONG_FOURHOURS;
			break;
		case EIGHTHOURS:
			result =LONG_EIGTHHOURS;
			break;
		case TWELVEHOURS:
			result = LONG_TWELVEHOURS;
			break;
		case DAY:
			result =LONG_DAY;
			break;	
			
		default:
			result =LONG_TWODAYS;
			break;
		}
		return result;
	}
	//Cambiar y hacer con R.array.etc
	public static Notice getNotice(String noticeAsString, Context ctx){
		
		String[] array  = ctx.getResources().getStringArray(R.array.new_array_spinnerNotice);
		//String[] array = Resources.getSystem().getStringArray(R.array.new_spinnerNotice);
		int position = 0;
		
		for(String string: array){
			if (noticeAsString.contentEquals(string))
				
				break;
			else
				position++;
		}
		return Notice.values()[position];
		
		
	}
	
}

package com.remindme.utils;

import java.util.Calendar;
import java.util.Date;

import com.remindme.ui.R;

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
	
	FIFHTEENMINUTES,
	
	THIRTYMINUTES,
	
	HOUR,
	
	TWOHOURS,
	
	FOURHOURS,
	
	EIGHTHOURS,
	
	TWELVEHOURS,
	
	DAY,
	
	TWODAYS;
	
	private static final Integer INT_FIFTHTEENMINUTES = Integer.valueOf(15);
	private static final Integer INT_THIRTYMINUTES = Integer.valueOf(30);
	private static final Integer INT_HOUR = Integer.valueOf(1);
	private static final Integer INT_TWOHOURS	= Integer.valueOf(2);
	private static final Integer INT_FOURHOURS = Integer.valueOf(4);
	private static final Integer INT_EIGTHHOURS = Integer.valueOf(8);
	private static final Integer INT_TWELVEHOURS = Integer.valueOf(12);
	private static final Integer INT_DAY = Integer.valueOf(1);
	private static final Integer INT_TWODAYS = Integer.valueOf(2);
	
	
	//Cambiar y hacer con R.array.etc
	public static Notice getNotice(String noticeAsString, Context ctx){
		
		String[] array  = ctx.getResources().getStringArray(R.array.new_array_spinnerNotice);
		//String[] array = Resources.getSystem().getStringArray(R.array.new_spinnerNotice);
		int position = -2;
		
		for(String string: array){
			if (noticeAsString.contentEquals(string))
				
				break;
			else
				position++;
		}
		return Notice.values()[position];
		
		
	}
	
	public static Notice getDelay(String strDelay, Context ctx)
	{
		
		String[] array  = ctx.getResources().getStringArray(R.array.dialogDelay_array_spinnerDelay);
		int position = 0;
		
		for(String string: array){
			if (strDelay.contentEquals(string))
				
				break;
			else
				position++;
		}
		return Notice.values()[position];
		
	}
	
	public static Date noticeDate(Date date, Notice not){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		switch (not) {
		case HOUR:
			cal.roll(Calendar.HOUR_OF_DAY, -INT_HOUR);
			break;
		case TWOHOURS:
			cal.roll(Calendar.HOUR_OF_DAY, -INT_TWOHOURS);
			break;
		case FOURHOURS:
			cal.roll(Calendar.HOUR_OF_DAY, -INT_FOURHOURS);
			break;
		case EIGHTHOURS:
			cal.roll(Calendar.HOUR_OF_DAY, -INT_EIGTHHOURS);
			break;
		case TWELVEHOURS:
			cal.roll(Calendar.HOUR_OF_DAY, -INT_TWELVEHOURS);
			break;
		case DAY:
			cal.roll(Calendar.DAY_OF_YEAR, -INT_DAY);
			break;	
		case TWODAYS:
			cal.roll(Calendar.DAY_OF_YEAR, -INT_TWODAYS);
			break;
		default:
			
			break;
		}
		return cal.getTime();
		
	}
	
	public static Date delayDate(Date date, Notice not){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		switch (not) {
		case FIFHTEENMINUTES:
			cal.roll(Calendar.MINUTE, INT_FIFTHTEENMINUTES);
			break;
		case THIRTYMINUTES:
			cal.roll(Calendar.MINUTE, INT_THIRTYMINUTES);
			break;
		case HOUR:
			cal.roll(Calendar.HOUR_OF_DAY, INT_HOUR);
			break;
		case TWOHOURS:
			cal.roll(Calendar.HOUR_OF_DAY, INT_TWOHOURS);
			break;
		case FOURHOURS:
			cal.roll(Calendar.HOUR_OF_DAY,INT_FOURHOURS);
			break;
		case EIGHTHOURS:
			cal.roll(Calendar.HOUR_OF_DAY, INT_EIGTHHOURS);
			break;
		case TWELVEHOURS:
			cal.roll(Calendar.HOUR_OF_DAY, INT_TWELVEHOURS);
			break;
		case DAY:
			cal.roll(Calendar.DAY_OF_YEAR, INT_DAY);
			break;	
		case TWODAYS:
			cal.roll(Calendar.DAY_OF_YEAR, INT_TWODAYS);
			break;
		default:
			
			break;
		}
		return cal.getTime();
		
	}
	/* static Integer getNoticeString(Long noticeAsLong){
		Notice not = null;
		if(noticeAsLong.compareTo(LONG_HOUR) == 0){
			not = Notice.HOUR;
		}else if (noticeAsLong.compareTo(LONG_TWOHOURS) == 0) {
			not = Notice.TWOHOURS;
		}else if(noticeAsLong.compareTo(LONG_FOURHOURS) == 0){
			not = Notice.FOURHOURS;
		}else if (noticeAsLong.compareTo(LONG_TWELVEHOURS) == 0) {
			not = Notice.TWELVEHOURS;
		}else if (noticeAsLong.compareTo(LONG_DAY) == 0) {
			not = Notice.DAY;
		}else if (noticeAsLong.compareTo(LONG_TWODAYS) == 0) {
			not = Notice.TWODAYS;
		}
		return not.ordinal();
	}*/
	
		
}

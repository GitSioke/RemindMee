package com.remindme.utils;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.Interval;
import org.joda.time.Period;

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
public enum NoticeNew implements Notice {
	
	
	HOUR,
	
	TWOHOURS,
	
	FOURHOURS,
	
	EIGHTHOURS,
	
	TWELVEHOURS,
	
	DAY,
	
	TWODAYS;
	
	
	private static final int INT_HOUR = 1;
	private static final int INT_TWOHOURS	= 2;
	private static final int INT_FOURHOURS = 4;
	private static final int INT_EIGTHHOURS = 8;
	private static final int INT_TWELVEHOURS = 12;
	private static final int INT_DAY = 1;
	private static final int INT_TWODAYS = 2;
	
	
	//Cambiar y hacer con R.array.etc
	public static NoticeNew getNotice(String noticeAsString, Context ctx){
		
		String[] array  = ctx.getResources().getStringArray(R.array.new_array_spinnerNotice);
		//String[] array = Resources.getSystem().getStringArray(R.array.new_spinnerNotice);
		int position = 0;
		
		for(String string: array){
			if (noticeAsString.contentEquals(string))
				
				break;
			else
				position++;
		}
		return NoticeNew.values()[position];
		
		
	}
	
	
	public static Date advanceDate(Date date, NoticeNew not){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		switch (not) {
		case HOUR:
			cal.add(Calendar.HOUR_OF_DAY, -INT_HOUR);
			break;
		case TWOHOURS:
			cal.add(Calendar.HOUR_OF_DAY, -INT_TWOHOURS);
			break;
		case FOURHOURS:
			cal.add(Calendar.HOUR_OF_DAY, -INT_FOURHOURS);
			break;
		case EIGHTHOURS:
			cal.add(Calendar.HOUR_OF_DAY, -INT_EIGTHHOURS);
			break;
		case TWELVEHOURS:
			cal.add(Calendar.HOUR_OF_DAY, -INT_TWELVEHOURS);
			break;
		case DAY:
			cal.add(Calendar.DAY_OF_YEAR, -INT_DAY);
			break;	
		case TWODAYS:
			cal.add(Calendar.DAY_OF_YEAR, -INT_TWODAYS);
			break;
		default:
			
			break;
		}
		return cal.getTime();
		
	}
	
	public static Date delayDate(Date date, NoticeNew not){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		switch (not) {
		
		case HOUR:
			cal.add(Calendar.HOUR_OF_DAY, INT_HOUR);
			break;
		case TWOHOURS:
			cal.add(Calendar.HOUR_OF_DAY, INT_TWOHOURS);
			break;
		case FOURHOURS:
			cal.add(Calendar.HOUR_OF_DAY,INT_FOURHOURS);
			break;
		case EIGHTHOURS:
			cal.add(Calendar.HOUR_OF_DAY, INT_EIGTHHOURS);
			break;
		case TWELVEHOURS:
			cal.add(Calendar.HOUR_OF_DAY, INT_TWELVEHOURS);
			break;
		case DAY:
			cal.add(Calendar.DAY_OF_YEAR, INT_DAY);
			break;	
		case TWODAYS:
			cal.add(Calendar.DAY_OF_YEAR, INT_TWODAYS);
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

	public static Integer getNoticeOrdinal(Date startDate, Date endDate)
	{
		NoticeNew result = null;
		Interval interval = new Interval(startDate.getTime(), endDate.getTime());
		Period period = interval.toPeriod();
		
		switch (period.getHours())
		{
		case INT_HOUR:
			result = HOUR;
			break;
		case INT_TWODAYS:
			result = TWOHOURS;
		case INT_FOURHOURS:
			result = FOURHOURS;
		case INT_EIGTHHOURS:
			result = EIGHTHOURS;
		case INT_TWELVEHOURS:
			result = TWELVEHOURS;
		
		default:
			switch (period.getDays())
			{
			case INT_DAY:
				result = DAY;
				break;
			case INT_TWODAYS:
				result = TWODAYS;
			default:
				break;
			
			}
			break;
		}
				
		return result.ordinal();
	}

	
		
}

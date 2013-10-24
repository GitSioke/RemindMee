package com.remindme.utils;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.Interval;
import org.joda.time.Period;

import android.content.Context;

import com.remindme.ui.R;

public enum NoticeDelay implements Notice {

	FIFHTEENMINUTES,
	
	THIRTYMINUTES,
	
	HOUR,
	
	TWOHOURS,
	
	FOURHOURS,
	
	EIGHTHOURS,

	DAY;
	
	private static final int INT_FIFTHTEENMINUTES = 15;
	private static final int INT_THIRTYMINUTES = 30;
	private static final int INT_HOUR = 1;
	private static final int INT_TWOHOURS	= 2;
	private static final int INT_FOURHOURS = 4;
	private static final int INT_EIGTHHOURS = 8;
	private static final int INT_DAY = 1;
	
		
	public static NoticeDelay getDelay(String strDelay, Context ctx)
	{
		
		String[] array  = ctx.getResources().getStringArray(R.array.dialogDelay_array_spinnerDelay);
		int position = 0;
		
		for(String string: array){
			if (strDelay.contentEquals(string))
				
				break;
			else
				position++;
		}
		return NoticeDelay.values()[position];
		
	}
	
	public static Date advanceDate(Date date, NoticeDelay not){
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
		case DAY:
			cal.add(Calendar.DAY_OF_YEAR, -INT_DAY);
			break;	
		
		default:
			
			break;
		}
		return cal.getTime();
		
	}
	
	public static Date delayDate(Date date, NoticeDelay not){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		switch (not) {
		case FIFHTEENMINUTES:
			cal.add(Calendar.MINUTE, INT_FIFTHTEENMINUTES);
			break;
		case THIRTYMINUTES:
			cal.add(Calendar.MINUTE, INT_THIRTYMINUTES);
			break;
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
		case DAY:
			cal.add(Calendar.DAY_OF_YEAR, INT_DAY);
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
		NoticeDelay result = null;
		Interval interval = new Interval(startDate.getTime(), endDate.getTime());
		Period period = interval.toPeriod();
		switch (period.getMinutes()) {
		case INT_FIFTHTEENMINUTES:
			result = FIFHTEENMINUTES;
			break;
		case INT_THIRTYMINUTES:
			result = THIRTYMINUTES;
		
		default:
			switch (period.getHours())
			{
			case INT_HOUR:
				result = HOUR;
				break;
			case INT_TWOHOURS:
				result = TWOHOURS;
			case INT_FOURHOURS:
				result = FOURHOURS;
			case INT_EIGTHHOURS:
				result = EIGHTHOURS;
			
			default:
				switch (period.getDays())
				{
				case INT_DAY:
					result = DAY;
					break;
				default:
					break;
				
				}
				break;
			}
			break;
		
		}
		
		
		
		return result.ordinal();
	}

}

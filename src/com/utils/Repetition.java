package com.utils;

import android.util.Log;

public enum Repetition {
		
	SINGLE,
	
	DAILY,
	
	WEEKLY,
	
	MONTHLY,
	
	ANNUAL;
	
	private static final Long LONG_DAY = Long.valueOf(3600000);
	private static final Long LONG_WEEK		 = Long.valueOf(7200000);
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
	
	public static Repetition changeStringToEnum(String string){
		Repetition notice = null;
		if (string.contentEquals("single")){
			notice = Repetition.SINGLE;		
		}else if (string.contentEquals("daily")){
			notice = Repetition.DAILY;
		}else if (string.contentEquals("weekly")){
			notice = Repetition.WEEKLY;
		}else if (string.contentEquals("monthly")){
			notice = Repetition.MONTHLY;
		}else if (string.contentEquals("annual")){
			notice = Repetition.ANNUAL;
		}
		return notice;
	}
	

}

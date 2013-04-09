package com.utils;

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
	
	DAY,
	
	TWODAYS;
	
	private static final Long LONG_HOUR = Long.valueOf(3600000);
	private static final Long LONG_TWOHOURS		 = Long.valueOf(7200000);
	private static final Long LONG_FOURHOURS = Long.valueOf(14400000);
	private static final Long LONG_EIGTHHOURS = Long.valueOf(28800000);
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
	public static Notice getNotice(String noticeAsString){
		Notice notice = null;
		if (noticeAsString.contentEquals("1 hour")){
			notice = Notice.HOUR;		
		}else if (noticeAsString.contentEquals("2 hours")){
			notice = Notice.TWOHOURS;
		}else if (noticeAsString.contentEquals("4 hours")){
			notice = Notice.FOURHOURS;
		}else if (noticeAsString.contentEquals("8 hours")){
			notice = Notice.EIGHTHOURS;
		}else if (noticeAsString.contentEquals("1 day")){
			notice = Notice.DAY;
		}else if (noticeAsString.contentEquals("2 days")) {
			notice = Notice.TWODAYS;
		}
		return notice;
		
	}
	
}

package com.remindme.sqlite;

public class RemindTaskNotifSQLite implements RemindTaskNotifDAO{
	
	/** Atributos relativos a Task
	  */
	public static final String KEY_ROWTASKID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_DATE="date";
	public static final String KEY_DATENOTICE="dateNotice";
	public static final String KEY_TIME="time";
	public static final String KEY_REPETITION="repetition";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_TAG="tag";
	public static final String KEY_COMPLETED="completed";
	public static final String KEY_SUPERTASK="supertask";
		
	/**
	 * Atributos relativos a Notification
	 */
	public static final String KEY_ROWNOTIFID = "id";
	public static final String KEY_IDTASK = "idTask";
	public static final String KEY_DATENOTIF="date";
	public static final String KEY_DELAY="delay";
	public static final String KEY_READY="ready";
	public static final String KEY_DONE="done";
	
	private static final String DATABASE_TABLETASK = "tasks";
	private static final String DATABASE_TABLENOTIF = "notifications";

}

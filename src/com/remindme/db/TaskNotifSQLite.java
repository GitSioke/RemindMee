package com.remindme.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.remindme.utils.RemindTask;
import com.remindme.utils.Time;

public class TaskNotifSQLite implements TaskNotifDAO{
	
	private final Context context;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public TaskNotifSQLite(Context ctx){
		this.context = ctx;
		dbHelper = new DatabaseHelper(context);
	}
	
	public TaskNotifSQLite open() throws SQLException{
		db = dbHelper.getReadableDatabase();
		return this;		
	}
	
	public void close(){
		dbHelper.close();
	}
	
	/**
	 * 
	 */
	public ArrayList<RemindTask> getAllTasksWith(Date date) 
	{
		this.open();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(NotificationSQLite.DATABASE_TABLE + " LEFT OUTER JOIN " + TaskSQLite.DATABASE_TABLE + " ON " +
				TaskSQLite.DATABASE_TABLE+"." + TaskSQLite.KEY_ROWID + " = " + NotificationSQLite.DATABASE_TABLE + "."
				+ NotificationSQLite.KEY_IDTASK);
		long endOfDay = date.getTime()+86400000;
		queryBuilder.appendWhere(NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + ">" + date.getTime() );
		queryBuilder.appendWhere(" AND " +NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + "<" + endOfDay );
		
		Log.d("TaskNotifSQLite", Long.toString(date.getTime()));
		Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
		RemindTask task;
		ArrayList<RemindTask> taskList = new ArrayList<RemindTask>();
			
		if (cursor.moveToFirst()){
        	do {
        		Integer idEvent = cursor.getInt(0);
        		String name = cursor.getString(9);
        		Long dateAsLong = cursor.getLong(2);
        		Date taskDate = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotify = new Date(dateAsLong);
        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
        		String time = Time.formatTime(taskDate);
        		String repetition = cursor.getString(13);
        		String description = cursor.getString(14);
        		String tag = cursor.getString(15);
        		Integer superEvent = cursor.getInt(7);
        		Boolean done = cursor.getInt(5)==1 ? true: false;
        		Log.d("SQLITE", idEvent.toString());
        		task = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
        		taskList.add(task);
        		
        	}while(cursor.moveToNext());
        		
        }
		this.close();
		
		return taskList;
	}

	public ArrayList<RemindTask> getAllTasksBefore(Date date) {
		this.open();
		ArrayList<RemindTask> taskList = new ArrayList<RemindTask>();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(NotificationSQLite.DATABASE_TABLE + " LEFT OUTER JOIN " + TaskSQLite.DATABASE_TABLE + " ON " +
				TaskSQLite.DATABASE_TABLE+"." + TaskSQLite.KEY_ROWID + " = " + NotificationSQLite.DATABASE_TABLE + "."
				+ NotificationSQLite.KEY_IDTASK);
		queryBuilder.appendWhere(NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + "<" + date.getTime() );
		queryBuilder.appendWhere(" AND " + NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DONE + "=" + Integer.toString(0) );
		
		Log.d("TaskNotifSQLite", Long.toString(date.getTime()));
		Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
		RemindTask task;
			
		if (cursor.moveToFirst()){
        	do {
        		Integer idEvent = cursor.getInt(0);
        		String name = cursor.getString(9);
        		Long dateAsLong = cursor.getLong(2);
        		Date taskDate = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotify = new Date(dateAsLong);
        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
        		String time = Time.formatTime(taskDate);
        		String repetition = cursor.getString(13);
        		String description = cursor.getString(14);
        		String tag = cursor.getString(15);
        		Integer superEvent = cursor.getInt(7);
        		Boolean done = cursor.getInt(5)==1 ? true: false;
        		Log.d("SQLITE", idEvent.toString());
        		task = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
        		taskList.add(task);
        		
        	}while(cursor.moveToNext());
		}		
		this.close();
		
		return taskList;
		
	}
	
	
	public List<RemindTask> getSubNotif(Integer idNotif) {
		RemindTask task = null;
		List<RemindTask> subTasks= new ArrayList<RemindTask>();
		this.open();
        Cursor cursor =db.query(NotificationSQLite.DATABASE_TABLE, null, NotificationSQLite.KEY_SUPERNOTIF+"=?", new String[]{Long.toString(idNotif)}, null, null, null);
        
        if (cursor.moveToFirst()){
        	do{
        		Integer id = cursor.getInt(0);
            	String name = cursor.getString(1);
            	Long dateAsLong = cursor.getLong(2);
        		Date date = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotice = new Date(dateAsLong);
            	String time =cursor.getString(4);
            	String repetition = cursor.getString(5);
            	String description = cursor.getString(6);
            	String tag = cursor.getString(7);
            	Integer superTask = cursor.getInt(8);
            	Boolean completed = cursor.getInt(9)==1 ? true: false;
            	task = new RemindTask(id, name, date,dateNotice, time, repetition, description, tag, superTask, completed);
            	subTasks.add(task);
        	}while(cursor.moveToNext());
        
        }
        this.close();
		return subTasks;
	}
	//Subatareas
	public RemindTask getRelatedTaskFromEvent(Integer idEvent) {
		RemindTask task = null;
		this.open();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(NotificationSQLite.DATABASE_TABLE + " LEFT OUTER JOIN " + TaskSQLite.DATABASE_TABLE + " ON " +
				TaskSQLite.DATABASE_TABLE+"." + TaskSQLite.KEY_ROWID + " = " + NotificationSQLite.DATABASE_TABLE + "."
				+ NotificationSQLite.KEY_IDTASK);
		queryBuilder.appendWhere(NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_ROWID + "=" + idEvent );
        Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
        	do{
        		Integer id = cursor.getInt(0);
            	String name = cursor.getString(9);
            	Long dateAsLong = cursor.getLong(2);
        		Date date = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotice = new Date(dateAsLong);
            	String time =cursor.getString(12);
            	String repetition = cursor.getString(13);
            	String description = cursor.getString(14);
            	String tag = cursor.getString(15);
            	Integer superEvent = cursor.getInt(7);
            	Boolean done = cursor.getInt(5)==1 ? true: false;
            	task = new RemindTask(id, name, date,dateNotice, time, repetition, description, tag, superEvent, done);
            	
        	}while(cursor.moveToNext());
        
        }
        this.close();
        return task;
	}

	public ArrayList<RemindTask> getTaskWithTag(String tag) {
		this.open();
		ArrayList<RemindTask> taskEventList = new ArrayList<RemindTask>();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(NotificationSQLite.DATABASE_TABLE + " LEFT OUTER JOIN " + TaskSQLite.DATABASE_TABLE + " ON " +
				TaskSQLite.DATABASE_TABLE+"." + TaskSQLite.KEY_ROWID + " = " + NotificationSQLite.DATABASE_TABLE + "."
				+ NotificationSQLite.KEY_IDTASK);
		queryBuilder.appendWhere(TaskSQLite.DATABASE_TABLE+"."+TaskSQLite.KEY_TAG + "=" + tag);
		queryBuilder.appendWhere(" AND " + NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DONE + "=" + Integer.toString(0) );
		
		
		Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
		RemindTask taskEvent;
		if (cursor.moveToFirst()){
        	do {
        		Integer idEvent = cursor.getInt(0);
        		String name = cursor.getString(9);
        		Long dateAsLong = cursor.getLong(2);
        		Date taskDate = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotify = new Date(dateAsLong);
        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
        		String time = Time.formatTime(taskDate);
        		String repetition = cursor.getString(13);
        		String description = cursor.getString(14);
        		Integer superEvent = cursor.getInt(7);
        		Boolean done = cursor.getInt(5)==1 ? true: false;
        		Log.d("SQLITE", idEvent.toString());
        		taskEvent = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
        		taskEventList.add(taskEvent);
        		
        	}while(cursor.moveToNext());
		}		
		this.close();
		return taskEventList;
	}
/**
	public ArrayList<RemindTask> getEventsBetweenDates(Date startDate, Date endDate) {
		this.open();
		ArrayList<RemindTask> taskEventList = new ArrayList<RemindTask>();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(NotificationSQLite.DATABASE_TABLE + " LEFT OUTER JOIN " + TaskSQLite.DATABASE_TABLE + " ON " +
				TaskSQLite.DATABASE_TABLE+"." + TaskSQLite.KEY_ROWID + " = " + NotificationSQLite.DATABASE_TABLE + "."
				+ NotificationSQLite.KEY_IDTASK);
		queryBuilder.appendWhere(NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + ">=" + startDate.getTime());
		queryBuilder.appendWhere(" AND "+NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + "<=" + endDate.getTime());
		
		
		Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
		RemindTask taskEvent;
		if (cursor.moveToFirst()){
        	do {
        		Integer idEvent = cursor.getInt(0);
        		String name = cursor.getString(9);
        		Long dateAsLong = cursor.getLong(2);
        		Date taskDate = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotify = new Date(dateAsLong);
        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
        		String time = Time.parseTime(taskDate);
        		String repetition = cursor.getString(13);
        		String description = cursor.getString(14);
        		String tag = cursor.getString(15);
        		Integer superEvent = cursor.getInt(7);
        		Boolean done = cursor.getInt(5)==1 ? true: false;
        		Log.d("SQLITE", idEvent.toString());
        		taskEvent = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
        		taskEventList.add(taskEvent);
        		
        	}while(cursor.moveToNext());
		}		
		//this.close();
		taskEventList.addAll(getUnhandleEvents(startDate));
		return taskEventList;
	}
*/
	
	public ArrayList<RemindTask> weeklyEventsBetweenDates(Date startDate,
			Date endDate, Integer dayOfWeek) {
		this.open();
		String strWeekly= "WEEKLY";
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TaskSQLite.DATABASE_TABLE + " LEFT OUTER JOIN " + NotificationSQLite.DATABASE_TABLE + " ON " +
				TaskSQLite.DATABASE_TABLE+"." + TaskSQLite.KEY_ROWID + " = " + NotificationSQLite.DATABASE_TABLE + "."
				+ NotificationSQLite.KEY_IDTASK);
		queryBuilder.appendWhere(TaskSQLite.DATABASE_TABLE+"."+TaskSQLite.KEY_REPETITION + "='" +strWeekly + "'");
		queryBuilder.appendWhere(" AND "+TaskSQLite.DATABASE_TABLE+"."+TaskSQLite.KEY_REPDAY + "=" + dayOfWeek );
		
		
		
		Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
		RemindTask taskEvent;
		TreeMap<Integer, RemindTask> map = new TreeMap<Integer, RemindTask>();
		if (cursor.moveToFirst()){
        	do {
        		//
        		
        		Integer idEvent = cursor.getInt(11);
        		String name = cursor.getString(1);
        		Long dateAsLong = cursor.getLong(2);
        		Date taskDate = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotify = new Date(dateAsLong);
        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
        		String time = Time.formatTime(taskDate);
        		String repetition = cursor.getString(5);
        		String description = cursor.getString(6);
        		String tag = cursor.getString(7);
        		Integer superEvent = cursor.getInt(18);
        		Boolean done = cursor.getInt(16)==1 ? true: false;
        		Log.d("SQLITE", idEvent.toString());
        		taskEvent = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
        		
        		map.put(cursor.getInt(0), taskEvent);
        		
        	}while(cursor.moveToNext());
		}		
		
		queryBuilder.appendWhere(" AND "+NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + ">" + startDate.getTime());
		queryBuilder.appendWhere(" AND "+NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + "<" + endDate.getTime());
		cursor = queryBuilder.query(db, null, null, null, null, null, null);
		ArrayList<RemindTask> taskEventList = new ArrayList<RemindTask>();
		if (cursor.moveToFirst()){
        	do {
        		
        		Integer idEvent = cursor.getInt(11);
        		String name = cursor.getString(1);
        		Long dateAsLong = cursor.getLong(13);
        		Date taskDate = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(14);
        		Date dateNotify = new Date(dateAsLong);
        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
        		String time = Time.formatTime(taskDate);
        		String repetition = cursor.getString(5);
        		String description = cursor.getString(6);
        		String tag = cursor.getString(7);
        		Integer superEvent = cursor.getInt(18);
        		Boolean done = cursor.getInt(16)==1 ? true: false;
        		Log.d("SQLITE", idEvent.toString());
        		taskEvent = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
        		taskEventList.add(taskEvent);
        		map.remove(cursor.getInt(0));
        		
        	}while(cursor.moveToNext());
		}		
		this.close();
		
		for(RemindTask task:map.values()){
			task.setDate(Time.updateDate(task.getDate(), startDate));
			task.setDateNotice(Time.updateDate(task.getDate(), startDate));
			taskEventList.add(task);
		}
		
		return taskEventList;
	}

	public ArrayList<RemindTask> monthlyEventsBetweenDates(Date startDate,
			Date endDate, Integer dayOfMonth) {
				this.open();
				String strMonthly= "MONTHLY";
				SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
				queryBuilder.setTables(TaskSQLite.DATABASE_TABLE + " LEFT OUTER JOIN " + NotificationSQLite.DATABASE_TABLE + " ON " +
						TaskSQLite.DATABASE_TABLE+"." + TaskSQLite.KEY_ROWID + " = " + NotificationSQLite.DATABASE_TABLE + "."
						+ NotificationSQLite.KEY_IDTASK);
				queryBuilder.appendWhere(TaskSQLite.DATABASE_TABLE+"."+TaskSQLite.KEY_REPETITION + "='" +strMonthly + "'");
				queryBuilder.appendWhere(" AND "+TaskSQLite.DATABASE_TABLE+"."+TaskSQLite.KEY_REPDAY + "=" + dayOfMonth );
				
				
				
				Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
				RemindTask taskEvent;
				TreeMap<Integer, RemindTask> map = new TreeMap<Integer, RemindTask>();
				if (cursor.moveToFirst()){
		        	do {
		        		//
		        		
		        		Integer idEvent = cursor.getInt(11);
		        		String name = cursor.getString(1);
		        		Long dateAsLong = cursor.getLong(2);
		        		Date taskDate = new Date(dateAsLong);
		        		dateAsLong = cursor.getLong(3);
		        		Date dateNotify = new Date(dateAsLong);
		        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
		        		String time = Time.formatTime(taskDate);
		        		String repetition = cursor.getString(5);
		        		String description = cursor.getString(6);
		        		String tag = cursor.getString(7);
		        		Integer superEvent = cursor.getInt(18);
		        		Boolean done = cursor.getInt(16)==1 ? true: false;
		        		Log.d("SQLITE", idEvent.toString());
		        		taskEvent = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
		        		
		        		map.put(cursor.getInt(0), taskEvent);
		        		
		        	}while(cursor.moveToNext());
				}		
				
				queryBuilder.appendWhere(" AND "+NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + ">" + startDate.getTime());
				queryBuilder.appendWhere(" AND "+NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + "<" + endDate.getTime());
				cursor = queryBuilder.query(db, null, null, null, null, null, null);
				ArrayList<RemindTask> taskEventList = new ArrayList<RemindTask>();
				if (cursor.moveToFirst()){
		        	do {
		        		
		        		Integer idEvent = cursor.getInt(11);
		        		String name = cursor.getString(1);
		        		Long dateAsLong = cursor.getLong(13);
		        		Date taskDate = new Date(dateAsLong);
		        		dateAsLong = cursor.getLong(14);
		        		Date dateNotify = new Date(dateAsLong);
		        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
		        		String time = Time.formatTime(taskDate);
		        		String repetition = cursor.getString(5);
		        		String description = cursor.getString(6);
		        		String tag = cursor.getString(7);
		        		Integer superEvent = cursor.getInt(18);
		        		Boolean done = cursor.getInt(16)==1 ? true: false;
		        		Log.d("SQLITE", idEvent.toString());
		        		taskEvent = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
		        		taskEventList.add(taskEvent);
		        		map.remove(cursor.getInt(0));
		        		
		        	}while(cursor.moveToNext());
				}		
				this.close();
				
				for(RemindTask task:map.values()){
					task.setDate(Time.updateDate(task.getDate(), startDate));
					task.setDateNotice(Time.updateDate(task.getDate(), startDate));
					taskEventList.add(task);
				}
				
				return taskEventList;
			}


	public ArrayList<RemindTask> yearlyEventsBetweenDates(Date startDate,
			Date endDate, Integer dayOfYear) {
				this.open();
				String strYearly= "ANNUAL";
				SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
				queryBuilder.setTables(TaskSQLite.DATABASE_TABLE + " LEFT OUTER JOIN " + NotificationSQLite.DATABASE_TABLE + " ON " +
						TaskSQLite.DATABASE_TABLE+"." + TaskSQLite.KEY_ROWID + " = " + NotificationSQLite.DATABASE_TABLE + "."
						+ NotificationSQLite.KEY_IDTASK);
				queryBuilder.appendWhere(TaskSQLite.DATABASE_TABLE+"."+TaskSQLite.KEY_REPETITION + "='" +strYearly+ "'");
				queryBuilder.appendWhere(" AND "+TaskSQLite.DATABASE_TABLE+"."+TaskSQLite.KEY_REPDAY + "=" + dayOfYear );
				
				
				
				Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
				RemindTask taskEvent;
				TreeMap<Integer, RemindTask> map = new TreeMap<Integer, RemindTask>();
				if (cursor.moveToFirst()){
		        	do {
		        		//
		        		
		        		Integer idEvent = cursor.getInt(11);
		        		String name = cursor.getString(1);
		        		Long dateAsLong = cursor.getLong(2);
		        		Date taskDate = new Date(dateAsLong);
		        		dateAsLong = cursor.getLong(3);
		        		Date dateNotify = new Date(dateAsLong);
		        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
		        		String time = Time.formatTime(taskDate);
		        		String repetition = cursor.getString(5);
		        		String description = cursor.getString(6);
		        		String tag = cursor.getString(7);
		        		Integer superEvent = cursor.getInt(18);
		        		Boolean done = cursor.getInt(16)==1 ? true: false;
		        		Log.d("SQLITE", idEvent.toString());
		        		taskEvent = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
		        		
		        		map.put(cursor.getInt(0), taskEvent);
		        		
		        	}while(cursor.moveToNext());
				}		
				
				queryBuilder.appendWhere(" AND "+NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + ">" + startDate.getTime());
				queryBuilder.appendWhere(" AND "+NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + "<" + endDate.getTime());
				cursor = queryBuilder.query(db, null, null, null, null, null, null);
				ArrayList<RemindTask> taskEventList = new ArrayList<RemindTask>();
				if (cursor.moveToFirst()){
		        	do {
		        		
		        		Integer idEvent = cursor.getInt(11);
		        		String name = cursor.getString(1);
		        		Long dateAsLong = cursor.getLong(13);
		        		Date taskDate = new Date(dateAsLong);
		        		dateAsLong = cursor.getLong(14);
		        		Date dateNotify = new Date(dateAsLong);
		        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
		        		String time = Time.formatTime(taskDate);
		        		String repetition = cursor.getString(5);
		        		String description = cursor.getString(6);
		        		String tag = cursor.getString(7);
		        		Integer superEvent = cursor.getInt(18);
		        		Boolean done = cursor.getInt(16)==1 ? true: false;
		        		Log.d("SQLITE", idEvent.toString());
		        		taskEvent = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
		        		taskEventList.add(taskEvent);
		        		map.remove(cursor.getInt(0));
		        		
		        	}while(cursor.moveToNext());
				}		
				this.close();
				
				for(RemindTask task:map.values()){
					task.setDate(Time.updateDate(task.getDate(), startDate));
					task.setDateNotice(Time.updateDate(task.getDate(), startDate));
					taskEventList.add(task);
				}
				
				return taskEventList;
			}

	public Collection<? extends RemindTask> dailyEventsBetweenDates(Date startDate,
			Date endDate) {
		this.open();
		String strDaily= "DAILY";
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TaskSQLite.DATABASE_TABLE + " LEFT OUTER JOIN " + NotificationSQLite.DATABASE_TABLE + " ON " +
				TaskSQLite.DATABASE_TABLE+"." + TaskSQLite.KEY_ROWID + " = " + NotificationSQLite.DATABASE_TABLE + "."
				+ NotificationSQLite.KEY_IDTASK);
		queryBuilder.appendWhere(TaskSQLite.DATABASE_TABLE+"."+TaskSQLite.KEY_REPETITION + "='" +strDaily + "'");
				
		
		
		Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
		RemindTask taskEvent;
		TreeMap<Integer, RemindTask> map = new TreeMap<Integer, RemindTask>();
		if (cursor.moveToFirst()){
        	do {
        		//
        		
        		Integer idEvent = cursor.getInt(11);
        		String name = cursor.getString(1);
        		Long dateAsLong = cursor.getLong(2);
        		Date taskDate = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotify = new Date(dateAsLong);
        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
        		String time = Time.formatTime(taskDate);
        		String repetition = cursor.getString(5);
        		String description = cursor.getString(6);
        		String tag = cursor.getString(7);
        		Integer superEvent = cursor.getInt(18);
        		Boolean done = cursor.getInt(16)==1 ? true: false;
        		Log.d("SQLITE", idEvent.toString());
        		taskEvent = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
        		
        		map.put(cursor.getInt(0), taskEvent);
        		
        	}while(cursor.moveToNext());
		}		
		
		queryBuilder.appendWhere(" AND "+NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + ">" + startDate.getTime());
		queryBuilder.appendWhere(" AND "+NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + "<" + endDate.getTime());
		cursor = queryBuilder.query(db, null, null, null, null, null, null);
		ArrayList<RemindTask> taskEventList = new ArrayList<RemindTask>();
		if (cursor.moveToFirst()){
        	do {
        		
        		Integer idEvent = cursor.getInt(11);
        		String name = cursor.getString(1);
        		Long dateAsLong = cursor.getLong(13);
        		Date taskDate = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(14);
        		Date dateNotify = new Date(dateAsLong);
        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
        		String time = Time.formatTime(taskDate);
        		String repetition = cursor.getString(5);
        		String description = cursor.getString(6);
        		String tag = cursor.getString(7);
        		Integer superEvent = cursor.getInt(18);
        		Boolean done = cursor.getInt(16)==1 ? true: false;
        		Log.d("SQLITE", idEvent.toString());
        		taskEvent = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
        		taskEventList.add(taskEvent);
        		map.remove(cursor.getInt(0));
        		
        	}while(cursor.moveToNext());
		}		
		this.close();
		
		for(RemindTask task:map.values()){
			task.setDate(Time.updateDate(task.getDate(), startDate));
			task.setDateNotice(Time.updateDate(task.getDate(), startDate));
			taskEventList.add(task);
		}
		
		return taskEventList;
	}

	public Collection<? extends RemindTask> singleEventsBetweenDates(Date startDate,
			Date endDate) {
		this.open();
		String strSingle= "SINGLE";
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TaskSQLite.DATABASE_TABLE + " LEFT OUTER JOIN " + NotificationSQLite.DATABASE_TABLE + " ON " +
				TaskSQLite.DATABASE_TABLE+"." + TaskSQLite.KEY_ROWID + " = " + NotificationSQLite.DATABASE_TABLE + "."
				+ NotificationSQLite.KEY_IDTASK);
		queryBuilder.appendWhere(TaskSQLite.DATABASE_TABLE+"."+TaskSQLite.KEY_REPETITION + "='" +strSingle + "'");
		queryBuilder.appendWhere(" AND "+NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + ">" + startDate.getTime());
		queryBuilder.appendWhere(" AND "+NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + "<" + endDate.getTime());		
		
		
		Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
		RemindTask taskEvent;
		ArrayList<RemindTask> taskEventList = new ArrayList<RemindTask>();
		if (cursor.moveToFirst()){
        	do {
        		//
        		
        		Integer idEvent = cursor.getInt(11);
        		String name = cursor.getString(1);
        		Long dateAsLong = cursor.getLong(2);
        		Date taskDate = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotify = new Date(dateAsLong);
        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
        		String time = Time.formatTime(taskDate);
        		String repetition = cursor.getString(5);
        		String description = cursor.getString(6);
        		String tag = cursor.getString(7);
        		Integer superEvent = cursor.getInt(18);
        		Boolean done = cursor.getInt(16)==1 ? true: false;
        		Log.d("SQLITE", idEvent.toString());
        		taskEvent = new RemindTask(idEvent, name, taskDate,dateNotify, time, repetition,description, tag, superEvent, done);
        		
        		taskEventList.add(taskEvent);
        		
        	}while(cursor.moveToNext());
		}		
		this.close();
		
		
		return taskEventList;
	}


}

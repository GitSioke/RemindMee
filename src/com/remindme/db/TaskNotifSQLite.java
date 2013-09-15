package com.remindme.db;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.remindme.utils.RemindTask;

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
		queryBuilder.setTables(TaskSQLite.DATABASE_TABLE + " AS TASKTABLE LEFT OUTER JOIN " + NotificationSQLite.DATABASE_TABLE + " AS NOTIFTABLE ON " +
		        "NOTIFTABLE."+TaskSQLite.KEY_ROWID + " = " + NotificationSQLite.KEY_IDTASK);
		queryBuilder.appendWhere("NOTIFTABLE."+NotificationSQLite.KEY_DATE + "=" + date.getTime() );
		
		Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
		RemindTask task;
		ArrayList<RemindTask> taskList = new ArrayList<RemindTask>();
		boolean isCompleted = false;
		Integer boolValue = isCompleted== true ? 1: 0;
		
		if (cursor.moveToFirst()){
        	do {
        		Integer id = cursor.getInt(0);
        		String name = cursor.getString(1);
        		Long dateAsLong = cursor.getLong(2);
        		Date date1 = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotice = new Date(dateAsLong);
        		String time =cursor.getString(4);
        		String repetition = cursor.getString(5);
        		String description = cursor.getString(6);
        		String tag = cursor.getString(7);
        		Integer superTask = cursor.getInt(8);
        		Boolean completed = cursor.getInt(9)==1 ? true: false;
        		if(completed==true){
        			Log.d("Handler", "tarea erronea");
        		}
        		Log.d("SQLITE", id.toString());
        		task = new RemindTask(id, name, date,dateNotice, time, repetition,description, tag, superTask, completed);
        		taskList.add(task);
        		
        	}while(cursor.moveToNext());
        		
        }
		this.close();
		
		return taskList;
	}

	public ArrayList<RemindTask> getAllTasksBefore(Date date) {
		this.open();
		ArrayList<RemindTask> taskList = new ArrayList<RemindTask>();
        
		this.close();
		
		return taskList;
	}

}

package com.remindme.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		queryBuilder.setTables(NotificationSQLite.DATABASE_TABLE + " LEFT OUTER JOIN " + TaskSQLite.DATABASE_TABLE + " ON " +
				TaskSQLite.DATABASE_TABLE+"." + TaskSQLite.KEY_ROWID + " = " + NotificationSQLite.DATABASE_TABLE + "."
				+ NotificationSQLite.KEY_IDTASK);
		queryBuilder.appendWhere(NotificationSQLite.DATABASE_TABLE+"."+NotificationSQLite.KEY_DATE + "=" + date.getTime() );
		
		Log.d("TaskNotifSQLite", Long.toString(date.getTime()));
		Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
		RemindTask task;
		ArrayList<RemindTask> taskList = new ArrayList<RemindTask>();
			
		if (cursor.moveToFirst()){
        	do {
        		Integer idTask = cursor.getInt(1);
        		String name = cursor.getString(8);
        		Long dateAsLong = cursor.getLong(2);
        		Date taskDate = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotify = new Date(dateAsLong);
        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
        		String time = cursor.getString(11);
        		String repetition = cursor.getString(12);
        		String description = cursor.getString(13);
        		String tag = cursor.getString(14);
        		Integer superTask = cursor.getInt(15);
        		Boolean completed = cursor.getInt(16)==1 ? true: false;
        		if(completed==true){
        			Log.d("Handler", "tarea erronea");
        		}
        		Log.d("SQLITE", idTask.toString());
        		task = new RemindTask(idTask, name, taskDate,dateNotify, time, repetition,description, tag, superTask, completed);
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
        		Integer idTask = cursor.getInt(1);
        		String name = cursor.getString(8);
        		Long dateAsLong = cursor.getLong(2);
        		Date taskDate = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotify = new Date(dateAsLong);
        		//TODO el time de aqui estara mal en los casos en que haya repeticion de tareas. hay que recalcularlo a partir de notify date
        		String time = cursor.getString(11);
        		String repetition = cursor.getString(12);
        		String description = cursor.getString(13);
        		String tag = cursor.getString(14);
        		//TODO las supertareas deben corresnpoder a cada uno de las notificaciones si no cuando haya que completar las super habra errores pues siempre habra tareas infinitas por completar digamos
        		Integer superTask = cursor.getInt(15);
        		Boolean completed = cursor.getInt(16)==1 ? true: false;
        		if(completed==true){
        			Log.d("Handler", "tarea erronea");
        		}
        		Log.d("SQLITE", idTask.toString());
        		task = new RemindTask(idTask, name, taskDate,dateNotify, time, repetition,description, tag, superTask, completed);
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
	
	
	
}

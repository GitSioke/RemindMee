package com.remindme.db;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.remindme.utils.RemindTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskSQLite implements TaskDAO{
	
	public static final String KEY_ROWID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_DATE="date";
	public static final String KEY_DATENOTICE="dateNotice";
	public static final String KEY_TIME="time";
	public static final String KEY_REPETITION="repetition";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_TAG="tag";
	public static final String KEY_COMPLETED="completed";
	public static final String KEY_SUPERTASK="supertask";
		
	
	public static final String DATABASE_TABLE = "tasks";

	
	private final Context context;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public TaskSQLite(Context ctx){
		this.context = ctx;
		dbHelper = new DatabaseHelper(context);
	}
	
	public TaskSQLite open() throws SQLException{
		db= dbHelper.getReadableDatabase();
		return this;		
	}
	
	public void close(){
		dbHelper.close();
	}
		
	/**
	 * Inserta en la tabla los parametros que le llegan de la nueva tarea
	 * @param taskName
	 * @param date
	 * @param time
	 * @param repetition
	 * @param tag
	 * @return
	 */
	//TODO Revisar si se puede hacer un con ContentValues de parametro o crear una clase de tipo Task
	public long insertTask(RemindTask task){
		this.open();
		ContentValues taskValues = new ContentValues();
		taskValues.put(KEY_ROWID, task.getId());
		taskValues.put(KEY_NAME, task.getName());
		taskValues.put(KEY_DATE, task.getDate().getTime());
		taskValues.put(KEY_DATENOTICE, task.getDateNotice().getTime());
		taskValues.put(KEY_TIME, task.getTime());
		taskValues.put(KEY_REPETITION, task.getRepetition());
		taskValues.put(KEY_DESCRIPTION, task.getDescription());
		taskValues.put(KEY_TAG, task.getTag());
		taskValues.put(KEY_SUPERTASK, task.getSuperTask());
		taskValues.put(KEY_COMPLETED, task.isCompleted());
		long changes = db.insert(DATABASE_TABLE, null, taskValues);
		this.close();
		return changes;
		
	}


	/**public String read() {
		String result = "";
		String[] columns = {"id", "name"};
		Cursor newTask = this.getReadableDatabase().query("Tasks", columns, null, null, null, null, null);
		newTask.moveToFirst();
		result= newTask.getString(newTask.getColumnIndex("id")) +""+ 
				newTask.getString(newTask.getColumnIndex("name"))+"\n";
				
				
		return  result;
		
	}*/


	public ArrayList<RemindTask> getAllTasks() {
		this.open();
		RemindTask task;
		ArrayList<RemindTask> taskList = new ArrayList<RemindTask>();
		Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
		if (cursor.moveToFirst()){
        	do {
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
        		task = new RemindTask(id, name, date, dateNotice, time, repetition, description, tag, superTask, completed);
        		taskList.add(task);
        		
        	}while(cursor.moveToNext());
        		
        }
		this.close();
		
		return taskList;
	}
	

	public String[] getFromColumnsTask() {
		String[] fromColumns = {KEY_ROWID, KEY_NAME};
		return fromColumns;
	}

	public ArrayList<String> getAllTags() {
		this.open();
		String tag;
		ArrayList<String> tagList = new ArrayList<String>();
		Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_TAG}, null, null, null, null, null);
		if (cursor.moveToFirst()){
        	do {
        		Log.d("getAllTags",cursor.getString(0));
        		tag = cursor.getString(0);
        		if(!tagList.contains(tag) && tag!=null){
        			Log.d("getALLTag", "Entra en if");
        			tagList.add(tag);
        		}
        	}while(cursor.moveToNext());
        		
        }
		this.close();
		return tagList;
	}

	public ArrayList<RemindTask> getTaskWithTag(String tag) {
		this.open();
		RemindTask task;
		ArrayList<RemindTask> taskList = new ArrayList<RemindTask>();
		Cursor cursor = db.query(DATABASE_TABLE, null, KEY_TAG+"=?", new String[]{tag}, null, null, null);
		if (cursor.moveToFirst()){
        	do {
        		Integer id = cursor.getInt(0);
        		String name = cursor.getString(1);
        		Long dateAsLong = cursor.getLong(2);
        		Date date = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date dateNotice = new Date(dateAsLong);
        		String time =cursor.getString(4);
        		String repetition = cursor.getString(5);
        		String description = cursor.getString(6);
        		Integer superTask = cursor.getInt(7);
        		Boolean completed = cursor.getInt(8)==1 ? true: false;
        		task = new RemindTask(id, name, date,dateNotice, time, repetition, description, tag, superTask, completed);
        		taskList.add(task);
        		
        	}while(cursor.moveToNext());
        		
        }
		this.close();
		return taskList;
	}

	public int updateTask(RemindTask task) {
		this.open();
		ContentValues taskValues = new ContentValues();
		taskValues.put(KEY_ROWID, task.getId());
		taskValues.put(KEY_NAME, task.getName());
		taskValues.put(KEY_DATE, task.getDate().getTime());
		taskValues.put(KEY_DATENOTICE, task.getDateNotice().getTime());
		taskValues.put(KEY_TIME, task.getTime());
		taskValues.put(KEY_REPETITION, task.getRepetition());
		taskValues.put(KEY_DESCRIPTION, task.getDescription());
		taskValues.put(KEY_TAG, task.getTag());
		taskValues.put(KEY_SUPERTASK, task.getSuperTask());
		taskValues.put(KEY_COMPLETED, task.isCompleted());
		int id = db.update(DATABASE_TABLE, taskValues, KEY_ROWID+"=?", new String[]{task.getId().toString()});
		this.close();
		return id;
	}

	public void updateTaskCompleted(RemindTask task) {
		this.open();
		
		ContentValues taskValues = new ContentValues();
		if(task.isCompleted()){
			task.setCompleted(false);
		}else{
			task.setCompleted(true);
		}
				
		taskValues.put(KEY_COMPLETED, task.isCompleted());
	
		Integer acbd = db.update(DATABASE_TABLE, taskValues, "id=?", new String[]{Long.toString(task.getId())});
		Log.d("UPDATE", acbd.toString());
		this.close();
		
	}
	
	public ArrayList<RemindTask> getPendingTasks(Boolean isCompleted){
		this.open();
		RemindTask task;
		ArrayList<RemindTask> taskList = new ArrayList<RemindTask>();
		Integer boolValue = isCompleted== true ? 1: 0;
		Cursor cursor = db.query(DATABASE_TABLE, 
				null, KEY_COMPLETED+"=?", new String[]{Integer.toString(boolValue)}, null, null, null);
		if (cursor.moveToFirst()){
        	do {
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
	
	public ArrayList<RemindTask> getCompletedTask(){
		this.open();
		RemindTask task;
		ArrayList<RemindTask> taskList = new ArrayList<RemindTask>();
		Cursor cursor = db.query(DATABASE_TABLE, null, KEY_COMPLETED+"=?", new String[]{Integer.toString(0)}, null, null, null);
		if (cursor.moveToFirst()){
        	do {
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
        		if(completed==true){
        			Log.d("Handler", "tarea erronea");
        		}
        		task = new RemindTask(id, name, date, dateNotice, time, repetition, description, tag, superTask, completed);
        		taskList.add(task);
        		
        	}while(cursor.moveToNext());
        		
        }
		this.close();
		
		return taskList;
	}

    public RemindTask getTaskWithID(Integer idTask) {
        RemindTask task = null;
        this.open();
        Cursor cursor =db.query(DATABASE_TABLE, null, KEY_ROWID+"=?", new String[]{Long.toString(idTask)}, null, null, null);
        
        if (cursor.moveToFirst()){
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
        }
        this.close();
        return task;
}

	public List<RemindTask> getSubtasks(Integer idTask) {
		RemindTask task = null;
		List<RemindTask> subTasks= new ArrayList<RemindTask>();
		this.open();
        Cursor cursor =db.query(DATABASE_TABLE, null, KEY_SUPERTASK+"=?", new String[]{Long.toString(idTask)}, null, null, null);
        
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

	public int deleteTask(Integer idTask) {
		this.open();
		int changes = db.delete(DATABASE_TABLE, KEY_ROWID+"=?", new String[]{idTask.toString()});
		this.close();
		return changes;
	}

	public int deleteSubtask(Integer idTask) {
		this.open();
		int changes = db.delete(DATABASE_TABLE, KEY_SUPERTASK+"=?", new String[]{idTask.toString()});
		this.close();
		return changes;
	}

	public boolean hasSubtask(Integer idTask) {
		this.open();
		Boolean hasSubtask=false;
		Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_SUPERTASK}, KEY_SUPERTASK+"=?", new String[]{Long.toString(idTask)}, null, null, null);
		if (cursor.getCount()>0){
			hasSubtask=true;
		}
		this.close();
		return hasSubtask;
	}

	public ArrayList<RemindTask> getTaskBetweenDates(Date startDate, Date endDate) {
		//TODO Revisar que lo haga sin mirar los minutos y segundos
		ArrayList<RemindTask> taskList = new ArrayList<RemindTask>();
		this.open();
        Cursor cursor =db.query(DATABASE_TABLE, null, KEY_DATE+">? AND "+ KEY_DATE+"<?", 
        		new String[]{Long.toString(startDate.getTime()),Long.toString(endDate.getTime()) }, null, null, null);
        
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
            	RemindTask task = new RemindTask(id, name, date,dateNotice, time, repetition, description, tag, superTask, completed);
            	taskList.add(task);
        	}while(cursor.moveToNext());
        
        }
        this.close();
        
		return taskList;
	}

	
}

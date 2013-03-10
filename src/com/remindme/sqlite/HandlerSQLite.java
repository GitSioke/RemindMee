package com.remindme.sqlite;



import java.util.ArrayList;
import java.util.List;

import remind.me.RemindTask;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HandlerSQLite{
	
	public static final String KEY_ROWID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_DATE="date";
	public static final String KEY_TIME="time";
	public static final String KEY_REPETITION="repetition";
	public static final String KEY_TAG="tag";
		
	private static final String TAG = "DBHandler";
	
	private static final String DATABASE_NAME = "RemindMeDB";
	private static final String DATABASE_TABLE = "tasks";
	private static final int DATABASE_VERSION = 3;
	
	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS tasks(id INTEGER PRIMARY KEY, " 
			+"name VARCHAR not null, date VARCHAR not null, time VARCHAR, repetition VARCHAR, tag VARCHAR);" ;
	
	private static final String DATABASE_CREATE4 = "CREATE TABLE IF NOT EXISTS tasks(id INTEGER PRIMARY KEY, " +
	 		"name VARCHAR not null)";

	
	private static final String DATABASE_CREATE3 = "CREATE TABLE IF NOT EXISTS" + DATABASE_TABLE +"("+KEY_ROWID +" INTEGER PRIMARY KEY," 
			+KEY_NAME+" VARCHAR not null "+KEY_DATE+ " VARCHAR not null " +KEY_TIME+ " VARCHAR " +KEY_REPETITION+ " VARCHAR " +KEY_TAG+ " VARCHAR);";
	
	/**private static final String DATABASE_CREATE2 = String.format("create table %s(%s TEXT, %s TEXT,%s TEXT, %s TEXT,%s TEXT, %s TEXT,)", KEY_ROWID, KEY_NAME, 
			KEY_DATE, KEY_TIME, KEY_REPETITION, KEY_TAG);*/
	
	
	private final Context context;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public HandlerSQLite(Context ctx){
		this.context = ctx;
		dbHelper = new DatabaseHelper(context);
	}
	
	public HandlerSQLite open() throws SQLException{
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
	public long insertNewTask(RemindTask task){
		
		ContentValues taskValues = new ContentValues();
		taskValues.put(KEY_ROWID, task.getId());
		taskValues.put(KEY_NAME, task.getName());
		taskValues.put(KEY_DATE, task.getDate());
		taskValues.put(KEY_TIME, task.getTime());
		taskValues.put(KEY_REPETITION, task.getRepetition());
		taskValues.put(KEY_TAG, task.getTag());
		return db.insert(DATABASE_TABLE, null, taskValues);		
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

	private static class DatabaseHelper extends SQLiteOpenHelper{

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try{
			db.execSQL(DATABASE_CREATE);
			}catch (SQLException e){
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version" +oldVersion + "to" 
					+ newVersion + ", which will destroy all old data");
			
			db.execSQL("DROP TABLE IF EXISTS tasks");
			onCreate(db);
		}
	}


	public List<RemindTask> getAllTasks() {
		RemindTask task;
		List<RemindTask> taskList = new ArrayList<RemindTask>();
		Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME, 
				KEY_DATE, KEY_TIME, KEY_REPETITION, KEY_TAG}, null, null, null, null, null);
		if (cursor.moveToFirst()){
        	do {
        		
        		String name = cursor.getString(0);
        		String date = cursor.getString(1);
        		String time =cursor.getString(2);
        		String repetition = cursor.getString(3);
        		String tag = cursor.getString(4);
        		task = new RemindTask(name, date, time, repetition, tag);
        		taskList.add(task);
        	}while(cursor.moveToNext());
        		
        }
		
		return taskList;
	}

	public String[] getFromColumnsTask() {
		String[] fromColumns = {KEY_ROWID, KEY_NAME};
		return fromColumns;
	}
	
}

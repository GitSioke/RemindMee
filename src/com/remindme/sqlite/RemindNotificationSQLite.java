package com.remindme.sqlite;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import remind.me.RemindNotification;
import remind.me.RemindNotificationDAO;

public class RemindNotificationSQLite implements RemindNotificationDAO {
	public static final String KEY_ROWID = "id";
	public static final String KEY_IDTASK = "idTask";
	public static final String KEY_DATE="date";
	public static final String KEY_DELAY="delay";
	public static final String KEY_CHECKED="cheked";

		
	private static final String TAG = "DBHandler";
	
	private static final String DATABASE_NAME = "RemindMeDB";
	private static final String DATABASE_TABLE = "notifications";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS notifications(id INTEGER PRIMARY KEY, " 
			+"idTask INTEGER not null, date LONG not null, delay LONG, checked BOOL);" ;
	
		
	private final Context context;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public RemindNotificationSQLite(Context ctx){
		this.context = ctx;
		dbHelper = new DatabaseHelper(context);
	}
	
	public RemindNotificationSQLite open() throws SQLException{
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
	public long insertNotification(RemindNotification notification){
		this.open();
		ContentValues taskValues = new ContentValues();
		taskValues.put(KEY_ROWID, notification.getId());
		taskValues.put(KEY_IDTASK, notification.getIdTask());
		taskValues.put(KEY_DATE, notification.getDate().getTime());
		taskValues.put(KEY_DELAY, notification.getDelay().getTime());
		taskValues.put(KEY_CHECKED, notification.getChecked());
		long changes = db.insert(DATABASE_TABLE, null, taskValues);
		this.close();
		return changes;
		
	}


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

	public ArrayList<RemindNotification> getAllNotifications() {
		this.open();
		RemindNotification notification;
		ArrayList<RemindNotification> notifyList = new ArrayList<RemindNotification>();
		Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
		if (cursor.moveToFirst()){
        	do {
        		Integer id = cursor.getInt(0);
        		Integer idTask = cursor.getInt(1);
        		Long dateAsLong = cursor.getLong(2);
        		Date date = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date delay= new Date(dateAsLong);
        		Boolean checked =  cursor.getInt(8)==1 ? true: false;
        		notification = new RemindNotification(id, idTask, date, delay, checked);
        		notifyList.add(notification);
        		
        	}while(cursor.moveToNext());
        		
        }
		this.close();
		
		return notifyList;
	}
	

	
}

package com.remindme.sqlite;

import java.util.ArrayList;
import java.util.Date;

import com.utils.RemindNotification;
import com.utils.RemindTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class RemindNotificationSQLite implements RemindNotificationDAO {
	public static final String KEY_ROWID = "id";
	public static final String KEY_IDTASK = "idTask";
	public static final String KEY_DATE="date";
	public static final String KEY_DELAY="delay";
	public static final String KEY_READY="ready";
	public static final String KEY_DONE="done";

		
	private static final String TAG = "DBHandler";
	
	private static final String DATABASE_NAME = "RemindMeDB";
	private static final String DATABASE_TABLE = "notifications";
	private static final int DATABASE_VERSION = 9;
	
	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS notifications(id INTEGER PRIMARY KEY, " 
			+"idTask INTEGER not null, date LONG not null, delay LONG, ready BOOL, done BOOL);" ;
	
		
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
		ContentValues notValues = new ContentValues();
		notValues.put(KEY_ROWID, notification.getId());
		notValues.put(KEY_IDTASK, notification.getIdTask());
		notValues.put(KEY_DATE, notification.getDate().getTime());
		notValues.put(KEY_DELAY, notification.getDelay().getTime());
		notValues.put(KEY_READY, notification.isReady());
		notValues.put(KEY_DONE, notification.isDone());
		long changes = db.insert(DATABASE_TABLE, null, notValues);
		this.close();
		return changes;
		
	}
	
	public long insertAll(ArrayList<RemindNotification> notificationList){
		long changes = 0;
		for(RemindNotification notification: notificationList){
			changes = insertNotification(notification);
		}
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
	/**
	 * 
	 * @return List of notifications that are ready to be notified and are not done
	 */
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
        		Boolean ready =  cursor.getInt(4)==1 ? true: false;
        		Boolean done =  cursor.getInt(5)==1 ? true: false;
        		notification = new RemindNotification(id, idTask, date, delay, ready, done);
        		notifyList.add(notification);
        		
        	}while(cursor.moveToNext());
        		
        }
		this.close();
		
		return notifyList;
	}
	
	/**
	 * 
	 * @return List of notifications that are ready to be notified but not done
	 */
	public ArrayList<RemindNotification> getReadyNotifications(){
		this.open();
		RemindNotification notification;
		ArrayList<RemindNotification> notificationList = new ArrayList<RemindNotification>();
		Cursor cursor = db.query(DATABASE_TABLE, null, KEY_READY+"=? AND " + KEY_DONE+"=?", 
				new String[]{Integer.toString(1), Integer.toString(0)}, null, null, null);
		if (cursor.moveToFirst()){
        	do {
        		Integer id = cursor.getInt(0);
        		Integer idTask = cursor.getInt(1);
        		Long dateAsLong = cursor.getLong(2);
        		Date date = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date delay = new Date(dateAsLong);
        		Boolean ready = cursor.getInt(4)==1 ? true: false;
        		Boolean done = cursor.getInt(5)==1 ? true: false;
        		notification = new RemindNotification(id, idTask, date, delay, ready, done);
        		notificationList.add(notification);
        		
        	}while(cursor.moveToNext());
        		
        }
		this.close();
		return notificationList;
	}
	/**
	 * 
	 * @param date 
	 * @return Devuelve todas aquellas notificaciones que no esten listas, ni completas y con fecha inferior a la indicada
	 */
	public ArrayList<RemindNotification> getUnreadyNotifications(long dateMax){
		this.open();
		RemindNotification notification;
		ArrayList<RemindNotification> notificationList = new ArrayList<RemindNotification>();
		Cursor cursor = db.query(DATABASE_TABLE, null, KEY_READY+"=? AND " + KEY_DONE+"=? AND " + KEY_DATE+"<?", 
				new String[]{Integer.toString(0), Integer.toString(0), Long.toString(dateMax)}, null, null, null);
		if (cursor.moveToFirst()){
        	do {
        		Integer id = cursor.getInt(0);
        		Integer idTask = cursor.getInt(1);
        		Long dateAsLong = cursor.getLong(2);
        		Date date = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date delay = new Date(dateAsLong);
        		Boolean ready = cursor.getInt(4)==1 ? true: false;
        		Boolean done = cursor.getInt(5)==1 ? true: false;
        		notification = new RemindNotification(id, idTask, date, delay, ready, done);
        		notificationList.add(notification);
        		
        	}while(cursor.moveToNext());
        		
        }
		this.close();
		return notificationList;
	}
	
	public int updateNotification(RemindNotification notif){
		this.open();
		ContentValues taskValues = new ContentValues();
		taskValues.put(KEY_ROWID, notif.getId());
		taskValues.put(KEY_IDTASK, notif.getIdTask());
		taskValues.put(KEY_DATE, notif.getDate().getTime());
		taskValues.put(KEY_DELAY, notif.getDelay().getTime());
		taskValues.put(KEY_READY, notif.isReady());
		taskValues.put(KEY_DONE, notif.isDone());
		int id = db.update(DATABASE_TABLE, taskValues, KEY_ROWID+"=?", new String[]{notif.getId().toString()});
		this.close();
		return id;
		
	}
	
	public ArrayList<RemindNotification> lapsedNotifications(long dateLong){
		this.open();
		RemindNotification notification;
		ArrayList<RemindNotification> notificationList = new ArrayList<RemindNotification>();
		Cursor cursor = db.query(DATABASE_TABLE, null, KEY_READY+"=? AND " + KEY_DONE+"=? AND " + KEY_DATE+"<?", 
				new String[]{Integer.toString(1), Integer.toString(0), Long.toString(dateLong)}, null, null, null);
	
		if (cursor.moveToFirst()){
        	do {
        		Integer id = cursor.getInt(0);
        		Integer idTask = cursor.getInt(1);
        		Long dateAsLong = cursor.getLong(2);
        		Date date = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date delay = new Date(dateAsLong);
        		Boolean ready = cursor.getInt(4)==1 ? true: false;
        		Boolean done = cursor.getInt(5)==1 ? true: false;
        		notification = new RemindNotification(id, idTask, date, delay, ready, done);
        		notificationList.add(notification);
        		
        	}while(cursor.moveToNext());
        		
        }
		this.close();
		return notificationList;
	}

	/**
	 * Devuelve la notificacion proxima con la idTask que se pasa como parametro
	 */
	public int amountReadyNotifications(Integer idTask) {
		this.open();
		int amount=-1;
		Cursor cursor = db.rawQuery(
				"SELECT count(*) from "+ DATABASE_TABLE+ " where "+ KEY_IDTASK+"=? AND " +
				KEY_READY+"=? AND " + KEY_DONE+"=?", 
				new String[]{Integer.toString(idTask), Integer.toString(1),Integer.toString(0)},null);
		
		if (cursor.moveToFirst()){
        		amount = cursor.getInt(0);	
        	}
		
		this.close();
		return amount;
	}
	
}

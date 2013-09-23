package com.remindme.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.remindme.utils.Event;
import com.remindme.utils.RemindTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



public class NotificationSQLite implements NotificationDAO {
	public static final String KEY_ROWID = "id";
	public static final String KEY_IDTASK = "idTask";
	public static final String KEY_DATE="date";
	public static final String KEY_NOTIFYDATE="notifyDate";
	public static final String KEY_READY="ready";
	public static final String KEY_DONE="done";
	public static final String KEY_UNATTENDED="unattended";
	public static final String KEY_SUPERNOTIF="superNotif";

	public static final String DATABASE_TABLE = "notifications";

		
	private final Context context;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public NotificationSQLite(Context ctx){
		this.context = ctx;
		dbHelper = new DatabaseHelper(context);
	}
	
	public NotificationSQLite open() throws SQLException{
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
	public long insert(Event notification){
		this.open();
		ContentValues notValues = new ContentValues();
		notValues.put(KEY_ROWID, notification.getId());
		notValues.put(KEY_IDTASK, notification.getIdTask());
		notValues.put(KEY_DATE, notification.getDate().getTime());
		notValues.put(KEY_NOTIFYDATE, notification.getNotifyDate().getTime());
		notValues.put(KEY_READY, notification.isReady());
		notValues.put(KEY_DONE, notification.isDone());
		notValues.put(KEY_UNATTENDED, this.unattendedNotifications(notification.getIdTask()));
		notValues.put(KEY_SUPERNOTIF, notification.getSuperEvent());
		long changes = db.insert(DATABASE_TABLE, null, notValues);
		this.close();
		return changes;
		
	}
	
	public long insertAll(ArrayList<Event> notificationList){
		long changes = 0;
		for(Event notification: notificationList){
			changes = insert(notification);
		}
		return changes;
		
	}

	/**
	 * 
	 * @return List of notifications that are ready to be notified and are not done
	 */
	public ArrayList<Event> getAllNotifications() {
		this.open();
		Event notification;
		ArrayList<Event> notifyList = new ArrayList<Event>();
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
        		Integer superNotif = cursor.getInt(6);
        		notification = new Event(id, idTask, date, delay, ready, done, superNotif);
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
	public ArrayList<Event> getReadyNotifications(){
		this.open();
		Event notification;
		ArrayList<Event> notificationList = new ArrayList<Event>();
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
        		Integer superNotif = cursor.getInt(6);
        		notification = new Event(id, idTask, date, delay, ready, done, superNotif);
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
	public ArrayList<Event> getUnreadyNotifications(long dateMax){
		this.open();
		Event notification;
		ArrayList<Event> notificationList = new ArrayList<Event>();
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
        		Integer superNotif = cursor.getInt(6);
        		notification = new Event(id, idTask, date, delay, ready, done, superNotif);
        		notificationList.add(notification);
        		
        	}while(cursor.moveToNext());
        		
        }
		this.close();
		return notificationList;
	}
	
	public int updateNotification(Event notif){
		this.open();
		ContentValues taskValues = new ContentValues();
		taskValues.put(KEY_ROWID, notif.getId());
		taskValues.put(KEY_IDTASK, notif.getIdTask());
		taskValues.put(KEY_DATE, notif.getDate().getTime());
		taskValues.put(KEY_NOTIFYDATE, notif.getNotifyDate().getTime());
		taskValues.put(KEY_READY, notif.isReady());
		taskValues.put(KEY_DONE, notif.isDone());
		int id = db.update(DATABASE_TABLE, taskValues, KEY_ROWID+"=?", new String[]{notif.getId().toString()});
		this.close();
		return id;
		
	}
	
	public ArrayList<Event> lapsedNotifications(long dateLong){
		this.open();
		Event notification;
		ArrayList<Event> notificationList = new ArrayList<Event>();
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
        		Integer superNotif = cursor.getInt(6);
        		notification = new Event(id, idTask, date, delay, ready, done, superNotif);
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

	public ArrayList<Event> allNotifIdTask(Integer idTask) {
		this.open();
		Event notification;
		ArrayList<Event> notificationList = new ArrayList<Event>();
		Cursor cursor = db.query(DATABASE_TABLE, null, KEY_IDTASK+"=?", 
				new String[]{Integer.toString(idTask)}, null, null, null);
	
		if (cursor.moveToFirst()){
        	do {
        		Integer id = cursor.getInt(0);
        		Long dateAsLong = cursor.getLong(2);
        		Date date = new Date(dateAsLong);
        		dateAsLong = cursor.getLong(3);
        		Date delay = new Date(dateAsLong);
        		Boolean ready = cursor.getInt(4)==1 ? true: false;
        		Boolean done = cursor.getInt(5)==1 ? true: false;
        		Integer superNotif = cursor.getInt(6);
        		notification = new Event(id, idTask, date, delay, ready, done, superNotif);
        		notificationList.add(notification);
        		
        	}while(cursor.moveToNext());
        		
        }
		return notificationList;
	}

	public int deleteAllIdTask(Integer idTask) {
		this.open();
		int changes = db.delete(DATABASE_TABLE, KEY_IDTASK+"=?", new String[]{idTask.toString()});
		this.close();
		return changes;
		
	}
	
	public int unattendedNotifications(Integer idTask){
		Integer result = 0;
		Cursor cursor;
		
		cursor = db.rawQuery(
				"SELECT count(*) from "+ DATABASE_TABLE+ " where "+ KEY_IDTASK+"=? AND " +
				KEY_READY+"=? AND " + KEY_DONE+"=?", 
				new String[]{Integer.toString(idTask), Integer.toString(1),Integer.toString(0)},null);

		if (cursor.moveToFirst())
		{
			result = cursor.getInt(0);
			result = (result==0 || result ==-1) ? 0 : result-1;
			
		}
		return result;
	}
	
	//TODO Check funciona
	public int deleteSubNotification(Integer idNotif) {
		this.open();
		int changes = db.delete(DATABASE_TABLE, KEY_SUPERNOTIF+"=?", new String[]{idNotif.toString()});
		this.close();
		return changes;
	}
	//TODO Check funciona
	public boolean hasSubNotification(Integer idNotif) {
		this.open();
		Boolean hasSubtask=false;
		Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_SUPERNOTIF}, KEY_SUPERNOTIF+"=?", new String[]{Long.toString(idNotif)}, null, null, null);
		if (cursor.getCount()>0){
			hasSubtask=true;
		}
		this.close();
		return hasSubtask;
	}
	//TODO Check que funciona 
	public List<RemindTask> getSubNotification(Integer idNotif) {
		RemindTask task = null;
		List<RemindTask> subTasks= new ArrayList<RemindTask>();
		this.open();
        Cursor cursor =db.query(DATABASE_TABLE, null, KEY_SUPERNOTIF+"=?", new String[]{Long.toString(idNotif)}, null, null, null);
        
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
	
	/**
	 * Recibe el id de un evento desde la "tarea"
	 */
	public void changeDone(Integer idEvent) {
		this.open();
		ContentValues taskValues = new ContentValues();
		taskValues.put(KEY_DONE, isDone(idEvent) ? 0: 1);
		db.update(DATABASE_TABLE, taskValues, KEY_ROWID+"=?", new String[]{Integer.toString(idEvent)});
		this.close();
		
		
		
	}
	
	private Boolean isDone(Integer idEvent){
		Cursor c = db.query(DATABASE_TABLE, new String[]{KEY_DONE}, KEY_ROWID+"=?", new String[]{Long.toString(idEvent)}, null, null, null);
		c.moveToFirst();
		c.getInt(0);
		return c.getInt(0) == 1 ? true: false ; 
	}

	public Event getNotificationWithID(Integer idEvent) {
		this.open();
		Cursor cursor = db.query(DATABASE_TABLE, null, KEY_ROWID+"=?", 
				new String[]{Integer.toString(idEvent)}, null, null, null);
		cursor.moveToFirst();
		Integer idTask = cursor.getInt(1);
		Long dateAsLong = cursor.getLong(2);
		Date date = new Date(dateAsLong);
		dateAsLong = cursor.getLong(3);
		Date delay = new Date(dateAsLong);
		Boolean ready = cursor.getInt(4)==1 ? true: false;
		Boolean done = cursor.getInt(5)==1 ? true: false;
		Boolean unattended = cursor.getInt(6)==1 ? true: false;
		Integer superNotif = cursor.getInt(7);
		Event notification = new Event(idEvent, idTask, date, delay, ready, done, superNotif);
		this.close();
		return notification;
	}

}

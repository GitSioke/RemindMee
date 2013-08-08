package com.remindme.sqlite;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	
	private static final String DATABASE_NAME = "RemindMeDB";
	
	private static final int DATABASE_VERSION = 12;
	
	private static final String TAG = "DBHandler";
	
	private static final String DATABASE_CREATETASK = "CREATE TABLE IF NOT EXISTS tasks(id INTEGER PRIMARY KEY, " 
			+"name VARCHAR not null, date LONG not null, dateNotice LONG, time VARCHAR, repetition VARCHAR, " +
			"description VARCHAR, tag VARCHAR, supertask INTEGER, completed BOOL);" ;
	
	private static final String DATABASE_CREATENOTIF = "CREATE TABLE IF NOT EXISTS notifications(id INTEGER PRIMARY KEY, " 
			+"idTask INTEGER not null, date LONG not null, delay LONG, ready BOOL, done BOOL, unattended INTEGER);" ;
	
	
	
	DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try{
		db.execSQL(DATABASE_CREATETASK);
		db.execSQL(DATABASE_CREATENOTIF);
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version" +oldVersion + "to" 
				+ newVersion + ", which will destroy all old data");
		
		db.execSQL("DROP TABLE IF EXISTS notifications");
		onCreate(db);
	}
}

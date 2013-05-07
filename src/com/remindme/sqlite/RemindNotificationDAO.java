package com.remindme.sqlite;

import java.util.ArrayList;

import remind.me.RemindNotification;



public interface RemindNotificationDAO {
	
	public long insertNotification(RemindNotification notification);
	public ArrayList<RemindNotification> getAllNotifications();
	public long insertAll(ArrayList<RemindNotification> notification);
	
}

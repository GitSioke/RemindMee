package com.remindme.sqlite;

import java.util.ArrayList;

import com.remindme.utils.RemindNotification;




public interface RemindNotificationDAO {
	
	public long insertNotification(RemindNotification notification);
	public ArrayList<RemindNotification> getAllNotifications();
	public long insertAll(ArrayList<RemindNotification> notification);
	public int amountReadyNotifications(Integer idTask);
	public ArrayList<RemindNotification> getUnreadyNotifications(long date);
	public int updateNotification(RemindNotification notif);
	public ArrayList<RemindNotification> lapsedNotifications(long dateLong);
	
}

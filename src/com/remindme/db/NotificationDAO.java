package com.remindme.db;

import java.util.ArrayList;
import java.util.List;

import com.remindme.utils.RemindNotification;
import com.remindme.utils.RemindTask;




public interface NotificationDAO {
	
	public ArrayList<RemindNotification> getAllNotifications();
	public long insertAll(ArrayList<RemindNotification> notification);
	public int amountReadyNotifications(Integer idTask);
	public ArrayList<RemindNotification> getUnreadyNotifications(long date);
	public int updateNotification(RemindNotification notif);
	public ArrayList<RemindNotification> lapsedNotifications(long dateLong);
	public ArrayList<RemindNotification> allNotifIdTask(Integer idTask);
	public int deleteAllIdTask(Integer idTask);
	public long insert(RemindNotification notif);
	public int deleteSubNotification(Integer idNotif);
	public boolean hasSubNotification(Integer idNotif);
	public List<RemindTask> getSubNotification(Integer idNotif);
}

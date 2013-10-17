package com.remindme.db;

import java.util.ArrayList;
import java.util.List;

import com.remindme.utils.Event;
import com.remindme.utils.RemindTask;




public interface NotificationDAO {
	
	public ArrayList<Event> getAllNotifications();
	public long insertAll(ArrayList<Event> notification);
	public int amountReadyNotifications(Integer idTask);
	public ArrayList<Event> getUnreadyNotifications(long date);
	public int updateNotification(Event notif);
	public ArrayList<Event> lapsedNotifications(long dateLong);
	public ArrayList<Event> allNotifIdTask(Integer idTask);
	public int deleteAllIdTask(Integer idTask);
	public long insert(Event notif);
	public int deleteSubNotification(Integer idNotif);
	public boolean hasSubNotification(Integer idNotif);
	public List<RemindTask> getSubNotification(Integer idNotif);
	public void changeDone(Integer id);
	public Event getNotificationWithID(Integer id);
	public ArrayList<Event> getAllReadyNotifications();
}

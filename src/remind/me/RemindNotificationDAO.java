package remind.me;

import java.util.ArrayList;



public interface RemindNotificationDAO {
	
	public long insertNotification(RemindNotification notification);
	public ArrayList<RemindNotification> getAllNotifications();
	
}

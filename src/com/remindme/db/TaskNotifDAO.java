package com.remindme.db;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.remindme.utils.*;

public interface TaskNotifDAO {

		public ArrayList<RemindTask> getAllTasksWith(Date date);
		public ArrayList<RemindTask> getAllTasksBefore(Date date);
		public List<RemindTask> getSubNotif(Integer idNotif);
		public RemindTask getRelatedTask(Integer idTask);
}

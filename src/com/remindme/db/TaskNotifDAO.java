package com.remindme.db;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.remindme.utils.*;

public interface TaskNotifDAO {

		public ArrayList<RemindTask> getAllTasksWith(Date date);
		public ArrayList<RemindTask> getAllTasksBefore(Date date);
		public List<RemindTask> getSubNotif(Integer idNotif);
		public RemindTask getRelatedTask(Integer idTask);
		public ArrayList<RemindTask> getTaskWithTag(String tag);
		/*public ArrayList<RemindTask> getEventsBetweenDates(Date day,
				Date endOfDay);*/
		public ArrayList<RemindTask> weeklyEventsBetweenDates(Date day,
				Date endOfDay, Integer dayOfWeek);
		public ArrayList<RemindTask> monthlyEventsBetweenDates(
				Date day, Date endOfDay, Integer dayOfMonth);
		public ArrayList<RemindTask> yearlyEventsBetweenDates(
				Date day, Date endOfDay, Integer dayOfYear);
}

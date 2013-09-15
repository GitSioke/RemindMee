package com.remindme.db;
import java.util.ArrayList;
import java.util.Date;

import com.remindme.utils.*;

public interface TaskNotifDAO {

		ArrayList<RemindTask> getAllTasksWith(Date date);
		ArrayList<RemindTask> getAllTasksBefore(Date date);
}

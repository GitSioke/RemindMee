package com.remindme.sqlite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.utils.RemindTask;


public interface RemindTaskDAO {
	public ArrayList<RemindTask> getTaskWithTag(String tag);
	public ArrayList<String> getAllTags();
	public ArrayList<RemindTask> getAllTasks();
	public long insertTask(RemindTask task);
	public int updateTask(RemindTask task);
	public void updateTaskCompleted(RemindTask task);
	public ArrayList<RemindTask> getPendingTasks(Boolean isCompleted);
	public RemindTask getTaskWithID(Integer idTask);
	public List<RemindTask> getSubtasks(Integer idTask);
	public int deleteTask(Integer idTask);
	public int deleteSubtask(Integer idTask);
	public boolean hasSubtask(Integer idTask);
	public ArrayList<RemindTask> getTaskWithDate(Date date);
}

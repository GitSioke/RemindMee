package remind.me;

import java.util.ArrayList;

public interface RemindTaskDAO {
	public ArrayList<RemindTask> getTaskWithTag(String tag);
	public ArrayList<String> getAllTags();
	public ArrayList<RemindTask> getAllTasks();
	public long insertNewTask(RemindTask task);
	public int updateTask(RemindTask task);
	public void updateTaskCompleted(RemindTask task);
	public ArrayList<RemindTask> getPendingTasks(Boolean isCompleted);
}

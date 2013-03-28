package remind.me;

import java.util.ArrayList;
import java.util.List;

public interface RemindTaskDAO {
	public ArrayList<RemindTask> getTaskWithTag(String tag);
	public ArrayList<String> getAllTags();
	public ArrayList<RemindTask> getAllTasks();
	public long insertNewTask(RemindTask task);
	public int updateTask(RemindTask task);
	public void updateTaskCompleted(RemindTask task);
	public ArrayList<RemindTask> getPendingTasks(Boolean isCompleted);
	public RemindTask getTaskWithID(Integer idTask);
	public List<RemindTask> getSubtasks(Integer idTask);
	public int deleteTask(Integer idTask);
	public int deleteSubtask(Integer idTask);
}

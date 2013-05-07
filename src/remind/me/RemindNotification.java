package remind.me;

import java.util.Date;


public class RemindNotification {

	private Integer id;
	private Integer idTask;
	private Date date;
	private Date delay;
	private Boolean checked;
	
	

	public RemindNotification(Integer id, Integer idTask, Date date, Date delay, Boolean checked){
		if (id==null)
			this.id = hashCode();
		else{
			this.id = id;
		}
		
		this.idTask= idTask;
		this.date = date;
		this.delay = delay;
		this.checked = checked;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdTask() {
		return idTask;
	}
	public void setIdTask(Integer idTask) {
		this.idTask = idTask;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDelay() {
		return delay;
	}
	public void setDelay(Date delay) {
		this.delay = delay;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
}

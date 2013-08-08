package com.remindme.utils;

import java.util.Date;


public class RemindNotification {

	private Integer id;
	private Integer idTask;
	private Date date;
	private Date notifyDate;
	private Boolean ready;
	private Boolean done;

	

	public RemindNotification(Integer id, Integer idTask, Date date, Date notifyDate, Boolean ready, Boolean done){
		if (id==null)
			this.id = hashCode();
		else{
			this.id = id;
		}
		
		this.idTask= idTask;
		this.date = date;
		this.notifyDate = notifyDate;
		this.ready= ready;
		this.done = done;
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
	public Date getNotifyDate() {
		return notifyDate;
	}
	public void setnotifyDate(Date notifyDate) {
		this.notifyDate = notifyDate;
	}
	public void setReady(Boolean ready){
		this.ready = ready;
	}
	
	public Boolean isReady() {
		return this.ready;
	}
	
	public Boolean isDone() {
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}


	
}

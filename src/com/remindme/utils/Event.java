package com.remindme.utils;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;


public class Event  implements Parcelable {

	private Integer id;
	private Integer idTask;
	private Date date;
	private Date notifyDate;
	private Boolean ready;
	private Boolean done;
	private Integer superEvent;

	

	public Event(Integer id, Integer idTask, Date date, Date notifyDate, Boolean ready, Boolean done, Integer superEvent){
		if (id==null || id == 0)
			this.id = hashCode();
		else{
			this.id = id;
		}
		
		this.idTask= idTask;
		this.date = date;
		this.notifyDate = notifyDate;
		this.ready= ready;
		this.done = done;
		if (superEvent == null || superEvent == 0){
			this.superEvent = Integer.valueOf(0); 
		
		}else{
			this.superEvent = superEvent;
		}
	}
	
	public Event(Parcel in) {
		super();
		readFromParcel(in);
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

	public Event getNextNotification(){
		//TODO Recalcula la siguiente notificacion?
		return new Event(null, null, date, notifyDate, false, false, superEvent);
	}

	public Integer getSuperEvent() {
		return superEvent;
	}

	public void setSuperEvent(Integer superEvent) {
		this.superEvent = superEvent;
	}
	
	public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }
 
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeInt(this.idTask);
		dest.writeLong(this.date.getTime());
		dest.writeLong(this.notifyDate.getTime());
		dest.writeByte((byte) (this.ready ? 1 : 0));
		dest.writeByte((byte) (this.done ? 1 : 0));
		dest.writeInt(this.superEvent);
				
	}
	
	private void readFromParcel(Parcel parcel){
		this.id = parcel.readInt();
		this.idTask = parcel.readInt();
		Long dateAsLong = parcel.readLong();
		this.date = new Date(dateAsLong);
		Long dateNoticeAsLong = parcel.readLong();
		this.notifyDate = new Date(dateNoticeAsLong);
		this.ready = parcel.readByte() == 1;
		this.done = parcel.readByte() == 1;
		this.superEvent = parcel.readInt();
	}
}

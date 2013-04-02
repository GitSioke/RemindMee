package remind.me;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class RemindTask implements Parcelable, Comparable<RemindTask>{
	private static final String KEY_ROWID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_DATE = "date";
	private static final String KEY_TIME = "time";
	private static final String KEY_REPETITION = "repetition";
	private static final String KEY_TAG = "tag";
	private static final String KEY_SUPERTASK = "supertask";
	private static final String KEY_COMPLETED = "completed";
	private Integer id;
	private String name;
	private String tag;
	private Date date;
	private String time;
	private String repetition;
	private Integer superTask;
	private boolean completed;
	private List<RemindTask> subTaskList;
	
	
	
	//TODO Revisar si se puede conseguir con un ContentValues como parametro
	public RemindTask(Integer id,
					  String name, 
					  Date date, 
					  String time, 
					  String repetition, 
					  String tag, 
					  Integer superTask, 
					  Boolean completed){
		super();
		if (id==null)
			this.id = hashCode();
		else{
			this.id = id;
		}
		this.name = name;
		this.date = date;
		this.time = time;
		this.repetition = repetition;
		this.tag =tag;
		this.superTask=superTask;
		this.completed=completed;
		
	}
	
	public RemindTask(ContentValues content){
		super();
		this.id =((Integer) content.get(KEY_ROWID));
		this.name = ((String) content.get(KEY_NAME));
		Long dateAsLong = (Long) content.getAsLong(KEY_DATE);
		this.date= new Date(dateAsLong);
		this.time=((String) content.get(KEY_TIME));
		this.repetition =((String) content.get(KEY_REPETITION));
		this.tag =((String) content.get(KEY_TAG));
		this.superTask =((Integer) content.get(KEY_SUPERTASK));
		this.completed=((Boolean) content.get(KEY_COMPLETED));
	}
	public RemindTask(Parcel parcel){
		super();
		readFromParcel(parcel);
		
	}
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(this.id);
		parcel.writeString(this.name);
		parcel.writeLong(this.date.getTime());
		parcel.writeString(this.time);
		parcel.writeString(this.repetition);
		parcel.writeString(this.tag);
		parcel.writeInt(this.superTask);
		parcel.writeByte((byte) (this.completed ? 1 : 0));
		
		
	}
	
	public static final Parcelable.Creator<RemindTask> CREATOR = new Parcelable.Creator<RemindTask>() {
        public RemindTask createFromParcel(Parcel in) {
            return new RemindTask(in);
        }
 
        public RemindTask[] newArray(int size) {
            return new RemindTask[size];
        }
    };
	private void readFromParcel(Parcel parcel){
		this.id = parcel.readInt();
		this.name = parcel.readString();
		Long dateAsLong = parcel.readLong();
		this.date = new Date(dateAsLong);
		this.time = parcel.readString();
		this.repetition = parcel.readString();
		this.tag = parcel.readString();
		this.superTask = parcel.readInt();
		this.completed = parcel.readByte() ==1;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRepetition() {
		return repetition;
	}

	public void setRepetition(String repetition) {
		this.repetition = repetition;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Integer getSuperTask() {
		return superTask;
	}

	public void setSuperTask(Integer superTask) {
		this.superTask = superTask;
	}

	public List<RemindTask> getSubTaskList() {
		return subTaskList;
	}

	public void setSubTaskList(List<RemindTask> subTaskList) {
		this.subTaskList = subTaskList;
	}

	public class TaskDateComparator implements Comparator<RemindTask>{

		public int compare(RemindTask lhs, RemindTask rhs) {
			return lhs.getDate().compareTo(rhs.getDate());
		}
		
	}

	public int compareTo(RemindTask another) {
		return this.getDate().compareTo(another.getDate());
	}

	
}

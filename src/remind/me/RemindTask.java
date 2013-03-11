package remind.me;

import android.os.Parcel;
import android.os.Parcelable;

public class RemindTask implements Parcelable{
	private Integer id;
	private String name;
	private String tag;
	private String date;
	private String time;
	private String repetition;
	
	
	//TODO Revisar si se puede conseguir con un ContentValues como parametro
	public RemindTask(String name, String date, String time, String repetition, String tag){
		super();
		this.id = hashCode();
		this.name = name;
		this.date = date;
		this.time = time;
		this.repetition = repetition;
		this.tag =tag;
		
	}
	
	public RemindTask(Parcel parcel){
		super();
		readFromParcel(parcel);
		
	}
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(id);
		parcel.writeString(name);
		parcel.writeString(date);
		parcel.writeString(time);
		parcel.writeString(repetition);
		parcel.writeString(tag);
		
		
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
		this.date = parcel.readString();
		this.time = parcel.readString();
		this.repetition = parcel.readString();
		this.tag = parcel.readString();
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
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

	
}

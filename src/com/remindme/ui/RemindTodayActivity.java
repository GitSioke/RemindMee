package com.remindme.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.remindme.ui.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.remindme.db.NotificationDAO;
import com.remindme.db.NotificationSQLite;
import com.remindme.db.TaskDAO;
import com.remindme.db.TaskNotifDAO;
import com.remindme.db.TaskNotifSQLite;
import com.remindme.db.TaskSQLite;
import com.remindme.utils.Event;
import com.remindme.utils.RemindTask;
import com.remindme.utils.Repetition;
import com.remindme.utils.Time;

public class RemindTodayActivity extends RemindActivity {
    
	//private ListView taskListView;
	//private ListView expiredTaskListView;
	//private ArrayList<RemindTask> taskList;
	//private ArrayList<RemindTask> expiredTaskList;
	/** Called when the activity is first created. */
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Today", "Set content view");
        setContentView(R.layout.today);
        Log.d("Today", "Initialize cursor");
        
        ImageView imageNew = (ImageView) findViewById(R.id.Header_NewImage);
        imageNew.setOnClickListener(new View.OnClickListener() {
			
        	public void onClick(View v) {
				startActivity(new Intent(RemindTodayActivity.this, RemindNewActivity.class));
			}
		});
        ImageView imageHome = (ImageView) findViewById(R.id.Header_HomeImage);
        imageHome.setOnClickListener(new View.OnClickListener() {
			
        	public void onClick(View v) {
				startActivity(new Intent(RemindTodayActivity.this, RemindMenuActivity.class));
			}
		});
        TextView txtHeader =(TextView)findViewById(R.id.Today_HeaderTxtView);
        String strToday = getResources().getString(R.string.today_header);
        txtHeader.setText(strToday);
         
        
        TaskDAO db = new TaskSQLite(this);
        NotificationDAO dbNotif= new NotificationSQLite(this);
        TaskNotifDAO dbJoin = new TaskNotifSQLite(this);
        
        /**
         * Prueba de Join. La tarea 1 y la tarea 2 son las que tienen que mostrarse. La tarea 3 no deberia de mostrarse. Las tareas del día aunque 
         * esten completadas se mostraran como completadas.
         */
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String time = "no hora";
        String repetition = Repetition.DAILY.toString();
        
        /*
        RemindTask task1 = new RemindTask(null, "HOY", date, date, time, repetition, "", "Naranja", null, false);
        Event notif1 = new Event(null, task1.getId(), date, date, true, false, null);
        Date dateOfRecord = date;
        
        cal.roll(Calendar.DAY_OF_YEAR, -1);
        date = cal.getTime();
        RemindTask task2 = new RemindTask(null, "AYER", date, date, time, repetition, "", "Sandia", null, false);
        RemindTask task3 = new RemindTask(null, "AYER Completa", date, date, time, repetition, "", "Melon", null, true);
        Event notif2 = new Event(null, task2.getId(), date, date, true, false, null);
        Event notif3 = new Event(null, task3.getId(), date, date, true, true, null);
        
        db.insertTask(task1);
        db.insertTask(task2);
        db.insertTask(task3);
        dbNotif.insert(notif1);
        dbNotif.insert(notif2);
        dbNotif.insert(notif3);
        */
        //TODO REalizar bien las dos llamadas a base de datos para recoger los correctos valores de las tareas
        Date dateOfRecord = cal.getTime();
        dateOfRecord = Time.retainMainDate(dateOfRecord);
		ArrayList<RemindTask> taskList =  dbJoin.getAllTasksWith(dateOfRecord);
		ArrayList<RemindTask> expiredTaskList = dbJoin.getAllTasksBefore(dateOfRecord);
        if (taskList.isEmpty() && expiredTaskList.isEmpty()){
        	Toast.makeText(this, R.string.today_toastEmpty, Toast.LENGTH_LONG).show();
        }else{
        	displayListView(taskList, false);
        	displayListView(expiredTaskList, true);
        }
        
        if(expiredTaskList.isEmpty()){
        	TextView text = (TextView)findViewById(R.id.Today_ExpiredText);
        	text.setText(R.string.today_noexpiredtasks);
        	text.setTextSize(R.dimen.today_noDelayedTask);
        }
        
    	/**CheckBox box = (CheckBox) findViewById(R.id.ListItemCheck_Checkbox);
    	box.
    	box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					
					NotificationDAO notifDB = new NotificationSQLite(getApplicationContext());
					if (notifDB.hasSubNotification(notif.getId())){
						Toast.makeText(this, R.string.task_toast_hasSubtask, Toast.LENGTH_LONG).show();
					}else{
						notif.setDone(notif.isDone()? false:true);
						notifDB.updateNotification(notif);
				}
			}
		});*/

        
    }
   /**
     * Show 
     * @param c
     */
	private void displayTaskWithToast(Cursor cursor) {
		String android_id = Secure.getString(getBaseContext().getContentResolver(),
                Secure.ANDROID_ID); 
		Toast.makeText(this, "id:"+ cursor.getInt(0) + "\n" +
	"Tarea: " + cursor.getString(1), Toast.LENGTH_LONG).show();
		
	}
	
	/**
	 * Muestra en el TextView de all.xml las tareas almacenadas
	 */
	private void displayListView(final ArrayList<RemindTask> taskList, Boolean expiredList){
		ListView listView;
		Boolean isEvent = true;
		if (expiredList==true){
			listView = (ListView) findViewById(R.id.Today_ListViewExpiredTask);
			listView.setAdapter(new RemindTaskAdapter(this, R.layout.list_item_task, taskList, isEvent));
		}else{
			listView = (ListView)findViewById(R.id.Today_ListViewTask);
			listView.setAdapter(new RemindTaskAdapter(this, R.layout.list_item_task, taskList, isEvent));
		}
				
		OnItemClickListener listener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO 
				Intent intent = new Intent(RemindTodayActivity.this, RemindTaskActivity.class);
				RemindTask task = taskList.get(position);
				intent.putExtra("task", task);
				startActivity(intent);
				
			}
		};
		listView.setOnItemClickListener(listener);		
	}
	
}
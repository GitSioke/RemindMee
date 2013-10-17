package com.remindme.ui;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.remindme.ui.R;

import com.remindme.db.NotificationDAO;
import com.remindme.db.NotificationSQLite;
import com.remindme.db.TaskDAO;
import com.remindme.db.TaskNotifDAO;
import com.remindme.db.TaskNotifSQLite;
import com.remindme.db.TaskSQLite;
import com.remindme.utils.Event;
import com.remindme.utils.NoticeNew;
import com.remindme.utils.RemindTask;
import com.remindme.utils.Repetition;
import com.remindme.utils.Time;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RemindDayActivity extends RemindActivity {
    /** Called when the activity is first created. */
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day);
        setHeaderButton();
        
        
        TaskDAO db = new TaskSQLite(this);
        NotificationDAO dbNotif= new NotificationSQLite(this);
        TaskNotifDAO dbJoin = new TaskNotifSQLite(this);
        
        Calendar calme = Calendar.getInstance();
         Date date = calme.getTime();
        String time = "no hora";
        String repetition = Repetition.WEEKLY.toString();
        RemindTask task1 = new RemindTask(null, "TIENE", date, date, time, repetition, "", "Naranja", null, false);
        calme.roll(Calendar.DAY_OF_YEAR, -1);
        date = calme.getTime();
        RemindTask task2 = new RemindTask(null, "NO TIENE", date, date, time, repetition, "", "Sandia", null, false);
        calme.roll(Calendar.DAY_OF_YEAR, -1);
        date = calme.getTime();
        RemindTask task3 = new RemindTask(null, "NO ES SEMANAL", date, date, time, Repetition.MONTHLY.toString(), "", "Melon", null, true);
        db.insertTask(task1);
        db.insertTask(task2);
        db.insertTask(task3);
        
        calme.roll(Calendar.DAY_OF_YEAR, 3);
        date = calme.getTime();
        Date dateOfRecord = date;
        Event notif1 = new Event(null, task1.getId(), date, date, true, false, null);
        calme.roll(Calendar.DAY_OF_YEAR, -2);
        date = calme.getTime();
        Event notif2 = new Event(null, task2.getId(), date, date, true, false, null);
        Event notif3 = new Event(null, task3.getId(), date, date, true, true, null);

        dbNotif.insert(notif1);
        dbNotif.insert(notif2);
        dbNotif.insert(notif3);
        
       	Long dateLong = getIntent().getLongExtra("date", 0);
       	Date day = new Date(dateLong);
       	String dateString = Time.parseDateDay(day);
       			
		TextView txtHeader =(TextView) findViewById(R.id.Day_HeaderTxtView);
		txtHeader.setText(dateString);
		
		Calendar cal = Calendar.getInstance();
		cal.roll(Calendar.DAY_OF_YEAR, 1);
		Date endOfDay = cal.getTime();
		
		
		//TaskNotifDAO dbJoin = new TaskNotifSQLite(this);
		ArrayList<RemindTask> taskList =  dbJoin.weeklyEventsBetweenDates(day, endOfDay, Time.dayOfWeek(day));
		//taskList.addAll(dbJoin.monthlyEventsBetweenDates(day, endOfDay, Time.dayOfMonth(day)));
		//taskList.addAll(dbJoin.yearlyEventsBetweenDates(day, endOfDay, Time.dayOfYear(day)));
		if (taskList.isEmpty()){
        	Toast.makeText(this, R.string.day_toast_noTask, Toast.LENGTH_LONG).show();
        }else{
        	
        	displayTaskWithTextView(taskList);
         }
        
        
    }
    	
	/**
	 * Muestra en el TextView de all.xml las tareas almacenadas
	 */
	private void displayTaskWithTextView(final ArrayList<RemindTask> taskList){
		ListView taskListView = (ListView)findViewById(R.id.Day_ListViewTask);
		taskListView.setAdapter(new RemindTaskAdapter(this, R.layout.list_item_task, taskList, true));
		
		OnItemClickListener listener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO 
				Intent intent = new Intent(RemindDayActivity.this, RemindTaskActivity.class);
				RemindTask task = taskList.get(position);
				Log.d("Pending", "Tarea seleccionada:"+task.getName());
				intent.putExtra("task", task);
				startActivity(intent);
				
			}
		};
		taskListView.setOnItemClickListener(listener);
		
		/**
		 * Crea los remind y los muestra 
		 */	
		
		
	}
	/**
	private void displayCheckBoxes(final ArrayList<RemindTask> taskList){
		checkListView = (ListView)findViewById(R.id.All_ListViewCheckBox);
		checkListView.setAdapter(new RemindTaskAdapter(this, R.layout.list_item_check, taskList));
		
	}*/
	
	private void setHeaderButton(){
		ImageView imageNew = (ImageView) findViewById(R.id.Header_NewImage);
        ImageView imageHome = (ImageView) findViewById(R.id.Header_HomeImage);
		imageNew.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(RemindDayActivity.this, RemindNewActivity.class));
				
			}
		});
		
		imageHome.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(RemindDayActivity.this, RemindMenuActivity.class));
				
			}
		});
	}

}

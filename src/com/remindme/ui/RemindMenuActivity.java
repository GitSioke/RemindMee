package com.remindme.ui;




import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.remindme.ui.R;

import com.remindme.db.NotificationDAO;
import com.remindme.db.NotificationSQLite;
import com.remindme.db.TaskDAO;
import com.remindme.db.TaskNotifDAO;
import com.remindme.db.TaskNotifSQLite;
import com.remindme.db.TaskSQLite;
import com.remindme.fragments.DatePickerFragment;
import com.remindme.fragments.DatePickerFragment.OnDateSelectedListener;
import com.remindme.services.NotificationCompleteService;
import com.remindme.services.NotificationManagementService;
import com.remindme.utils.Event;
import com.remindme.utils.RemindTask;
import com.remindme.utils.Repetition;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RemindMenuActivity extends RemindActivity implements OnDateSelectedListener {
    /** Called when the activity is first created. */
	public static final String NOTIFICATION_DATA = "NOTIFICATION_DATA";
	public TextView text;
	private DialogFragment dateFragment;
	private Long dateLong;
	private TextView notificationData;
	private Button createEvent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("MENU", "Entrando en onCreate");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        Context ctx = getApplicationContext();
        //TODO ¿Porque lanza notification complete?
        //ctx.startService(new Intent(RemindMenuActivity.this, NotificationCompleteService.class));
        
        //Test eventos
        TaskDAO db = new TaskSQLite(this);
        NotificationDAO dbNotif= new NotificationSQLite(this);
        TaskNotifDAO dbJoin = new TaskNotifSQLite(this);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String time = "no hora";
        String repetition = Repetition.DAILY.toString();
        RemindTask task1 = new RemindTask(null, "EVENTO 1", date, date, time, repetition, "", "Naranja", null, false);
        Event notif1 = new Event(null, task1.getId(), date, new Date(date.getTime()-3600000), true, false, null);
        Date dateOfRecord = date;
        
        cal.roll(Calendar.DAY_OF_YEAR, -1);
        date = cal.getTime();
        RemindTask task2 = new RemindTask(null, "EVENTO 2", date, date, time, repetition, "", "Sandia", null, false);
        RemindTask task3 = new RemindTask(null, "EVENTO 3", date, date, time, repetition, "", "Melon", null, true);
        Event notif2 = new Event(null, task2.getId(), date, new Date(date.getTime()-7200000), true, false, null);
        Event notif3 = new Event(null, task3.getId(), date, new Date(date.getTime()-7200000), true, true, null);
        
        db.insertTask(task1);
        db.insertTask(task2);
        db.insertTask(task3);
        dbNotif.insert(notif1);
        dbNotif.insert(notif2);
        dbNotif.insert(notif3);
        if (isMyServiceRunning())
        {
        	Log.d("Menu.Service", "SI");
        }else{
        	Log.d("Menu.Service", "NO");
        }
        
        //TODO Averiguear que coño hace esto
        /* No se que coño hace esto
        Repetition rep;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();		
		cal.set(2013, 1, 28);
		Date date  = cal.getTime();
		Log.d("NEW", "Day: "+format.format(date));
		rep = Repetition.DAILY;
		boolean fin = false;
		while(Repetition.values().length >= rep.ordinal() && !fin){
			
			Date nextDay = Repetition.getNextDate(date, rep);
			Log.d("NEW", "Next day: "+format.format(nextDay));
			if(rep.ordinal()==Repetition.values().length-1)
				fin = true;
			if(!fin)
				rep = Repetition.values()[rep.ordinal()+1];
		}*/
		 //Elimina las tareas
        if (removedPendingTasks()){
        	TaskDAO taskDB = new TaskSQLite(this);        	
        	RemindTask task = getIntent().getParcelableExtra("Task");
        	taskDB.deleteTask(task.getId());
        	Toast toast = Toast.makeText(this, R.string.menu_toast_delete, Toast.LENGTH_SHORT);
        	
        	if (getIntent().getBooleanExtra("DeleteAll", false)){
        		taskDB.deleteSubtask(task.getId());
        		toast.setText(R.string.menu_toast_deleteAll);
        	}
        	toast.show();
        }
        //
        
        RelativeLayout imageAll = (RelativeLayout) findViewById(R.id.Menu_RelativeTableAll);
        RelativeLayout imageTags = (RelativeLayout) findViewById(R.id.Menu_RelativeTableTags);
        RelativeLayout imageToday = (RelativeLayout) findViewById(R.id.Menu_RelativeTableToday);
        ImageView imageNew = (ImageView) findViewById(R.id.Header_NewImage);
        ImageView imageHome = (ImageView) findViewById(R.id.Header_HomeImage);
        ImageView imageCalendar = (ImageView) findViewById(R.id.Menu_ImageView_Calendar);
        
        imageAll.setOnClickListener(new View.OnClickListener() {
			
        	public void onClick(View v) {
        		Log.d("MENU", "Entrando en onStartActivity");
        		startActivity(new Intent(RemindMenuActivity.this, RemindAllTaskActivity.class));					
				
				
			}
		});
        
        imageTags.setOnClickListener(new View.OnClickListener() {
			
        	public void onClick(View v) {
				startActivity(new Intent(RemindMenuActivity.this, RemindTagsActivity.class));
			}
		});
        
        imageNew.setOnClickListener(new View.OnClickListener() {
			
        	public void onClick(View v) {
				startActivity(new Intent(RemindMenuActivity.this, RemindNewActivity.class));
			}
		});

        imageToday.setOnClickListener(new View.OnClickListener() {
        	//TODO Cambio provisional de Today por CompletedTask
        	public void onClick(View v) {
        		startActivity(new Intent(RemindMenuActivity.this, RemindTodayActivity.class));
        	}
        });
        
        imageCalendar.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				dateFragment = new DatePickerFragment();
				Bundle bundle = new Bundle();
				bundle.putBoolean("pickDay", true);
				dateFragment.setArguments(bundle);
				dateFragment.show(getFragmentManager(), "dateNoticePicker");		
				
			}
		});
        
        imageHome.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(RemindMenuActivity.this, RemindMenuActivity.class));
				
			}
		});
    }
    


	/**
     * Elimina las tareas que tenga que eliminar si ha recibido orden desde RemindTaskActivity
     */
	private Boolean removedPendingTasks() {
		// TODO 
		return getIntent().getBooleanExtra("DeleteAll", false) || getIntent().getBooleanExtra("DeleteTask", false);
		
	}
	
	public void setDateLong(Long date){
		this.dateLong = date;
	}



	public void onDateSelected(Date date) {
		this.dateLong = date.getTime();
		Intent intent = new Intent(this, RemindDayActivity.class);
		intent.putExtra("date",this.dateLong);
		startActivity(intent);
	}
	
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (NotificationManagementService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	

}
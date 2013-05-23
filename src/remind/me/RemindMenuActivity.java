package remind.me;




import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.remind.fragments.DatePickerFragment;
import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.utils.RemindTask;
import com.utils.Repetition;

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
import android.widget.TextView;
import android.widget.Toast;

public class RemindMenuActivity extends RemindActivity {
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
		}
		
		
		cal.set(2013,11,31);
		date  = cal.getTime();
		rep = Repetition.DAILY;
		Log.d("NEW", "Day: "+format.format(date));
		rep = Repetition.DAILY;
		fin = false;
		while(Repetition.values().length >= rep.ordinal() && !fin){
			
			Date nextDay = Repetition.getNextDate(date, rep);
			Log.d("NEW", "Next day: "+format.format(nextDay));
			if(rep.ordinal()==Repetition.values().length-1)
				fin = true;
			if(!fin)
				rep = Repetition.values()[rep.ordinal()+1];
		}
		cal.set(1970,0,31);
		date  = cal.getTime();
		Log.d("NEW", "Day: "+format.format(date));
		rep = Repetition.DAILY;
		fin = false;
		while(Repetition.values().length >= rep.ordinal() && !fin){
			
			Date nextDay = Repetition.getNextDate(date, rep);
			Log.d("NEW", "Next day: "+format.format(nextDay));
			if(rep.ordinal()==Repetition.values().length-1)
				fin = true;
			if(!fin)
				rep = Repetition.values()[rep.ordinal()+1];
		}
		cal.set(1970,12,1);
		date  = cal.getTime();
		Log.d("NEW", "Day: "+format.format(date));
		rep = Repetition.DAILY;
		fin = false;
		while(Repetition.values().length >= rep.ordinal() && !fin){
			
			Date nextDay = Repetition.getNextDate(date, rep);
			Log.d("NEW", "Next day: "+format.format(nextDay));
			if(rep.ordinal()==Repetition.values().length-1)
				fin = true;
			if(!fin)
				rep = Repetition.values()[rep.ordinal()+1];
		}
		cal.set(1970,2,28);
		date  = cal.getTime();
		Log.d("NEW", "Day: "+format.format(date));
		rep = Repetition.DAILY;
		fin = false;
		while(Repetition.values().length >= rep.ordinal() && !fin){
			
			Date nextDay = Repetition.getNextDate(date, rep);
			Log.d("NEW", "Next day: "+format.format(nextDay));
			if(rep.ordinal()==Repetition.values().length-1)
				fin = true;
			if(!fin)
				rep = Repetition.values()[rep.ordinal()+1];
		}
		
        //Elimina las tareas
        if (removedPendingTasks()){
        	RemindTaskDAO taskDB = new RemindTaskSQLite(this);        	
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
        
        ImageView imageAll = (ImageView) findViewById(R.id.Menu_ImageView_All);
        ImageView imageTags = (ImageView) findViewById(R.id.Menu_ImageView_Tags);
        ImageView imageToday = (ImageView) findViewById(R.id.Menu_ImageView_Today);
        ImageView imageNew = (ImageView) findViewById(R.id.Menu_ImageView_New);
        ImageView imageClock = (ImageView) findViewById(R.id.Menu_ImageView_Clock);
        
        imageAll.setOnClickListener(new View.OnClickListener() {
			
        	public void onClick(View v) {
        		Log.d("MENU", "Entrando en onStartActivity");
        		startActivity(new Intent(RemindMenuActivity.this, RemindPendingTaskActivity.class));					
				
				
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
        		startActivity(new Intent(RemindMenuActivity.this, RemindCompletedTaskActivity.class));
        	}
        });
        
        imageClock.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				dateFragment = new DatePickerFragment();
				Bundle bundle = new Bundle();
				bundle.putBoolean("pickDay", true);
				dateFragment.setArguments(bundle);
				dateFragment.show(getFragmentManager(), "dateNoticePicker");		
				
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
	
	

}
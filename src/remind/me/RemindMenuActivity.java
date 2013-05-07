package remind.me;




import java.util.Calendar;
import java.util.Date;

import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;

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
        setNotification();
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
    
    
    private void setNotification() {
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.ic_launcher)
    	        .setContentTitle("My notification")
    	        .setContentText("Hello World!");
    	// Creates an explicit intent for an Activity in your app
    	Intent resultIntent = new Intent(this, RemindMenuActivity.class);

    	// The stack builder object will contain an artificial back stack for the
    	// started Activity.
    	// This ensures that navigating backward from the Activity leads out of
    	// your application to the Home screen.
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    	// Adds the back stack for the Intent (but not the Intent itself)
    	stackBuilder.addParentStack(RemindMenuActivity.class);
    	// Adds the Intent that starts the Activity to the top of the stack
    	stackBuilder.addNextIntent(resultIntent);
    	PendingIntent resultPendingIntent =
    	        stackBuilder.getPendingIntent(
    	            0,
    	            PendingIntent.FLAG_UPDATE_CURRENT
    	        );
    	mBuilder.setContentIntent(resultPendingIntent);
    	NotificationManager mNotificationManager =
    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	// mId allows you to update the notification later on.
    	mNotificationManager.notify(28309, mBuilder.build());
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
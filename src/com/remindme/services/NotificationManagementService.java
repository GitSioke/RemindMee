package com.remindme.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.remindme.sqlite.RemindNotificationDAO;
import com.remindme.sqlite.RemindNotificationSQLite;
import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.remindme.ui.DialogDelayActivity;
import com.remindme.ui.R;
import com.remindme.ui.RemindMenuActivity;
import com.remindme.ui.RemindSplashActivity;
import com.remindme.ui.RemindTaskActivity;
import com.remindme.utils.RemindNotification;
import com.remindme.utils.RemindTask;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.AlarmClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class NotificationManagementService extends IntentService{
	
	
	private Thread thread;
	private static final long sleepTime;
	

	static {
	    Calendar cal;
		cal = GregorianCalendar.getInstance();
		cal.clear();
	    cal.set(Calendar.SECOND, 5);
	    
	    sleepTime = cal.getTimeInMillis();
	}

	public NotificationManagementService() {
		super("NotificationManagementService");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	protected void onHandleIntent(final Intent intent)
	{
		Log.d("ServiceManagement", "Servicio iniciado");
		thread = new Thread() 
		{
			private Context ctx = getApplicationContext();
			RemindNotificationDAO dbNoti = new RemindNotificationSQLite(ctx);
			RemindTaskDAO dbTask = new RemindTaskSQLite(ctx);
			
			public void start()
			{
				Log.d("ServiceManagement", "Starting");
				this.run();
			}
			
			public void run() 
			{
				Log.d("ServiceManagement", "Running");
	        		//TODO Actualizar lo que se deba
					//Se eliminan todas las notificaciones que esten pasadas de fecha(haya una notificacion pediente de la misma
					//idTask) preparada. 
					
					//Se coloca a ready todas las tareas que esten listas para ser notificadas en un periodo de 24h 
					//todas estas tareas entran en el notification manager
	        		Log.d("ServiceManagement", "Loop");
	        		ArrayList<RemindNotification> pendingNotifications = dbNoti.getUnreadyNotifications(Calendar.getInstance().getTimeInMillis());
					for(RemindNotification notif : pendingNotifications)
					{
						notif.setReady(true);
						dbNoti.updateNotification(notif);
						RemindTask task = dbTask.getTaskWithID(notif.getIdTask());
						if(task!=null)
							createNotification(task, notif);	                		
					}
					
					Intent intentManage = new Intent(NotificationManagementService.this, NotificationManagementService.class);
					intent.putExtra("firstLoop", false);
					PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intentManage, 0);
					
				    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				    alarmManager.cancel(pendingIntent);
				    alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis()+ sleepTime, pendingIntent);
					            
	        }
			
	        /**
	         * Crea notificacion en notification manager a partir de la notification pasada por parametro
	         * @param notif
	         */
	        private void createNotification(RemindTask task,RemindNotification notif){
	        	Log.d("ServiceManagement", "Creando notification "+task.getName());
	        	
	        	Locale loc = new Locale("es ES");
	        	SimpleDateFormat format= new SimpleDateFormat("E dd//MM/yyyy HH:mm", loc);
	        	String strDate = format.format(notif.getDate());
	        	
	        	Intent completeIntent = new Intent(NotificationManagementService.this, NotificationIntentService.class);
	        	completeIntent.putExtra("task", task);
	        	
	        	PendingIntent pcIntent = PendingIntent.getService(ctx, 0,completeIntent, 0);
	        	
	        	Intent delayIntent = new Intent(NotificationManagementService.this, DialogDelayActivity.class);
	        	PendingIntent pdIntent = PendingIntent.getActivity(ctx, 0, delayIntent, 0);
	        	
	        	// Creates an explicit intent for an Activity in your app
    	    	Intent resultIntent = new Intent(ctx, RemindTaskActivity.class);
    	    	resultIntent.putExtra("task", task);
    	    	
    	    	PendingIntent rpIntent = PendingIntent.getActivity(ctx, 0, resultIntent, 0);

    	    	NotificationCompat.Builder mBuilder =
    	    	        new NotificationCompat.Builder(ctx)
    	    	        .setSmallIcon(R.drawable.ic_notif)
    	    	        .setWhen(notif.getDate().getTime())
    	    	        .setContentTitle(task.getName().toString())
    	    	        .setContentText(strDate)
    	    	        .addAction(R.drawable.notif_check_opt, getString(R.string.notif_complete), pcIntent)
    	    	        .addAction(R.drawable.notif_clock_opt, getString(R.string.notif_delay), pdIntent);
    					
    	
    	    	// The stack builder object will contain an artificial back stack for the
    	    	// started Activity.
    	    	// This ensures that navigating backward from the Activity leads out of
    	    	// your application to the Home screen.
    	    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
    	    	// Adds the back stack for the Intent (but not the Intent itself)
    	    	stackBuilder.addParentStack(RemindMenuActivity.class);
    	    	// Adds the Intent that starts the Activity to the top of the stack
    	    	//stackBuilder.addNextIntent(resultIntent);
    	    	//PendingIntent resultPendingIntent =
    	    	//        stackBuilder.getPendingIntent(
    	    //	            0,
    	    //	            PendingIntent.FLAG_UPDATE_CURRENT
    	    	//        );
    	    	mBuilder.setAutoCancel(true);
    	    	mBuilder.setContentIntent(rpIntent);
    	    	NotificationManager mNotificationManager =
    	    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	    	// mId allows you to update the notification later on.
    	    	mNotificationManager.notify(notif.getId(), mBuilder.build());
    	    	
	        }
	       
	    };
	    
	    thread.start();
	    //Log.d("ServiceManagement", "Stopping");
		//this.stopSelf();
		
	}
}

package com.remindme.ui;




import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.remindme.ui.R;

import com.remindme.sqlite.RemindNotificationDAO;
import com.remindme.sqlite.RemindNotificationSQLite;
import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.remindme.utils.RemindNotification;
import com.remindme.utils.RemindTask;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class RemindSplashActivity extends RemindActivity {
	private Thread thread;
	private static final Date sleepTime;
	static {
	    Calendar cal;
		cal = GregorianCalendar.getInstance();
	    cal.set(Calendar.MINUTE, 2);
	    sleepTime = cal.getTime();
	}
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		launchLoopThread();
		startFadeIn();
	}
	
	private void launchLoopThread() {
		thread = new Thread() {
			
			private Context ctx = getApplicationContext();
			RemindNotificationDAO dbNoti = new RemindNotificationSQLite(ctx);
			RemindTaskDAO dbTask = new RemindTaskSQLite(ctx);
			public void run() {
				Log.d("Thread", "Start");
	        	Boolean firstLoop = true;
	           	while (true) {
	            	try {
	                    // do something here
	            		if (firstLoop){
	            			ArrayList<RemindNotification> lapsedNotifs = dbNoti.lapsedNotifications(Calendar.getInstance().getTimeInMillis());
		            		for(RemindNotification notif :lapsedNotifs){
		            			if(notif.getDate().before(Calendar.getInstance().getTime()) 
		            					&& dbNoti.amountReadyNotifications(notif.getIdTask())>1){
		            				
		            				notif.setReady(false);
		            				dbNoti.updateNotification(notif);
		            			}
		            		}
	            			//TODO Iniciar como si estuviese apagad
	                		//RemindNotification not = new RemindNotification(12345, 6789, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), true, false);
	                		//dbNoti.insertNotification(not);
	                		//Long longas = Calendar.getInstance().getTime().getTime() + 10000;
	                		//not = new RemindNotification(12346, 6789,  new Date(longas), new Date(longas), false, false);
	                		//dbNoti.insertNotification(not);
	                		ArrayList<RemindNotification> notificationList= dbNoti.getAllNotifications();
	                		NotificationManager notificationManager =(NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
	                		
	                		for(RemindNotification notification: notificationList){
	                			RemindTask task= dbTask.getTaskWithID(notification.getIdTask());
	                			if(task!=null)
	                				createNotification(task, notification);
	                		}
	                		firstLoop = false;
	                	}
	            		
	                	//TODO Actualizar lo que se deba
	            		//Se eliminan todas las notificaciones que esten pasadas de fecha(haya una notificacion pediente de la misma
	            		//idTask) preparada. 
	            		
	            		//Se coloca a ready todas las tareas que esten listas para ser notificadas en un periodo de 24h 
	            		//todas estas tareas entran en el notification manager
	                	ArrayList<RemindNotification> pendingNotifications = dbNoti.getUnreadyNotifications(Calendar.getInstance().getTimeInMillis());
	                	for(RemindNotification notif : pendingNotifications){
	                		notif.setDone(true);
	                		dbNoti.updateNotification(notif);
	                		RemindTask task = dbTask.getTaskWithID(notif.getIdTask());
	                		if(task!=null)
	                			createNotification(task, notif);	                		
	                	}
	            		Log.d("Thread", "local Thread sleeping");
	                    Thread.sleep(sleepTime.getTime());
	                    
	                } catch (InterruptedException e) {
	                    Log.e("Splash", "local Thread error", e);
	                }
	            }
	        }
			
	        /**
	         * Crea notificacion en notification manager a partir de la notification pasada por parametro
	         * @param notif
	         */
	        private void createNotification(RemindTask task,RemindNotification notif){
	        	Locale loc = new Locale("es ES");
	        	SimpleDateFormat format= new SimpleDateFormat("E dd//MM/yyyy HH:mm", loc);
	        	String strDate = format.format(notif.getDate());
	        	NotificationCompat.Builder mBuilder =
    	    	        new NotificationCompat.Builder(ctx)
    	    	        .setSmallIcon(R.drawable.ic_notif)
    	    	        .setWhen(notif.getDate().getTime())
    	    	        .setContentTitle(task.getName().toString())
    	    	        .setContentText(strDate);
    					
    	    	// Creates an explicit intent for an Activity in your app
    	    	Intent resultIntent = new Intent(ctx, RemindTaskActivity.class);
    	    	resultIntent.putExtra("task", task);

    	    	// The stack builder object will contain an artificial back stack for the
    	    	// started Activity.
    	    	// This ensures that navigating backward from the Activity leads out of
    	    	// your application to the Home screen.
    	    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
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
    	    	mNotificationManager.notify(notif.getId(), mBuilder.build());
	        }
	       
	    };
	 
	    thread.start();
		
	}

	private void startFadeIn(){
		TextView title = (TextView) findViewById(R.id.Splash_TextView01);
		Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.splash_fade);
		title.startAnimation(fadeIn);
		
		fadeIn.setAnimationListener(new AnimationListener(){
			/**
			 * TODO Retrasar el inicio de MenuActivity
			 */
			public void onAnimationEnd(Animation animation) {
				startActivity(new Intent(RemindSplashActivity.this, RemindMenuActivity.class));
				RemindSplashActivity.this.finish();
			}

			public void onAnimationRepeat(Animation animation) {
								
			}

			public void onAnimationStart(Animation animation) {
				
			}
			
		});
	}
	
	protected void onPause(){
		super.onPause();
		TextView logo1 = (TextView) findViewById(R.id.Splash_TextView01);
		logo1.clearAnimation();
				
	}
	protected void onResume(){
		super.onResume();
		startFadeIn();
	}
	
	
}

package com.remindme.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.remindme.db.NotificationDAO;
import com.remindme.db.NotificationSQLite;
import com.remindme.db.TaskDAO;
import com.remindme.db.TaskSQLite;
import com.remindme.ui.DialogDelayActivity;
import com.remindme.ui.R;
import com.remindme.ui.RemindMenuActivity;
import com.remindme.ui.RemindSplashActivity;
import com.remindme.ui.RemindTaskActivity;
import com.remindme.utils.Event;
import com.remindme.utils.RemindTask;
import com.remindme.utils.Time;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import android.util.Log;

public class NotificationRestartService extends Service {
	
	private static final long sleepTime;
	private static final long twohour;

	static {
	    Calendar cal;
		cal = GregorianCalendar.getInstance();
		cal.clear();
	    cal.set(Calendar.SECOND, 5);
	    
	    sleepTime = cal.getTimeInMillis();
	}
	static{
		Calendar cal;
		cal = GregorianCalendar.getInstance();
		cal.clear();
		cal.set(Calendar.HOUR, 2);
		twohour = cal.getTimeInMillis();
	}
	private Context ctx;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		this.ctx =   getApplicationContext();
		NotificationDAO dbNoti = new NotificationSQLite(this.ctx);
		TaskDAO dbTask = new TaskSQLite(this.ctx);
		
		Log.d("ServiceRestart", "FirstLoop");
        Log.d("ServiceRestart", Long.toString(Calendar.getInstance().getTimeInMillis()));	
        ArrayList<Event> lapsedNotifs = 
		dbNoti.lapsedNotifications(Calendar.getInstance().getTimeInMillis());
        //Se comprueba que haya notificaciones ya pasadas, si han sido superadas por algunas de ellas se dejan como ready = false
        // para que dejen de notificarse
		for(Event notif :lapsedNotifs)
		{
			if(notif.getDate().before(Calendar.getInstance().getTime()) 
					&& dbNoti.amountReadyNotifications(notif.getIdTask())>1)
			{
							
				notif.setReady(false);
				dbNoti.updateNotification(notif);
			}
		}
		//TODO Iniciar como si estuviese apagad
		//Test prueba que solo los eventos con ready =  true salen de la base de datos
		/*Event not = new Event(12345, 6789, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), true, false, startId);
		dbNoti.insert(not);
		Long longas = Calendar.getInstance().getTime().getTime() + 10000;
		not = new Event(12346, 6789,  new Date(longas), new Date(longas), false, false, startId);
		dbNoti.insert(not);
		RemindTask task1 = new RemindTask(6789, "Prueba Ready", Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), "", "SINGLE", "", "TAG", 0, true);
		dbTask.insertTask(task1);*/
		//Se recuperan todas las notificaciones anteriores. Ready = true
		ArrayList<Event> notificationList= dbNoti.getAllReadyNotifications();
		NotificationManager notificationManager =
				(NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		
		for(Event notification: notificationList)
		{
			RemindTask task= dbTask.getTaskWithID(notification.getIdTask());
			if(task!=null)
				createNotification(task, notification);
		}
		
		//TODO descomentar cuando funcione el completeservice
		launchNotificationManagment();
		this.stopSelf();
		return startId;
		
	}
		private void createNotification(RemindTask task,Event notif){
        	Log.d("ServiceRestart", "Creando notification "+task.getName());
        	String strDate = Time.formatLongDate(notif.getDate());
        	
        	Intent completeIntent = new Intent(NotificationRestartService.this, NotificationCompleteService.class);
        	completeIntent.putExtra("notif", notif);
        	
        	PendingIntent pcIntent = PendingIntent.getService(ctx, 0,completeIntent, 0);
        	
        	Intent delayIntent = new Intent(NotificationRestartService.this, DialogDelayActivity.class);
        	delayIntent.putExtra("notifID", notif.getId());
        	PendingIntent pdIntent = PendingIntent.getActivity(ctx, 0, delayIntent, 0);
        	
        	// Creates an explicit intent for an Activity in your app
	    	Intent resultIntent = new Intent(ctx, RemindTaskActivity.class);
	    	task.setId(notif.getId());
	    	resultIntent.putExtra("father", false);
	    	resultIntent.putExtra("task", task);
	    	
	    	PendingIntent rpIntent = PendingIntent.getActivity(ctx, 0, resultIntent, 0);

	    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
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
	    	mBuilder.setContentIntent(rpIntent);
	    	mBuilder.setAutoCancel(true);
	    	mBuilder.setDeleteIntent(rpIntent);
	    	mBuilder.setDeleteIntent(pcIntent);
	    	mBuilder.setDeleteIntent(pdIntent);
	    	
	    	NotificationManager mNotificationManager =
	    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	    	// mId allows you to update the notification later on.
	    	mNotificationManager.notify(notif.getId(), mBuilder.build());    	
	    	
        }
		
		private void launchNotificationManagment(){
			
			Intent intentManage = new Intent(this, NotificationManagementService.class);
			PendingIntent pendingIntent = PendingIntent.getService(this, 0, intentManage, 0);
		    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis()+ sleepTime + 7200000, sleepTime, pendingIntent);
		    //alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis()+ sleepTime, pendingIntent);
		    Log.d("ServiceRestart", "Two Hours:" + new Date(twohour));
		    Log.d("ServiceRestart", "Sleep for:" + new Date(sleepTime) +" /n til: "+ new Date(Calendar.getInstance().getTimeInMillis()+sleepTime+7200000) );
		}
		
}

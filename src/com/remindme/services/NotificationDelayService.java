package com.remindme.services;

import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.remindme.db.NotificationDAO;
import com.remindme.db.NotificationSQLite;
import com.remindme.utils.Notice;
import com.remindme.utils.NoticeDelay;
import com.remindme.utils.Event;

/** This service is launched when an user select "Delay" option on the notification pop-up.
 * 
 * @author pick
 *
 */

public class NotificationDelayService extends Service {
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		
		
		Log.d("ServiceDelay", "Servicio iniciado");
		Context ctx = getApplicationContext();
		Integer posDelay = intent.getIntExtra("delay", -1);
		
		Integer notifID = intent.getIntExtra("notifID", -1);
		if (notifID !=-1){
			NotificationDAO notifDB = new NotificationSQLite(this);
			Event event = notifDB.getNotificationWithID(notifID);
			Log.d("Service", notifID.toString());
			NotificationDAO dbNotif = new NotificationSQLite(ctx);
			
			Date notifDate = event.getNotifyDate();
			NoticeDelay not = NoticeDelay.values()[posDelay];
			notifDate = NoticeDelay.delayDate(notifDate, not);
			event.setnotifyDate(notifDate);
			dbNotif.updateNotification(event);
			
		}
		String str = intent.getStringExtra("extra");
		if (str!=null)
			Log.d("Service", str);		
		this.stopSelf();
		return startId;
		
	}
	
	@Override
	public void onDestroy()
	{
		Log.d("ServiceDelay", "Servicio parado");
		super.onDestroy();
	}
	@Override
	public IBinder onBind(Intent intent) {
		Log.d("ServiceDelay", "Servicio binder funcionado");
		return null;
	}
	
	

}

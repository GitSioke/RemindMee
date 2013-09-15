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
import com.remindme.utils.RemindNotification;

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
		String strDelay = intent.getStringExtra("delay");
		
		RemindNotification notif = intent.getParcelableExtra("notif");
		if (notif!=null){
			
			Log.d("Service", notif.getId().toString());
			NotificationDAO dbNotif = new NotificationSQLite(ctx);
			
			Date date = notif.getNotifyDate();
			NoticeDelay not = NoticeDelay.getDelay(strDelay, ctx);
			NoticeDelay.delayDate(date, not);
			dbNotif.updateNotification(notif);
			
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

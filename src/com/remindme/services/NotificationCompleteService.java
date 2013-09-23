package com.remindme.services;

import com.remindme.db.NotificationDAO;
import com.remindme.db.NotificationSQLite;
import com.remindme.db.TaskDAO;
import com.remindme.db.TaskNotifDAO;
import com.remindme.db.TaskNotifSQLite;
import com.remindme.db.TaskSQLite;
import com.remindme.utils.Event;
import com.remindme.utils.RemindTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NotificationCompleteService extends Service{

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		
		
		Log.d("ServiceComplete", "Servicio iniciado");
		Event notif = intent.getParcelableExtra("notif");
		if (notif!=null){
			
			Log.d("ServiceComplete", "Notification id:"+notif.getId().toString());
			NotificationDAO dbNotif = new NotificationSQLite(getApplicationContext());
			TaskNotifDAO dbTaskNotif = new TaskNotifSQLite(getApplicationContext());
			notif.setDone(true);
			dbNotif.updateNotification(notif);
			
		}
		String str = intent.getStringExtra("extra");
		if (str!=null)
			Log.d("ServiceComplete", str);		
		this.stopSelf();
		return startId;
		
	}
	
	@Override
	public void onDestroy()
	{
		Log.d("Service", "Servicio parado");
		super.onDestroy();
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("NotificationIntentService", "Servicio funcionado");
		return null;
	}


}

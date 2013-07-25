package com.remindme.services;

import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.remindme.utils.RemindTask;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NotificationIntentService extends Service{

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		
		
		Log.d("Service", "Servicio iniciado");
		RemindTask task = intent.getParcelableExtra("task");
		if (task!=null){
			
			Log.d("Service", task.getName());
			RemindTaskDAO dbTask = new RemindTaskSQLite(getApplicationContext());
			task.setCompleted(true);
			dbTask.updateTask(task);
			
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
		Log.d("Service", "Servicio parado");
		super.onDestroy();
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("NotificationIntentService", "Servicio funcionado");
		return null;
	}

	/**
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		// TODO Auto-generated method stub
		Context ctx = getApplicationContext();
		RemindTaskSQLite dbTask = new RemindTaskSQLite(ctx);
		RemindTask task = dbTask.getTaskWithID(intent.getIntExtra("task", 0));
		task.setCompleted(true);
		dbTask.updateTask(task);
		//TODO Pensar si eliminar la notificacion desde aquí
		Log.d("ResponseReceiver", "Completando tarea desde notificacion");
	}*/
	
	

}

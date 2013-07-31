package com.remindme.ui;




import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.remindme.ui.R;

import com.remindme.services.NotificationDelayService;
import com.remindme.services.NotificationIntentService;
import com.remindme.services.NotificationManagementService;
import com.remindme.services.NotificationRestartService;
import com.remindme.sqlite.RemindNotificationDAO;
import com.remindme.sqlite.RemindNotificationSQLite;
import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.remindme.utils.RemindNotification;
import com.remindme.utils.RemindTask;

import android.app.AlarmManager;
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
	
private static final long sleepTime;
	

	static {
	    Calendar cal;
		cal = GregorianCalendar.getInstance();
		cal.clear();
	    cal.set(Calendar.SECOND, 5);
	    
	    sleepTime = cal.getTimeInMillis();
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		launchLoopThread();
		startFadeIn();
	}
	
	private void launchLoopThread() {
		Intent intent  = new Intent(RemindSplashActivity.this, NotificationRestartService.class);
		intent.putExtra("firstLoop", true);
		startService(intent);
		
		Intent intentManage = new Intent(RemindSplashActivity.this, NotificationRestartService.class);
		intent.putExtra("firstLoop", false);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intentManage, 0);
	    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis()+ sleepTime, pendingIntent);
		Log.d("ServiceManagement", "Sleep for:" + new Date(sleepTime) +" /n til: "+ new Date(Calendar.getInstance().getTimeInMillis()+sleepTime) );
	    //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), sleepTime, pendingIntent);
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

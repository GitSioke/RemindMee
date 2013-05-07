package remind.me;




import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

import com.remindme.sqlite.RemindNotificationDAO;
import com.remindme.sqlite.RemindNotificationSQLite;

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
	private static long sleepTime = 60000;
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		startFadeIn();
		launchLoopThread();
			
	}
	
	private void launchLoopThread() {
		thread = new Thread() {
	        public void run() {
	        	Boolean firstLoop = true;
	        	Context ctx = getApplicationContext();
	        	while (true) {
	            	try {
	                    // do something here
	            		if (firstLoop){
	                		//TODO Iniciar como si estuviese apagado
	                		RemindNotificationDAO dbNoti = new RemindNotificationSQLite(ctx);
	                		RemindNotification not = new RemindNotification(12345, 6789, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), false);
	                		dbNoti.insertNotification(not);
	                		Long longas = Calendar.getInstance().getTime().getTime() + 10000;
	                		not = new RemindNotification(12346, 6789,  new Date(longas), new Date(longas), false);
	                		dbNoti.insertNotification(not);
	                		ArrayList<RemindNotification> notificationList= dbNoti.getAllNotifications();
	                		NotificationManager notificationManager =(NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
	                		
	                		for(RemindNotification notification: notificationList){
	                			NotificationCompat.Builder mBuilder =
	                	    	        new NotificationCompat.Builder(ctx)
	                	    	        .setSmallIcon(R.drawable.ic_launcher)
	                	    	        .setWhen(notification.getDate().getTime())
	                	    	        .setContentTitle(notification.getIdTask().toString())
	                	    	        .setContentText(notification.getDate().toString());
	                					
	                	    	// Creates an explicit intent for an Activity in your app
	                	    	Intent resultIntent = new Intent(ctx, RemindMenuActivity.class);

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
	                	    	mNotificationManager.notify(notification.getId(), mBuilder.build());
	                		}
	                		firstLoop = false;
	                	}
	                	//TODO Actualizar lo que se deba
	                	Log.d("Splash", "local Thread sleeping");
	                    Thread.sleep(sleepTime);
	                } catch (InterruptedException e) {
	                    Log.e("Splash", "local Thread error", e);
	                }
	            }
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

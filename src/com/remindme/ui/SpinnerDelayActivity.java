package com.remindme.ui;

import com.remindme.db.NotificationDAO;
import com.remindme.db.NotificationSQLite;
import com.remindme.services.NotificationCompleteService;
import com.remindme.services.NotificationDelayService;
import com.remindme.utils.Event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SpinnerDelayActivity extends Activity implements OnItemSelectedListener{
	
	Context ctx;
	
	public SpinnerDelayActivity(Context ctx){
		super();
		this.ctx = ctx;
	}
	//TODO Esta lanzando ntificationcompleteservice y debeeria hacer otra cosa
	public void onItemSelected(AdapterView<?> parent, View view,
			int position, long id) {
		Object item = parent.getItemAtPosition(position);
		
		Integer notifID = getIntent().getIntExtra("notifID", -1);
		Log.d("SpinnerDialogDelay", "Time: " + item.toString() +", NotifID: "+ notifID);
		if(notifID != -1)
		{
			Intent intent = new Intent(ctx, NotificationDelayService.class);
			intent.putExtra("delay", position);
			intent.putExtra("notifID", notifID);
			startService(intent);
		}
		
	}

	public void onNothingSelected(AdapterView<?> parent) {
		return;
		
	}


}

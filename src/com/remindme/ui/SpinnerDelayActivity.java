package com.remindme.ui;

import com.remindme.services.NotificationCompleteService;

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
	
	public void onItemSelected(AdapterView<?> parent, View view,
			int position, long id) {
		Object item = parent.getItemAtPosition(position);
		Log.d("DialogDelay", item.toString());
		Intent intent = new Intent(ctx, NotificationCompleteService.class);
		startService(intent);
	}

	public void onNothingSelected(AdapterView<?> parent) {
		return;
		
	}


}

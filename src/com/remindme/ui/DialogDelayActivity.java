package com.remindme.ui;



import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class DialogDelayActivity extends RemindActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.dialog_delay);
	    final Spinner delaySpinner = (Spinner) findViewById(R.id.DialogDelay_Spinner);
	    getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dialogDelay_array_spinnerDelay, 
        		android.R.layout.simple_selectable_list_item); 
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        delaySpinner.setAdapter(adapter);
        
        final OnItemSelectedListener listener = new OnItemSelectedListener() {
        	
        	public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Object item = parent.getItemAtPosition(position);
				Log.d("DialogDelay", item.toString());
				Intent intent = new Intent(DialogDelayActivity.this, RemindMenuActivity.class);
				startActivity(intent);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				return;
				
			}
        
        };
        
        delaySpinner.post(new Runnable(){
        	public void run(){
        		delaySpinner.setOnItemSelectedListener(listener);
        	}
        });
       
        
	}
}

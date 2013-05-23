package com.remindme.ui;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import remind.me.R;

import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.remindme.utils.Notice;
import com.remindme.utils.RemindTask;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RemindDayActivity extends RemindActivity {
    /** Called when the activity is first created. */
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day);
        RemindTaskDAO dbTask = new RemindTaskSQLite(this);
        
       	Long dateLong = (Long)getIntent().getLongExtra("date", 0);
        Date day = new Date(dateLong);
        SimpleDateFormat format = new SimpleDateFormat("EE d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
		String dateString = format.format(day);
		Date endOfDay = new Date(dateLong + Notice.getAsLong(Notice.DAY));
		
		TextView txtHeader =(TextView) findViewById(R.id.Day_HeaderTxtView);
		txtHeader.setText(dateString);
		
		ArrayList<RemindTask> taskList =  dbTask.getTaskBetweenDates(day, endOfDay);
		if (taskList.isEmpty()){
        	Toast.makeText(this, R.string.day_toast_noTask, Toast.LENGTH_LONG).show();
        }else{
        	
        	displayTaskWithTextView(taskList);
        	//displayCheckBoxes(taskList);
        }
        
        
    }
    	
	/**
	 * Muestra en el TextView de all.xml las tareas almacenadas
	 */
	private void displayTaskWithTextView(final ArrayList<RemindTask> taskList){
		ListView taskListView = (ListView)findViewById(R.id.Day_ListViewTask);
		taskListView.setAdapter(new RemindTaskAdapter(this, R.layout.list_item_task, taskList));
		
		OnItemClickListener listener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO 
				Intent intent = new Intent(RemindDayActivity.this, RemindTaskActivity.class);
				RemindTask task = taskList.get(position);
				Log.d("Pending", "Tarea seleccionada:"+task.getName());
				intent.putExtra("task", task);
				startActivity(intent);
				
			}
		};
		taskListView.setOnItemClickListener(listener);
		
		/**
		 * Crea los remind y los muestra 
		 */	
		
		
	}
	/**
	private void displayCheckBoxes(final ArrayList<RemindTask> taskList){
		checkListView = (ListView)findViewById(R.id.All_ListViewCheckBox);
		checkListView.setAdapter(new RemindTaskAdapter(this, R.layout.list_item_check, taskList));
		
	}*/

}

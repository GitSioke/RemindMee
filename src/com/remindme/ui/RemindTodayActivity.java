package com.remindme.ui;

import java.util.ArrayList;

import com.remindme.ui.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.remindme.db.RemindTaskDAO;
import com.remindme.db.RemindTaskSQLite;
import com.remindme.utils.RemindTask;

public class RemindTodayActivity extends RemindActivity {
    
	private ListView taskListView;
	/** Called when the activity is first created. */
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Completed", "Set content view");
        setContentView(R.layout.today);
        Log.d("Completed", "Initialize cursor");
        
        TextView txtHeader =(TextView)findViewById(R.id.All_HeaderTxtView);
        txtHeader.setText(R.string.today_header);
         
        
        RemindTaskDAO db = new RemindTaskSQLite(this);
        //TODO REalizar bien las dos llamadas a base de datos para recoger los correctos valores de las tareas
		ArrayList<RemindTask> taskList =  db.getPendingTasks(true);
        if (taskList.isEmpty()){
        	Toast.makeText(this, R.string.completed_toastEmpty, Toast.LENGTH_LONG).show();
        }else{
        	displayTaskWithTextView(taskList);
        }
        ArrayList<RemindTask> expiredTaskList = db.getPendingTasks(true);
        if(expiredTaskList.isEmpty()){
        	TextView text = (TextView)findViewById(R.id.Today_TextNoExpiredTask);
        	text.setText(R.string.today_noexpiredtasks);
        }
        
    }
   /**
     * Show 
     * @param c
     */
	private void displayTaskWithToast(Cursor cursor) {
		String android_id = Secure.getString(getBaseContext().getContentResolver(),
                Secure.ANDROID_ID); 
		Toast.makeText(this, "id:"+ cursor.getInt(0) + "\n" +
	"Tarea: " + cursor.getString(1), Toast.LENGTH_LONG).show();
		
	}
	
	/**
	 * Muestra en el TextView de all.xml las tareas almacenadas
	 */
	private void displayTaskWithTextView(final ArrayList<RemindTask> taskList){
		taskListView = (ListView)findViewById(R.id.All_ListViewTask);
		taskListView.setAdapter(new RemindTaskAdapter(this, R.layout.list_item_task, taskList));
		
		OnItemClickListener listener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO 
				Intent intent = new Intent(RemindTodayActivity.this, RemindTaskActivity.class);
				RemindTask task = taskList.get(position);
				intent.putExtra("task", task);
				startActivity(intent);
				
			}
		};
		taskListView.setOnItemClickListener(listener);
		
		/**
		 * Crea los remind y los muestra 
		 */	
		
		
	}
	
	public void onCheckTaskItem(View view){
		
	}
}
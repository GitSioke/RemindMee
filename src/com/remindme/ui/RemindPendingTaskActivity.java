package com.remindme.ui;




import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.remindme.ui.R;

import android.provider.Settings.Secure;

import com.remindme.db.TaskDAO;
import com.remindme.db.TaskSQLite;
import com.remindme.utils.RemindTask;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import static android.provider.BaseColumns._ID;

public class RemindPendingTaskActivity extends RemindActivity {
    
	private ListView taskListView;
	private ListView checkListView;
	/** Called when the activity is first created. */
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ALL", "Set content view");
        setContentView(R.layout.all);
        Log.d("ALL", "Initialize cursor");
       
        /**db.open();
        Log.d("ALL", "Initialize cursor");
        Cursor cursor =  db.getAllTasks();
        String [] fromColumns = db.getFromColumnsTask();
        int[] toViews = {R.id.All_TaskID_Content, R.id.All_TaskName_Content};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.id., cursor, fromColumns, toViews);
        ListView listView = getListView();
        listView.setAdapter(adapter);*/
        
        
        /**
         * Se insertan correctamente los datos en la base de datos. 
         * Y se muestran correctamente
         */
        TaskDAO db = new TaskSQLite(this);
        /**
        RemindTask task =new RemindTask("Name1", "10/05/2013", "10:00", "DIARIA", "Tag2","none");
        RemindTask task2=new RemindTask("Name2", "20/03/2013", "21:01", "ANUAL", "Tag1", null);
        RemindTask task3= new RemindTask("Name3", "13/01/1950", "23:05", "ANUAL", "Tag4", null);
        db.open();
        long id = db.insertNewTask(task);
        id = db.insertNewTask(task2);
        id = db.insertNewTask(task3);
        db.close();
		*/
       
        /**
         * Apertura database y muestreo de todas las tareas
         */
        //RemindTask task2= new RemindTask("Name2", "20/03/2013", "21:01", "ANUAL", "Tag1", null);
        //task2.setCompleted(true);
        //db.updateTask(task2);
        
		ArrayList<RemindTask> taskList =  db.getPendingTasks(false);		
        if (taskList.isEmpty()){
        	Toast.makeText(this, R.string.pending_toastEmpty, Toast.LENGTH_LONG).show();
        }else{
        	Collections.sort(taskList);
        	displayTaskWithTextView(taskList);
        	//displayCheckBoxes(taskList);
        }
        
        
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
				Intent intent = new Intent(RemindPendingTaskActivity.this, RemindTaskActivity.class);
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
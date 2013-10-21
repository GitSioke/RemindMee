    package com.remindme.ui;
     
    import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.remindme.ui.R;
     
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
     
import com.remindme.db.NotificationDAO;
import com.remindme.db.NotificationSQLite;
import com.remindme.db.TaskDAO;
import com.remindme.db.TaskSQLite;
import com.remindme.dialogs.RemindAlertDialog;
import com.remindme.dialogs.RemindDeleteDialog;
import com.remindme.utils.Event;
import com.remindme.utils.RemindTask;
import com.remindme.utils.Repetition;
     
    public class RemindTaskActivity extends RemindActivity{
    	private RemindTask task;
    	private Boolean isFatherTask;
    	private Event notif;
    	private TaskDAO taskDB;
    	private NotificationDAO notifDB;
    	private CheckBox box;
        @Override
        public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           Log.d("TASK", "Set content view");
           setContentView(R.layout.task);
           Log.d("TASK", "Initialize cursor");
           setHeaderButton();
           this.box = (CheckBox) findViewById(R.id.Task_Checkbox);
           //RemindTaskDAO taskDB = new  RemindTaskSQLite(this);
           //Integer id = 1088402616;
           //task = taskDB.getTaskWithID(id);
           /**
           RemindTask subTask1 =new RemindTask(null, "SubTask1", "10/05/2013", "10:00", "Diaria", "Tag2",id, false);
           RemindTask subTask2 =new RemindTask(null, "SubTask2", "10/05/2013", "10:00", "Diaria", "Tag2",id, false);
           RemindTask subTask3 =new RemindTask(null, "SubTask3", "10/05/2013", "10:00", "Diaria", "Tag2",id, false);
           RemindTaskDAO db = new HandlerSQLite(this);
           db.insertNewTask(subTask1);
           db.insertNewTask(subTask3);
           */
           //Inserta tarea manualmente
           //RemindTask taskDeleted = new RemindTask(null, "Tarea Para Eliminar","10/05/2013", "10:00", "Diaria", "Tag2",id, false );
           //taskDB.insertNewTask(taskDeleted);
           //task = taskDB.getTaskWithID(taskDeleted.getId());
           
           
           task = getIntent().getParcelableExtra("task");
           isFatherTask = getIntent().getBooleanExtra("father", false);
           if (!isFatherTask){
        	   notif = getIntent().getParcelableExtra("notif");
           }
           Log.d("TASK", task.getName());
           TextView txtName = (TextView)findViewById(R.id.Task_Name);
           txtName.setText(task.getName());
           TextView txtDate = (TextView)findViewById(R.id.Task_Date);
           //TODO Revisar Date
           SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
   		   String dateString = format.format(task.getDate());
           txtDate.setText(dateString);
           TextView txtDateNotice = (TextView)findViewById(R.id.Task_DateNotice);
           dateString = format.format(task.getDateNotice());
           txtDateNotice.setText(dateString);
           TextView txtRepetition = (TextView)findViewById(R.id.Task_Repetition);
           String strTranslated = Repetition.translateStringToLocal(task.getRepetition(), getApplicationContext());
           txtRepetition.setText(strTranslated);
           TextView txtTag = (TextView) findViewById(R.id.Task_Tag);
           txtTag.setText(task.getTag());
           TextView txtDescr = (TextView)findViewById(R.id.Task_TextDescription);
           txtDescr.setText(task.getDescription());
           box.setChecked(task.isCompleted());
           
           
           notifDB = new NotificationSQLite(this);
           taskDB = new TaskSQLite(this);
           //TODO Revisar que sean subnotificaciones
           ArrayList<RemindTask> subTasks;
           if(isFatherTask){
        	   subTasks = (ArrayList<RemindTask>) taskDB.getSubtasks(task.getId());
           }else{
               subTasks = (ArrayList<RemindTask>) notifDB.getSubNotification(task.getId());

           }
           if (!subTasks.isEmpty()){
        		   displayTaskWithTextView(subTasks);         
           }
           //SubTask
           ImageView img = (ImageView) findViewById(R.id.Task_ImgNewSubtask);
           img.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(RemindTaskActivity.this, RemindNewActivity.class);
   				intent.putExtra("superTaskID", task.getId());
   				intent.putExtra("notEvent", isFatherTask);
   				startActivity(intent);
				
			}
		});

           
           
            //TODO Sacar si esta completo o no y modificar el checkbox
            }
            
            private void displayTaskWithTextView(final ArrayList<RemindTask> taskList){
           		ListView taskListView = (ListView)findViewById(R.id.Task_ListSubTask);
           		taskListView.setAdapter(new RemindTaskAdapter(this, R.layout.list_item_task, taskList, !isFatherTask));
           		
           		OnItemClickListener listener = new OnItemClickListener() {

           			public void onItemClick(AdapterView<?> parent, View view, int position,
           					long id) {
           				// TODO 
           				Intent intent = new Intent(RemindTaskActivity.this, RemindTaskActivity.class);
           				RemindTask task = taskList.get(position);
           				intent.putExtra("task", task);
           				intent.putExtra("notEvent", isFatherTask);
           				startActivity(intent);
           				
           			}
           		};
           		taskListView.setOnItemClickListener(listener);
           		
           		/**
           		 * Crea los remind y los muestra 
           		 */	
           		
           		
           	}
       
       /**
        * Edita la tarea al hacer click en la capa Relative correspondiente RelativeTask     
        */
       public void editTask(View view){
    	   DialogFragment dialog = new RemindAlertDialog();
    	   Bundle bundle = new Bundle();
    	   bundle.putParcelable("Task", task);
    	   bundle.putBoolean("notEvent", isFatherTask);
    	   dialog.setArguments(bundle);
    	   dialog.show(getFragmentManager(), "dialog");
       }
       /**
        * Elimina la tarea al hacer click en el icono delete de task.xml
        * @param view
        */
       public void deleteTask(View view){   	   
    	   DialogFragment dialog = new RemindDeleteDialog();
	       Bundle bundle = new Bundle();
	       bundle.putParcelable("Task", task);
	       dialog.setArguments(bundle);
	       dialog.show(getFragmentManager(), "dialog");
	      }
       
       /**TODO En vez de un toast comprobar que lo haga un Dialog para completar todas las subtareas 
        * 
        * Al hacer checkbox la tarea se completa o se deshace dependiendo de su estado anterior
        * @param view
        */ 
    	public void onCheckBoxClicked(View view){
    		
    		if (box.isChecked()){
    			if( isFatherTask){
     			
        			if(taskDB.hasSubtaskUndone(task.getId())){
        				Toast.makeText(this, R.string.task_toast_hasSubtask, Toast.LENGTH_LONG).show();
        				box.setChecked(false);
        			}else{
        				
        				task.setCompleted(task.isCompleted()? false: true);
        				taskDB.updateTask(task);
        			}
        			
        		}else{
            		if (notifDB.hasSubNotification(task.getId())){
            			Toast.makeText(this, R.string.task_toast_hasSubtask, Toast.LENGTH_LONG).show();
            		}else{
            			notif.setDone(notif.isDone()? false:true);
            			notifDB.updateNotification(notif);
            			//TODO Tachar los textview
            			//crossOutTaskElements();
            		}
        		}
    		}
    		
    		
    	}
    	//Home button
    	private void setHeaderButton(){
    		ImageView imageNew = (ImageView) findViewById(R.id.Header_NewImage);
            ImageView imageHome = (ImageView) findViewById(R.id.Header_HomeImage);
    		imageNew.setOnClickListener(new View.OnClickListener() {
    			
    			public void onClick(View v) {
    				startActivity(new Intent(RemindTaskActivity.this, RemindNewActivity.class));
    				
    			}
    		});
    		
    		imageHome.setOnClickListener(new View.OnClickListener() {
    			
    			public void onClick(View v) {
    				startActivity(new Intent(RemindTaskActivity.this, RemindMenuActivity.class));
    				
    			}
    		});
    		
    	}
    }

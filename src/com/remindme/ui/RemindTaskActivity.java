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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
     
import com.remindme.dialogs.RemindAlertDialog;
import com.remindme.dialogs.RemindDeleteDialog;
import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.remindme.utils.RemindTask;
import com.remindme.utils.Repetition;
     
    public class RemindTaskActivity extends RemindActivity{
    	private RemindTask task;
        @Override
        public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           Log.d("TASK", "Set content view");
           setContentView(R.layout.task);
           Log.d("TASK", "Initialize cursor");
           
           //RemindTaskDAO taskDB = new  RemindTaskSQLite(this);
           //Integer id = 1088402616;
           //task = taskDB.getTaskWithID(id);
           /**
           RemindTask subTask1 =new RemindTask(null, "SubTask1", "10/05/2013", "10:00", "Diaria", "Tag2",id, false);
           RemindTask subTask2 =new RemindTask(null, "SubTask2", "10/05/2013", "10:00", "Diaria", "Tag2",id, false);
           RemindTask subTask3 =new RemindTask(null, "SubTask3", "10/05/2013", "10:00", "Diaria", "Tag2",id, false);
           RemindTaskDAO db = new HandlerSQLite(this);
           db.insertNewTask(subTask1);
           db.insertNewTask(subTask2);
           db.insertNewTask(subTask3);
           */
           //Inserta tarea manualmente
           //RemindTask taskDeleted = new RemindTask(null, "Tarea Para Eliminar","10/05/2013", "10:00", "Diaria", "Tag2",id, false );
           //taskDB.insertNewTask(taskDeleted);
           //task = taskDB.getTaskWithID(taskDeleted.getId());
           
           
           task = getIntent().getParcelableExtra("task");
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
           String strTranslated = Repetition.translateString(task.getRepetition(), getApplicationContext());
           txtRepetition.setText(strTranslated);
           TextView txtTag = (TextView) findViewById(R.id.Task_Tag);
           txtTag.setText(task.getTag());
           TextView txtDescr = (TextView)findViewById(R.id.Task_TextDescription);
           txtDescr.setText(task.getDescription());
           
           RemindTaskDAO taskDAO = new RemindTaskSQLite(this);
           ArrayList<RemindTask> subTasks = (ArrayList<RemindTask>) taskDAO.getSubtasks(task.getId());
           if (!subTasks.isEmpty()){
        		   displayTaskWithTextView(subTasks);         
           }
           ImageView img = (ImageView) findViewById(R.id.Task_ImgNewSubtask);
           img.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(RemindTaskActivity.this, RemindNewActivity.class);
   				intent.putExtra("superTaskID", task.getId());
   				startActivity(intent);
				
			}
		});

           
           
            //TODO Sacar si esta completo o no y modificar el checkbox
            }
            
            private void displayTaskWithTextView(final ArrayList<RemindTask> taskList){
           		ListView taskListView = (ListView)findViewById(R.id.Task_ListSubTask);
           		taskListView.setAdapter(new RemindTaskAdapter(this, R.layout.list_item_task, taskList));
           		
           		OnItemClickListener listener = new OnItemClickListener() {

           			public void onItemClick(AdapterView<?> parent, View view, int position,
           					long id) {
           				// TODO 
           				Intent intent = new Intent(RemindTaskActivity.this, RemindTaskActivity.class);
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
       
       /**
        * Edita la tarea al hacer click en la capa Relative correspondiente RelativeTask     
        */
       public void editTask(View view){
    	   DialogFragment dialog = new RemindAlertDialog();
    	   Bundle bundle = new Bundle();
    	   bundle.putParcelable("Task", task);
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
    		RemindTaskDAO taskDB = new RemindTaskSQLite(this);
    		if (taskDB.hasSubtask(task.getId())){
    			Toast.makeText(this, R.string.task_toast_hasSubtask, Toast.LENGTH_LONG).show();
    		}else{
    			taskDB.updateTask(task);
    			//TODO Tachar los textview
    			//crossOutTaskElements();
    		}
    	}
    }

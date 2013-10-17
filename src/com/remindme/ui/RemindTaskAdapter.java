package com.remindme.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.remindme.ui.R;

import com.remindme.db.NotificationDAO;
import com.remindme.db.NotificationSQLite;
import com.remindme.db.TaskDAO;
import com.remindme.db.TaskSQLite;
import com.remindme.utils.Event;
import com.remindme.utils.RemindTask;

import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class RemindTaskAdapter extends ArrayAdapter<RemindTask>{
	
	private Context context;
	private Boolean isEvent;
	
	public RemindTaskAdapter(Context context, int textViewResourceId, ArrayList<RemindTask> taskList, Boolean isEvent) {
		super(context, textViewResourceId, taskList);
		this.context = context;
		this.isEvent = isEvent;
		//Log.d("ADAPTER", taskList.get(0).getName().toString());
	}
	
	/**
	 * Contenedor de las vistas de la tarea
	 * @author pick
	 *
	 */
	private class ViewHolder{
		TextView txtName;
		TextView txtDate;
		TextView txtTime;
		CheckBox check;
	}
	
	/**
	 * Rellena las tareas a medida que aparecen en la pantalla
	 */
	@Override
    public View getView (int position, View convertView, ViewGroup parent ) {
		
		ViewHolder holder = null;
        final RemindTask task = getItem(position);
        
 
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            
        	convertView = mInflater.inflate(R.layout.list_item_task, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.ListItemTask_Name);
            holder.txtDate = (TextView) convertView.findViewById(R.id.ListItemTask_Date);
            holder.txtTime = (TextView) convertView.findViewById(R.id.ListItemTask_Time);
            holder.check = (CheckBox) convertView.findViewById(R.id.ListItemCheck_Checkbox);
            if(task.isCompleted())
            	holder.check.setChecked(true);
            convertView.setTag(holder);
        } else{
        
           holder = (ViewHolder) convertView.getTag();
        }
        OnClickListener listener = new OnClickListener() {

        	public void onClick(View view) {
        	// TODO

        		Log.d("Adapter", task.getName());
        		Log.d("ADAPTER", task.getId().toString());
        		if (isEvent){
	        		//boolean checked = ((CheckBox) view).isChecked();
	        		NotificationDAO notifDB= new NotificationSQLite(context);
	        		notifDB.changeDone(task.getId());
	    			Event auxTask = notifDB.getNotificationWithID(task.getId());
	    			if(auxTask.isDone())
	    				Log.d("ADAPTER", "Completada");
        		}else{
        			TaskDAO taskDAO = new TaskSQLite(context);
        			taskDAO.changeCompleted(task.getId());
        		}
        	}
       	};

       	holder.check.setOnClickListener(listener);
        holder.txtName.setText(task.getName());
        //Revisar Date
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = format.format(task.getDate());
		holder.txtDate.setText(dateString);
		format = new SimpleDateFormat("HH:mm");
		String timeString = format.format(task.getDate());
        holder.txtTime.setText(task.getTime());
        
        return convertView;
		        
     }

	
}

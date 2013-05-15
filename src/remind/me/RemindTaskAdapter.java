package remind.me;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.utils.RemindTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RemindTaskAdapter extends ArrayAdapter<RemindTask>{
	
	private Context context;

	
	public RemindTaskAdapter(Context context, int textViewResourceId, ArrayList<RemindTask> taskList) {
		super(context, textViewResourceId, taskList);
		this.context = context;
		Log.d("ADAPTER", taskList.get(0).getName().toString());
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
    public View getView ( int position, View convertView, ViewGroup parent ) {
		
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
            
            convertView.setTag(holder);
        } else{
        
           holder = (ViewHolder) convertView.getTag();
        }
        OnClickListener listener = new OnClickListener() {

        	public void onClick(View view) {
        	// TODO

        		Log.d("Adapter", task.getName());
        		Log.d("ADAPTER", task.getId().toString());
        		RemindTaskDAO taskDB= new RemindTaskSQLite(context);
        		boolean checked = ((CheckBox) view).isChecked();
        		if(checked){
        			
        			taskDB.updateTaskCompleted(task);
        			RemindTask auxTask = taskDB.getTaskWithID(task.getId());
        			if(auxTask.isCompleted())
        			Log.d("ADAPTER", "Completada");
        		}else{
        			taskDB.updateTaskCompleted(task);

        		}

        	}
       	};

       	holder.check.setOnClickListener(listener);
        holder.txtName.setText(task.getName());
        //Revisar Date
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = format.format(task.getDate());
		holder.txtDate.setText(dateString);
        holder.txtTime.setText(task.getTime());
        
        return convertView;
		        
     }

	
}

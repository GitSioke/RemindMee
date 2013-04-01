package remind.me;

import java.util.ArrayList;

import com.remindme.sqlite.RemindTaskSQLite;

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
		TextView txtRepeat;
		TextView txtTag;
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
            holder.txtRepeat = (TextView) convertView.findViewById(R.id.ListItemTask_Repetition);           
            
            convertView.setTag(holder);
        } else{
        
           holder = (ViewHolder) convertView.getTag();
        }
        holder.txtName.setText(task.getName());
        //Revisar Date
        Long dateAsLong = task.getDate().getTime();
        holder.txtDate.setText(dateAsLong.toString());
        holder.txtTime.setText(task.getTime());
        holder.txtRepeat.setText(task.getRepetition());
        
        return convertView;
		        
     }

	
}

package remind.me;

import java.util.ArrayList;

import com.remindme.sqlite.RemindTaskSQLite;

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

public class RemindCheckAdapter extends ArrayAdapter<RemindTask>{
	
	private Context context;

	
	public RemindCheckAdapter(Context context, int textViewResourceId, ArrayList<RemindTask> taskList) {
		super(context, textViewResourceId, taskList);
		this.context = context;
		Log.d("ADAPTER_Check", taskList.get(0).getName().toString());
	}
	
	/**
	 * Contenedor de las vistas de la tarea
	 * @author pick
	 *
	 */
	private class ViewHolder{
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
            
        	convertView = mInflater.inflate(R.layout.list_item_check, null);
            holder = new ViewHolder();
            holder.check= (CheckBox)convertView.findViewById(R.id.ListItemCheck_Checkbox);
            
            convertView.setTag(holder);
        
        }else{
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
				}else{
					taskDB.updateTaskCompleted(task);
					
				}
				
			}
		};
		
		holder.check.setOnClickListener(listener);
                
        return convertView;
		        
     }

	
}

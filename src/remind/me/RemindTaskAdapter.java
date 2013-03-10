package remind.me;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RemindTaskAdapter extends ArrayAdapter<RemindTask>{
	
	private Context context;
	
	public RemindTaskAdapter(Context context, int textViewResourceId, List<RemindTask> taskList) {
		super(context, textViewResourceId, taskList);
		this.context = context;
		
		
	}
	
	private class ViewHolder{
		TextView txtName;
		TextView txtDate;
		TextView txtTime;
		TextView txtRepeat;
		TextView txtTag;
	}
	@Override
    public View getView ( int position, View convertView, ViewGroup parent ) {
		ViewHolder holder = null;
        RemindTask task = getItem(position);
 
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            Log.d("Entra en if", "WACK");
        	convertView = mInflater.inflate(R.layout.tasks, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.Task_Name);
            holder.txtDate = (TextView) convertView.findViewById(R.id.Task_Date);
            holder.txtTime = (TextView) convertView.findViewById(R.id.Task_Time);
            holder.txtRepeat = (TextView) convertView.findViewById(R.id.Task_Repetition);
            holder.txtTag = (TextView) convertView.findViewById(R.id.Task_Tag);
            
            convertView.setTag(holder);
        } else{
        Log.d("Entra en else", "WACK");
           holder = (ViewHolder) convertView.getTag();
        }
        
        
        holder.txtName.setText(task.getName());
        Log.d("REMINDTASKADAPTER", task.getName());
        holder.txtDate.setText(task.getDate());
        Log.d("REMINDTASKADAPTER", task.getDate());
        holder.txtTime.setText(task.getTime());
        Log.d("REMINDTASKADAPTER", task.getTime());
        holder.txtRepeat.setText(task.getRepetition());
        Log.d("REMINDTASKADAPTER", task.getRepetition());
        holder.txtTag.setText(task.getTag());
        Log.d("REMINDTASKADAPTER", task.getTag());
        
        return convertView;
		        
     }

	
}

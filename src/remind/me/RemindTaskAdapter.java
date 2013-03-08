package remind.me;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RemindTaskAdapter extends ArrayAdapter<RemindTask>{
	
	private Context context;
	
	public RemindTaskAdapter(Context context, int textViewResourceId, List<RemindTask> taskList) {
		super(context, textViewResourceId, taskList);
		this.context = context;
		
		
	}
	
	private class ViewHolder{
		ImageView imageView;
		TextView txtTitle;
		TextView txtDesc;
	}
	@Override
    public View getView ( int position, View convertView, ViewGroup parent ) {
		ViewHolder holder = null;
        RemindTask task = getItem(position);
 
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tasks, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.Task_cityLinkWiki);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.Task_cityName);
            //holder.imageView = (ImageView) convertView.findViewById(R.id.);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
        holder.txtDesc.setText(task.getId().toString());
        holder.txtTitle.setText(task.getName());
        //holder.imageView.setImageResource(task.getImageId());
 
        return convertView;
		        
     }

	
}

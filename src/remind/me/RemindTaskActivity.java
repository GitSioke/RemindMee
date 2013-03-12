package remind.me;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import com.remindme.sqlite.HandlerSQLite;

public class RemindTaskActivity extends RemindActivity{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TASK", "Set content view");
        setContentView(R.layout.task);
        Log.d("TASK", "Initialize cursor");
        
        
        RemindTask task = getIntent().getParcelableExtra("task");
        TextView txtName = (TextView)findViewById(R.id.Task_Name);
        txtName.setText(task.getName());
        
    }
    
}

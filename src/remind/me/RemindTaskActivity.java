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
           
           RemindTaskDAO taskDB = new  HandlerSQLite(this);
           Integer id = 100000;
           RemindTask task = taskDB.getTaskWithID(id);
           //RemindTask task = getIntent().getParcelableExtra("task");
           TextView txtName = (TextView)findViewById(R.id.Task_Name);
           txtName.setText(task.getName());
           TextView txtDate = (TextView)findViewById(R.id.Task_Date);
           txtDate.setText(task.getDate());
           TextView txtTime = (TextView)findViewById(R.id.Task_Time);
           txtTime.setText(task.getTime());
           TextView txtRepetition = (TextView)findViewById(R.id.Task_Repetition);
           txtRepetition.setText(task.getRepetition());
           
           
           
            //TODO Sacar si esta completo o no y modificar el checkbox
            }
                   
       
    }

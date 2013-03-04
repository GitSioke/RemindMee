package remind.me;




import java.util.ArrayList;
import java.util.List;

import android.provider.Settings.Secure;
import com.remindme.sqlite.HandlerSQLite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import static android.provider.BaseColumns._ID;

public class RemindAllActivity extends RemindActivity {
    
	private ListView taskListView;
	/** Called when the activity is first created. */
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ALL", "Set content view");
        setContentView(R.layout.all);
        Log.d("ALL", "Initialize cursor");
        displayTaskWithTextView();
        //TextView text = (TextView)findViewById(R.id.All_TaskName);
        //Log.d("ALL", "Creating handler");
        //HandlerSQLite db = new HandlerSQLite(this);
        /*db.open();
        long id = db.insertNewTask("Matar a tooooodos los humanos");
        id = db.insertNewTask("Humanos");
        db.close();
        */
        /**db.open();
        Log.d("ALL", "Initialize cursor");
        Cursor cursor =  db.getAllTasks();
        String [] fromColumns = db.getFromColumnsTask();
        int[] toViews = {R.id.All_TaskID_Content, R.id.All_TaskName_Content};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.id., cursor, fromColumns, toViews);
        ListView listView = getListView();
        listView.setAdapter(adapter);*/
        /*if (cursor.moveToFirst()){
        	displayTaskWithTextView(cursor);
        }*/
        
        /**
         * Se insertan correctamente los datos en la base de datos. 
         * Y se muestran correctamente
         * db.open();        
        long id = db.insertNewTask("Matar a tooooodos los humanos");
        id = db.insertNewTask("Humanos");
        db.close();
        
        db.open();
        Cursor c =  db.getAllTasks();
        if (c.moveToFirst()){
        	do {
        		displayTask(c);
        	}while(c.moveToNext());
        		
        }*/
        
    }
    /**
     * Show 
     * @param c
     */
	private void displayTaskWithToast(Cursor cursor) {
		String android_id = Secure.getString(getBaseContext().getContentResolver(),
                Secure.ANDROID_ID); 
		Toast.makeText(this, "id:"+ cursor.getInt(0) + "\n" +
	"Tarea: " + cursor.getString(1), Toast.LENGTH_LONG).show();
		
	}
	
	private void displayTaskWithTextView(){
		ArrayList<RemindTask> tasksList = new ArrayList<RemindTask>();
		tasksList.add(new RemindTask(10000, "Pero"));
		tasksList.add(new RemindTask(20000, "esto"));
		tasksList.add(new RemindTask(30000, "que es"));
		Log.d("Elemento 1 de tarea",tasksList.get(0).getName());
		taskListView = (ListView)findViewById(R.id.All_ListViewTask);
		taskListView.setAdapter(new RemindTaskAdapter(this, R.layout.tasks, tasksList));
		/**
		Log.d("ALL", "fin text view id");
		TextView view = (TextView)findViewById(R.id.All_TaskID);
		Log.d("ALL", "Set id");
		view.setText(cursor.getInt(0));
		TextView view2 = (TextView)findViewById(R.id.All_TaskName);
		view2.setText(cursor.getString(1));
		*/
	}
}
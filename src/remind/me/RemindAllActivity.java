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
        
        
        /**
         * Se insertan correctamente los datos en la base de datos. 
         * Y se muestran correctamente
         */
        HandlerSQLite db = new HandlerSQLite(this);
        /**
         * Insercion de elementos
         * db.open();
        RemindTask task = new RemindTask("Churri", "10/08/1098", "10:20", "DIARIA", "Cuadradohip");
        RemindTask task2 = new RemindTask("Perry", "10/08/1098", "10:20", "DIARIA", "Cuadradohip");
        RemindTask task3 = new RemindTask("Maison", "10/08/1098", "10:20", "DIARIA", "Cuadradohip");
        long id = db.insertNewTask(task);
        id = db.insertNewTask(task2);
        id = db.insertNewTask(task3);
        db.close();*/
        
        /**
         * Apertura database y muestreo de todas las tareas
         */
        db.open();
        ArrayList<RemindTask> taskList =  db.getAllTasks();
        displayTaskWithTextView(taskList);
        
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
	
	/**
	 * Muestra en el TextView de all.xml las tareas almacenadas
	 */
	private void displayTaskWithTextView(ArrayList<RemindTask> taskList){
		
		taskListView = (ListView)findViewById(R.id.All_ListViewTask);
		taskListView.setAdapter(new RemindTaskAdapter(this, R.layout.tasks, taskList));
		
		/**
		 * Crea los remind y los muestra 
		 */
		/**tasksList.add(new RemindTask("Esto", "es", "carnaval", "volumen", "1"));
		tasksList.add(new RemindTask("Mapo", "es", "carnaval", "volumen", "2"));
		tasksList.add(new RemindTask("Chupi", "es", "carnaval", "volumen", "3"));
		Log.d("Elemento 1 de tarea",tasksList.get(0).getName());
		Log.d("Elemento 2 de tarea",tasksList.get(1).getName());
		Log.d("Elemento 3 de tarea",tasksList.get(2).getName());
		taskListView = (ListView)findViewById(R.id.All_ListViewTask);
		taskListView.setAdapter(new RemindTaskAdapter(this, R.layout.tasks, tasksList));
		*/
		
		
	}
}
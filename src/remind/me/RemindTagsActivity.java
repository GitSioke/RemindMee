package remind.me;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.remindme.sqlite.HandlerSQLite;

public class RemindTagsActivity extends RemindActivity {
	
	private ListView tagListView;
	/** Called when the activity is first created. */
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "Set content view");
        setContentView(R.layout.tags);
        Log.d("TAG", "Initialize cursor");
       
        /**
         * Se insertan correctamente los datos en la base de datos. 
         * Y se muestran correctamente
         */
        HandlerSQLite db = new HandlerSQLite(this);
         /**
         * Apertura database y muestreo de todas las tareas
         */
        db.open();
        RemindTask task = new RemindTask("Filf", "10/08/1213", "10:20", "ANUAL", "FILFOLF");
        long id = db.insertNewTask(task);
        ArrayList<String> tagList =  db.getAllTags();
        displayTagWithTextView(tagList);
        //displayTaskWithToast(tagList);
        
    }
   
	
	/**
	 * Muestra en el TextView de all.xml las tareas almacenadas
	 */
	private void displayTagWithTextView(ArrayList<String> tagList){
		
		tagListView = (ListView)findViewById(R.id.Tags_ListViewTags);
		tagListView.setAdapter(new ArrayAdapter<String>(this, R.layout.tag, R.id.Tag_TextTag, tagList));
		
		
	}
	
	private void displayTaskWithToast(ArrayList<String> tagList) {
		Toast.makeText(this, "Hola", Toast.LENGTH_LONG).show();
		if(tagList.isEmpty())
			Toast.makeText(this, "Vacio", Toast.LENGTH_LONG).show();
		for(String tag:tagList){
			Toast.makeText(this, "Bucle", Toast.LENGTH_LONG).show();
			Toast.makeText(this, "Etiqueta: " + tag, Toast.LENGTH_LONG).show();
		}
	}
}

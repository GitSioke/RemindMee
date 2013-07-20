package com.remindme.ui;

import java.util.ArrayList;
import java.util.List;

import com.remindme.ui.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.remindme.sqlite.RemindTaskSQLite;

public class RemindTagsActivity extends RemindActivity {
	
	private ListView tagListView;
	/** Called when the activity is first created. */
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "Set content view");
        setContentView(R.layout.tags);
        setHeaderButton();
        Log.d("TAG", "Initialize cursor");
       
        /**
         * Se insertan correctamente los datos en la base de datos. 
         * Y se muestran correctamente
         */
        RemindTaskSQLite db = new RemindTaskSQLite(this);
         /**
         * Apertura database y muestreo de todas las tareas
         */
        db.open();
        ArrayList<String> tagList =  db.getAllTags();
        displayTagWithTextView(tagList);
        
        
    }
   
	
	/**
	 * Muestra en el TextView de all.xml las tareas almacenadas
	 */
	private void displayTagWithTextView(final ArrayList<String> tagList){
		
		tagListView = (ListView)findViewById(R.id.Tags_ListViewTags);
		tagListView.setAdapter(new ArrayAdapter<String>(this, R.layout.tag, R.id.Tag_TextTag, tagList));
		OnItemClickListener listener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(RemindTagsActivity.this, RemindTagTaskActivity.class);
				String tagTask = tagList.get(position);
				intent.putExtra("tag", tagTask);
				startActivity(intent);			
			}
		};
		
		tagListView.setOnItemClickListener(listener);
		
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
	
	private void setHeaderButton(){
		ImageView imageNew = (ImageView) findViewById(R.id.Header_NewImage);
        ImageView imageHome = (ImageView) findViewById(R.id.Header_HomeImage);
		imageNew.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(RemindTagsActivity.this, RemindNewActivity.class));
				
			}
		});
		
		imageHome.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(RemindTagsActivity.this, RemindMenuActivity.class));
				
			}
		});
	}
}

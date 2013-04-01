package remind.me;





import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.remindme.sqlite.RemindTaskSQLite;


import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RemindNewActivity extends RemindActivity {
    
	TextView textDate;
	Long dateAsLong;
	TextView textTime;
	DialogFragment dateFragment;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_edit);
        textDate = (TextView) findViewById(R.id.New_TextViewDateShow);
        textTime = (TextView) findViewById(R.id.New_TextViewTimeShow);
        
        Spinner repeatSpinner = (Spinner)findViewById(R.id.New_SpinnerRepeat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repeat_array, 
        		android.R.layout.simple_selectable_list_item); 
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        repeatSpinner.setAdapter(adapter);
        
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
        Button addTask = (Button)findViewById(R.id.New_Button_Add);
        addTask.setOnClickListener(new View.OnClickListener() {
			
		
        	public void onClick(View v) {
				// TODO Auto-generated method stub
        		try {
        			initTaskNameEntry();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (InstantiationException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (IllegalAccessException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
				startActivity(new Intent(RemindNewActivity.this, RemindPendingTaskActivity.class));				
			}
		});
        
    }

	private void initTaskNameEntry() throws IOException, InstantiationException, IllegalAccessException {
		// TODO Meter todos los datos en la base de datos, averiguar como se guarda el dato en el spinner para sacarlo
		EditText taskName = (EditText)findViewById(R.id.EditText_Name);
		String name = taskName.getText().toString();
		String time = textTime.getText().toString();
		//Revisar Date
		
		Date date= new Date(dateAsLong);
		
		EditText taskTag = (EditText)findViewById(R.id.New_EditTextTag);
		String tag = taskTag.getText().toString();
		
		final Spinner spinnerRep = (Spinner)findViewById(R.id.New_SpinnerRepeat);
		spinnerRep.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				parent.getItemAtPosition(position);
				
			}

			public void onNothingSelected(AdapterView<?> parent) {
				return;
				
			}
		});
		String repetition = (String) spinnerRep.getSelectedItem();
		
		Integer superTaskID = getIntent().getIntExtra("superTaskID", -1);
		Log.d("NEW", Integer.toString(superTaskID));
		RemindTask task = new RemindTask(null, name, date, time, repetition, tag, superTaskID, false);
		RemindTaskDAO taskDB = new RemindTaskSQLite(this);
		
		taskDB.insertNewTask(task);		
		
	}
	/**TODO Pendiente de revision. Intentar pasar por Bundle la actividad o los datos necesarios
	 * Se encarga de mostrar el fragmento datepicker
	 * @param view
	 */
	public void showDatePickerDialog(View view){
		dateFragment = new DatePickerFragment(this);
		dateFragment.show(getFragmentManager(), "datepicker");
	}	
	public void showTimePickerDialog(View view){
		dateFragment = new TimePickerFragment(textTime);
		dateFragment.show(getFragmentManager(), "timepicker");
	}

	public void doPositiveClick() {
		Log.d("Datepicker", "aceptar");
		
	}

	public void doNegativeClick() {
		Log.d("Datepicker", "cancel");
		
	}

	
	

}
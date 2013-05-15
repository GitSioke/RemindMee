package remind.me;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.remind.fragments.DatePickerFragment;
import com.remind.fragments.TimePickerFragment;
import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.utils.RemindTask;

import android.app.DialogFragment;
import android.content.Intent;
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


public class RemindEditActivity extends RemindActivity {
    
	TextView textDate;
	TextView txtDateNotice;
	TextView textTime;
	DialogFragment dateFragment;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit); 
        RemindTask task = getIntent().getParcelableExtra("Task");
        initializeFromTask(task);
        
        textDate = (TextView) findViewById(R.id.Edit_EditTextDate);
        textTime = (TextView) findViewById(R.id.Edit_TextViewTimeShow);
        
        Spinner repeatSpinner = (Spinner)findViewById(R.id.Edit_SpinnerRepeat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repeat_array, 
        		android.R.layout.simple_selectable_list_item); 
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        repeatSpinner.setAdapter(adapter);
        
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
        Button addTask = (Button)findViewById(R.id.Edit_Button_Add);
        addTask.setOnClickListener(new View.OnClickListener() {
			
		
        	public void onClick(View v) {
				// TODO Auto-generated method stub
        		try {
        			saveTaskChanges();
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
				startActivity(new Intent(RemindEditActivity.this, RemindTaskActivity.class));				
			}
		});
        
    }
    
    	/**
    	 * Inicializa los parametros de la tarea desde el parcelable que contiene la tarea.
    	 */
		private void initializeFromTask(RemindTask task) {
		// TODO 
			EditText txtName = (EditText) findViewById(R.id.Edit_EditText_Name);
			txtName.setText(task.getName());
			TextView txtDate = (TextView) findViewById(R.id.Edit_EditTextDate);
			//TODO Revisar date
			SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
			String dateString = format.format(task.getDate());
			txtDate.setText(dateString);
			TextView txtDateNotice = (TextView) findViewById(R.id.Edit_EditTextDateNotice);
			dateString = format.format(task.getDateNotice());
			txtDateNotice.setText(dateString);
			TextView txtTime = (TextView) findViewById(R.id.Edit_TextViewTimeShow);
			txtTime.setText(task.getTime());
			EditText txtTag = (EditText)findViewById(R.id.Edit_EditTextTag);
			txtTag.setText(task.getTag());
			EditText txtRepetition = (EditText)findViewById(R.id.Edit_EditTextDescription);
			txtRepetition.setText(task.getRepetition());
			Spinner spinner = (Spinner)findViewById(R.id.Edit_SpinnerRepeat);
			//TODO Mirar el valor de task.getRepetition y modificar el valor por defecto
			//del spinner
			spinner.setSelection(0);
			
	}
		/**
		 * Guarda los cambios que se hayan producido en la base de datos
		 * @throws IOException
		 * @throws InstantiationException
		 * @throws IllegalAccessException
		 */
		private void saveTaskChanges() throws IOException, InstantiationException, IllegalAccessException {
		// TODO Meter todos los datos en la base de datos, averiguar como se guarda el dato en el spinner para sacarlo
		EditText taskName = (EditText)findViewById(R.id.Edit_EditText_Name);
		String name = taskName.getText().toString();
		String time = textTime.getText().toString();
		Long dateAsLong = Long.getLong(textDate.getText().toString());
		Date date = new Date(dateAsLong);
		
		dateAsLong = Long.getLong(textDate.getText().toString());
		Date dateNotice = new Date(dateAsLong);
		EditText taskTag = (EditText)findViewById(R.id.Edit_EditTextTag);
		String tag = taskTag.getText().toString();
		
		final Spinner spinnerRep = (Spinner)findViewById(R.id.Edit_SpinnerRepeat);
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
		RemindTask task = new RemindTask(null, name, date,dateNotice, time, repetition, tag, superTaskID, false);
		RemindTaskDAO taskDB = new RemindTaskSQLite(this);
		
		taskDB.insertTask(task);		
		
	}
	/**
	 * Se encarga de mostrar el fragmento datepicker
	 * @param view
	 */
	public void showDatePickerDialog(View view){
		//TODO Cambiar los contructores y meter un bundle en los dos
		dateFragment = new DatePickerFragment(textDate);
		dateFragment.show(getFragmentManager(), "datepicker");
	}	
	
	public void showDateNoticePickerDialog(View view){
		dateFragment = new DatePickerFragment(txtDateNotice);
		dateFragment.show(getFragmentManager(), "dateNoticePicker");
	}
	public void showTimePickerDialog(View view){
		//dateFragment = new TimePickerFragment(textTime);
		dateFragment.show(getFragmentManager(), "timepicker");
	}
		public void doPositiveClick() {
		Log.d("Datepicker", "aceptar");
		
	}
		public void doNegativeClick() {
		Log.d("Datepicker", "cancel");
		
	}

}
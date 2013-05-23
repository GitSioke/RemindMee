package com.remindme.ui;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import remind.me.R;

import com.remind.fragments.DatePickerFragment;
import com.remind.fragments.TimePickerFragment;
import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.remindme.utils.Notice;
import com.remindme.utils.RemindTask;
import com.remindme.utils.Repetition;

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
import android.widget.Toast;


public class RemindEditActivity extends RemindActivity {
    
	
	TextView txtDateNotice;
	DialogFragment dateFragment;
	private Button dateButton;
	private Button timeButton;
	private Spinner spinnerRepeat;
	private Spinner spinnerNotice;
	private EditText txtName;
	private EditText txtDesc;
	private EditText txtTag;
	private RemindTask task;
	private Integer taskID;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        
    	setContentView(R.layout.edit);
    	this.task = getIntent().getParcelableExtra("Task");
    	
    	initializeFromTask(task);
       
        spinnerRepeat.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				parent.getItemAtPosition(position);
				
			}

			public void onNothingSelected(AdapterView<?> parent) {
				return;
				
			}
		});
		
		
        spinnerNotice.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				parent.getItemAtPosition(position);
				
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
        
        Button addTask = (Button)findViewById(R.id.Edit_Button_Edit);
        addTask.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Boolean correctData=false;
        		try {
        			correctData = saveTaskChanges();        			
        		} catch (IOException e) {
        			e.printStackTrace();
        		} catch (InstantiationException e) {
        			e.printStackTrace();
        		} catch (IllegalAccessException e) {
        			e.printStackTrace();
        		} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		if (correctData){
        			startActivity(new Intent(RemindEditActivity.this, RemindPendingTaskActivity.class));		
        		}
			}
		});
        
    }
        
  
    
    	/**
    	 * Inicializa los parametros de la tarea desde el parcelable que contiene la tarea.
    	 */
		private void initializeFromTask(RemindTask task) {
		// TODO 
			taskID = task.getId();
			txtName = (EditText) findViewById(R.id.Edit_EditText_Name);
			txtName.setText(task.getName());
			dateButton = (Button) findViewById(R.id.Edit_ButtonDate);
			//TODO Revisar date
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String dateString = format.format(task.getDate());
			dateButton.setText(dateString);
			
			if (task.getDateNotice()!=null){
				dateString = format.format(task.getDateNotice());
				//TextView txtDateNotice = (TextView) findViewById(R.id.Edit_EditTextDateNotice);
				//txtDateNotice.setText(dateString);
			}
			timeButton = (Button) findViewById(R.id.Edit_ButtonTime);
			format = new SimpleDateFormat("HH:mm");
			String timeString = format.format(task.getDate());
			timeButton.setText(timeString);
			txtTag = (EditText)findViewById(R.id.Edit_EditTextTag);
			txtTag.setText(task.getTag());
			txtDesc = (EditText)findViewById(R.id.Edit_EditText_Description);
			txtDesc.setText(task.getDescription());
			
			ArrayAdapter<CharSequence> adapterNotice = ArrayAdapter.createFromResource(this, R.array.new_array_spinnerNotice, 
		        		android.R.layout.simple_selectable_list_item); 
		    adapterNotice.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		    spinnerNotice = (Spinner)findViewById(R.id.Edit_SpinnerNotice);
		    spinnerNotice.setAdapter(adapterNotice);
			Long advanceTime = task.getDate().getTime() - task.getDateNotice().getTime();
			Log.d("EDIT", "Long advance time:"+advanceTime);
			Integer ordinal = Notice.getNoticeString(advanceTime);
		    spinnerNotice.setSelection(ordinal);
			
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.new_array_spinnerRepetition, 
	        		android.R.layout.simple_selectable_list_item); 
	        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	        spinnerRepeat = (Spinner)findViewById(R.id.Edit_SpinnerRepeat);
	        spinnerRepeat.setAdapter(adapter);
			Repetition rep = Repetition.valueOf(task.getRepetition());
			spinnerRepeat.setSelection(rep.ordinal());
			
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
			
	}
		/**
		 * Guarda los cambios que se hayan producido en la base de datos
		 * @throws IOException
		 * @throws InstantiationException
		 * @throws IllegalAccessException
		 * @throws ParseException 
		 */
		private Boolean saveTaskChanges() throws IOException, InstantiationException, IllegalAccessException, ParseException {
		// TODO Meter todos los datos en la base de datos, averiguar como se guarda el dato en el spinner para sacarlo
			Boolean correctData = false;
			
			String name = txtName.getText().toString();
			//Revisar Date
			if (!name.contentEquals("") && dateButton.getText().length()>2 ){
				String timeStr = timeButton.getText().toString();
				SimpleDateFormat format = new SimpleDateFormat("HH:mm");
				Date time = format.parse(timeStr);
				
				String dateStr = dateButton.getText().toString();
				format = new SimpleDateFormat("dd/MM/yyyy");
				Date dateWithoutTime = format.parse(dateStr);
				Date date= new Date(dateWithoutTime.getTime()+time.getTime());
								
				String tag = txtTag.getText().toString();
				String description = txtDesc.getText().toString();		
				String repString = (String) spinnerRepeat.getSelectedItem();
				Repetition repetition = Repetition.getRepetition(repString, getApplicationContext());
				repString = repetition.toString();
				
				String noticeAsString = (String) spinnerNotice.getSelectedItem();
				Notice notice = Notice.getNotice(noticeAsString, getApplicationContext());
				
				Long longNotice = Notice.getAsLong(notice);
				Date noticeDate = new Date(date.getTime() - longNotice);
				
				
				if (checkDateHasSense(date, noticeDate)){
					correctData = true;
					Integer superTaskID = getIntent().getIntExtra("superTaskID", -1);
					Log.d("NEW", Integer.toString(superTaskID));
					RemindTask task = new RemindTask(taskID, name, date, noticeDate, timeStr,
							repString, description, tag, superTaskID, false);
					RemindTaskDAO taskDB = new RemindTaskSQLite(this);
					
					taskDB.updateTask(task);
					
				}else{
					Toast.makeText(this, R.string.new_toast_dateNoSense, Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, R.string.new_toast_missingData, Toast.LENGTH_SHORT).show();
			}
			return correctData;
		}
				
			
			
	/**
	 * Se encarga de mostrar el fragmento datepicker
	 * @param view
	 */
	public void showDatePickerDialog(View view){
		//TODO Cambiar los contructores y meter un bundle en los dos
		dateFragment = new DatePickerFragment();
		dateFragment.show(getFragmentManager(), "datepicker");
	}	
	
	public void showDateNoticePickerDialog(View view){
		dateFragment = new DatePickerFragment(txtDateNotice);
		dateFragment.show(getFragmentManager(), "dateNoticePicker");
	}
	public void showTimePickerDialog(View view){
		dateFragment = new TimePickerFragment();
		dateFragment.show(getFragmentManager(), "timepicker");
	}
		public void doPositiveClick() {
		Log.d("Datepicker", "aceptar");
		
	}
		public void doNegativeClick() {
		Log.d("Datepicker", "cancel");
		
	}

	private Boolean checkDateHasSense(Date date, Date noticeDate){
			return !date.before(noticeDate);
		}
}
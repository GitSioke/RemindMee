package com.remindme.ui;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.Interval;
import org.joda.time.Period;

import com.remindme.ui.R;


import com.remindme.fragments.DatePickerFragment;
import com.remindme.fragments.TimePickerFragment;
import com.remindme.fragments.DatePickerFragment.*;
import com.remindme.fragments.TimePickerFragment.OnTimeSelectedListener;
import com.remindme.sqlite.RemindNotificationDAO;
import com.remindme.sqlite.RemindNotificationSQLite;
import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.remindme.utils.NoticeNew;
import com.remindme.utils.RemindNotification;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class RemindEditActivity extends RemindActivity implements OnDateSelectedListener, OnTimeSelectedListener {
    
	
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
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        
    	setContentView(R.layout.edit);
    	setHeaderButton();
    	
    	this.task = getIntent().getParcelableExtra("Task");
    	
    	initializeFromTask(task);
       
        spinnerRepeat.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
			
				parent.getItemAtPosition(position);
				TextView selectedText = (TextView) parent.getChildAt(0);
				selectedText.setTextColor(getResources().getColor(R.color.newedit_normalText));
				selectedText.setTextSize(getResources().getDimension(R.dimen.new_textSpinner));
				
			}

			public void onNothingSelected(AdapterView<?> parent) {
				return;
				
			}
		});
		
		
        spinnerNotice.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				parent.getItemAtPosition(position);
				TextView selectedText = (TextView) parent.getChildAt(0);
				selectedText.setTextColor(getResources().getColor(R.color.newedit_normalText));
				selectedText.setTextSize(getResources().getDimension(R.dimen.new_textSpinner));
				
			}

			public void onNothingSelected(AdapterView<?> parent) {
				
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
			//TODO Hay qeu cambiarlo y buscar la forma de comparar las dos 
			//fechas para asi saber a que spinner corresponde. Joda Time
			
						
			Integer ordinal = NoticeNew.getNoticeOrdinal(task.getDateNotice(), task.getDate());
		    spinnerNotice.setSelection(ordinal-2);
			
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
				NoticeNew notice = NoticeNew.getNotice(noticeAsString, getApplicationContext());
				Date noticeDate = NoticeNew.advanceDate(date, notice);
				//Long longNotice = Notice.getAsLong(notice);
				//Date noticeDate = new Date(date.getTime() - longNotice);
				
				
				if (checkDateHasSense(date, noticeDate)){
					correctData = true;
					Integer superTaskID = getIntent().getIntExtra("superTaskID", -1);
					Log.d("NEW", Integer.toString(superTaskID));
					RemindTask newTask = new RemindTask(this.task.getId(), name, date, noticeDate, timeStr,
							repString, description, tag, superTaskID, false);
					RemindTaskDAO taskDB = new RemindTaskSQLite(this);
					
					taskDB.updateTask(newTask);
					changeNotifications(task, newTask);
					
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
		Bundle bundle = new Bundle();
		bundle.putBoolean("isEditActivity", true);
		dateFragment.setArguments(bundle);
		dateFragment.show(getFragmentManager(), "datepicker");
	}	
	
	public void showDateNoticePickerDialog(View view){
		dateFragment = new DatePickerFragment(txtDateNotice);
		dateFragment.show(getFragmentManager(), "dateNoticePicker");
	}
	public void showTimePickerDialog(View view){
		dateFragment = new TimePickerFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean("isEditActivity", true);
		dateFragment.setArguments(bundle);
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



	public void onDateSelected(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		dateButton.setText(format.format(date));
		
	}



	public void onTimeSelected(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		timeButton.setText(format.format(date));
		
	}
	
	private void changeNotifications(RemindTask task, RemindTask newTask){
		RemindNotificationDAO notifDB = new RemindNotificationSQLite(this);
		Boolean changedDate = task.getDate().compareTo(newTask.getDate())!= 0 ;
		Boolean changedDateNotice = task.getDateNotice().compareTo(newTask.getDateNotice())!=0;
		if (!task.getRepetition().contentEquals(newTask.getRepetition()) || changedDate || changedDateNotice){
			notifDB.deleteAllIdTask(task.getId());	
			RemindNotification not = new RemindNotification(null, newTask.getId(), newTask.getDate(), newTask.getDateNotice(), false, false);
			notifDB.insertNotification(not);
			//Repetition rep = Repetition.valueOf(newTask.getRepetition());
			Date newDate = Repetition.getNextDate(newTask.getDate(), newTask.getRepetition());
			Date newDateNotice = Repetition.getNextDate(newTask.getDateNotice(), newTask.getRepetition());
			if(newDate!=null && newDateNotice!=null){
				not = new RemindNotification(null, newTask.getId(), newDate, newDateNotice, false, false);
				notifDB.insertNotification(not);
			}
		}
				
	}
	
	private void setHeaderButton(){
		ImageView imageNew = (ImageView) findViewById(R.id.Header_NewImage);
        ImageView imageHome = (ImageView) findViewById(R.id.Header_HomeImage);
		imageNew.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(RemindEditActivity.this, RemindNewActivity.class));
				
			}
		});
		
		imageHome.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(RemindEditActivity.this, RemindMenuActivity.class));
				
			}
		});
		
	}
}
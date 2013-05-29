package com.remindme.ui;





import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import com.remindme.ui.R;

import com.remind.fragments.DatePickerFragment;
import com.remind.fragments.DatePickerFragment.OnDateSelectedListener;
import com.remind.fragments.TimePickerFragment;
import com.remind.fragments.TimePickerFragment.OnTimeSelectedListener;
import com.remindme.sqlite.RemindNotificationDAO;
import com.remindme.sqlite.RemindNotificationSQLite;
import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.remindme.utils.Notice;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RemindNewActivity extends RemindActivity implements OnDateSelectedListener, OnTimeSelectedListener {
    
	
	TextView txtDateNotice;
	private Long dateLong;
	private Long dateNoticeLong;
	private long time;
	DialogFragment dateFragment;
	private Spinner repeatSpinner;
	private Spinner noticeSpinner;
	private Button dateButton;
	private Button timeButton;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_edit);
        setDateButton((Button) findViewById(R.id.New_ButtonDate));
        txtDateNotice = (TextView)findViewById(R.id.New_TextViewDateNoticeShow);
        setTimeButton((Button) findViewById(R.id.New_ButtonTime));
        //Spinner 1
        repeatSpinner = (Spinner)findViewById(R.id.New_SpinnerRepeat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.new_array_spinnerRepetition, 
        		android.R.layout.simple_selectable_list_item); 
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        repeatSpinner.setAdapter(adapter);
        //Spinner 2
        noticeSpinner = (Spinner)findViewById(R.id.New_SpinnerNotice);
        ArrayAdapter<CharSequence> adapterNotice = ArrayAdapter.createFromResource(this, R.array.new_array_spinnerNotice, 
        		android.R.layout.simple_selectable_list_item); 
        adapterNotice.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        noticeSpinner.setAdapter(adapterNotice);
        
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
        
		repeatSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				parent.getItemAtPosition(position);
				
			}

			public void onNothingSelected(AdapterView<?> parent) {
				return;
				
			}
		});
		
		
		noticeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				parent.getItemAtPosition(position);
				
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
        
        Button addTask = (Button)findViewById(R.id.New_Button_Add);
        addTask.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Boolean correctData=false;
        		try {
        			correctData = initTaskNameEntry();        			
        		} catch (IOException e) {
        			e.printStackTrace();
        		} catch (InstantiationException e) {
        			e.printStackTrace();
        		} catch (IllegalAccessException e) {
        			e.printStackTrace();
        		}
        		if (correctData){
        			startActivity(new Intent(RemindNewActivity.this, RemindPendingTaskActivity.class));		
        		}
			}
		});
        
    }

	private Boolean initTaskNameEntry() throws IOException, InstantiationException, IllegalAccessException {
		// TODO Meter todos los datos en la base de datos, averiguar como se guarda el dato en el spinner para sacarlo
		Boolean correctData = false;
		EditText taskName = (EditText)findViewById(R.id.EditText_Name);
		String name = taskName.getText().toString();
		//Revisar Date
		if (!name.contentEquals("") && dateButton.getText().length()>2 ){
			Date timeAsDate = new Date(this.time);
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			String timeString = format.format(timeAsDate);
			Date date= new Date(this.dateLong+this.time);
			
			Log.d("NEW_INIT", dateLong.toString());
			Log.d("NEW_INIT", Long.toString(date.getTime()));
			
			EditText taskTag = (EditText)findViewById(R.id.New_EditTextTag);
			String tag = taskTag.getText().toString();
			
			EditText txtDesc = (EditText)findViewById(R.id.New_EditText_Description);		
			String description = txtDesc.getText().toString();		
			String repString = (String) repeatSpinner.getSelectedItem();
			Repetition repetition = Repetition.getRepetition(repString, getApplicationContext());
			repString = repetition.toString();
			
			String noticeAsString = (String) noticeSpinner.getSelectedItem();
			Notice notice = Notice.getNotice(noticeAsString, getApplicationContext());
			
			Long longNotice = Notice.getAsLong(notice);
			Date noticeDate = new Date(date.getTime() - longNotice);
			
			
			if (checkDateHasSense(date, noticeDate)){
				correctData = true;
				Integer superTaskID = getIntent().getIntExtra("superTaskID", -1);
				Log.d("NEW", Integer.toString(superTaskID));
				RemindTask task = new RemindTask(null, name, date, noticeDate, timeString,
						repString, description, tag, superTaskID, false);
				RemindTaskDAO taskDB = new RemindTaskSQLite(this);
				Log.d("NEW", "Create task: "+ task.getDate().toString());
				taskDB.insertTask(task);
				createRemindNotification(task);
			}else{
				Toast.makeText(this, R.string.new_toast_dateNoSense, Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(this, R.string.new_toast_missingData, Toast.LENGTH_SHORT).show();
		}
		return correctData;
	}
	
	private void createRemindNotification(RemindTask task) {
		RemindNotificationDAO dbNot = new RemindNotificationSQLite(this);
		Boolean ready = false;
		Boolean done = false;
		Date date = task.getDate();
		Date dateNotice = task.getDateNotice();
		
		RemindNotification not = new RemindNotification(null, task.getId(), date, dateNotice, ready, done);
		dbNot.insertNotification(not);
		Log.d("NEW", "Create not1: "+not.getDate().toString());
		Log.d("NEW", "Create not1: "+not.getDate().toString());
		Log.d("NEW", "Create not1: "+not.getDelay().toString());
		Repetition rep = Repetition.valueOf(task.getRepetition());
				
		if(rep.compareTo(Repetition.SINGLE) > 0){
			date = Repetition.getNextDate(date, rep);
			dateNotice = Repetition.getNextDate(dateNotice, rep);
			not = new RemindNotification(null, task.getId(), date, dateNotice, ready, done);
			dbNot.insertNotification(not);
		}
		
		Log.d("NEW", "Create not2: "+not.getDate().toString());
		Log.d("NEW", "Create not2: "+not.getDelay().toString());
	}

	/**TODO Pendiente de revision. Intentar pasar por Bundle la actividad o los datos necesarios
	 * Se encarga de mostrar el fragmento datepicker
	 * @param view
	 */
	public void showDatePickerDialog(View view){
		dateFragment = new DatePickerFragment(this);
		Bundle bundle = new Bundle();
		bundle.putBoolean("islimitdate", true);
		dateFragment.setArguments(bundle);
		dateFragment.show(getFragmentManager(), "datepicker");
	}
	
	public void showDateNoticePickerDialog(View view){
		dateFragment = new DatePickerFragment(this);
		Bundle bundle = new Bundle();
		bundle.putBoolean("islimitdate", false);
		dateFragment.setArguments(bundle);
		dateFragment.show(getFragmentManager(), "dateNoticePicker");
	}
	public void showTimePickerDialog(View view){
		dateFragment = new TimePickerFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean("isNewActivity", true);
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


	public void setDateLong(Long dateLong) {
		this.dateLong = dateLong;
	}

	
	public void setDateNoticeLong(Long dateNoticeLong) {
		this.dateNoticeLong = dateNoticeLong;
	}

	
	public void setDateButton(Button dateButton) {
		this.dateButton = dateButton;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public void setTimeButton(Button timeButton) {
		this.timeButton = timeButton;
	}

	public Button getTimeButton() {
		return this.timeButton;
	}

	public Button getDateButton() {
		return this.dateButton;
	}

	public void onDateSelected(Date date) {
		//TODO
	}

	public void onTimeSelected(Date date) {
		// TODO 
		this.time = date.getTime();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		timeButton.setText(format.format(date));
	}

}
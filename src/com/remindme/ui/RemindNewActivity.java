package com.remindme.ui;





import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import com.remindme.ui.R;

import com.remindme.db.NotificationDAO;
import com.remindme.db.NotificationSQLite;
import com.remindme.db.TaskDAO;
import com.remindme.db.TaskSQLite;
import com.remindme.fragments.DatePickerFragment;
import com.remindme.fragments.TimePickerFragment;
import com.remindme.fragments.DatePickerFragment.OnDateSelectedListener;
import com.remindme.fragments.TimePickerFragment.OnTimeSelectedListener;
import com.remindme.utils.NoticeNew;
import com.remindme.utils.Event;
import com.remindme.utils.RemindTask;
import com.remindme.utils.Repetition;


import android.app.DialogFragment;

import android.content.Intent;
import android.content.res.Resources;

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

public class RemindNewActivity extends RemindActivity implements OnDateSelectedListener, OnTimeSelectedListener {
    
	
	TextView txtDateNotice;
	private Long dateLong;
	private Long dateNoticeLong;
	private long time;
	DialogFragment dateFragment;
	private SpinnerRepeat repeatSpinner;
	private Spinner noticeSpinner;
	private TextView datePicked;
	private TextView timePicked;
	private Button dateButton;
	private Button timeButton;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);
        setHeaderButton();
        
        txtDateNotice = (TextView)findViewById(R.id.New_TxtDatePicked);
        //Spinner 1
        repeatSpinner = (SpinnerRepeat)findViewById(R.id.New_SpinnerRepeat);
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
		
		
		noticeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

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
        			startActivity(new Intent(RemindNewActivity.this, RemindAllTaskActivity.class));		
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
		if (!name.contentEquals("") && datePicked.getText().length()>2 ){
			Date timeAsDate = new Date(this.time);
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			Calendar cal = Calendar.getInstance();
			cal.setTime(timeAsDate);
			int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
			int minutes = cal.get(Calendar.MINUTE);
			String timeString = format.format(timeAsDate);
			Date date= new Date(this.dateLong);
			cal.setTime(date);
			cal.roll(Calendar.HOUR_OF_DAY, hourOfDay);
			cal.roll(Calendar.MINUTE, minutes);
			date = cal.getTime();
			
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
			NoticeNew notice = NoticeNew.getNotice(noticeAsString, getApplicationContext());
			Date noticeDate = NoticeNew.advanceDate(date, notice);
						
			
			if (checkDateHasSense(date, noticeDate)){
				correctData = true;
				Boolean notEvent = false;
				Integer superTaskID = getIntent().getIntExtra("superTaskID", -1);
				if (superTaskID!= -1){
					notEvent = getIntent().getBooleanExtra("notEvent", false);
				}
				Log.d("NEW", Integer.toString(superTaskID));
				RemindTask task = new RemindTask(null, name, date, noticeDate, timeString,
						repString, description, tag, superTaskID, false);
				TaskDAO taskDB = new TaskSQLite(this);
				NotificationDAO notifDB = new NotificationSQLite(this);
				
				Log.d("NEW", "Create task: "+ task.getDate().toString());
				if(!notEvent){
					task.setSuperTask(-1);
					notifDB.insert(new Event(null, task.getId(), task.getDate(), task.getDateNotice(), false, task.isCompleted(), superTaskID));
				}
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
		NotificationDAO dbNot = new NotificationSQLite(this);
		Boolean ready = false;
		Boolean done = false;
		Date date = task.getDate();
		Date dateNotice = task.getDateNotice();
		
		Event not = new Event(null, task.getId(), date, dateNotice, ready, done, null);
		dbNot.insert(not);
		Log.d("NEW", "Create not1: "+not.getDate().toString());
		Log.d("NEW", "Create not1: "+not.getDate().toString());
		Log.d("NEW", "Create not1: "+not.getNotifyDate().toString());
		
		date = Repetition.getNextDate(date, task.getRepetition());
		dateNotice = Repetition.getNextDate(dateNotice, task.getRepetition());
		if(date !=null && dateNotice!=null)
		{
			not = new Event(null, task.getId(), date, dateNotice, ready, done, null);
			dbNot.insert(not);
		}
			
		
		
		Log.d("NEW", "Create not2: "+not.getDate().toString());
		Log.d("NEW", "Create not2: "+not.getNotifyDate().toString());
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



	public void setTime(Long time) {
		this.time = time;
	}

	

	public void onDateSelected(Date date) {
		//TODO
		Log.d("NEW", "OnDateSelected");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = format.format(date);
		this.dateButton = (Button) findViewById(R.id.New_ButtonDate);
		this.dateButton.setHint("");
		this.dateLong = date.getTime();
        this.datePicked = (TextView) findViewById(R.id.New_TxtDatePicked);
		this.datePicked.setText(dateString);
		}

	public void onTimeSelected(Date date) {
		// TODO 
		this.time = date.getTime();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		this.timeButton = (Button) findViewById(R.id.New_ButtonTime);
		this.timeButton.setHint("");
		this.timePicked = (TextView) findViewById(R.id.New_TxtTimePicked);
		this.timePicked.setText(format.format(this.time));
		//timeButton.setText(format.format(date));
	}
	
	private void setHeaderButton(){
		ImageView imageNew = (ImageView) findViewById(R.id.Header_NewImage);
        ImageView imageHome = (ImageView) findViewById(R.id.Header_HomeImage);
		imageNew.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(RemindNewActivity.this, RemindNewActivity.class));
				
			}
		});
		
		imageHome.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(RemindNewActivity.this, RemindMenuActivity.class));
				
			}
		});
		
	}
}
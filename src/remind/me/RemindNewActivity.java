package remind.me;





import java.io.IOException;
import java.util.Date;

import com.remindme.sqlite.RemindTaskDAO;
import com.remindme.sqlite.RemindTaskSQLite;
import com.utils.Notice;


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

public class RemindNewActivity extends RemindActivity {
    
	TextView textDate;
	TextView txtDateNotice;
	Long dateLong;
	Long dateNoticeLong;
	Long time;
	TextView textTime;
	DialogFragment dateFragment;
	private Spinner repeatSpinner;
	private Spinner noticeSpinner;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_edit);
        textDate = (TextView) findViewById(R.id.New_TextViewDateShow);
        txtDateNotice = (TextView)findViewById(R.id.New_TextViewDateNoticeShow);
        textTime = (TextView) findViewById(R.id.New_TextViewTimeShow);
        //Spinner 1
        repeatSpinner = (Spinner)findViewById(R.id.New_SpinnerRepeat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repeat_array, 
        		android.R.layout.simple_selectable_list_item); 
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        repeatSpinner.setAdapter(adapter);
        //Spinner 2
        noticeSpinner = (Spinner)findViewById(R.id.New_SpinnerNotice);
        ArrayAdapter<CharSequence> adapterNotice = ArrayAdapter.createFromResource(this, R.array.new_spinnerNotice, 
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
		String timeAsString = textTime.getText().toString();
		//Revisar Date
		if (!name.contentEquals("") && textDate.getText().length()>2 /*&& txtDateNotice.getText().length()>2*/){
			Date date= new Date(this.dateLong+this.time);
			
			EditText taskTag = (EditText)findViewById(R.id.New_EditTextTag);
			String tag = taskTag.getText().toString();
			
			String repetition = (String) repeatSpinner.getSelectedItem();
				
			String noticeAsString = (String) noticeSpinner.getSelectedItem();
			String[] array = getResources().getStringArray(R.array.new_spinnerNotice);
			for(String string: array){
				if (noticeAsString.contentEquals(string));
					
			}
			Notice notice =Notice.getNotice(noticeAsString, getApplicationContext());
			
			Long longNotice = Notice.getAsLong(notice);
			Date noticeDate = new Date(dateLong - longNotice);
			Log.d("NEW", Long.toString(dateLong));
			Log.d("NEW", Long.toString(time));
			Log.d("NEW", Long.toString(longNotice));
			if (checkDateHasSense(date, noticeDate)){
				correctData = true;
				Integer superTaskID = getIntent().getIntExtra("superTaskID", -1);
				Log.d("NEW", Integer.toString(superTaskID));
				RemindTask task = new RemindTask(null, name, date, noticeDate, timeAsString, repetition, tag, superTaskID, false);
				RemindTaskDAO taskDB = new RemindTaskSQLite(this);
				
				taskDB.insertNewTask(task);	
			}else{
				Toast.makeText(this, R.string.new_toast_dateNoSense, Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(this, R.string.new_toast_missingData, Toast.LENGTH_SHORT).show();
		}
		return correctData;
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
		dateFragment = new TimePickerFragment(textTime);
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
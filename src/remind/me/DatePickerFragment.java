package remind.me;





import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener{

	public TextView dateTextView;
	public CalendarView calendarView;
	private ContentValues dateValues;
	private Long dateLong;
	private RemindNewActivity activity;
	private View viewPicker;
	
	public DatePickerFragment(TextView text) {
		this.dateTextView = text;
		
	}
	
	public DatePickerFragment(RemindNewActivity activity) {
		this.activity = activity;
	}

	/** TODO
	 * Operacion con los datos recogidos en el picker
	 */
	public void onDateSet(DatePicker view, int year, int month, int day) {
		dateValues = new ContentValues();
		dateValues.put("Year", year);
		dateValues.put("Month", month);
		dateValues.put("Day", day);
	
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(year, month, day);
		CalendarView calendarView =(CalendarView)this.viewPicker.findViewById(R.id.DatePicker_Calendar);
        dateLong = calendarView.getDate();
		
		Date date = new Date(dateLong);
		
		this.activity.textDate.setText(date.toString());
		
	}
	/** 
	 * Muestra el Dialog 
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstance){
		
		
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		this.viewPicker = inflater.inflate(R.layout.date_picker_fragment, null);
		final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.setView(this.viewPicker);
        
        return dialog;
	}
	

	public ContentValues getDateValues(){
		return this.dateValues;
	}
	
	public Long getDateAsLong(){
		return this.dateLong;
	}
}

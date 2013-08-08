package com.remindme.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.remindme.fragments.DatePickerFragment.OnDateSelectedListener;
import com.remindme.ui.RemindNewActivity;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
	
	ContentValues timeValues;
	TextView timeText;
	private OnTimeSelectedListener listener;
	
	/**
	 * Operaciones con la hora seleccionada
	 */
	public interface OnTimeSelectedListener {
        public void onTimeSelected(Date date);
    }
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnTimeSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTimeSelectedListener");
        }
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		
		
        Date date = cal.getTime();
		Log.d("TIMEPICKER", Long.toString(date.getTime()));
		
		Log.d("TIMEPICKER", date.toString());
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		String dateString = format.format(date);
		
		listener.onTimeSelected(date);
		
		
	}
	
	/** 
	 * Muestra el Dialog 
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstance){
		final Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        
        return new TimePickerDialog(getActivity(), this, hourOfDay, minute, false);
	}
	

}

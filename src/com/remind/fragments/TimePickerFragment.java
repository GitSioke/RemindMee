package com.remind.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.remindme.ui.RemindNewActivity;


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
	/**
	 * Operaciones con la hora seleccionada
	 */
	
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
		
		RemindNewActivity activity  = (RemindNewActivity)getActivity();
		//TODO controlar que no se metan varias veces un tiempo o fecha
		activity.setTime(date.getTime());
		
		activity.getTimeButton().setText(dateString);
		
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

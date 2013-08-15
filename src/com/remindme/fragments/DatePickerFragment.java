package com.remindme.fragments;





import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import com.remindme.ui.R;
import com.remindme.ui.RemindNewActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;

import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

	public TextView dateTextView;
	public CalendarView calendarView;
	private ContentValues dateValues;
	private Long dateLong;
	private RemindNewActivity activity;
	private View viewPicker;
	private OnDateSelectedListener listener;
	
	public DatePickerFragment(TextView text) {
		this.dateTextView = text;
		
	}
	public DatePickerFragment() {
		super();
	}
	
	public DatePickerFragment(RemindNewActivity activity) {
		this.activity = activity;
	}

	
	public interface OnDateSelectedListener {
        public void onDateSelected(Date date);
    }
	
	
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnDateSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDateSelectedListener");
        }
	}
	
	/** TODO gdg
	 * Operacion con los datos recogidos en el picker
	 */

	public void onDateSet(DatePicker view, int year, int month, int day) {
				
		CalendarView calView =(CalendarView)this.viewPicker.findViewById(R.id.DatePicker_Calendar);
		
        dateLong = calView.getDate();
        Date date = new Date(dateLong);
        this.listener.onDateSelected(date);        	
		
		
		/**Old code
		 * if (getArguments().getBoolean("islimitdate")){
			Log.d("PASA", "ISLIMITDATE");
			activity.setDateLong(this.dateLong);
			this.activity.getDateButton().setText(dateString);
		
		}else if(getArguments().getBoolean("pickDay")){
			Log.d("PASA", "POR AQUI");
			RemindMenuActivity activity = (RemindMenuActivity)getActivity();
			Intent intent = new Intent(activity, RemindDayActivity.class);
    		intent.putExtra("date",this.dateLong);
    		startActivity(intent);
	
		}else if(getArguments().getBoolean("isEditActivity")){
			RemindEditActivity activity = (RemindEditActivity)getActivity();
			listener.onDateSelected(date);
		}else{
			Log.d("PASA", "DATENOTICE");
			//TODO Revisar el pick de date notice
			activity.setDateNoticeLong(this.dateLong);
			//activity.txtDateNotice.setText(dateString);
		}*/

		
	}
	/** 
	 * Muestra el Dialog 
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstance){
		
		
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		this.viewPicker = inflater.inflate(R.layout.date_picker_fragment, null);
		final Calendar c = Calendar.getInstance();
		c.clear();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_YEAR);
        try
        {
            CalendarView cv = (CalendarView) this.viewPicker.findViewById(R.id.DatePicker_Calendar);
            Class<?> cvClass = cv.getClass();
            Field field = cvClass.getDeclaredField("mMonthName");
            field.setAccessible(true);

            try
            {
                TextView tv = (TextView) field.get(cv);
                tv.setTextColor(getResources().getColor(R.color.hintText2));
            } 
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
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

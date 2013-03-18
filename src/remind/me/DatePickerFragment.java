package remind.me;



import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

	public TextView dateTextView;
	public CalendarView calendarView;
	private ContentValues dateValues;
	
	
	public DatePickerFragment(TextView text) {
		this.dateTextView = text;
		
	}
	/** TODO
	 * Operacion con los datos recogidos en el picker
	 */
	public void onDateSet(DatePicker view, int year, int month, int day) {
		dateValues = new ContentValues();
		dateValues.put("Year", year);
		dateValues.put("Month", month);
		dateValues.put("Day", day);
		
		dateTextView.setText(String.valueOf(day)  + "/" + String.valueOf(month + 1 )+ "/" + String.valueOf(year));
		
		
	}
	/** 
	 * Muestra el Dialog 
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstance){
		/*LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View v = inflater.inflate(R.layout.date_picker_fragment, null);
		
		return new AlertDialog.Builder(getActivity())
        .setIcon(R.drawable.ic_launcher)
        .setView(v)
        .setPositiveButton(R.string.new_date,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ((RemindNewActivity)getActivity()).doPositiveClick();
                }
            }
        )
        .setNegativeButton(R.string.new_date_hint,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ((RemindNewActivity)getActivity()).doNegativeClick();
                }
            }
        )
        .create();
		*/
			
			
		
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View view = inflater.inflate(R.layout.date_picker_fragment, null);
		final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.setView(view);
        return dialog;
	}
	

	public ContentValues getDateValues(){
		return this.dateValues;
	}
}

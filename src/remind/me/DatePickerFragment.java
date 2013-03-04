package remind.me;



import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

	public TextView activity_edittext;
	private ContentValues dateValues;
	public DatePickerFragment(TextView edittext) {
		this.activity_edittext = edittext;
	}
	/** TODO
	 * Operacion con los datos recogidos en el picker
	 */
	public void onDateSet(DatePicker view, int year, int month, int day) {
		dateValues = new ContentValues();
		dateValues.put("Year", year);
		dateValues.put("Month", month);
		dateValues.put("Day", day);
		activity_edittext.setText(String.valueOf(day)  + "/" + String.valueOf(month + 1 )+ "/" + String.valueOf(year));
		
		
	}
	/** 
	 * Muestra el Dialog 
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstance){
		final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);

	}

	
	public ContentValues getDateValues(){
		return this.dateValues;
	}
}

package remind.me;



import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

	
	/** TODO
	 * Operacion con los datos recogidos en el picker
	 */
	public void onDateSet(DatePicker view, int year, int month, int day) {
		ContentValues values = new ContentValues();
		values.put("Year", year);
		values.put("Month", month);
		values.put("Day", day);
		
		
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

	

}

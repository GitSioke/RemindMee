package remind.me;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
	
	ContentValues timeValues;
	TextView timeText;
	/**
	 * Operaciones con la hora seleccionada
	 */
	public TimePickerFragment(TextView txtview){
		this.timeText = txtview;
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		timeValues = new ContentValues();
		timeValues.put("Hour", hourOfDay);
		timeValues.put("Minute", minute);
		
		timeText.setText(String.valueOf(hourOfDay)  + ":" + String.valueOf(minute));
		
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

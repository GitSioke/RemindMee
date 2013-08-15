package com.remindme.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class SpinnerRepeat extends Spinner implements OnItemSelectedListener{

	public SpinnerRepeat(Context context,AttributeSet attb)
	{
		super(context,attb);
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) 
	{
		parent.getItemAtPosition(pos);
		TextView selectedText = (TextView) parent.getChildAt(0);
		selectedText.setTextColor(getResources().getColor(R.color.newedit_normalText));
		selectedText.setTextSize(getResources().getDimension(R.dimen.new_textSpinner));
		
	}

	public void onNothingSelected(AdapterView<?> parent) {
				
	}

}

package com.remindme.dialogs;

import com.remindme.ui.R;
import com.remindme.ui.R.layout;
import com.remindme.ui.R.string;

import com.remindme.ui.RemindEditActivity;
import com.remindme.ui.RemindNewActivity;
import com.remindme.utils.RemindTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;


public class RemindAlertDialog extends DialogFragment {
	
	private RemindTask task;
	private Boolean isFatherTask;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstance){
		
		this.task = getArguments().getParcelable("Task");
		this.isFatherTask = getArguments().getBoolean("notEvent");
		Log.d("Task", task.getId().toString());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		builder.setView(inflater.inflate(R.layout.dialog_edit, null))
		.setPositiveButton(R.string.dialog_edit, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				
				Intent intent = new Intent(getActivity(), RemindEditActivity.class);
				intent.putExtra("Task", task);
				intent.putExtra("notEvent", isFatherTask);
				startActivity(intent);
			}
		})
			.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					RemindAlertDialog.this.getDialog().cancel();					
				}
			})
		;
		
		return builder.create();
		
	}
	
	
	
}

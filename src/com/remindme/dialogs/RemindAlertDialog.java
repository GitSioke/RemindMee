package com.remindme.dialogs;

import remind.me.R;
import remind.me.R.layout;
import remind.me.R.string;
import remind.me.RemindEditActivity;
import remind.me.RemindNewActivity;

import com.utils.RemindTask;

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
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstance){
		
		this.task = getArguments().getParcelable("Task");
		Log.d("Task", task.getId().toString());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		builder.setView(inflater.inflate(R.layout.dialog_edit, null))
		.setPositiveButton(R.string.dialog_edit, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				
				Intent intent = new Intent(getActivity(), RemindEditActivity.class);
				intent.putExtra("Task", task);
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

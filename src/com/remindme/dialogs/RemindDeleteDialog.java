package com.remindme.dialogs;

import com.remindme.ui.R;
import com.remindme.ui.R.layout;
import com.remindme.ui.R.string;

import com.remindme.ui.RemindMenuActivity;
import com.remindme.utils.RemindTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

public class RemindDeleteDialog extends DialogFragment {
	
	private RemindTask task;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstance){
		
		this.task = getArguments().getParcelable("Task");
		Log.d("Task", task.getId().toString());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		builder.setView(inflater.inflate(R.layout.dialog_delete, null))
		.setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				//TODO Eliminar tarea y subtareas y enviar al menu
				Intent intent = new Intent(getActivity(), RemindMenuActivity.class);
				intent.putExtra("Task", task);
				intent.putExtra("DeleteAll", true);
				startActivity(intent);
			}
		})
			.setNeutralButton(R.string.dialog_deleteTask, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Eliminar solo la tarea y enviar al menu
					Intent intent = new Intent(getActivity(), RemindMenuActivity.class);
					intent.putExtra("Task", task);
					intent.putExtra("DeleteTask", true);
					startActivity(intent);
					
				}
			})
		
			.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					RemindDeleteDialog.this.getDialog().cancel();					
				}
			})
		;
		
		return builder.create();
		
	}
	
	
	
}


package remind.me;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;


public class RemindAlertDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstance){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		builder.setView(inflater.inflate(R.layout.dialog_edit, null))
		.setPositiveButton(R.string.dialog_edit, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startActivity(new Intent(null, RemindNewActivity.class));
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

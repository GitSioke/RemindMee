package remind.me;




import java.util.Date;

import com.remindme.sqlite.RemindTaskSQLite;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RemindMenuActivity extends RemindActivity {
    /** Called when the activity is first created. */
	
	public TextView text;
	private DialogFragment dateFragment;
	private Long dateLong;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("MENU", "Entrando en onCreate");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        //Elimina las tareas
        if (removedPendingTasks()){
        	RemindTaskDAO taskDB = new RemindTaskSQLite(this);        	
        	RemindTask task = getIntent().getParcelableExtra("Task");
        	taskDB.deleteTask(task.getId());
        	Toast toast = Toast.makeText(this, R.string.menu_toast_delete, Toast.LENGTH_SHORT);
        	
        	if (getIntent().getBooleanExtra("DeleteAll", false)){
        		taskDB.deleteSubtask(task.getId());
        		toast.setText(R.string.menu_toast_deleteAll);
        	}
        	toast.show();
        }
        //
        
        ImageView imageAll = (ImageView) findViewById(R.id.Menu_ImageView_All);
        ImageView imageTags = (ImageView) findViewById(R.id.Menu_ImageView_Tags);
        ImageView imageToday = (ImageView) findViewById(R.id.Menu_ImageView_Today);
        ImageView imageNew = (ImageView) findViewById(R.id.Menu_ImageView_New);
        ImageView imageClock = (ImageView) findViewById(R.id.Menu_ImageView_Clock);
        
        imageAll.setOnClickListener(new View.OnClickListener() {
			
        	public void onClick(View v) {
        		Log.d("MENU", "Entrando en onStartActivity");
        		startActivity(new Intent(RemindMenuActivity.this, RemindPendingTaskActivity.class));					
				
				
			}
		});
        
        imageTags.setOnClickListener(new View.OnClickListener() {
			
        	public void onClick(View v) {
				startActivity(new Intent(RemindMenuActivity.this, RemindTagsActivity.class));
			}
		});
        
        imageNew.setOnClickListener(new View.OnClickListener() {
			
        	public void onClick(View v) {
				startActivity(new Intent(RemindMenuActivity.this, RemindNewActivity.class));
			}
		});

        imageToday.setOnClickListener(new View.OnClickListener() {
        	//TODO Cambio provisional de Today por CompletedTask
        	public void onClick(View v) {
        		startActivity(new Intent(RemindMenuActivity.this, RemindCompletedTaskActivity.class));
        	}
        });
        
        imageClock.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				dateFragment = new DatePickerFragment();
				Bundle bundle = new Bundle();
				bundle.putBoolean("pickDay", true);
				dateFragment.setArguments(bundle);
				dateFragment.show(getFragmentManager(), "dateNoticePicker");		
				
			}
		});
    }
    
    
    /**
     * Elimina las tareas que tenga que eliminar si ha recibido orden desde RemindTaskActivity
     */
	private Boolean removedPendingTasks() {
		// TODO 
		return getIntent().getBooleanExtra("DeleteAll", false) || getIntent().getBooleanExtra("DeleteTask", false);
		
	}
	
	public void setDateLong(Long date){
		this.dateLong = date;
	}
}
package remind.me;





import java.io.IOException;


import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RemindNewActivity extends RemindActivity {
    
	TextView edittext;
	DialogFragment dateFragment;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_edit);
        edittext = (TextView) findViewById(R.id.TextView_DateShow);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        
        
        Button addTask = (Button)findViewById(R.id.New_Button_Add);
        addTask.setOnClickListener(new View.OnClickListener() {
			
		
        	public void onClick(View v) {
				// TODO Auto-generated method stub
        		try {
        			initTaskNameEntry();
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (InstantiationException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (IllegalAccessException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
				startActivity(new Intent(RemindNewActivity.this, RemindAllActivity.class));				
			}
		});
        
    }

	private void initTaskNameEntry() throws IOException, InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		EditText taskName = (EditText)findViewById(R.id.EditText_Name);
		String name = taskName.getText().toString();
		/*TasksSQLiteHelper tsq = new TasksSQLiteHelper(context, name, factory, version)
		TasksSQLiteHelper taskdb = new TasksSQLiteHelper(this, "DBTasks", null, 1);
		SQLiteDatabase db = taskdb.getWritableDatabase();
		if(db!=null){
			//int _ID = hashCode();
			String name = taskName.getText().toString();*/
			
			
		
		
	}
	/**
	 * Se encarga de mostrar el fragmento datepicker
	 * @param view
	 */
	public void showDatePickerDialog(View view){
		dateFragment = new DatePickerFragment(edittext);
		dateFragment.show(getFragmentManager(), "datepicker");
	}
}
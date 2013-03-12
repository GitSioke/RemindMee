package remind.me;

import java.util.ArrayList;

import com.remindme.sqlite.HandlerSQLite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RemindTagTaskActivity extends RemindActivity{
	
	ListView taskListView;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all);
			
		String tag = getIntent().getStringExtra("tag");
		HandlerSQLite db = new HandlerSQLite(this);
		db.open();
		ArrayList<RemindTask> taskList = db.getTaskWithTag(tag);
		displayTaskWithTextView(taskList);
		
	}
	
	private void displayTaskWithTextView(final ArrayList<RemindTask> taskList){
		
		taskListView = (ListView)findViewById(R.id.All_ListViewTask);
		taskListView.setAdapter(new RemindTaskAdapter(this, R.layout.list_item_task, taskList));
		
		OnItemClickListener listener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO 
				Intent intent = new Intent(RemindTagTaskActivity.this, RemindTaskActivity.class);
				RemindTask task = taskList.get(position);
				intent.putExtra("task", task);
				startActivity(intent);
				
			}
		};
		taskListView.setOnItemClickListener(listener);		
		
	}
	
}

package com.remindme.ui;

import java.util.ArrayList;

import com.remindme.ui.R;

import com.remindme.db.TaskNotifDAO;
import com.remindme.db.TaskNotifSQLite;
import com.remindme.db.TaskSQLite;
import com.remindme.utils.RemindTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class RemindTagTaskActivity extends RemindActivity{
	
	ListView taskListView;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		String tag = getIntent().getStringExtra("tag");
		
		setContentView(R.layout.all);
		TextView txtHeader = (TextView)findViewById(R.id.All_HeaderTxtView);
		txtHeader.setText(tag);
		TaskNotifDAO dbJoin = new TaskNotifSQLite(this);
		ArrayList<RemindTask> taskList = dbJoin.getTaskWithTag(tag);
		displayTaskWithTextView(taskList);
		
	}
	
	private void displayTaskWithTextView(final ArrayList<RemindTask> taskList){
		
		taskListView = (ListView)findViewById(R.id.All_ListViewTask);
		taskListView.setAdapter(new RemindTaskAdapter(this, R.layout.list_item_task, taskList, true));
		
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

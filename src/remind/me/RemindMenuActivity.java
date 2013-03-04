package remind.me;




import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class RemindMenuActivity extends RemindActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("MENU", "Entrando en onCreate");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        ImageView imageAll = (ImageView) findViewById(R.id.Menu_ImageView_All);
        ImageView imageTags = (ImageView) findViewById(R.id.Menu_ImageView_Tags);
        ImageView imageToday = (ImageView) findViewById(R.id.Menu_ImageView_Today);
        ImageView imageNew = (ImageView) findViewById(R.id.Menu_ImageView_New);
        
        
        imageAll.setOnClickListener(new View.OnClickListener() {
			
        	public void onClick(View v) {
        		Log.d("MENU", "Entrando en onStartActivity");
        		startActivity(new Intent(RemindMenuActivity.this, RemindAllActivity.class));					
				
				
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
	
        	public void onClick(View v) {
        		startActivity(new Intent(RemindMenuActivity.this, RemindTodayActivity.class));
        	}
        });
    }
}
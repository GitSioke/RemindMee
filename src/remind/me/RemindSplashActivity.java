package remind.me;




import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RemindSplashActivity extends RemindActivity {
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		startFadeIn();
			
	}
	
	private void startFadeIn(){
		TextView title = (TextView) findViewById(R.id.Splash_TextView01);
		Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.splash_fade);
		title.startAnimation(fadeIn);
		
		fadeIn.setAnimationListener(new AnimationListener(){
			/**
			 * TODO Retrasar el inicio de MenuActivity
			 */
			public void onAnimationEnd(Animation animation) {
				startActivity(new Intent(RemindSplashActivity.this, RemindMenuActivity.class));
				RemindSplashActivity.this.finish();
			}

			public void onAnimationRepeat(Animation animation) {
								
			}

			public void onAnimationStart(Animation animation) {
				
			}
			
		});
	}
	
	protected void onPause(){
		super.onPause();
		TextView logo1 = (TextView) findViewById(R.id.Splash_TextView01);
		logo1.clearAnimation();
				
	}
	protected void onResume(){
		super.onResume();
		startFadeIn();
	}
}

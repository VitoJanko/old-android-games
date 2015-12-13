package com.igrargti;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Run extends Activity {

	/** Our own OpenGL View overridden */
	private Lesson10 lesson10;
	private Music ambientMuzika;
	
	Vibrator vibrator;
	LinearLayout ll;
	TextView hud;
	
	//MENU
	private static final int MENU_EXIT = 1;
	private static final int MENU_RESTART = 2;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		ll = new LinearLayout(this);
		hud = new TextView(this);
		ambientMuzika = new Music(this);
		
		

		lesson10 = new Lesson10(this,this);
		setContentView(lesson10);
		
		//HUD  
		  ll.setGravity(Gravity.RIGHT);
		  this.addContentView(ll, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		  ll.setVisibility(LinearLayout.VISIBLE);
		  ll.setOrientation(LinearLayout.VERTICAL);
		  hud.setText("Save points left: 20");  
		  hud.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		  ((LinearLayout) ll).addView(hud);
	}
	
	public void setHudText(String text){
		hud.setText(text);
	}


    public void vibrate(int length){
    	vibrator.vibrate(length);
    }
	

	@Override
	protected void onResume() {
		super.onResume();
		lesson10.onResume();
		lesson10.reset();
		ambientMuzika.play();
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	}


	@Override
	protected void onPause() {
		super.onPause();
		lesson10.onPause();
		lesson10.reset();
		ambientMuzika.pause();
		MiscSound.release();
    	vibrator.cancel();
	}
	
	public void koncaj(){
		this.finish();
	}
}
package jb.batteryindicator.lite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main extends Activity {
	   /** Messenger for communicating with the service. */

    
    
    /** Flag indicating whether we have called bind on the service. */

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startService(new Intent(this,BatteryService.class));
	}
	
	public void onStart(){
		super.onStart();

	}
	
	public void onStop(){
		super.onStop();
	}
	
	public void exit(View v){
		this.finish();
	}

	

}

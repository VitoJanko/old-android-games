package org.bizjak.c.network.info;

import org.bizjak.c.network.info.service.Info;
import org.bizjak.c.network.info.service.InfoService;
import org.bizjak.c.network.info.service.ServiceBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	private static final String TAG = "main";
	
    InfoService mService;
    boolean mBound = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent intent = new Intent(this, InfoService.class);
        startService(intent);

        Log.d(TAG, "LAST");
    }
    
    
    
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServiceBinder binder = (ServiceBinder) service;
            mService = binder.getService();   
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    
    public void onResume(){
    	super.onResume();
    	
    	
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!mBound || mService == null){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				runOnUiThread(new Runnable(){
					@Override
					public void run(){
						getInfo();
					}
				});

			}
		}).start();
    }
    
    public void onStart(){
    	super.onStart();
    	
        Intent intent = new Intent(this, InfoService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    
    public void onStop() {
    	super.onStop();
	    if (mBound) {
	        unbindService(mConnection);
	        mBound = false;
	    }
    }
    
    public void getInfo(View view){
    	getInfo();
    }
    
    public void getInfo(){
    	if(mBound){
    		Info info = mService.getInfo();
    		((TextView) findViewById(R.id.cdmaTextView)).setText("CDMA: "+info.CDMA);
    		((TextView) findViewById(R.id.evdoTextView)).setText("EVDO: "+info.EVDO);
    		((TextView) findViewById(R.id.strengthTextView)).setText("STRENGTH: "+info.strength);
    		
    		
    		((TextView) findViewById(R.id.countryTextView)).setText("Country: "+ info.country);
    		((TextView) findViewById(R.id.operatorTextView)).setText("Operator: "+info.operator);
    		((TextView) findViewById(R.id.networkTextView)).setText("Network: "+info.network);

    	}else{
    		Toast.makeText(this, "Service not bound.", Toast.LENGTH_LONG).show();
    	}
    }

}

package com.example.huntedseas;



import android.app.Activity;
import android.app.ActivityManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements SensorEventListener{

    private Sea view;
    
    private static String ICICLE_KEY = "sea-view";

    private SensorManager sm;
	private Sensor mAccelerometer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        view = new Sea(this);
        setContentView(view);
        
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        Log.v("onCreate", "maxMemory:" + Long.toString(maxMemory));
        
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));

        if (savedInstanceState == null) {
            //view.setMode(view.READY);
        } else {
            // We are being restored
            Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
            if (map != null) {
               // view.restoreState(map);
            } else {
                //mSnakeView.setMode(SnakeView.PAUSE);
            }
        }
    }

    public void onSensorChanged(SensorEvent event) {
		view.angleX=event.values[0];
		view.angleY=event.values[1];
		view.angleZ=event.values[2];		
	}
    
    protected void onResume(){
    	super.onResume();
    	view.stopUpdate=false;
    	if(view.ready)
    		view.update();
    	sm.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME); 
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        view.stopUpdate=true;
        sm.unregisterListener(this);
        // Pause the game along with the activity
        //view.setMode(Sea.PAUSE);
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the game state
        //outState.putBundle(ICICLE_KEY, mSnakeView.saveState());
    }

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
package com.huntedseas;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;

public class GameActivity extends Activity implements SensorEventListener {

	private GameView view;
    private SensorManager sm;
	private Sensor mAccelerometer;
	private PowerManager pm;
	private PowerManager.WakeLock wl;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Hunted Seas");
        
        view = new GameView(this);
        setContentView(view);
    }
    
    public void onResume(){
    	super.onResume();
    	sm.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    	wl.acquire();
    }
    
    public void onPause(){
    	super.onPause();
    	sm.unregisterListener(this);
    	wl.release();
    }
    
    public void onDestroy(){
    	super.onDestroy();
    	view.destroy();
    }
    
    
    public void onSensorChanged(SensorEvent event) {
		GameRenderer.angleX=event.values[0];
		GameRenderer.angleY=event.values[1];
		GameRenderer.angleZ=event.values[2];	
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}

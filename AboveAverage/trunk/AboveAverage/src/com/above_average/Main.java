package com.above_average;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class Main extends  Activity implements SensorEventListener{

	static int stage;
	
	private SensorManager sm = null;
	private Sensor mAccelerometer;
	public boolean sensorWorking;
	public float angleX = 0;
	public float angleY = 0;
	public float angleZ = 0;
	Darkness mainView;
    
	public void zamenjajActivity(int stage, float health){
		try{
			MainMenu.number=stage;
			MainMenu.demage=health;
			finish();
		}
		catch (Exception e){};
	}
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mainView = new Darkness(this,this);
        setContentView(mainView);
    }

	protected void onResume() {
        super.onResume();
        mainView.stopUpdate=false;
        if(mainView.ready) mainView.update();
        sm.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME); 
	}
	
	protected void onPause() {
        super.onPause();
        mainView.stopUpdate=true;
        mainView.gameState=mainView.PAUSE;
        sm.unregisterListener(this);
    }
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	public void onSensorChanged(SensorEvent event) {
		angleX=event.values[0];
		angleY=event.values[1];
		angleZ=event.values[2];		
	}
	
	protected void onDestroy(){
		super.onDestroy();
		mainView.destroy();
		mainView.destroyDrawingCache();
		
		System.gc();
	}
  
}
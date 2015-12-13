package com.example.huntedseasgl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class MainActivity extends Activity implements SensorEventListener{


	//private GLSurfaceView mGLSurfaceView;
	Sea view;
    private SensorManager sm;
	private Sensor mAccelerometer;
	

	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
     
        
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mGLSurfaceView = new GLSurfaceView(this);
     
        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
     
        if (supportsEs2)
        {
            // Request an OpenGL ES 2.0 compatible context.
            //mGLSurfaceView.setEGLContextClientVersion(2);
     
            // Set the renderer to our demo renderer, defined below.
            //mGLSurfaceView.setRenderer(new MainView());
        	view = new Sea(this);
            setContentView(view);
        }
        else
        {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return;
        }
     
       // setContentView(mGLSurfaceView);
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

    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    }

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}

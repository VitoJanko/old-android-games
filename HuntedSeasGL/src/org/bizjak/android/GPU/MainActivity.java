package org.bizjak.android.GPU;

import org.bizjak.android.HUD.HUD;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class MainActivity extends Activity implements SensorEventListener{
	private Sea view;
	private HUD hud;
	
    private SensorManager sm;
	private Sensor mAccelerometer;
	
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
     
        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
     
        if (supportsEs2)
        {
        	setContentView(R.layout.gameview);
        	view = (Sea) findViewById(R.id.Sea);
        	hud = new HUD(this,(LinearLayout) findViewById(R.id.gameHud));
        }
        else
        {
            System.exit(0);
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

    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	hud.onDestroy();
    	hud = null;
    }

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}

package com.hunted_seas.game;

import static com.hunted_seas.debugging.Logger.logGameActivity;
import static com.hunted_seas.game.world.acommon.GameSettings.accelerometerSensitivity;
import static com.hunted_seas.game.world.acommon.GameSettings.invertX;
import static com.hunted_seas.game.world.acommon.GameSettings.invertY;
import static com.hunted_seas.game.world.acommon.GameSettings.switchXY;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.hunted_seas.game.graphics.Camera;
import com.hunted_seas.game.graphics.GameRenderer;
import com.hunted_seas.game.world.acommon.GameManager;
import com.hunted_seas.game.world.acommon.GameSettings;

/**
 * Tilt sensor listener.
 * <br />
 * When device changes position it registers and changes camera accordingly.
 * 
 * @see Camera
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class GameSensorListener implements SensorEventListener{
	private GameRenderer gameRenderer;
	
	public GameSensorListener(GameRenderer gameRenderer){
		this.gameRenderer = gameRenderer;
	}
	
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//nothing
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		logGameActivity(Log.INFO, event.values[0] + " : " + event.values[1] + " : " + event.values[2]);
		
		if(switchXY){
			event.values[1] += GameSettings.offsetY;
		}else{
			event.values[0] += GameSettings.offsetY;
		}
		
		if(!invertX)
			event.values[1] *= -1;
		if(!invertY)
			event.values[0] *= -1;
		
		if(switchXY){
			gameRenderer.accelerometerVector(-event.values[0]*accelerometerSensitivity, event.values[1]*accelerometerSensitivity);
		}else{
			gameRenderer.accelerometerVector(-event.values[1]*accelerometerSensitivity, event.values[0]*accelerometerSensitivity);
		}
	}
	
	

}

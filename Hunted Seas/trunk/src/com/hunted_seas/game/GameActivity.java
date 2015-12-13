package com.hunted_seas.game;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hunted_seas.game.graphics.GameRenderer;
import com.hunted_seas.game.graphics.HUD;
import com.hunted_seas.game.graphics.loading.LoadingScreenAnimation;
import com.hunted_seas.game.world.acommon.CommonVariables;
import com.hunted_seas.menu.MenuActivity;

/**
 * 
 * Main activity for game.
 * This is called after level is selected from MenuActivity.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class GameActivity extends Activity {
	private GLSurfaceView gameView;
	private boolean rendererSet = false;
	
	private HUD gameHud;
	private LoadingScreenAnimation loadingScreen;
	private GameRenderer gameRenderer;
	
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private GameSensorListener accelerometerListener;

	private int world;
	private int level;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		Intent gameInfo = getIntent();
		
		world = gameInfo.getIntExtra(MenuActivity.WORLD, 1);
		level = gameInfo.getIntExtra(MenuActivity.LEVEL, 1);
		
		
		
		
		setContentView(R.layout.activity_game);
		
		checkOsVersion();
		
		/**
		 * Creates layout overlay over "renderer". Layout is transparent and works like regular android layout.
		 * You can edit layout in .xml file.
		 */
		gameHud = new HUD(this, (RelativeLayout) findViewById(R.id.game_hud));
		loadingScreen = new LoadingScreenAnimation(this,(RelativeLayout)findViewById(R.id.loading_screen_layout));
		gameRenderer = new GameRenderer(this,gameHud,loadingScreen,world,level);
		
		loadingScreen.startAnimation();
		/**
		 * Finds view where renderer will be attached to.
		 */
		gameView = (GLSurfaceView) findViewById(R.id.GameSurfaceView);
		gameView.setKeepScreenOn(true); //prevents game from sleeping
		
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
		
		
		/**
		 * We only support >= OpenGL es 2.0
		 */
		if (supportsEs2) {
			// Request an OpenGL ES 2.0 compatible context.
			gameView.setEGLContextClientVersion(2);
			
			// Assign our renderer.
			gameView.setRenderer(gameRenderer);
			rendererSet = true;
		} else {
			Toast.makeText(this, "This device does not support OpenGL ES < 2.0.",
			Toast.LENGTH_LONG).show();
			return;
		}
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		accelerometerListener = new GameSensorListener(gameRenderer);
	}
	
	
	/**
	 * Checks version of SDK on device. It then writes it to CommonVariables.
	 * @see CommonVariables
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	private void checkOsVersion(){
		 CommonVariables.androidVersion = Build.VERSION.SDK_INT;
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	
	
	public void onPause(){
		super.onPause();
		
		if (rendererSet) {
			gameView.onPause();
		}
		
		sensorManager.unregisterListener(accelerometerListener);
	}
	
	
	public void onResume(){
		super.onResume();
		
		if (rendererSet) {
			gameView.onResume();
		}
		
		sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
	}
	
	public void onDestroy(){
		super.onDestroy();
		
		if(rendererSet){
			gameView.onPause();
			if(gameRenderer != null)
				gameRenderer.forceExit();
		}
		
		gameView = null;
		gameHud = null;
		gameRenderer = null;
	}
}

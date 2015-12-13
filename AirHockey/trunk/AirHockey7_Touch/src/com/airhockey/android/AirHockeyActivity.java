package com.airhockey.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class AirHockeyActivity extends Activity {
	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;
	
	final AirHockeyRenderer airHockeyRender = new AirHockeyRenderer(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurfaceView = new GLSurfaceView(this);
		
		
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
		
		if (supportsEs2) {
			// Request an OpenGL ES 2.0 compatible context.
			glSurfaceView.setEGLContextClientVersion(2);
			// Assign our renderer.
			glSurfaceView.setRenderer(airHockeyRender);
			rendererSet = true;
		} else {
			Toast.makeText(this, "This device does not support OpenGL ES 2.0.",
			Toast.LENGTH_LONG).show();
			return;
		}
		
		
		glSurfaceView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event != null){
					//Convert touch coordinates into normalized device
					//coordinates, keeping in mind that Anrdoid's Y
					//coordinates are inverted.
					
					final float normalizedX = (event.getX() / (float) v.getWidth())*2-1;
					final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2-1);
				
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						glSurfaceView.queueEvent(new Runnable(){
							@Override
							public void run(){
								airHockeyRender.handleTouchPress(//touch runs on UI thread
										normalizedX,normalizedY);
							}
						});
					}else if(event.getAction() == MotionEvent.ACTION_MOVE){
						glSurfaceView.queueEvent(new Runnable(){
							@Override
							public void run(){
								airHockeyRender.handleTouchDrag(//touch runs on UI thread we need to talk to GL thread
										normalizedX,normalizedY);
							}
						});
					}
					
					return true;
				}else{
					return false;
				}
			}
		});
		
		
		setContentView(glSurfaceView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_open_glproject, menu);
		return true;
	}
	
	protected void onPause(){
		super.onPause();
		
		if (rendererSet) {
			glSurfaceView.onPause();
		}
	}
	
	protected void onResume() {
		super.onResume();
		if (rendererSet) {
			glSurfaceView.onResume();
		}
	}

}

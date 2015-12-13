package com.igrargti;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.view.KeyEvent;
import android.view.MotionEvent;


public class Lesson10 extends GLSurfaceView implements Renderer {
		
	/** Our World */				
	private World world;	
	private GL10 gl;
	
	private int filter = 0;				//Which texture filter?
	
	/** Is blending enabled */
	private boolean blend = false;
	
	/** The Activity Context */
	private Context context;
	
	public Run run;
	
	
	/**
	 * Set this class as renderer for this GLSurfaceView.
	 * Request Focus and set if focusable in touch mode to
	 * receive the Input from Screen
	 * 
	 * @param context - The Activity Context
	 */
	public Lesson10(Context context,Run run) {
		super(context);
		
		this.run = run;
		
		//Set this as Renderer
		this.setRenderer(this);
		//Request focus
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		
		//
		this.context = context;
		
		//Instance our World
		world = new World(this.context,this);
		
		//Set the world as listener to this view
		this.setOnKeyListener(world);
		this.setOnTouchListener(world);
	}

	/**
	 * The Surface is created/init()
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		this.gl = gl;
		
		//Settings
		gl.glDisable(GL10.GL_DITHER);						//Disable dithering
		gl.glEnable(GL10.GL_TEXTURE_2D);					//Enable Texture Mapping
		gl.glShadeModel(GL10.GL_SMOOTH); 					//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 			//Black Background
		gl.glClearDepthf(1.0f); 							//Depth Buffer Setup
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);		//Set The Blending Function For Translucency
		gl.glDepthFunc(GL10.GL_LESS); 					//The Type Of Depth Testing To Do
		
		
		gl.glClearDepthf(1);
		gl.glEnable(gl.GL_DEPTH_TEST);
		
		//Svetloba
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, allocFloats(new float[]{0.5f, 0.5f, 0.5f, 1.0f}));		//Setup The Ambient Light ( NEW )
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, allocFloats(new float[]{1.0f, 1.0f, 1.0f, 1.0f}));		//Setup The Diffuse Light ( NEW )
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, allocFloats(new float[]{0.0f, 0.0f, 0.4f, 1.0f}));	//Position The Light ( NEW )
		gl.glEnable(GL10.GL_LIGHT0);
		
		
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);

		
		//megla
		gl.glFogf(gl.GL_FOG_MODE, gl.GL_EXP2);
		gl.glFogf(gl.GL_FOG_DENSITY,0.35f);
		gl.glFogfv(gl.GL_FOG_COLOR, allocFloats(new float[] { 0.0f, 0.0f,0.0f, 1.0f}));
		gl.glFogf(gl.GL_FOG_HINT, gl.GL_NICEST);
		gl.glFogf(GL10.GL_FOG_START, 0.3f);					//Fog Start Depth ( NEW )
		gl.glFogf(GL10.GL_FOG_END, 2.0f);					//Fog End Depth ( NEW )
		gl.glEnable(GL10.GL_FOG);							//Enables GL_FOG ( NEW )
		 
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
				
		//Load our world from the textual description
		world.loadWorld("world.txt");
		//Load the texture for our world once during Surface creation
		world.loadGLTexture(gl,this.context);
	}
	
	
	public static FloatBuffer allocFloats(float[] floatarray)
	  {
	    FloatBuffer fb = ByteBuffer.allocateDirect(floatarray.length * 4).order(
	        ByteOrder.nativeOrder()).asFloatBuffer();
	    fb.put(floatarray).flip();
	    return fb;
	  }	
	
	/**
	 * Here we do our drawing
	 */
	public void onDrawFrame(GL10 gl) {
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		
		gl.glLoadIdentity();					//Reset The Current Modelview Matrix
		
		//Check if the blend flag has been set to enable/disable blending
		if(blend) {
			gl.glEnable(GL10.GL_BLEND);			//Turn Blending On
			gl.glDisable(GL10.GL_DEPTH_TEST);	//Turn Depth Testing Off
			
		} else {
			gl.glDisable(GL10.GL_BLEND);		//Turn Blending On
			gl.glEnable(GL10.GL_DEPTH_TEST);	//Turn Depth Testing Off
		}
		
		world.draw(gl,0);
		
	}
	
	


	/**
	 * If the surface changes, reset the view
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 10.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
	}
	

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		//Middle pressed
		if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			world.RESETLVL();
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_HOME){
			run.koncaj();
			
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_BACK){
			
			
		}
		
		return false;
	}
	
	public void reset(){
		world.RESETLVL();
	}
	
	public void vibrate(int ms){
		run.vibrate(ms);
	}
	
	public void setHudText(String str){
		run.setHudText(str);
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//
		float x = event.getX();
        float y = event.getY();
        
        //A press on the screen
        
        //
		return true;
	}
}

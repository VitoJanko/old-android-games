package com.huntedseas.live.wallpaper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;


public class GameRenderer implements Renderer {
	protected LevelGenerator generator;
	
	protected static float angleX;
	protected static float angleY;
	protected static float angleZ;
	protected static float viewX = 0;
	protected static float viewY = 0;
	
	protected static float offsetX = 0;
	protected static float offsetY = 0;
	//private float viewZ = 0;
	
	private float cameraAway = -200;
	
	private float[] mProjectionMatrix = new float[16]; //Projekcijska matrika
	private float[] mVMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];

	
	public void onSurfaceCreated(GL10 unused, EGLConfig conunused) {
		GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
    	
    	GLES20.glDisable(GLES20.GL_CULL_FACE); //No culling of back faces  \\ To nevem al je treba da je uklopljeno al ne
    	GLES20.glDisable(GLES20.GL_DEPTH_TEST); //No depth testing			\\  --||--
    	GLES20.glEnable(GLES20.GL_BLEND); //Blending
    	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA); //Interpolated blending
    	GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
    
    	generator = new LevelGenerator();
	}
		
	
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		//moveView();
		
		Matrix.setLookAtM(mVMatrix, 0, viewY, viewX, cameraAway, viewY, viewX, 0, 0.0f, 1.0f, 0.0f); //set view
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mVMatrix, 0); //calculate view transformation
		
		generator.draw(mMVPMatrix);
	}

	
	public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        
        float ratio = (float) width/height;
        float near = 1.0f;
        float far = 1500.0f;
        float fov = 80;
        float top = (float) (Math.tan(fov * Math.PI / 360.0f) * near);
        float bottom = -top;
        float left = ratio * bottom;
        float right = ratio * top;
 
        offsetY = (float) (Math.tan(Math.PI/180*fov/2) * Math.abs(cameraAway));
        offsetX = offsetY * ratio;
        
        generator.makeOzadje();
        
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}
	
	/*
    private long lastTime = System.currentTimeMillis();
    public void moveView(){
    	if((System.currentTimeMillis()-lastTime) >= 0){
    		lastTime = System.currentTimeMillis();
    		if( (viewX - angleX/10) < Level1.viewXP - offsetY && (viewX - angleX/10) > Level1.viewXM + offsetY) viewX -= angleX/10;	
    		if( (viewY - angleY/10) < Level1.viewYP - offsetX && (viewY - angleY/10) > Level1.viewYM + offsetX) viewY -= angleY/10;
    	}
    }
    */
    
    protected void destroy(){
    	generator.destroy();
    }
    
    
}

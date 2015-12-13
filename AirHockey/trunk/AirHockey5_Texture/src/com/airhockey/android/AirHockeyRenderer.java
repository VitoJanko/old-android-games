package com.airhockey.android;


import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.EGLConfig;
import android.opengl.GLSurfaceView.Renderer;

import com.airhockey.androd.objects.Mallet;
import com.airhockey.androd.objects.Table;
import com.airhockey.androd.programs.ColorShaderProgram;
import com.airhockey.androd.programs.TextureShaderProgram;
import com.airhockey.androd.util.MatrixHelper;
import com.airhockey.androd.util.TextureHelper;

import static android.opengl.GLES20.*;
public class AirHockeyRenderer implements Renderer{
	private final Context context;
	private final float[] projectionMatrix = new float[16];
	private final float[] modelMatrix = new float[16];
	private Table table;
	private Mallet mallet;
	private TextureShaderProgram textureProgram;
	private ColorShaderProgram colorProgram;
	private int texture;
	
	
	public AirHockeyRenderer(Context context) {
		this.context = context;
	}
	
	
	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		glViewport(0, 0, width, height);	
		
		MatrixHelper.perspectiveM(projectionMatrix, 45,(float)width/(float)height,1f,10f);
	
		setIdentityM(modelMatrix, 0);
		translateM(modelMatrix, 0, 0f, 0f, -2.5f);
		rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f); //rotate around x axis
		
		final float[] temp = new float[16];
		multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
		System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
	}
	
	@Override
	public void onDrawFrame(GL10 glUnused) {
		// Clear the rendering surface.
		glClear(GL_COLOR_BUFFER_BIT);
		
		// Draw the table.
		textureProgram.useProgram();
		textureProgram.setUniforms(projectionMatrix, texture);
		table.bindData(textureProgram);
		table.draw();
		
		// Draw the mallets.
		colorProgram.useProgram();
		colorProgram.setUniforms(projectionMatrix);
		mallet.bindData(colorProgram);
		mallet.draw();
	}

	@Override
	public void onSurfaceCreated(GL10 gl,javax.microedition.khronos.egl.EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		table = new Table();
		mallet = new Mallet();
		textureProgram = new TextureShaderProgram(context);
		colorProgram = new ColorShaderProgram(context);
		texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
		
	}

}

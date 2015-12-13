package com.huntedseas;

import android.opengl.Matrix;

public class Protagonist {
	private float coordinates[] = {
			-2.0f,  2.0f, 0.0f, //top left
			-2.0f, -2.0f, 0.0f, //bottom left
			 2.0f, -2.0f, 0.0f, //bottom right
			 2.0f,  2.0f, 0.0f  //top right
		};
	
	private SquareGL protagonist;
	
	private float[] mModelMatrix = new float[16];
	
	public Protagonist(){
		protagonist = new SquareGL(R.drawable.protagonist, coordinates);
	}
	
	protected void draw(float[] mMVPMatrix){
		Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix,0,GameRenderer.viewY,GameRenderer.viewX,0);
		Matrix.multiplyMM(mModelMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0);
		
		protagonist.draw(mModelMatrix);
	}
}

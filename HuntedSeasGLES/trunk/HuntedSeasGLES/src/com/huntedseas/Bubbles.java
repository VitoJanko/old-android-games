package com.huntedseas;

import java.util.ArrayList;

import android.opengl.Matrix;

public class Bubbles {
	ArrayList<SquareGL> bubblesP = new ArrayList<SquareGL>();//positive
	ArrayList<SquareGL> bubblesN = new ArrayList<SquareGL>();//negative
	
	long time = System.currentTimeMillis();
	private float[] mModelMatrix = new float[16];
	
	private int maxBubbles = 100;
	private int bubblesCount = 0;
	float tempX;
	float tempZ;
		
	public Bubbles(){
		
	}
	
	
	public void drawPositive(float[] mMVPMatrix){
		if(bubblesCount < maxBubbles && System.currentTimeMillis()-time > 33){
			spawnBubbles();
			time = System.currentTimeMillis();
		}
		
		for(int i=0;i<bubblesP.size();i++){
			bubblesP.get(i).offsetY += 0.1;
			if(bubblesP.get(i).offsetY < 22.4*2){
				Matrix.setIdentityM(mModelMatrix,0);
		        Matrix.translateM(mModelMatrix,0,0,bubblesP.get(i).offsetY,0);
				Matrix.multiplyMM(mModelMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0);
				
				bubblesP.get(i).draw(mModelMatrix);
			}else{
				bubblesP.get(i).offsetY = 0;
				//bubblesP.get(i).destroy();
				//bubblesP.remove(i);
				//bubblesCount--;
			}
		}
	}
	
	public void drawNegative(float[] mMVPMatrix){
		if(bubblesCount < maxBubbles && System.currentTimeMillis()-time > 33){
			spawnBubbles();
			time = System.currentTimeMillis();
		}
		
		for(int i=0;i<bubblesN.size();i++){
			bubblesN.get(i).offsetY += 0.1;
			if(bubblesN.get(i).offsetY < 22.4*2){
				Matrix.setIdentityM(mModelMatrix,0);
		        Matrix.translateM(mModelMatrix,0,0,bubblesN.get(i).offsetY,0);
				Matrix.multiplyMM(mModelMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0);
				
				bubblesN.get(i).draw(mModelMatrix);
			}else{
				bubblesN.get(i).offsetY = 0;
				//bubblesN.get(i).destroy();
				//bubblesN.remove(i);
				//bubblesCount--;
			}
		}
	}
	
	
	private void spawnBubbles(){
		for(int i=0;i<(maxBubbles-bubblesCount);i++){
			if(Math.random()>0.99){
				bubblesCount++;
				tempX = (float) ((Math.random()*80)-40);
				tempZ = (float) ((Math.random()*10)-5);
				if(tempZ < 0){ //Pazi koordinate proti kamri so negativne
					bubblesP.add(new SquareGL(R.drawable.bubble_l,getCoordinates()));
				}else{
					bubblesN.add(new SquareGL(R.drawable.bubble_l,getCoordinates()));
				}
				
			}
		}
	}
	
	private float[] getCoordinates(){
			float coordinates[] = {
				tempX,  -21.0f, tempZ, //top left
				tempX, -22.0f, tempZ, //bottom left
				tempX+1, -22.0f, tempZ, //bottom right
				tempX+1,  -21.0f, tempZ  //top right
			};
			
			return coordinates;
	}
	
	public void destroy(){
		for(int i=0;i< bubblesN.size();i++){
			bubblesN.get(0).destroy();
			bubblesN.remove(0);
		}
		
		for(int i=0;i< bubblesP.size();i++){
			bubblesN.get(0).destroy();
			bubblesN.remove(0);
		}
	}
	
}

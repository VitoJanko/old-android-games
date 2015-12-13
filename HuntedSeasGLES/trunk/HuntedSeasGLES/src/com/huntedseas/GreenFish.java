package com.huntedseas;

import java.util.ArrayList;

import android.opengl.Matrix;
import android.util.Log;

public class GreenFish {
	ArrayList<SquareGL> fish = new ArrayList<SquareGL>();
		
	private float[] mModelMatrix = new float[16];
	private float[] mRotateMatrix = new float[16];
	
	private int[] anime = new int[]{R.drawable.fish_animation1,R.drawable.fish_animation2,R.drawable.fish_animation3};

	private int maxFish = 50;
	private int fishCount = 0;
	
	private float tempY = 0;
	private float tempX = -40;
		
	public GreenFish(){

	}
	
	long time = System.currentTimeMillis();
	public void draw(float[] mMVPMatrix){
		if(System.currentTimeMillis()-time > 63){
			if(fishCount < maxFish){
				spawnFish();
			}
		}
		for(int i=0;i<fish.size();i++){
			fish.get(i).offsetX += 0.15;
			
			if(fish.get(i).offsetX < 40*2){
				Matrix.setIdentityM(mRotateMatrix,0);
				Matrix.rotateM(mRotateMatrix,0,fish.get(i).angle,0,1,0);
				Matrix.multiplyMM(mRotateMatrix, 0, mMVPMatrix, 0, mRotateMatrix, 0);
				
				Matrix.setIdentityM(mModelMatrix,0);
		        Matrix.translateM(mModelMatrix,0,fish.get(i).offsetX,0,0);
				Matrix.multiplyMM(mModelMatrix, 0, mRotateMatrix, 0, mModelMatrix, 0);
				
				if(System.currentTimeMillis() - fish.get(i).time > 300){
					fish.get(i).time = System.currentTimeMillis();
					fish.get(i).textureImage ++;
					fish.get(i).textureImage %= 3;
				}
				
				fish.get(i).draw(mModelMatrix);
			}else{
				fish.get(i).offsetX = 0;
				//fish.get(i).destroy();
				//fish.remove(i);
				//fishCount--;
			}
		}
	}
	
	
	private void spawnFish(){
		for(int i=0; i < (maxFish-fishCount);i++){
			if(Math.random() > 0.999){
				fishCount++;
				//tempY = (float) ((Math.random()*80)-40);
				tempY = (float) ((Math.random()*45)-22);
				fish.add(new SquareGL(anime,getCoordinates()));
				float tang = 0;
				if(Math.random() > 0.5) tang = 180;
				fish.get(fish.size()-1).angle = tang;
			}
		}
	}
	
	private float[] getCoordinates(){
		float coordinates[] = {
				tempX,  tempY, 0, //top left
				tempX, tempY-2, 0, //bottom left
				tempX+2, tempY-2, 0, //bottom right
				tempX+2,  tempY, 0  //top right
			};
		
		return coordinates;
	}
	
	public void destroy(){
		for(int i=0;i<fish.size();i++){
			fish.get(0).destroy();
			fish.remove(0);
			Log.d("destroy","i: "+i);
		}
	}
}

package com.hunted_seas.game.data;

import android.opengl.Matrix;
import android.util.Log;


public class LineSegment {
	private float[] startPoint = new float[4];
	private float[] endPoint = new float[4];
	
	private float offsetX  = 0;
	private float offsetY = 0;
	
	private float[] startTemp = new float[4];
	private float[] endTemp = new float[4];
	
	private float pointOfCollisionX = 0;
	private float pointOfCollisionY = 0;
	
	public void setStartPoint(float[] startPoint){
		if(startPoint.length != 4){
			int a = 5 / 0;
		}
			
		this.startPoint = startPoint;
	}
	
	public void setStartPoint(float x, float y){
		this.startPoint = new float[]{x,y,0,1};
	}
	
	public void setStartPoint(float x, float y, float z){
		this.startPoint = new float[]{x,y,z,1};
	}
	
	public void setEndPoint(float[] endPoint){
		if(endPoint.length != 4){
			int a = 5 / 0;
		}
		this.endPoint = endPoint;
	}
	
	public void setEndPoint(float x, float y){
		this.endPoint = new float[]{x,y,0,1};
	}
	
	public void setEndPoint(float x, float y, float z){
		this.endPoint = new float[]{x,y,z,1};
	}
	
	public float[] getStartPoint(){
		return startPoint;
	}
	
	public float[] getEndPoint(){
		return endPoint;
	}

	public void setOffsetX(float offsetX){
		this.offsetX = offsetX;
		setOffset(offsetX, 0);
	}
	
	public void setOffsetY(float offsetY){
		this.offsetY = offsetY;
		setOffset(0, offsetY);
	}
	
	public void setOffset(float offsetX, float offsetY){
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	/**
	 * Returns last point where collision happened.
	 * 
	 * 
	 * @return float[x,y]
	 * 
	 * @author Jani
	 */
	public float[] getPointOfCollisoin(){
		return new float[]{pointOfCollisionX, pointOfCollisionY};
	}
	
	public void moveColidableObject(float[] modelMatrix){
		Matrix.multiplyMV(startTemp, 0, modelMatrix, 0, startPoint, 0);
		Matrix.multiplyMV(endTemp, 0, modelMatrix, 0, endPoint, 0);
	}
	
	public void moveColidableObject(float dx, float dy){
		startTemp[0] += dx; startTemp[1] += dy;
		endTemp[1] += dx; endTemp[1] += dy;
	}
		
	public boolean doIntersect(LineSegment otherSegment){		
//		Matrix.multiplyMV(startTemp, 0, sprite, 0, startPoint, 0);
//		Matrix.multiplyMV(endTemp, 0, sprite, 0, endPoint, 0);
		
//		Matrix.multiplyMV(otherSegment.startTemp, 0, sprite2, 0, otherSegment.startPoint, 0);
//		Matrix.multiplyMV(otherSegment.endTemp, 0, sprite2, 0, otherSegment.endPoint, 0);
		
		float p0_x = startTemp[0]; float p0_y = startTemp[1];
		float p1_x = endTemp[0]; float p1_y = endTemp[1];
		
		float p2_x = otherSegment.startTemp[0]; float p2_y = otherSegment.startTemp[1];
		float p3_x = otherSegment.endTemp[0]; float p3_y = otherSegment.endTemp[1];
		
		float s1_x, s1_y, s2_x, s2_y;
	    s1_x = p1_x - p0_x;     s1_y = p1_y - p0_y;
	    s2_x = p3_x - p2_x;     s2_y = p3_y - p2_y;
		    
	    float s, t;
	    s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
	    t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);
   
	    if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
	    {
	        // Collision detected
	    	// Collision point
	    	pointOfCollisionX = p0_x + (t * s1_x);
	        pointOfCollisionY = p0_y + (t * s1_y);

	        return true;
	    }
	    
		return false;
	}
	
}

package com.hunted_seas.game.data;

public class BoundingBox {
	public float left = Integer.MAX_VALUE;
	public float right = Integer.MIN_VALUE;
	public float top = Integer.MIN_VALUE;
	public float bottom = Integer.MAX_VALUE;
	
	public float z = 0;
	
	public BoundingBox(){};
	
	public BoundingBox(float left, float right, float top, float bottom){
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}
	
	public BoundingBox(float left, float right, float top, float bottom, float z){
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.z = z;
	}
	
	public float getWidth(){
		return right-left;
	}
	
	public float getHeight(){
		return top - bottom;
	}
}

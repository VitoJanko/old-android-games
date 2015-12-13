package com.igrargti;

import java.util.ArrayList;

public class CollisionElement {
	private int ID;
	private float x1,x2,x3;
	private float z1,z2,z3;
	boolean spodnji = true;
	float k,n;

	
	public CollisionElement(int id,float x1,float x2,float x3,float z1,float z2,float z3){
		this.ID = id;
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
		this.z1 = z1;
		this.z2 = z2;
		this.z3 = z3;
		
		dobiFunkcijo();
	}
	
	public boolean isNotCollision(float x,float z){
		if(spodnji){
			if(x < x2 && z < z1 && z > (k*x+n)){
					return true;
			}else{
				return false;
			}
		}else{
				if(x > x1 && z > z2 && z < (k*x+n)){
					return true;
			}else{
				return false;
			}			
		}
	}
	
	public void dobiFunkcijo(){
		if(x1 == x2) spodnji = false;
		
		float dy = z3-z1;
		float dx = x3 - x1;
		k = dy/dx;
		
		n = z1-x1*k;
	}
	
	
	public float[] getCoordinaates(){
		float[] koordinate = new float[]{x1,x2,x3,z1,z2,z3};
		return koordinate;
	}
}

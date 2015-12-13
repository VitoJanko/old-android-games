package com.hunted_seas.game.world.acommon;

import android.util.Log;

public class Lights {
	public float x = 0;
	public float y = 0;
	public float z = 0;
	
	public void moveLight(float[] xyz){
//		Log.d("Lights","Lights: "+xyz[0]+" "+xyz[1]+" "+xyz[2]);
		
		this.x = xyz[0];
		this.y = xyz[1];
		this.z = xyz[2];
	}
}

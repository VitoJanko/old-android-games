package com.hunted_seas.game.world.acommon;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;

/**
 * Bubble.
 * 
 * @see Sprite
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class Bubble extends Sprite {
	public static final String FOLDER = "common_elements/background/";
	public static final float[] SIZE = {-30f,30f,30f,-30f};
	
	public static String texture = FOLDER+"bubble";

	
	public static int model = R.raw.bubble;
	
	private int fuel = 100000;
	
	public Bubble(float x, float y, float z, float speed){
		super(x, y, z, speed,1);
		scale = (float)(0.5 + Math.random());
	}
	
	public Bubble(float x, float y, float z, float speed, int fuel){
		super(x, y, z, speed,1);
		scale = (float)(0.2 + Math.random());
		this.fuel = fuel;
	}

	@Override
	public boolean step(float stepScale) {
		fuel --;
		
		y += stepScale * speed;
		if(y>master.getLvL().topBound+2*radius)
			setDead(true);
		
		
		if(fuel > 0)
			return true;
		else{
			setDead(true);
			return false;
		}
	}

	@Override
	public boolean fineCollisionDetection(ColidableObjectInterface sprite) {
		return false;
	}
	
	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {}


}

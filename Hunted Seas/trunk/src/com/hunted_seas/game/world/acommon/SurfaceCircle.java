package com.hunted_seas.game.world.acommon;

import com.hunted_seas.game.collision.ColidableObjectInterface;

/**
 * Bubble.
 * 
 * @see Sprite
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class SurfaceCircle extends Sprite {
	public static final String FOLDER = "common_elements/background/";
	public static final float[] SIZE = {-200f,200f,200f,-200f};
	
	public static String texture = FOLDER+"sky";


	float progression;
	float leftProgression;
		
	public SurfaceCircle(float x, float y, float z, float speed){
		super(x, y, z, speed,1);
		this.speed = 0.6f;
		progression = (float)(Math.random()*2*Math.PI);
		leftProgression = 0;
	}

	@Override
	public boolean step(float stepScale) {
		y += stepScale * speed * Math.sin(progression);
		x += stepScale * 4*speed * Math.sin(leftProgression);
		progression += (Math.PI/50.0)*stepScale;	
		leftProgression += (Math.PI/180.0)*stepScale;	
		return true;
	}

	@Override
	public boolean fineCollisionDetection(ColidableObjectInterface sprite) {
		return false;
	}
	
	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {}


}

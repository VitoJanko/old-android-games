package com.hunted_seas.game.world.player;

import static com.hunted_seas.game.world.player.Player.FOLDER;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.Sprite;

public class PlayerEyes extends Sprite {
	public static String[] texture = {FOLDER+"hero_eyes1",FOLDER+"hero_eyes2", FOLDER+"hero_eyes3"};
	public static int[] model = {R.raw.hero_eyes1,R.raw.hero_eyes2, R.raw.hero_eyes3};
	
	private float animationStepScale = 0;
	private int animationCount = 0;
	
	@Override
	public boolean step(float stepScale) {
		animationStepScale += stepScale;
		
		if(animationStepScale >= 1){
			animationCount++;
		}
		
		if(animationCount < 60){
			animationState = 0;
		}else if(animationCount < 90){
			animationState = 1;
		}else if(animationCount < 150){
			animationState = 2;
		}else if(animationCount < 180){
				animationState = 1;	
		}else{
			animationCount = 0;
		}
		
		
		return true;
	}
	@Override
	public void resolveCollision(ColidableObjectInterface sprite,
			float[] pointOfCollision) {
		// TODO Auto-generated method stub
		
	}
	
	public void move(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
}

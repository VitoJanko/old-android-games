package com.hunted_seas.game.world.player;

import static com.hunted_seas.game.world.player.Player.FOLDER;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.Sprite;

public class PlayerEyebrows extends Sprite {
	public static String[] texture = {FOLDER+"hero_eyebrows",FOLDER+"hero_eyebrows2"};
	public static int[] model = {R.raw.hero_eyebrows,R.raw.hero_eyebrows2};
	
	
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
		}else if(animationCount < 120){
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
		this.x = x+5;
		this.y = y;
		this.z = z;
	}
}

package com.hunted_seas.game.world.awone.blowfish;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

public class BlowfishFins extends Sprite{
	float angleProgression; 
	public static int[] model = {R.raw.blowfish_0fin1,R.raw.blowfish_0fin2,R.raw.blowfish_0fin3,R.raw.blowfish_0fin4,R.raw.blowfish_0fin5,
		R.raw.blowfish_6fin1,R.raw.blowfish_6fin2,R.raw.blowfish_6fin3,R.raw.blowfish_6fin4,R.raw.blowfish_6fin5,R.raw.blowfish_6fin6,R.raw.blowfish_6fin7};
	
	private boolean blownUp = false;
	
	private float animationSum = 0;
	private float animationCounter = 0;
	private int direction = 1;
	
	public BlowfishFins(float x, float y, float z){
		super(x, y, z, 0,1);
		
		scale = 1.1f;
		collision = false;
	}


	@Override
	public boolean step(float stepScale) {
		animationSum += stepScale;
		
		if(animationSum >= 1){
			animationSum = 0;
			animationCounter ++;
		}else{
			return true;
		}
		
		if(blownUp){
			if(direction == 1){
				if(animationCounter % 3 == 0){
					animationState++;
				}
				
				if(animationCounter >= 20){
					direction = -1;
					animationCounter = 0;
				}
			}else{
				if(animationCounter % 3 == 0){
					animationState--;
				}
				
				if(animationCounter >= 20){
					direction = 1;
					animationCounter = 0;
				}
			}
		}else{
			if(direction == 1){
				if(animationCounter % 3 == 0){
					animationState++;
				}
				
				if(animationCounter >= 14){
					direction = -1;
					animationCounter = 0;
				}
			}else{
				if(animationCounter % 3 == 0){
					animationState--;
				}
				
				if(animationCounter >= 14){
					direction = 1;
					animationCounter = 0;
				}
			}
		}
		
		return true;
	}


	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {
		Player player = (Player)sprite; 
		player.changeHealth(-15);
		float alpha = getDirectionToPoint(sprite.getPosition(),getPosition());
		player.graduallyMovePlayer(alpha, 40, 4f);
	}
	
	public void blowUp(){
		if(blownUp)
			return;
		blownUp = true;
		animationState = 5;
		animationCounter = 0;
		direction = 1;
	}
	
	public void deflate(){
		if(!blownUp)
			return;
		
		blownUp = false;
		
		animationState = 0;
		animationCounter = 0;
		direction = 1;
	}
}

package com.hunted_seas.game.world.awone.blowfish;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

public class BlowfishEyes extends Sprite{
	float angleProgression; 

	public static int[] model = {R.raw.blowfish_0eye1,R.raw.blowfish_0eye2, R.raw.blowfish_0eye3, R.raw.blowfish_2eyes,R.raw.blowfish_3eyes,
		R.raw.blowfish_6eyes1,R.raw.blowfish_6eyes2,R.raw.blowfish_6eyes3};
	
	
	public boolean blownUp = false;
	public float animationSpeed = 0;
	public int animationCounter = 0;
	public int direction = 1;
	
	public BlowfishEyes(float x, float y, float z){
		super(x, y, z, 0,1);
		angleProgression = (float) (Math.random()*(2*Math.PI));
		collision = true;
	}


	@Override
	public boolean step(float stepScale) {
		animationSpeed += stepScale;
		if(animationSpeed >= 1){
			animationCounter++;
			animationSpeed = 0;
			
			if(direction == 1){
				if(animationCounter % 5 == 0){
					animationState ++;
				}
			}else{
				if(animationCounter % 5 == 0){
					animationState --;
				}
			}
			
			if(animationCounter >= 34){
				direction *= -1;
				animationCounter = 0;
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
}

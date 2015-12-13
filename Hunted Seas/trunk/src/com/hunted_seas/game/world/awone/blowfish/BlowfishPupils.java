package com.hunted_seas.game.world.awone.blowfish;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

public class BlowfishPupils extends Sprite{
	float angleProgression; 

	public static int[] model = {R.raw.blowfish_pupils};
	
	
	public BlowfishPupils(float x, float y, float z){
		super(x, y, z, 0,1);
		angleProgression = (float) (Math.random()*(2*Math.PI));
		collision = true;
	}


	@Override
	public boolean step(float stepScale) {
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

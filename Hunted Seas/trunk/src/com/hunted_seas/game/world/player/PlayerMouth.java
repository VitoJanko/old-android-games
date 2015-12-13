package com.hunted_seas.game.world.player;

import static com.hunted_seas.game.world.player.Player.FOLDER;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.Sprite;

public class PlayerMouth extends Sprite{
	public static String[] texture = {FOLDER+"hero_mouth", FOLDER+"hero_mouth2"};
	public static int[] model = {R.raw.hero_mouth, R.raw.hero_mouth2};
	
	public boolean hurt = false;
	
	@Override
	public boolean step(float stepScale) {
		
		if(hurt)
			animationState = 1;
		else
			animationState = 0;
		
		return true;
	}
	@Override
	public void resolveCollision(ColidableObjectInterface sprite,
			float[] pointOfCollision) {
	}
	
	public void move(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
}

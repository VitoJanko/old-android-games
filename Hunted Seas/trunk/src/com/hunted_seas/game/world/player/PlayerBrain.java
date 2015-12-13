package com.hunted_seas.game.world.player;

import static com.hunted_seas.game.world.player.Player.FOLDER;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.Sprite;

public class PlayerBrain extends Sprite {
	public static String[] texture = {FOLDER+"hero_brain"};
	public static int[] model = {R.raw.hero_brain};
	
	
	@Override
	public boolean step(float stepScale) {
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

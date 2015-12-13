package com.hunted_seas.game.world.player;

import static com.hunted_seas.game.world.player.Player.FOLDER;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.world.acommon.Sprite;

public class PlayerTentacles extends Sprite {
	public static String[] texture = {FOLDER+"hero_body"};
	public static int[] model = {R.raw.hero_body};
	
	
	@Override
	public boolean step(float stepScale) {
		return true;
	}
	@Override
	public void resolveCollision(ColidableObjectInterface sprite,
			float[] pointOfCollision) {
	}
	
	public BoundingBox getBoundingBox(){
		return boundingBox[animationState];
	}
	
	public void move(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
}

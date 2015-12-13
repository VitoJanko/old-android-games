package com.hunted_seas.game.world.player;

import static com.hunted_seas.game.world.acommon.GameSettings.player_skin_id;
import static com.hunted_seas.game.world.player.Player.FOLDER;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.world.acommon.Sprite;

public class PlayerHead extends Sprite {
	public static String[] texture = {FOLDER+"hero_head",FOLDER+"hero_headb"};
	public static int[] model = {R.raw.hero_head,R.raw.hero_head};
	
	
	@Override
	public boolean step(float stepScale) {
		switch(player_skin_id){
		case 1:
			animationState = 1;
			break;
		default:
			animationState = 0;
			break;
		}
				
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
	
	public BoundingBox getBoundingBox(){
		return boundingBox[animationState];
	}
	
	
}

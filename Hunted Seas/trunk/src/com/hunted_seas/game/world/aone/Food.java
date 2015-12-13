package com.hunted_seas.game.world.aone;

import java.util.Random;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

/**
 * Food sprite.
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class Food extends Sprite {
	public static final String FOLDER = "common_elements/collectable/";
	
	
	private float animationSpeedCounter = 0;
	
	private int horizontalSpeedholder = 0;
	
	private Random random = new Random();
	
	public static final String TEXTURE = FOLDER+"food";
	public static int model = R.raw.food1;

	
	public Food(float x, float y, float speed, float direction){
		super(x, y, 0, speed, 1);
		collision = true;
		model =  R.raw.food1;
	}
	

	@Override
	public boolean step(float stepScale) {
		animationSpeedCounter += stepScale;
		
		if(animationSpeedCounter >= 3){ // frame speed time has passed (40 ms = 25fps)
			animationSpeedCounter = 0;
			horizontalSpeedholder = random.nextInt(7)-3;
		}
		
		y += stepScale * speed;
		x += stepScale * horizontalSpeedholder;
		
		if(y > master.getLvL().getTopBound()+radius)
			setDead(true);
		
		return true;
	}

	
	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {
		Player player = (Player)sprite; 
		
		player.eatFood(1);
		player.changeHealth(5);
		setDead(true);
	}
}

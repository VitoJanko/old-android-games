package com.hunted_seas.game.world.aone;

import java.util.Random;

import android.util.Log;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

/**
 * Green fish sprite
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class GreenFishPupil extends Sprite {
	public static final String FOLDER = "simple_fish/";
	
	
	public static String[] texture = {FOLDER + "green_fish_pupil"};
	public static int[] model = {R.raw.green_fish_pupil};
	
	private Random random = new Random();
	
	public int eyeState = 0;
	
	public GreenFishPupil(){
		super(0,0,0);
		spawnFish();
	}
	
	public GreenFishPupil(float x, float y, float speed, float direction){
		super(x,y,0.002f,speed,direction);
		flipped = (int)-direction;
	}
	
	public GreenFishPupil(float x, float y, float z, float speed, float direction){
		super(x,y,z,speed,direction);
		this.z += 0.6f;
		flipped = (int)-direction;
	}
	
	@Override
	public boolean step(float stepScale) {
		if(eyeState==0){
			float alpha = getDirectionToPoint(master.getLvL().getPlayer().getPosition(),getPosition());
			float distanceX = 6;
			float distanceY = 8;
			if(Math.cos(alpha)*direction>0)
				x+= Math.cos(alpha)*distanceX;
			y+= Math.sin(alpha)*distanceY;
		}
		if(eyeState==2)
			y=master.getLvL().getTopBound()*2;
		return true;
	}
	
	private void spawnFish(){
		x = master.getLvL().getRightBound()+2*radius;
		y = random.nextInt(1400) - 700;
		speed = random.nextInt(6)+1;
	}

	
	@Override
	public boolean fineCollisionDetection(ColidableObjectInterface sprite) {
		return false;
	}
	
	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {}

}

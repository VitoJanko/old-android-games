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
public class GreenFishEye extends Sprite {
	public static final String FOLDER = "simple_fish/";
	
	
	public static String[] texture = {FOLDER+"green_fish_eye_open",FOLDER + "green_fish_eye_half",
		FOLDER + "green_fish_eye_closed"};

	
	public static int[] model = {R.raw.green_eye_open,R.raw.green_fish_eye_half_open,
		R.raw.green_fish_eye_closed};
	
	private Random random = new Random();
	
	private float animationSpeed = 0;
	private int animationCurrent = 0;
	private int animationCounter = 0;
	public int[] states = {0,0,0,0,1,2,2,1,0,0,3,4,4,3}; 
	
	
	public GreenFishEye(){
		super(0,0,0);
		spawnFish();
	}
	
	public GreenFishEye(float x, float y, float speed, float direction){
		super(x,y,0.001f,speed,direction);
		flipped = (int)-direction;
		animationCounter = 50+random.nextInt(100);
	}
	
	public GreenFishEye(float x, float y, float z, float speed, float direction){
		super(x,y,z,speed,direction);
		this.z += 0.3f;
		flipped = (int)-direction;
		animationCounter = 50+random.nextInt(100);
	}
	
	@Override
	public boolean step(float stepScale) {
		animationSpeed+=stepScale;
		if(animationSpeed >= 1){
			animationSpeed = 0;
			
			if(animationCurrent==0){
				animationCounter--;
				if(animationCounter==0){
					animationCounter = 2;
					animationCurrent=1;
					animationState = 1;
				}
			}
			else if(animationCurrent==1){
				animationCounter--;
				if(animationCounter==0){
					animationCounter = 6;
					animationCurrent=2;
					animationState = 2;
				}
			}
			else if(animationCurrent==2){
				animationCounter--;
				if(animationCounter==0){
					animationCounter = 2;
					animationCurrent=3;
					animationState = 1;
				}
			}
			else if(animationCurrent==3){
				animationCounter--;
				if(animationCounter==0){
					animationCounter = 50+random.nextInt(100);
					animationCurrent=0;
					animationState = 0;
				}
			}
		}
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

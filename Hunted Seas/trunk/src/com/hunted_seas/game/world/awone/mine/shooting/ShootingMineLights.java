package com.hunted_seas.game.world.awone.mine.shooting;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.Sprite;

public class ShootingMineLights extends Sprite {
	public static final int[] MODEL = {R.raw.shooting_mine_red_lights_1,R.raw.shooting_mine_red_lights_2,R.raw.shooting_mine_red_lights_3,
		R.raw.shooting_mine_white_lights_1,R.raw.shooting_mine_exhaust_1,R.raw.shooting_mine_exhaust_2,
		R.raw.shooting_mine_white_lights_2,R.raw.shooting_mine_white_lights_3};
	
	float animationCounter = 0;
	float animBuilder = 0;
	
	private boolean launchBullets = false;
	
	public ShootingMineLights(float x,float y,float scale){
		super(x,y,0);
		
		this.scale = scale;
	}
	
	
	public void  move(float newX, float newY){
		this.x = newX;
		this.y = newY;
	}
	
	@Override
	public boolean step(float stepScale) {
		animBuilder += stepScale;
		
		if(!launchBullets){
			if(animBuilder < 20){
				draw = false;
			}else if(animBuilder < 22){
				draw = true;
				animationState = 0;
			}else if(animBuilder < 42){
				animationState = 1;
			}else if(animBuilder < 44){
				animationState = 0;
			}else{
				animBuilder = 0;
				draw =  false;
			}
		}else{
			draw = true;
			if(animBuilder < 2){
				animationState = 2;
			}else if(animBuilder < 4){
				animationState = 3;
			}else if(animBuilder < 6){
				animationState = 5;
			}else if(animBuilder < 16){
				animationState = 4;
			}else if(animBuilder < 36){
				animationState = 5;
			}else if(animBuilder < 46){
				animationState = 6;
			}else if(animBuilder < 48){
				animationState = 7;
			}else{
				animBuilder = 0;
				draw = false;
				launchBullets = false;
			}
		}
		
		return true;
	}
	
	public void launchBullets(){
		launchBullets = true;
		animBuilder = 0;
	}

	@Override
	public void resolveCollision(ColidableObjectInterface sprite,
			float[] pointOfCollision) {
		// TODO Auto-generated method stub
		
	}

}

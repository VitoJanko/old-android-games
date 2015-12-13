package com.hunted_seas.game.world.awone.blowfish;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

public class BlowfishBody extends Sprite {
	public static final String FOLDER = "blowfish/";
	float angleProgression; 
	
	public static int[] model = {R.raw.blowfish_0bodya,R.raw.blowfish_0bodyb,R.raw.blowfish_1bodya, R.raw.blowfish_1bodyb,
		R.raw.blowfish_2bodya, R.raw.blowfish_2bodyb, R.raw.blowfish_3body, R.raw.blowfish_4body, R.raw.blowfish_5body, R.raw.blowfish_6body};

	
	BlowfishEyes eyes;
	BlowfishPupils pupils;
	BlowfishFins fins;
	
	private boolean changeState = false;
	private boolean blowUp = false;
	private float animationScale = 0;
	private int animationCounter = 0;
	boolean first = true;	
	
	boolean goingForward = true;
	
	LevelManager level;
	
	public BlowfishBody(float x, float y, float speed, float scale,LevelManager level){
		super(x, y, 0, speed,1);
		this.level = level;
		this.speed = speed;
				
		eyes = new BlowfishEyes(x,y,5);
		pupils = new BlowfishPupils(x,y,5);
		fins = new BlowfishFins(x,y,-5);
		
		animationState = 1;
		angleProgression = (float) (Math.random()*(2*Math.PI));
		collision = true;
	}
	
	public void loadBodyParts(BlowFishMaster master){
		master.addEyes(eyes);
		master.addPupils(pupils);
		master.addFin(fins);
	}


	@Override
	public boolean step(float stepScale) {
		angle = (float) (Math.cos(angleProgression)*(Math.PI/6f));
		angleProgression += (Math.PI/60.0)*stepScale;
		
		eyes.angle = angle;
		pupils.angle = angle;
		fins.angle = angle;
		
		Float[] d  = level.waypoints.get(index);
		if(d!=null){
			float xDest = d[0];
			float yDest = d[1];
			if(!goingForward){
				xDest = d[2];
				yDest = d[3];
			}
			float alpha = getDirectionToPoint(new float[]{x,y},new float[]{xDest,yDest});
			x-=Math.cos(alpha)*speed*stepScale;
			y-=Math.sin(alpha)*speed*stepScale;
			if(getDistanceToObject(new float[]{xDest,yDest})<speed){
				goingForward = !goingForward;
			}
		}
		
		
		//if(first){
			eyes.setLocation(x,y);
			eyes.setScale(scale);
			pupils.setLocation(x, y);
			pupils.setScale(scale);
			fins.setLocation(x,y);
			fins.setScale(scale);
			first = false;
		//}
		
		if(changeState)
			changeStateAnimation(stepScale);
		
		
		
		return true;
	}
	
	private void changeStateAnimation(float stepScale){
		animationScale += stepScale;
		if(animationScale >= 1){
			animationScale = 0;
			animationCounter++;
		}
		
		if(blowUp){
			fins.blowUp();
			if(animationCounter < 18){
				if(animationCounter % 2 == 0)
					animationState++;
			}else{
				animationCounter = 0;
				changeState = false;
				blowUp = false;
			}
			
		}else{
			fins.deflate();
			if(animationCounter < 18){
				if(animationCounter % 2 == 0)
					animationState--;
			}else{
				animationCounter = 0;
				blowUp = true;
				changeState = false;
			}
		}
		
		if(animationState >= 9)
			animationState = 9;
		if(animationState <= 0)
			animationState = 0;
		
	}

	@Override
	public boolean coarseCollisionDetection(ColidableObjectInterface sprite) {		
		float distance = getDistanceToObject(sprite.getPosition());
		
		if(distance < 1000){
			if(blowUp)
				changeState = true;
		}else{
			if(!blowUp)
				changeState = true;
		}
		
		if(distance < radius+sprite.getCoarseCollisionRadius())
			return true;
		return false;
	}
	
	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {
		if(sprite.getClass() == Player.class){
			Player player = (Player)sprite; 
			player.changeHealth(-15);
			float alpha = getDirectionToPoint(sprite.getPosition(),getPosition());
			player.graduallyMovePlayer(alpha, 40, 4f);
		}
	}
}

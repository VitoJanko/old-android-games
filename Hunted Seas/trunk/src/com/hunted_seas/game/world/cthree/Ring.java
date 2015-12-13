package com.hunted_seas.game.world.cthree;

import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import java.util.Random;

import android.util.Log;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

/**
 * Food sprite.
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class Ring extends Sprite {
	public static final String FOLDER = "common_elements/race/";
	public static String[] texture = {FOLDER + "ring"};
	
	public static int[] model = {R.raw.ring1, R.raw.ring2, R.raw.ring3, R.raw.ring4};
	
	private Random random = new Random();
	
	float angleProgression;
	float angleProgression2;
	
	boolean angle1Direction = false;
	boolean angle2Direction = false;
	
	float rotationSpeed = 0;
	
	LevelManager manager;
	
	boolean slowDown = false;
	
	boolean nextRing = false;
	boolean activated = false;
	
	/**
	 * Player can access it from:
	 * 0 = both sides
	 * 1 = left to right
	 * 2 = right to left
	 */
	public int accessType = 0;
	boolean access = false;
	
	public Ring(float x, float y, float z, LevelManager manager){
		super(x, y, z, 0, 1);
		collision = true;
		
		angleProgression = (random.nextInt(16)-8+90);
		angleProgression2 = random.nextInt(360);
		
		angle1Direction = random.nextBoolean();
		angle2Direction = random.nextBoolean();
		
		this.scale =  0.8f;
		
		this.manager = manager;
		
		animationState = 1;
		accessType = 1;
	}
	
	@Override
	public void calculateModelMatrix(){
		setIdentityM(tempModelMatrix, 0);
		translateM(tempModelMatrix,0,tempModelMatrix,0,x - offsetX,y - offsetY,z);
		rotateM(tempModelMatrix, 0, angleProgression, 0, 1, 0);
		rotateM(tempModelMatrix, 0, angleProgression2, 0, 0, 1);
		scaleM(modelMatrix,0,tempModelMatrix, 0, flipped*getScale(), flippedV*getScale(), getScale());
	}
	

	@Override
	public boolean step(float stepScale) {
		
		
		if(angle1Direction){
			angleProgression += 0.5 * stepScale;
			
			if(angleProgression >= 98)
				angle1Direction = false;
		}else{
			angleProgression -= 0.5 * stepScale;
			
			if(angleProgression <= 82)
				angle1Direction = true;
		}
		
		if(angle2Direction){
			angleProgression2 += (2+rotationSpeed) * stepScale;
		}else{
			angleProgression2 -= (2+rotationSpeed) * stepScale;
		}
		
		if(angleProgression > 360)
			angleProgression -= 360;
		if(angleProgression < 0)
			angleProgression += 360;
				
		if(slowDown){
			rotationSpeed *= 0.97;
			
			if(rotationSpeed <= 5){
				rotationSpeed = 0;
				slowDown = false;
			}
		}
		
		return true;
	}

	@Override
	public boolean coarseCollisionDetection(ColidableObjectInterface sprite) {
		float distance = getDistanceToObject(sprite.getPosition());
		if(distance < radius * scale + sprite.getCoarseCollisionRadius())
			return true;
		else
			access = false;
		return false;
	}
	
	@Override
	public boolean fineCollisionDetection(ColidableObjectInterface sprite){
		if(sprite == null || sprite.getClass() != Player.class)
			return false;
		
		
		
		Player player = (Player)sprite;
		
		float[] pos = player.getPosition();
		
		float dist = Math.abs(pos[1] - y);
		
		Log.d("Ring","Ring: "+accessType+" "+access);
		switch(accessType){
		case 1://left to right
			if(dist < (boundingBox[animationState].getHeight() * scale * 0.5 * 0.9)){
				float temp = (pos[0] - x);
				if(!access && temp < 0 && temp > -120){
					access = true;
					return false;
				}else if(access){
					if(temp > 0 && temp < 120 ){
						resolveCollision(sprite, pos);
						return true;
					}
				}
			}
			break;
		case 2://right to left
			if(dist < (boundingBox[animationState].getHeight() * scale * 0.5 * 0.9)){
				float temp = (x - pos[0]);
				if(!access && temp < 0 && temp > -120){
					access = true;
					return false;
				}else if(access){
					if(temp > 0 && temp < 120 ){
						resolveCollision(sprite, pos);
						return true;
					}
				}
			}
			
			break;
		default:
			if(dist < (boundingBox[animationState].getHeight() * scale * 0.5 * 0.9)){
				if(Math.abs(pos[0] - x) < 60){
					resolveCollision(sprite, pos);
					return true;
				}
			}
			break;
		}
		

		
		
		return false;	
	}
	
	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {
		if(activated){
			rotationSpeed += 6;
			
			animationState = 2;
			slowDown = true;
			nextRing = true;
		}
	}
	
	public void activate(){
		animationState = 0;
		activated = true;
	}
	
	public void deactivate(){
		animationState = 1;
		activated = false;
	}
	
	public boolean wasReached(){
		return nextRing;
	}
}

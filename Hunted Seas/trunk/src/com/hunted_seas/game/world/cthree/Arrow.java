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
public class Arrow extends Sprite {
	public static final String FOLDER = "common_elements/race/";
	public static String[] texture = {FOLDER + "arrow"};
	
	public static int[] model = {R.raw.arrow};
	
	
	LevelManager manager;
	
	private float[] nextRingPosition = {0,0,0};	
	
	public Arrow(float x, float y, float z, LevelManager manager){
		super(x, y, 100, 0, 1);
		collision = false;
		
		this.scale =  0.4f;
		
		this.manager = manager;
	}
	
	@Override
	public void calculateModelMatrix(){
		setIdentityM(tempModelMatrix, 0);
		translateM(tempModelMatrix,0,tempModelMatrix,0,x - offsetX,y - offsetY,z);
//		rotateM(tempModelMatrix, 0, angleProgression, 0, 1, 0);
		rotateM(tempModelMatrix, 0, angle,  0, 0, 1);
		scaleM(modelMatrix,0,tempModelMatrix, 0, flipped*getScale(), flippedV*getScale(), getScale());
	}
	

	public void move(){
		float[] pPos = manager.getPlayer().getPosition();

		float direction = getDirectionToPoint(pPos, nextRingPosition);
		
		angle = (float) Math.toDegrees(direction);
		

		float r = manager.getPlayer().getCoarseCollisionRadius() * 2f;
		
		x = pPos[0] - (float) (Math.cos(direction) * r);
		y = pPos[1] - (float) (Math.sin(direction) * r);		
	}
	
	@Override
	public boolean step(float stepScale) {

		return true;
	}

	@Override
	public boolean fineCollisionDetection(ColidableObjectInterface sprite){
		return false;	
	}
	
	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {
		
	}
	
	/**
	 * New coordinates.
	 * @param position [x, y, z]
	 * 
	 * @author Jani
	 */
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Next ring position.
	 * @param position [x, y, z]
	 */
	public void setNextRingPosition(float[] position){
		nextRingPosition = position;
	}
}

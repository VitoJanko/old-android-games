package com.hunted_seas.game.world.cthree;

import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import java.util.Random;

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
public class Key extends Sprite {
	public static final String FOLDER = "common_elements/collectable/";
	public static String texture = FOLDER + "key";
	
	public static int model = R.raw.key;
	
	private Random random = new Random();
	
	float angleProgression; 
	LevelManager manager;
	
	public Key(float x, float y, float z, LevelManager manager){
		super(x, y, z, 0, 1);
		collision = true;
		angleProgression = (float) (Math.random()*(2*Math.PI));
		this.scale = 0.5f;
		angle = random.nextFloat()*360f;
		this.manager = manager;
	}
	
	@Override
	public void calculateModelMatrix(){
		setIdentityM(tempModelMatrix, 0);
		translateM(tempModelMatrix,0,tempModelMatrix,0,x - offsetX,y - offsetY,z);
		rotateM(tempModelMatrix, 0, angle*(180.0f/(float)Math.PI), 0.5f, 1, 0.3f);
		scaleM(modelMatrix,0,tempModelMatrix, 0, flipped*getScale(), flippedV*getScale(), getScale());
	}
	

	@Override
	public boolean step(float stepScale) {
//		angle = (float) (Math.cos(angleProgression)*(Math.PI));
//		angleProgression += (Math.PI/60f)*stepScale;
		angle += (float) ((Math.PI/90f) * stepScale);
		if(angle > Math.PI * 2)
			angle = 0;
//		 
//		animationSpeedCounter += stepScale;
//		
//		if(animationSpeedCounter >= 3){ // frame speed time has passed (40 ms = 25fps)
//			animationSpeedCounter = 0;
//			horizontalSpeedholder = random.nextInt(7)-3;
//		}
//		
//		y += stepScale * speed;
//		x += stepScale * horizontalSpeedholder;
//		
//		if(y > master.level.getTopBound()+radius)
//			setDead(true);
		
		return true;
	}

	
	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {
		Player player = (Player)sprite; 
		player.eatFood(30);
		player.changeHealth(30);
		manager.setFinishLevel(true);
		setDead(true);
	}
}

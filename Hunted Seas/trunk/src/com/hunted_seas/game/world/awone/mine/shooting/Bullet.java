package com.hunted_seas.game.world.awone.mine.shooting;

import static com.hunted_seas.game.world.awone.mine.shooting.ShootingMineMaster.FOLDER;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.immersion.SoundPoolHelper;
import com.hunted_seas.game.immersion.VibratorHelper;
import com.hunted_seas.game.world.acommon.Bubble;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

public class Bullet extends Sprite {
	private static final String TAG = "Bullet";
	
	public static final String[] TEXTURE_ATLAS = {FOLDER+"bullets_atlas"};
	public static final int[] MODEL = {R.raw.bullet_a, R.raw.bullet_b3, R.raw.bullet_c2, R.raw.bullet_c4,
		R.raw.bullet_e1, R.raw.bullet_e2, R.raw.bullet_f, R.raw.bullet_g};
	
	int stateChanger = 0;
	
	float fuel = 2000;
	
	private LevelManager level;
	private int bubbleSpawnDelay = 0;
	
	public Bullet(float x, float y, float speed, float scale, float rotationAngle, LevelManager level){
		super(x,y,0,speed,360);
		
		this.level = level;
		
		this.angle = (float) Math.toRadians(rotationAngle);
		this.scale = scale;
		
		y += scale * 130 * Math.cos(angle);
		x += scale * 130 * Math.sin(angle);
	}
	
	@Override
	public boolean step(float stepScale) {
		fuel -= speed * stepScale;
		bubbleSpawnDelay ++;
		
		y += speed * stepScale * Math.cos(angle);
		x += speed * stepScale * Math.sin(angle);
		
		
		if(Math.random() < 0.20 && bubbleSpawnDelay > 30){
			level.spawnBubble(new Bubble(x - (float)(speed * 4 * stepScale * Math.sin(angle)),(float) (y - (speed * 4 * stepScale * Math.cos(angle))),z,(float) (Math.random()*20), (int) ((Math.random()*25)+20)));
		}
		
		stateChanger += stepScale;
		if(stateChanger > 2){
			stateChanger = 0;
			animationState++;
			if(animationState >= MODEL.length)
				animationState = 0;
		}
		
		if(fuel < 0 || isDead()){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {
		if(sprite.getClass() == Player.class){
			Player player = (Player) sprite;
			player.changeHealth(-25);
			float alpha = getDirectionToPoint(sprite.getPosition(),getPosition());
			player.graduallyMovePlayer(alpha, 40, 4f);
			
			level.Vibrate(VibratorHelper.pattern_SHORT_INTERVALS, -1);
			level.playSound(SoundPoolHelper.bubbles);
		}
		
		setDead(true);
	}
	
	
	
}

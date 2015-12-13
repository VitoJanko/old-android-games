package com.hunted_seas.game.world.awone.mine.shooting;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.immersion.SoundPoolHelper;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

public class ShootingMine extends Sprite{
	private static final String TAG = "ShootingMine";
	
	public static final int[] MODEL = {R.raw.shooting_mine};
	
	public ShootingMineLights lights;
	
	LevelManager level;
	ShootingMineMaster master;
	
	float animationCounter = 0;
	
	public ShootingMine(float x, float y, float scale, LevelManager level){
		super(x, y, 0, 0, 1);
		
		this.level = level;
		lights = new ShootingMineLights(x - (16 * scale),y + (12 * scale),scale);
		
		collision = true;
		animationCounter = (float) (Math.random() * 152);
	}
	
	public void loadParts(ShootingMineMaster master){
		this.master = master;
		master.addLights(lights);
	}
	
	@Override
	public boolean step(float stepScale) {
		animationCounter += stepScale;
		
		if(animationCounter > 150){
			lights.launchBullets();
			
			animationCounter = 0;
			
			if(visible)
				level.playSound(SoundPoolHelper.rocket);
			
			master.addNewBullet(new Bullet(x,y,20,1,0, level));
			master.addNewBullet(new Bullet(x,y,20,1,45, level));
			master.addNewBullet(new Bullet(x,y,20,1,90, level));
			master.addNewBullet(new Bullet(x,y,20,1,135, level));
			master.addNewBullet(new Bullet(x,y,20,1,180, level));
			master.addNewBullet(new Bullet(x,y,20,1,225, level));
			master.addNewBullet(new Bullet(x,y,20,1,270, level));
			master.addNewBullet(new Bullet(x,y,20,1,315, level));
		}
		
		
		return true;
	}

	@Override
	public void resolveCollision(ColidableObjectInterface sprite,float[] pointOfCollision) {
		if(sprite.getClass() == Player.class){
			Player player = (Player)sprite; 
			float alpha = getDirectionToPoint(sprite.getPosition(),getPosition());
			player.graduallyMovePlayer(1f,alpha, 40, 4f);
		}
	}

	
	
}

package com.hunted_seas.game.world.aone;

import android.app.Activity;
import android.widget.Toast;

import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.objects.masters.BachedBackgroundMaster;
import com.hunted_seas.game.objects.masters.BachedSpriteMaster;
import com.hunted_seas.game.objects.masters.SpriteMaster;
import com.hunted_seas.game.world.acommon.Bubble;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.SkyBox;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;

/**
 * Level 1. <br />
 * 
 * @see LevelManager
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class Level4_Blowfish extends LevelManagerTemplate{

	private SkyBox background;	
	private BachedBackgroundMaster bubbles;
	
	public Level4_Blowfish(Activity activity){
		super(activity,"Levels/Level3.lvl");
	}
	
	@Override
	public boolean loadResources() {
		super.loadResources();
		
		TEXTURE_SHADER_PROGRAM.useProgram();
		
		background = new SkyBox(this, backgroundVerticalOffset,backgroundHorizontalOffset);
		background.loadSkyBox(activity, TEXTURE_SHADER_PROGRAM, "world_one/background/blue512_8", new BoundingBox(leftBound,rightBound,topBound, bottomBound,-1000));
		
		BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
			
		bubbles = new BachedBackgroundMaster(this);
		bubbles.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM,Bubble.texture, Bubble.SIZE);
					
		
		return true;
	}

	public void setStartingSprites(){
		spawnStartingBubbles(600);
	}
	
	
	@Override
	public boolean gameOver() {
		if(finishLevel){
			// code runs in a thread
            activity.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                	   Toast.makeText(activity,"Level timer has finished, TEMP??",Toast.LENGTH_LONG).show();
                   }
            });
           
            return true;
        }else
			return false;
	}

	@Override
	public void draw(float[] viewProjectionMatrix) {
		if(isFinishLevel()) return; 
		
		GM.glDisableCulling();
		TEXTURE_SHADER_PROGRAM.useProgram();
		GM.glDisableDepthTesting();
		
		background.draw(viewProjectionMatrix, lights);
		
		BACHING_TEXTURE_DEPTH_SHADER_PROGRAM.useProgram();
		
		for(BachedSpriteMaster master : objects.darkBachedMasters)
			master.draw(viewProjectionMatrix, lights);
		
		BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
		
		bubbles.draw(viewProjectionMatrix, lights);
		for(BachedBackgroundMaster master: objects.backgroundMasters)
			master.draw(viewProjectionMatrix, lights);
		
		TEXTURE_SHADER_PROGRAM.useProgram();
		
		for(SkyBox s : objects.backgrounds)
			s.draw(viewProjectionMatrix, lights);
		
		GM.glEnadlbeDepthTesting();
		
		for(SpriteManagerInterface master: objects.multipartMasters)
			master.draw(viewProjectionMatrix, lights);
	

        TEXTURE_SHADER_PROGRAM.useProgram();
        
        getPlayer().draw(viewProjectionMatrix, lights);
        for(SpriteMaster master: objects.regularMasters)
        	master.draw(viewProjectionMatrix, lights);
        
        BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
        
		for(BachedSpriteMaster master : objects.bachedMasters)
			master.draw(viewProjectionMatrix, lights);
		
		for(SpriteManagerInterface master : objects.multipartBachedMasters){
			master.draw(viewProjectionMatrix, lights);
		}
		
        GM.glDisableDepthTesting();
		TEXTURE_SHADER_PROGRAM.useProgram();
		
		for(SkyBox s : objects.foregrounds)
			s.draw(viewProjectionMatrix, lights);
		
		GM.glEnadlbeDepthTesting();
	}

	@Override
	public void finish() {
		super.finish();
		setFinishLevel(true);
		bubbles.finish();
		background.finish();
	}

	@Override
	public void destroy() {
		super.destroy();
		background = null;
		bubbles = null;
	}

	
	@Override
	public void step(float stepScale) {
		super.step(stepScale);
		spawnBubbles(0.02f, stepScale);
		bubbles.step(stepScale);
	}
	
	
	public void spawnStartingBubbles(int ammount){
		for(int i=0; i<ammount; i++){
			float width = rightBound-leftBound;
			float height = topBound-bottomBound;
			Bubble b = new Bubble(leftBound+(float)(Math.random()*width),
					bottomBound+(float)(Math.random()*height), 
					-600 + (int)(Math.random()*400),
					(float)(Math.random()*6+1));
			bubbles.addElement(b);
		}
	}
	
	public void spawnBubbles(float probability, float stepScale){
		for(int i=0; i<stepScale*100; i++){
			float width = rightBound-leftBound;
			Bubble b = new Bubble(leftBound+(float)(Math.random()*width),bottomBound-100, 
					-600 + (int)(Math.random()*400),(float)(Math.random()*30+1));
			bubbles.addElement(b);
		}
	}
	
	@Override
	public boolean setUp_CollisionDetector(CollisionDetector_Manager collisionDetector) {
		super.setUp_CollisionDetector(collisionDetector);
		return true;
	}


}

package com.hunted_seas.game.world.aone;

import android.app.Activity;

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
public class Level0_Sandbox extends LevelManagerTemplate{

	private SkyBox background;	
	private BachedBackgroundMaster bubbles;
	private GreenFishMaster fishes;
	
	public Level0_Sandbox(Activity activity){
		super(activity,"Levels/TestLevel.lvl");
	}
	
	@Override
	public boolean loadResources() {
		super.loadResources();
		
		TEXTURE_SHADER_PROGRAM.useProgram();
		
		background = new SkyBox(this,backgroundVerticalOffset,backgroundHorizontalOffset);
		background.loadSkyBox(activity, TEXTURE_SHADER_PROGRAM, "world_one/background/blue512_8", new BoundingBox(leftBound,rightBound,topBound, bottomBound,-1000));
		
		BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
			
		bubbles = new BachedBackgroundMaster(this);
		bubbles.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM,Bubble.texture, Bubble.SIZE);
					
		fishes = new GreenFishMaster(this,true,0);
		fishes.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM);
		
		return true;
	}

	public void setStartingSprites(){
		spawnStartingBubbles(900);
	}
	
	
	@Override
	public boolean gameOver() {
//		if(temp > 20000) {
//			// code runs in a thread
//            activity.runOnUiThread(new Runnable() {
//                   @Override
//                   public void run() {
//                	   Toast.makeText(activity,"Level timer has finished, TEMP??",Toast.LENGTH_LONG).show();
//                   }
//            });
//           
//            return true;
//        }else
//			return false;
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
		
		BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
		
		fishes.draw(viewProjectionMatrix, lights);

        TEXTURE_SHADER_PROGRAM.useProgram();
        
        getPlayer().draw(viewProjectionMatrix, lights);
        for(SpriteMaster master: objects.regularMasters)
        	master.draw(viewProjectionMatrix, lights);
        
        BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
        
		for(BachedSpriteMaster master : objects.bachedMasters)
			master.draw(viewProjectionMatrix, lights);
		
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
		fishes.finish();
		background.finish();
	}

	@Override
	public void destroy() {
		super.destroy();
		background = null;
		bubbles = null;
		fishes = null;
	}

	
	@Override
	public void step(float stepScale) {
		super.step(stepScale);
		spawnBubbles(0.02f, stepScale);
		spawnFish(0.0001f, stepScale);
		spawnBackgroundFish(0.00002f, stepScale);
		
		bubbles.step(stepScale);
		fishes.step(stepScale);
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
	
	public void spawnFish(float probability, float stepScale){
		for(int i=0; i<stepScale*100; i++){
			if(Math.random()<probability){
				float height = topBound-bottomBound;
				if(Math.random()<0.5){
					GreenFish g = new GreenFish(rightBound+100,bottomBound+
							height*0.25f + (float)(Math.random()*height*0.60), (float)(Math.random()*5+3),-1,fishes);
					fishes.addFish(g);
				}
				else{
					GreenFish g = new GreenFish(leftBound-100,bottomBound+
							height*0.25f + (float)(Math.random()*height*0.60),(float)(Math.random()*5+3),1,fishes);
					fishes.addFish(g);
				}
			}
		}
	}
	
	public void spawnBackgroundFish(float probability, float stepScale){
//		for(int i=0; i<stepScale*100; i++){
//			if(Math.random()<probability){
//				float height = topBound-bottomBound;
//				float speed = (float)(Math.random()*5+3);
//				float depth = (float)(Math.random()*400 - 200);
//				
//				if(Math.random()<0.5){
//					float startX = rightBound+100;
//					float startY = bottomBound+ height*0.25f + (float)(Math.random()*height*0.75);
//					GreenFish g = new GreenFish(startX,startY, -800+depth, speed,-1, fishes);
//					GreenFish g1 = new GreenFish(startX+200,startY-200, -800+depth, speed,-1, fishes);
//					GreenFish g2 = new GreenFish(startX+400,startY-400, -800+depth, speed,-1, fishes);		
//					backgroundFishes.addElement(g);
//					backgroundFishes.addElement(g1);
//					backgroundFishes.addElement(g2);
//				}
//				else{
//					float startX = leftBound-100;
//					float startY = bottomBound+height*0.25f + (float)(Math.random()*height*0.75);
//					GreenFish g = new GreenFish(startX,startY, -800+depth,speed,1);
//					GreenFish g1 = new GreenFish(startX-200,startY-200, -800+depth,speed,1);
//					GreenFish g2 = new GreenFish(startX-400,startY-400, -800+depth,speed,1);
//					backgroundFishes.addElement(g);
//					backgroundFishes.addElement(g1);
//					backgroundFishes.addElement(g2);
//				}
//			}
//		}
	}


	@Override
	public boolean setUp_CollisionDetector(CollisionDetector_Manager collisionDetector) {
		super.setUp_CollisionDetector(collisionDetector);
		fishes.setUp_CollisionDetector(collisionDetector);
		return true;
	}


}

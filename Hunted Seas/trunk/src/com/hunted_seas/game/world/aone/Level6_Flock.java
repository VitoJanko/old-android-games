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
public class Level6_Flock extends LevelManagerTemplate{

	private SkyBox background;	
	private BachedBackgroundMaster bubbles;
	
	private BachedSpriteMaster food;
	
	private GreenFishMaster fishGreen;
	private GreenFishMaster fishRed;
	private GreenFishMaster fishYellow;
	private GreenFishMaster fishOrange;
	private GreenFishMaster fishTorpedo;
	
	public Level6_Flock(Activity activity){
		super(activity,"Levels/Level5.lvl");
	}
	
	private float greenFish = -1;
	private float formation = -1;
	private float sinus = -1;
	private float torpedo = -1;
	private float follower = -1;
	
	
	@Override
	public boolean loadResources() {
		super.loadResources();
		
		TEXTURE_SHADER_PROGRAM.useProgram();
		
		background = new SkyBox(this, backgroundVerticalOffset,backgroundHorizontalOffset);
		background.loadSkyBox(activity, TEXTURE_SHADER_PROGRAM, "world_one/background/blue512_8", new BoundingBox(leftBound,rightBound,topBound, bottomBound,-1000));
		
		BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
			
		bubbles = new BachedBackgroundMaster(this);
		bubbles.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM,Bubble.texture, Bubble.SIZE);
					
		food = new BachedSpriteMaster(this,true);
		food.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM,Food.TEXTURE, Food.model);
		
		fishGreen = new GreenFishMaster(this,true,0);
		fishGreen.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM);
		fishRed = new GreenFishMaster(this,true,1);
		fishRed.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM);
		fishTorpedo = new GreenFishMaster(this,true,2);
		fishTorpedo.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM);
		fishYellow = new GreenFishMaster(this,true,3);
		fishYellow.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM);
		fishOrange = new GreenFishMaster(this,true,4);
		fishOrange.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM);
		
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
        
        fishGreen.draw(viewProjectionMatrix, lights);
        fishYellow.draw(viewProjectionMatrix, lights);
        fishOrange.draw(viewProjectionMatrix, lights);
        fishRed.draw(viewProjectionMatrix, lights);
        fishTorpedo.draw(viewProjectionMatrix, lights);
        
        food.draw(viewProjectionMatrix, lights);
        
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
		food.finish();
		fishGreen.finish();
        fishYellow.finish();
        fishOrange.finish();
        fishRed.finish();
        fishTorpedo.finish();
	}

	@Override
	public void destroy() {
		super.destroy();
		background = null;
		bubbles = null;
		food=null;
		fishGreen=null;
        fishYellow=null;
        fishOrange=null;
        fishRed=null;
        fishTorpedo=null;
	}

	
	@Override
	public void step(float stepScale) {
		super.step(stepScale);
		spawnBubbles(0.02f, stepScale);
		spawnFood(0.0002f, stepScale);
		bubbles.step(stepScale);
		food.step(stepScale);
		fishGreen.step(stepScale);
        fishYellow.step(stepScale);
        fishOrange.step(stepScale);
        fishRed.step(stepScale);
        fishTorpedo.step(stepScale);
		if(getPlayer().getFoodEaten()>=60)
			finishLevel = true;
		
		int food = getPlayer().getFoodEaten();
		

		
		
		
		if(food<10)
			spawnFish(stepScale,30,40);
		if(food>=10 && food<20){
			spawnFish(stepScale,50,50);
			spawnFormation(stepScale,300,100);
		}
		if(food>=20 && food<30){
			spawnFish(stepScale,80,40);
			spawnFormation(stepScale,400,200);
			spawnRedFish(stepScale,100,100);
		}
		if(food>=30 && food<45){
			spawnFish(stepScale,100,40);
			spawnFormation(stepScale,400,200);
			spawnRedFish(stepScale,200,150);
			spawnTorpedo(stepScale,100,50);
		}
		if(food>=45 && food<60){
			spawnFish(stepScale,150,40);
			spawnFormation(stepScale,500,200);
			spawnRedFish(stepScale,400,150);
			spawnTorpedo(stepScale,100,50);
			spawnPurpleFish(stepScale,300,100);
		}
		
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
	
	public void spawnFood(float probability, float stepScale){
		for(int i=0; i<stepScale*100; i++){
			if(Math.random()<probability){
				float width = rightBound-leftBound;
				Food f = new Food(leftBound+(float)(Math.random()*width),bottomBound-100, 
						(float)(Math.random()*3+1),1);
				food.addElement(f);
			}
		}
	}
	
	public void spawnFish(float stepScale, float start, float random){
		greenFish-=stepScale;
		if(greenFish<0){
			greenFish = (float) (start+Math.random()*random);
			float height = topBound-bottomBound;
			if(Math.random()<0.5){
				GreenFish g = new GreenFish(rightBound+100,bottomBound+
						height*0.25f + (float)(Math.random()*height*0.75), (float)(Math.random()*5+3),-1,fishGreen);
				fishGreen.addFish(g);
			}
			else{
				GreenFish g = new GreenFish(leftBound-100,bottomBound+
						height*0.25f + (float)(Math.random()*height*0.75),(float)(Math.random()*5+3),1,fishGreen);
				fishGreen.addFish(g);
			}
		}
	}

	public void spawnRedFish(float stepScale, float start, float random){
		sinus-=stepScale;
		if(sinus<0){
			sinus = (float) (start+Math.random()*random);
			float height = topBound-bottomBound;
			float speed = (float)(Math.random()*5+5);

			if(Math.random()<0.5){
				float startX = rightBound+100;
				float startY = bottomBound+ height*0.25f + (float)(Math.random()*height*0.75);
				RedFish g = new RedFish(startX,startY, speed,-1,fishRed,0);
				RedFish g1 = new RedFish(startX+200,startY, speed,-1, fishRed, -(float)(Math.PI/8));
				RedFish g2 = new RedFish(startX+400,startY, speed,-1, fishRed, -(float)(Math.PI/4));		
				fishRed.addFish(g);
				fishRed.addFish(g1);
				fishRed.addFish(g2);
			}
			else{
				float startX = leftBound-100;
				float startY = bottomBound+height*0.25f + (float)(Math.random()*height*0.75);
				RedFish g = new RedFish(startX,startY,speed,1, fishRed,0);
				RedFish g1 = new RedFish(startX-200,startY,speed,1, fishRed, -(float)(Math.PI/8));
				RedFish g2 = new RedFish(startX-400,startY,speed,1, fishRed,-(float)(Math.PI/4));
				fishRed.addFish(g);
				fishRed.addFish(g1);
				fishRed.addFish(g2);
			}
		}
	}

	public void spawnFormation(float stepScale, float start, float random){
		formation-=stepScale;
		if(formation<0){
			formation = (float) (start+Math.random()*random);
			if(Math.random()<0.5)
				spawnFishWall();
			else
				spawnFishTriangle();
		}
	}

	public void spawnPurpleFish(float stepScale, float start, float random){
		follower-=stepScale;
		if(follower<0){
			follower = (float) (start+Math.random()*random);
			float height = topBound-bottomBound;
			float speed = (float)(Math.random()*12+5);
			if(Math.random()<0.5){
				float startX = rightBound+100;
				float startY = bottomBound+ height*0.25f + (float)(Math.random()*height*0.75);
				PurpleFish g = new PurpleFish(startX,startY, speed,-1,fishOrange,this,null);
				PurpleFish g1 = new PurpleFish(startX+200,startY, speed,-1, fishOrange, this,g);
				PurpleFish g2 = new PurpleFish(startX+400,startY, speed,-1,fishOrange, this,g1);		
				fishOrange.addFish(g);
				fishOrange.addFish(g1);
				fishOrange.addFish(g2);
			}
			else{
				float startX = leftBound-100;
				float startY = bottomBound+height*0.25f + (float)(Math.random()*height*0.75);
				PurpleFish g = new PurpleFish(startX,startY,speed,1, fishOrange,this,null);
				PurpleFish g1 = new PurpleFish(startX-200,startY,speed,1, fishOrange,this,g);
				PurpleFish g2 = new PurpleFish(startX-400,startY,speed,1, fishOrange,this,g1);
				fishOrange.addFish(g);
				fishOrange.addFish(g1);
				fishOrange.addFish(g2);
			}
		}
	}

	public void spawnTorpedo(float stepScale, float start, float random){
		torpedo-=stepScale;
		if(torpedo<0){
			torpedo = (float) (start+Math.random()*random);
			float height = topBound-bottomBound;
			float speed = (float)(20);

			if(Math.random()<0.5){
				float startX = rightBound+100;
				float startY = bottomBound+ height*0.25f + (float)(Math.random()*height*0.75);
				GreenFish g = new GreenFish(startX+200,startY, speed,-1, fishTorpedo);
				GreenFish g1 = new GreenFish(startX,startY-200, speed,-1, fishTorpedo);	
				fishTorpedo.addFish(g);
				fishTorpedo.addFish(g1);
			}
			else{
				float startX = leftBound-100;
				float startY = bottomBound+height*0.25f + (float)(Math.random()*height*0.75);
				GreenFish g = new GreenFish(startX-200,startY,speed,1,fishTorpedo);
				GreenFish g1 = new GreenFish(startX,startY-200,speed,1,fishTorpedo);
				fishTorpedo.addFish(g);
				fishTorpedo.addFish(g1);
			}
		}
	}

	public void spawnFishTriangle(){

		float height = topBound-bottomBound;
		float speed = (float)(Math.random()*8+4);

		if(Math.random()<0.5){
			float startX = rightBound+100;
			float startY = bottomBound+ height*0.25f + (float)(Math.random()*height*0.75);
			GreenFish g = new GreenFish(startX+200,startY, speed,-1, fishYellow);
			GreenFish g1 = new GreenFish(startX,startY-200, speed,-1, fishYellow);
			GreenFish g2 = new GreenFish(startX+200,startY-400, speed,-1, fishYellow);		
			fishYellow.addFish(g);
			fishYellow.addFish(g1);
			fishYellow.addFish(g2);
		}
		else{
			float startX = leftBound-100;
			float startY = bottomBound+height*0.25f + (float)(Math.random()*height*0.75);
			GreenFish g = new GreenFish(startX-200,startY,speed,1,fishYellow);
			GreenFish g1 = new GreenFish(startX,startY-200,speed,1,fishYellow);
			GreenFish g2 = new GreenFish(startX-200,startY-400,speed,1,fishYellow);
			fishYellow.addFish(g);
			fishYellow.addFish(g1);
			fishYellow.addFish(g2);
		}
	}




	public void spawnFishWall(){

		float height = topBound-bottomBound;
		float speed = (float)(Math.random()*8+4);

		if(Math.random()<0.5){
			float startX = rightBound+100;
			float startY = bottomBound+ height*0.25f + (float)(Math.random()*height*0.75);
			GreenFish g = new GreenFish(startX,startY, speed,-1, fishYellow);
			GreenFish g1 = new GreenFish(startX+200,startY-200, speed,-1, fishYellow);
			GreenFish g2 = new GreenFish(startX+400,startY-400, speed,-1, fishYellow);		
			fishYellow.addFish(g);
			fishYellow.addFish(g1);
			fishYellow.addFish(g2);
		}
		else{
			float startX = leftBound-100;
			float startY = bottomBound+height*0.25f + (float)(Math.random()*height*0.75);
			GreenFish g = new GreenFish(startX,startY,speed,1,fishYellow);
			GreenFish g1 = new GreenFish(startX-200,startY-200,speed,1,fishYellow);
			GreenFish g2 = new GreenFish(startX-400,startY-400,speed,1,fishYellow);
			fishYellow.addFish(g);
			fishYellow.addFish(g1);
			fishYellow.addFish(g2);
		}
	}

	
	@Override
	public boolean setUp_CollisionDetector(CollisionDetector_Manager collisionDetector) {
		super.setUp_CollisionDetector(collisionDetector);
		food.setUp_CollisionDetector(collisionDetector);
		fishGreen.setUp_CollisionDetector(collisionDetector);
        fishYellow.setUp_CollisionDetector(collisionDetector);
        fishOrange.setUp_CollisionDetector(collisionDetector);
        fishRed.setUp_CollisionDetector(collisionDetector);
        fishTorpedo.setUp_CollisionDetector(collisionDetector);
		return true;
	}


}

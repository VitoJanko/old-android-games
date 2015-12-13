package com.hunted_seas.game.world.btwo;

import android.app.Activity;
import android.widget.Toast;

import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.objects.masters.BachedBackgroundMaster;
import com.hunted_seas.game.objects.masters.BachedSpriteMaster;
import com.hunted_seas.game.world.acommon.Bubble;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.SkyBox;
import com.hunted_seas.game.world.aone.Food;
import com.hunted_seas.game.world.aone.GreenFish;
import com.hunted_seas.game.world.awone.mine.shooting.ShootingMine;
import com.hunted_seas.game.world.awone.mine.shooting.ShootingMineMaster;
import com.hunted_seas.game.world.cthree.Chili;
import com.hunted_seas.game.world.cthree.Coin;
import com.hunted_seas.game.world.player.Player;

/**
 * Level 1. <br />
 * 
 * @see LevelManager
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class Sandbox extends LevelManager{
	int temp = 0;
	
	/**
	 * Temp
	 */
	private SkyBox background;		
	
	private BachedSpriteMaster backgroundFishes;
	private BachedSpriteMaster food;

	private BachedSpriteMaster coins;
	private BachedSpriteMaster chilis;
//	private SpriteMaster ring;
//	private SpriteMaster arrow;
	
	private ShootingMineMaster shootingMines;
	
	private RaceTrack race;
		
		
	public Sandbox(Activity activity){
		super(activity);
		bottomBound = -1200;
		topBound = 1200;
		leftBound = -2000;
		rightBound = 20000;
	}


	
	@Override
	public boolean loadResources() {
		TEXTURE_SHADER_PROGRAM.useProgram();
		
		background = new SkyBox(this,backgroundVerticalOffset,backgroundHorizontalOffset);
		background.loadSkyBox(activity, TEXTURE_SHADER_PROGRAM, "world_one/background/blue512_8", new BoundingBox(leftBound,rightBound,topBound, bottomBound,-1000));
	
		setPlayer(new Player(this));
		getPlayer().loadSprite(activity, TEXTURE_SHADER_PROGRAM);
		
		
//		ring = new SpriteMaster(this,true);
//		ring.loadSprite(activity, TEXTURE_SHADER_PROGRAM, Ring.texture, Ring.model,false,true);
//		
//		arrow = new SpriteMaster(this,false);
//		arrow.loadSprite(activity, TEXTURE_SHADER_PROGRAM, Arrow.texture, Arrow.model,false,false);	
		
		race = new RaceTrack(this);
		race.loadSprite(activity, TEXTURE_SHADER_PROGRAM);
		
		shootingMines = new ShootingMineMaster(this);
		shootingMines.loadSprite(activity, TEXTURE_SHADER_PROGRAM);
		
		
		BACHING_TEXTURE_DEPTH_SHADER_PROGRAM.useProgram();
		
		backgroundFishes = new BachedSpriteMaster(this,false);
    	backgroundFishes.loadSprite(activity, BACHING_TEXTURE_DEPTH_SHADER_PROGRAM, GreenFish.texture, GreenFish.model);
		
    	
    	
				
		BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
		
		
		bubbles = new BachedBackgroundMaster(this);
		bubbles.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM,Bubble.texture, Bubble.SIZE);
		
		food = new BachedSpriteMaster(this,true);
		food.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM, Food.TEXTURE, Food.model);

		
		coins = new BachedSpriteMaster(this,true);
		coins.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM, Coin.texture, Coin.model);
		
		
		chilis = new BachedSpriteMaster(this,true);
		chilis.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM, Chili.texture, Chili.model);			
		return true;
	}

	public void setStartingSprites(){
		spawnStartingFood(100);
		spawnStartingBubbles(900);
		spawnStartingUrchin(5);
		spawnCoins(16);
		spawnChilis(16);
//		spawnRings(16);
//		spawnArrows(16);
		
		spawnMines(16);
		
		race.spawnTrack();
	}
	
	
	@Override
	public boolean gameOver() {
		// TODO Auto-generated method stub
		if(temp > 20000) {
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
		temp++;
		
		if(isFinishLevel()) return; //TODO konec livela
		
		TEXTURE_SHADER_PROGRAM.useProgram();
//		GM.glDisableDepthTesting();
		background.draw(viewProjectionMatrix, lights);
		
//		GM.glEnadlbeDepthTesting();
		
		BACHING_TEXTURE_DEPTH_SHADER_PROGRAM.useProgram();
		backgroundFishes.draw(viewProjectionMatrix, lights);
       
		

        
        BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
        
		GM.glEnadlbeCulling();
		coins.draw(viewProjectionMatrix, lights);
		chilis.draw(viewProjectionMatrix, lights);
		GM.glDisableCulling();
		

		TEXTURE_SHADER_PROGRAM.useProgram();

		GM.glEnadlbeCulling();
//        ring.draw(viewProjectionMatrix, lights);
		race.draw(viewProjectionMatrix, lights);
		GM.glDisableCulling();
		
        getPlayer().draw(viewProjectionMatrix, lights);
		
        shootingMines.draw(viewProjectionMatrix, lights);
        
        BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
//        GM.glDisableDepthTesting();
        bubbles.draw(viewProjectionMatrix, lights);
//        GM.glEnadlbeDepthTesting();
        
//		TEXTURE_SHADER_PROGRAM.useProgram();
//        arrow.draw(viewProjectionMatrix, lights);
	}

	@Override
	public void finish() {
		setFinishLevel(true);
		
//		bubblesBehind.finish();
		bubbles.finish();
		backgroundFishes.finish();
		background.finish();
//		blowFishes.finish();
//		urchin.finish();
		food.finish();
		coins.finish();
		chilis.finish();
//		ring.finish();
//		arrow.finish();
		getPlayer().finish();
		
		race.finish();
		shootingMines.finish();
		
		activity.finish();
	}

	@Override
	public void destroy() {
		background = null;
//		bubblesBehind = null;
		bubbles = null;
//		blowFishes = null;
		backgroundFishes = null;
//		urchin = null;
		setPlayer(null);
		food = null;
		coins = null;
		chilis = null;
//		ring = null;
		race = null;
		shootingMines = null;
	}

	
	@Override
	public void step(float stepScale) {
		if(isFinishLevel())
			return;
		
		spawnBubbles(0.02f, stepScale);
		spawnFood(0.0004f, stepScale);
		spawnBackgroundFish(0.00002f, stepScale);
		
		bubbles.step(stepScale);
		backgroundFishes.step(stepScale);

		
		food.step(stepScale);
//		urchin.step(stepScale);
		
		coins.step(stepScale);
		chilis.step(stepScale);
//		ring.step(stepScale);
//		arrow.step(stepScale);
		
		shootingMines.step(stepScale);
		
		race.step(stepScale);
	}
	
	public void spawnMines(int ammount){
		for(int i=0; i<ammount; i++){
			float width = rightBound-leftBound;
			float height = topBound-bottomBound;
			ShootingMine u = new ShootingMine(leftBound+(float)(Math.random()*width), bottomBound + (float)(Math.random()*height), 1, this);
			shootingMines.addNewMine(u);
		}
	}
	
	public void spawnCoins(int ammount){
		for(int i=0; i<ammount; i++){
			float width = rightBound-leftBound;
			float height = topBound-bottomBound;
			Coin u = new Coin(leftBound+(float)(Math.random()*width), bottomBound + (float)(Math.random()*height), 0);
			coins.addElement(u);
		}
	}
	
	public void spawnChilis(int ammount){
		for(int i=0; i<ammount; i++){
			float width = rightBound-leftBound;
			float height = topBound-bottomBound;
			Chili u = new Chili(leftBound+(float)(Math.random()*width), bottomBound + (float)(Math.random()*height), 0,this);
			chilis.addElement(u);
		}
	}
	
	public void spawnRings(int ammount){
//		for(int i=0; i<ammount; i++){
//			float width = rightBound-leftBound;
//			float height = topBound-bottomBound;
//			Ring u = new Ring(leftBound+(float)(Math.random()*width), bottomBound + (float)(Math.random()*height), 0,this);
//			ring.addElement(u);
//		}
	}
	
	public void spawnArrows(int ammount){
//		for(int i=0; i<ammount; i++){
//			float width = rightBound-leftBound;
//			float height = topBound-bottomBound;
//			Arrow u = new Arrow(leftBound+(float)(Math.random()*width), bottomBound + (float)(Math.random()*height),500,this);
//			arrow.addElement(u);
//		}
	}
	
	public void spawnStartingUrchin(int ammount){
		for(int i=0; i<ammount; i++){
			float width = rightBound-leftBound;
//			Urchin u = new Urchin(leftBound+(float)(Math.random()*width), -1750, (float)(Math.random()*2+4));
//			urchin.addElement(u);
		}
	}
	

	
	public void spawnStartingBubbles(int ammount){
		for(int i=0; i<ammount; i++){
			float width = rightBound-leftBound;
			float height = topBound-bottomBound;
			Bubble b = new Bubble(leftBound+(float)(Math.random()*width),
					bottomBound+(float)(Math.random()*height), 
					-600 + (int)(Math.random()*1000),
					(float)(Math.random()*6+1));
			bubbles.addElement(b);
		}
	}
	
	public void spawnStartingFood(int ammount){
		for(int i=0; i<ammount; i++){
			float width = rightBound-leftBound;
			float height = topBound-bottomBound;
			Food f = new Food(leftBound+(float)(Math.random()*width),
					bottomBound+(float)(Math.random()*height), 
					(float)(Math.random()*3+1),1);
			food.addElement(f);
		}
	}
	
	public void spawnBubbles(float probability, float stepScale){
		for(int i=0; i<stepScale*100; i++){
			float width = rightBound-leftBound;
			Bubble b = new Bubble(leftBound+(float)(Math.random()*width),bottomBound-100, 
					-600 + (int)(Math.random()*1000),(float)(Math.random()*30+1));
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
		this.collisionDetector = collisionDetector;
		collisionDetector.setPlayer(getPlayer());
		food.setUp_CollisionDetector(collisionDetector);
		coins.setUp_CollisionDetector(collisionDetector);
		chilis.setUp_CollisionDetector(collisionDetector);
//		ring.setUp_CollisionDetector(collisionDetector);
		race.setUp_CollisionDetector(collisionDetector);
		shootingMines.setUp_CollisionDetector(collisionDetector);
		return true;
	}


}

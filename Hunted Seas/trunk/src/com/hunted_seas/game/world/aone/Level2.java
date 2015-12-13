//package com.hunted_seas.game.world.aone;
//
//import android.app.Activity;
//import android.widget.Toast;
//
//import com.hunted_seas.game.collision.CollisionDetector_Manager;
//import com.hunted_seas.game.objects.masters.BachedSpriteMaster;
//import com.hunted_seas.game.world.acommon.Bubble;
//import com.hunted_seas.game.world.acommon.LevelManager;
//import com.hunted_seas.game.world.acommon.SkyBox;
//import com.hunted_seas.game.world.aone.background.BackGroundManager;
//import com.hunted_seas.game.world.aone.background.ForeGroundManager;
//import com.hunted_seas.game.world.player.Player;
//
///**
// * Level 1. <br />
// * 
// * @see LevelManager
// * @author Jani Bizjak <janibizjak@gmail.com>
// *
// */
//public class Level2 extends LevelManager{
//	int temp = 0;
//	
//	/**
//	 * Temp
//	 */
//	private SkyBox background;	
////	private BubbleMastermind bubblesBehind;
////	private BubbleMastermind bubblesInFront;
//	private BachedSpriteMaster urchin;
//	private BachedSpriteMaster bubbles;
//	
//	private BachedSpriteMaster backgroundFishes;
////	private BlowFishMaster blowFishes;
//	private BachedSpriteMaster food;
//	private BachedSpriteMaster blowFishes;
//	
//	
//	private GreenFishMaster fishes;
//	private GreenFishMaster fishesG;
//	
//	
//	/**
//	 * Background test
//	 */
//	private BackGroundManager backgroundManager;
//	private ForeGroundManager foregroundManager;
//	
//	public Level2(Activity activity){
//		super(activity);
//		bottomBound = -1800;
//		leftBound = -4000;
//		rightBound = 4000;
//		topBound = 1800;
//		
//	}
//
//
//	
//	@Override
//	public boolean loadResources() {
//		TEXTURE_SHADER_PROGRAM.useProgram();
//		
//		background = new SkyBox(backgroundVerticalOffset,backgroundHorizontalOffset);
//		background.loadSkyBox(activity, TEXTURE_SHADER_PROGRAM, "blue512", new float[] {leftBound,rightBound,bottomBound,topBound});
//		
//		backgroundManager = new BackGroundManager();
//		backgroundManager.loadSprite(activity, TEXTURE_SHADER_PROGRAM);
//		
//		foregroundManager = new ForeGroundManager();
//		foregroundManager.loadSprite(activity, TEXTURE_SHADER_PROGRAM);
////		
////		bubblesInFront = new BubbleMastermind(150,true);
////		bubblesInFront.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM);
//		
//		setPlayer(new Player(this));
//		getPlayer().loadSprite(activity, TEXTURE_SHADER_PROGRAM);
//		
//		BACHING_TEXTURE_DEPTH_SHADER_PROGRAM.useProgram();
//		
//		backgroundFishes = new BachedSpriteMaster(this,false);
////		backgroundFishes.loadSprite(activity, BACHING_TEXTURE_DEPTH_SHADER_PROGRAM, GreenFish.texture, GreenFish.model);
//				
//		BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
//		
//		blowFishes = new BachedSpriteMaster(this,false);
//		blowFishes.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM);
//		
//		bubbles = new BachedSpriteMaster(this,false);
////		bubbles.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM,Bubble.texture,Bubble.model);
//
//		fishes = new GreenFishMaster(this,true,0);
//		fishes.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM);
//		
//		fishesG = new GreenFishMaster(this,true,1);
//		fishesG.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM);
//		
//		food = new BachedSpriteMaster(this,true);
////		food.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM,Food.texture, Food.model);
//		
//		urchin = new BachedSpriteMaster(this,true);
////		urchin.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM, Urchin.texture, Urchin.model);
//		
//		return true;
//	}
//
//	public void setStartingSprites(){
//		spawnStartingFood(10);
//		spawnStartingBubbles(300);
//		spawnStartingUrchin(5);
//	}
//	
//	
//	@Override
//	public boolean gameOver() {
//		// TODO Auto-generated method stub
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
//	}
//
//	@Override
//	public void draw(float[] viewProjectionMatrix) {
//		temp++;
//		
//		if(finishLevel) return; //TODO konec livela
//		
//		TEXTURE_SHADER_PROGRAM.useProgram();
//		GM.glDisableDepthTesting();
//		background.draw(viewProjectionMatrix, lights);
//		
//		BACHING_TEXTURE_DEPTH_SHADER_PROGRAM.useProgram();
//		backgroundFishes.draw(viewProjectionMatrix, lights);
//		
//		TEXTURE_SHADER_PROGRAM.useProgram();
//		backgroundManager.draw(viewProjectionMatrix, lights);
//		GM.glEnadlbeDepthTesting();
//		
////		blowFishes.draw(viewProjectionMatrix);
//		
//		
//		
//		BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
//		fishes.draw(viewProjectionMatrix, lights);
//		fishesG.draw(viewProjectionMatrix, lights);
//		food.draw(viewProjectionMatrix, lights);
//		urchin.draw(viewProjectionMatrix, lights);
//		blowFishes.draw(viewProjectionMatrix, lights);
////		bubblesBehind.draw(viewProjectionMatrix);
//		
//
//        TEXTURE_SHADER_PROGRAM.useProgram();
//        getPlayer().draw(viewProjectionMatrix, lights);
//
//        
//        BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
//		bubbles.draw(viewProjectionMatrix, lights);
//        
//        
//        GM.glDisableDepthTesting();
//		TEXTURE_SHADER_PROGRAM.useProgram();
//		foregroundManager.draw(viewProjectionMatrix, lights);
//		GM.glEnadlbeDepthTesting();
//	}
//
//	@Override
//	public void finish() {
//		finishLevel = true;
//		
////		bubblesBehind.finish();
//		bubbles.finish();
//		fishes.finish();
//		fishesG.finish();
//		backgroundFishes.finish();
//		background.finish();
////		blowFishes.finish();
//		urchin.finish();
//		food.finish();
//		getPlayer().finish();
//		backgroundManager.finish();
//		foregroundManager.finish();
//		blowFishes.finish();
//		
//		activity.finish();
//	}
//
//	@Override
//	public void destroy() {
//		background = null;
////		bubblesBehind = null;
//		bubbles = null;
//		fishes = null;
//		fishesG = null;
////		blowFishes = null;
//		backgroundFishes = null;
//		urchin = null;
//		setPlayer(null);
//		food = null;
//		backgroundManager = null;
//		foregroundManager = null;
//	}
//
//	
//	@Override
//	public void step(float stepScale) {
//		spawnBubbles(0.02f, stepScale);
//		
//		spawnFood(0.00004f, stepScale);
//		spawnBackgroundFish(0.00002f, stepScale);
//		
//		if(getPlayer().getFood_eaten()<10){
//			spawnFish(0.0002f, stepScale);
//		}
//		
//		else if(getPlayer().getFood_eaten()<15){
//			spawnFishTriangle(0.00004f, stepScale);
//			spawnFishWall(0.00004f, stepScale);
//		}
//		else{
//			spawnFishTriangle(0.00008f, stepScale);
//			spawnFishWall(0.000008f, stepScale);
//			spawnRedFish(0.000008f, stepScale);
//		}
//		
//		
//		bubbles.step(stepScale);
//		fishes.step(stepScale);
//		fishesG.step(stepScale);
//		backgroundFishes.step(stepScale);
//
//		blowFishes.step(stepScale);
//		
//		food.step(stepScale);
//		urchin.step(stepScale);
//
//		backgroundManager.step(stepScale);
//		foregroundManager.step(stepScale);
//	}
//	
//	public void spawnStartingUrchin(int ammount){
//		for(int i=0; i<ammount; i++){
//			float width = rightBound-leftBound;
//			Urchin u = new Urchin(leftBound+(float)(Math.random()*width), -1750, (float)(Math.random()*2+4));
//			urchin.addElement(u);
//		}
//	}
//	
//	public void spawnStartingBubbles(int ammount){
//		for(int i=0; i<ammount; i++){
//			float width = rightBound-leftBound;
//			float height = topBound-bottomBound;
//			Bubble b = new Bubble(leftBound+(float)(Math.random()*width),
//					bottomBound+(float)(Math.random()*height), 
//					-600 + (int)(Math.random()*1000),
//					(float)(Math.random()*6+1));
//			bubbles.addElement(b);
//		}
//	}
//	
//	public void spawnStartingFood(int ammount){
//		for(int i=0; i<ammount; i++){
//			float width = rightBound-leftBound;
//			float height = topBound-bottomBound;
//			Food f = new Food(leftBound+(float)(Math.random()*width),
//					bottomBound+(float)(Math.random()*height), 
//					(float)(Math.random()*3+1),1);
//			food.addElement(f);
//		}
//	}
//	
//	public void spawnBubbles(float probability, float stepScale){
//		for(int i=0; i<stepScale*100; i++){
//			float width = rightBound-leftBound;
//			Bubble b = new Bubble(leftBound+(float)(Math.random()*width),bottomBound-100, 
//					-600 + (int)(Math.random()*1000),(float)(Math.random()*30+1));
//			bubbles.addElement(b);
//		}
//	}
//	
//	public void spawnFood(float probability, float stepScale){
//		for(int i=0; i<stepScale*100; i++){
//			if(Math.random()<probability){
//				float width = rightBound-leftBound;
//				Food f = new Food(leftBound+(float)(Math.random()*width),bottomBound-100, 
//						(float)(Math.random()*3+1),1);
//				food.addElement(f);
//			}
//		}
//	}
//	
//	

//	
//	
//	public void spawnBackgroundFish(float probability, float stepScale){
////		for(int i=0; i<stepScale*100; i++){
////			if(Math.random()<probability){
////				float height = topBound-bottomBound;
////				float speed = (float)(Math.random()*5+3);
////				float depth = (float)(Math.random()*400 - 200);
////				
////				if(Math.random()<0.5){
////					float startX = rightBound+100;
////					float startY = bottomBound+ height*0.25f + (float)(Math.random()*height*0.75);
////					GreenFish g = new GreenFish(startX,startY, -800+depth, speed,-1, fishes);
////					GreenFish g1 = new GreenFish(startX+200,startY-200, -800+depth, speed,-1, fishes);
////					GreenFish g2 = new GreenFish(startX+400,startY-400, -800+depth, speed,-1, fishes);		
////					backgroundFishes.addElement(g);
////					backgroundFishes.addElement(g1);
////					backgroundFishes.addElement(g2);
////				}
////				else{
////					float startX = leftBound-100;
////					float startY = bottomBound+height*0.25f + (float)(Math.random()*height*0.75);
////					GreenFish g = new GreenFish(startX,startY, -800+depth,speed,1);
////					GreenFish g1 = new GreenFish(startX-200,startY-200, -800+depth,speed,1);
////					GreenFish g2 = new GreenFish(startX-400,startY-400, -800+depth,speed,1);
////					backgroundFishes.addElement(g);
////					backgroundFishes.addElement(g1);
////					backgroundFishes.addElement(g2);
////				}
////			}
////		}
//	}
//
//
//	@Override
//	public boolean setUp_CollisionDetector(CollisionDetector_Manager collisionDetector) {
//		collisionDetector.setPlayer(getPlayer());
//		food.setUp_CollisionDetector(collisionDetector);
//		fishes.setUp_CollisionDetector(collisionDetector);
//		fishesG.setUp_CollisionDetector(collisionDetector);
//		urchin.setUp_CollisionDetector(collisionDetector);
//		return true;
//	}
//
//
//}

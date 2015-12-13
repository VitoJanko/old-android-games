package com.hunted_seas.game.world.acommon;

import static com.hunted_seas.debugging.Logger.logGameManager;
import static com.hunted_seas.game.world.acommon.GameSettings.GAME_FRAME_LENGTH;
import static com.hunted_seas.game.world.acommon.CommonVariables.FOV;
import static com.hunted_seas.game.world.acommon.CommonVariables.CAMERA_DEPTH;
import android.util.Log;

import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.graphics.Camera;
import com.hunted_seas.game.graphics.GameRenderer;

/**
 * This manages all games.
 * <br />
 * You provide level and it will then run through it's steps.<br />
 * Inicialization, loading of lvl, then new thread is started.
 * Objects are moved in this new thread via step() call.
 * Object are drawn from drawing thread via draw() call.
 * 
 * When lvl finishese cleanup follows.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class GameManager implements Runnable {
	private final String TAG = "GameManager";
	
	private LevelManager lvlManager;
	private Thread gameThread;
	private boolean forcedStop = false;
	
	private GameRenderer renderer;
	private Camera camera;
	
	private CollisionDetector_Manager collisionDetector;
	

	private long sleepTimeHolder = 0;
	
	private boolean lvlLoaded = false;
	
	private float ratio;
	
	private float cameraHorizontalOffset = 0;
	private float cameraVerticalOffset = 0;
	
	private float backgroundHorizontalOffset = 0;
	private float backgroundVerticalOffset = 0;
	
	
	private long frameLength = 0; //time it took to render previous frame
	private long frameRateTime = 0;
	
	
	private float playerMovementVectorX = 0;
	private float playerMovementVectorY = 0;

	private long[] gameFpsTime = new long[2];
	private float[] gameFpsHistory = new float[6];
	private float playerMovementScale = 0;
	
	private long startSleepTimer = 0;
	private long stopSleepTimer = 0;
	
	private float slowMotionModifier = 0.005f; 
	
	
	public GameManager(GameRenderer renderer,Camera camera,LevelManager lvlManager){
		this.renderer = renderer;
		this.camera = camera;
		this.lvlManager = lvlManager;
		
		collisionDetector = new CollisionDetector_Manager();
		
		configurateForScreen();
		
		prepareAndStartLvL();
	}
	
	/**
	 * Loads resources of lvl and if everthing is OK it starts new thread from which step() is called.
	 */
	public void prepareAndStartLvL(){
		if(lvlManager.loadResources() && lvlManager.setUp_CollisionDetector(collisionDetector)){
			lvlManager.setGameManager(this);
			lvlManager.setStartingSprites();
			System.gc(); //use garbage collector to free memory that was used for TEMP variables during initialisation of objects
						
			frameLength = (long) GAME_FRAME_LENGTH;
			frameRateTime = System.currentTimeMillis();
			gameFpsTime[0] = System.currentTimeMillis();
			gameFpsTime[1] = System.currentTimeMillis();
			
			gameThread = new Thread(this);
			gameThread.start();
			lvlLoaded = true;
			renderer.stopLoadingScreen();
			Log.d(TAG, "LvL loaded");
		}else{
			logGameManager(Log.WARN, "Level failed to load!");
		}
	}
	
	
	/**
	 * Runs until game is finished. <br />
	 * It runs on 120fps FPS!
	 */
	@Override
	public void run() {
		while(!forcedStop && !lvlManager.gameOver()){
			if(slowMotionModifier < 1){
				slowMotionModifier += 0.002f;
				if(slowMotionModifier >= 1){
					slowMotionModifier = 1;
					Log.d(TAG,"Slow motion ended.");
				}
			}
			
			
			startSleepTimer = System.currentTimeMillis();
			lvlManager.step(calculateStepScale() * slowMotionModifier);
			collisionDetector.detectCollisions();
			
			renderer.calculateGameFps();
			lvlManager.checkPlayerStats();
			
			frameLength = System.currentTimeMillis() - frameRateTime;
			frameRateTime = System.currentTimeMillis();
			stopSleepTimer = System.currentTimeMillis();
			
			try {
				
				if(( 7- (stopSleepTimer - startSleepTimer)) > 0)
				Thread.sleep((7 - (stopSleepTimer-startSleepTimer))); //60fps = 16ms per frame, 120fps = 8.3
				while((System.currentTimeMillis() - startSleepTimer) < 9){ // = <= 8
					//
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		finish();
	}
	
	/**
	 * Calculates how long thread needs to sleep in order to have 30 FPS.
	 * 
	 * @return sleep time
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	@Deprecated
	private long calculateSleepTime(){
		sleepTimeHolder = (long) (GAME_FRAME_LENGTH -(System.currentTimeMillis() - frameRateTime));
		if(sleepTimeHolder < 0)
			return 0;
		else
			return sleepTimeHolder;
	}
	
	private float calculateStepScale(){
		return frameLength / GAME_FRAME_LENGTH;
	}
	
	private float calculatePlayerStepScale(){
		delayScale();
		return gameFpsHistory[5] / GAME_FRAME_LENGTH;
	}
	
	/**
	 * When game is finished, this is called.
	 */
	private void finish(){
		//TODO
		logGameManager(Log.INFO, "GameManager has finished.");
		lvlManager.finish();
		lvlManager.destroy();
	}
	
	public void forceExit(){
		forcedStop = true;
	}
	
	/**
	 * Runs on drawing thread.
	 * @param viewProjectionMatrix
	 */
	public void draw(float[] viewProjectionMatrix){
		if(!forcedStop && lvlManager != null && lvlLoaded)
			lvlManager.draw(viewProjectionMatrix);
	}
	
	public void onConfigurationChanged(){
		configurateForScreen();
	}
	
	public void accelerometerVector(float x, float y){
		if(!lvlLoaded)
			return;
		
		playerMovementVectorX = x;
		playerMovementVectorY = y;
	}
	


	
	public void delayScale(){
		gameFpsTime[1] = System.currentTimeMillis();
		gameFpsHistory[5] = 0.35f * (gameFpsTime[1] - gameFpsTime[0]) + gameFpsHistory[4] * 0.25f + gameFpsHistory[3] * 0.2f + gameFpsHistory[2] * 0.12f + gameFpsHistory[1] * 0.06f + gameFpsHistory[0] * 0.02f;
		gameFpsHistory[0] = gameFpsHistory[1];
		gameFpsHistory[1] = gameFpsHistory[2];
		gameFpsHistory[2] = gameFpsHistory[3];
		gameFpsHistory[3] = gameFpsHistory[4];
		gameFpsHistory[4] = gameFpsHistory[5];
		gameFpsTime[0] = gameFpsTime[1];
	}
	
	public boolean movePlayerAndCamera(){
		if(!lvlLoaded)
			return false;
		
		playerMovementScale = calculatePlayerStepScale();
		
		lvlManager.movePlayer(playerMovementVectorX * playerMovementScale * slowMotionModifier, playerMovementVectorY * playerMovementScale * slowMotionModifier);
		lvlManager.moveCamera(camera, cameraHorizontalOffset, cameraVerticalOffset);
		
		return true;
	}
	
	
	private void configurateForScreen(){
		this.ratio = renderer.getRatio();
		
		cameraVerticalOffset= (float)(Math.tan(Math.PI/180 * FOV/2f) * Math.abs(CAMERA_DEPTH)); //TODO Temp camera distance
		cameraHorizontalOffset = (cameraVerticalOffset * ratio);
		
		
		backgroundVerticalOffset = (float) (Math.tan(Math.PI/180 * FOV/2f) * Math.abs(CAMERA_DEPTH+1000)); //TODO Temp camera distance
		backgroundHorizontalOffset = (backgroundVerticalOffset * ratio);

		
//		cameraVerticalOffset /= 2;
//		cameraHorizontalOffset /= 2;
//		
//		backgroundVerticalOffset /= 2;
//		backgroundHorizontalOffset /= 2;
		
		if(lvlManager != null)
			lvlManager.loadCameraOffset(cameraVerticalOffset, cameraHorizontalOffset, backgroundVerticalOffset, backgroundHorizontalOffset);
	}
	
	public void setUpdatedHealth(int currentHealth){
		renderer.updatePlayerHealth(currentHealth);
	}
	
	public void setCurrentFoodEaten(int currentFood){
		renderer.updateFoodEaten(currentFood);
	}
	
	public void glEnadlbeDepthTesting(){
		renderer.glEnadlbeDepthTesting();
	}
	
	public void glDisableDepthTesting(){
		renderer.glDisableDepthTesting();
	}
	
	public void glEnadlbeCulling(){
		renderer.glEnadlbeCulling();
	}
	
	public void glDisableCulling(){
		renderer.glDisableCulling();
	}
}

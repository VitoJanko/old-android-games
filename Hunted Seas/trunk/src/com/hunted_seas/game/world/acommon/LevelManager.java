package com.hunted_seas.game.world.acommon;

import java.util.HashMap;

import android.app.Activity;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.graphics.Camera;
import com.hunted_seas.game.immersion.SoundPoolHelper;
import com.hunted_seas.game.immersion.VibratorHelper;
import com.hunted_seas.game.objects.masters.BachedBackgroundMaster;
import com.hunted_seas.game.programs.BachingTextureShaderProgram;
import com.hunted_seas.game.programs.TextureShaderProgram;
import com.hunted_seas.game.world.player.Player;

/**
 * Interface for different levels. <br />
 * All levels need to have following methods in order to run in GameMangaer.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public abstract class LevelManager {
	private static final String TAG = "LevelManager";
	
	protected Activity activity;
	protected GameManager GM;
	
	protected VibratorHelper vibrator;
	protected SoundPoolHelper soundPool;
	
	protected final Lights lights;
	
	protected final TextureShaderProgram TEXTURE_SHADER_PROGRAM;
	protected final BachingTextureShaderProgram BACHING_TEXTURE_SHADER_PROGRAM;
	protected final BachingTextureShaderProgram BACHING_TEXTURE_DEPTH_SHADER_PROGRAM;
	
	protected float cameraHorizontalOffset = 0;
	protected float cameraVerticalOffset = 0;
	protected float backgroundHorizontalOffset = 0;
	protected float backgroundVerticalOffset = 0;
	
	protected float leftBound = -5000;
	protected float rightBound = 5000;
	protected float topBound = 2500;
	protected float bottomBound = -2500;
	
	public HashMap<Integer,Float[]> waypoints;
	
	protected float cameraDX;
	protected float cameraDY;
	protected float[] cameraTempCoordinates = new float[3];
	
	protected float[] cameraInfo = new float[4];
	
	protected boolean finishLevel = false;
	
	private Player player;
	
	protected CollisionDetector_Manager collisionDetector;
	
	protected BachedBackgroundMaster bubbles; 
	
	public LevelManager(Activity activity){
		this.activity = activity;
		BACHING_TEXTURE_DEPTH_SHADER_PROGRAM = new BachingTextureShaderProgram(activity,R.raw.texture_depth_fragment_shader);
		BACHING_TEXTURE_SHADER_PROGRAM = new BachingTextureShaderProgram(activity,R.raw.texture_optimized_fragment_shader);
		if(CommonVariables.RENDERER_TYPE == CommonVariables.PNG){
			TEXTURE_SHADER_PROGRAM = new TextureShaderProgram(activity, R.raw.texture_fragment_shader_png_blending);
		}else{
			TEXTURE_SHADER_PROGRAM = new TextureShaderProgram(activity,R.raw.texture_simple_fragment_shader);
		}

		
		lights = new Lights();
		vibrator = new VibratorHelper(activity);
		soundPool = new SoundPoolHelper(activity);
	}
	
	public abstract boolean loadResources();
	
	public abstract boolean setUp_CollisionDetector(CollisionDetector_Manager collisionDetector);
	
	public abstract void setStartingSprites();
	
	public boolean finishedPreparation(){
		return false;
	};
	
	public float[] getCameraBounds(){
		return new float[]{0,1};
	};
	
	public abstract boolean gameOver();
	
	public void movePlayer(float vectorX, float vectorY){
		if(getPlayer() == null) return;
		
	//	Log.d("LevelManager",0.1f*vectorX*vectorX+"");
		if(vectorX>=0)
			vectorX = (float) Math.pow(vectorX, 1.1);
		else
			vectorX = (float) -Math.pow(-vectorX, 1.1);
		if(vectorY>=0)
			vectorY = (float) Math.pow(vectorY, 1.1);
		else
			vectorY = (float) -Math.pow(-vectorY, 1.1);
	//Log.d("LevelManager",vectorX+"");
		
		float[] temp = getPlayer().getPosition(); //tmep
		
		int tmpRadi = (int) getPlayer().getCoarseCollisionRadius();
		
		
		float dx = vectorX;
		float dy = vectorY;
		
		if (temp[0]-tmpRadi+vectorX < leftBound){
			dx = leftBound + tmpRadi - temp[0];
		}
		
		if (temp[0] + tmpRadi + vectorX > rightBound){
			dx = rightBound - temp[0] - tmpRadi;
		}
		
		if (temp[1] + tmpRadi + vectorY > topBound){
			dy = topBound - temp[1] - tmpRadi;
		}
		
		if (temp[1]-tmpRadi+vectorY < bottomBound){
			dy = bottomBound + tmpRadi - temp[1];
		}

		getPlayer().movePlayer(dx, dy);
		lights.moveLight(getPlayer().getPosition());
	};
	
	public abstract void step(float stepScale);
	
	public abstract void draw(float[] viewProjectionMatrix);
	
	public void pause() {
	}
	
	public void resume() {
	}
	
	public void restart() {
	}
	
	public abstract void finish();
	
	public abstract void destroy();
	
	public void moveCamera(Camera camera, float horizontalOffset, float verticalOffset){		
		if(getPlayer() == null) return;
		getPlayer().step(0.5f); //TODO
		cameraTempCoordinates = camera.getCameraLocation();
		
		cameraDX = 0;
		cameraDY = 0;
		
		float[] temp = getPlayer().getPosition();
		
		float distance = (float) Math.sqrt(Math.pow(cameraTempCoordinates[0]-temp[0], 2)+Math.pow(cameraTempCoordinates[1]-temp[1],2))+0.0000000001f;
		
		float[] vektor = {temp[0]-cameraTempCoordinates[0], temp[1] - cameraTempCoordinates[1]};
		float[] direction = {vektor[0] / distance, vektor[1] / distance};

		float k = 1 / 35f;
		
		float x = k * (distance) * direction[0];
		float y = k * (distance) * direction[1];
		
		
		if(distance < 100){
			return;
		}else if (distance < 400){
			x /= 5;
			y /= 5;
		}
		
		if(cameraTempCoordinates[0] - horizontalOffset + x > leftBound && cameraTempCoordinates[0] + horizontalOffset + x < rightBound)
			cameraDX = x;
		if(cameraTempCoordinates[1] - verticalOffset + y > bottomBound && cameraTempCoordinates[1] + verticalOffset + y < topBound)
			cameraDY = y;

		
		camera.moveXY(cameraDX, cameraDY);
		
		cameraInfo[0] = camera.getCameraLocation()[0];
		cameraInfo[1] = camera.getCameraLocation()[1];
		cameraInfo[2] = horizontalOffset;
		cameraInfo[3] = verticalOffset;
	};
	
	public void checkPlayerStats(){
		if(getPlayer().didFoodChanged()){
			getPlayer().getFoodEaten();
			GM.setCurrentFoodEaten(getPlayer().getFoodEaten());
		}
		
		if(getPlayer().didHealthChanged()){
			getPlayer().getPlayerHealth();
			GM.setUpdatedHealth(getPlayer().getPlayerHealth());
		}
	};
	
	public void setGameManager(GameManager gameManager){
		this.GM = gameManager;
	};
	
	public void loadCameraOffset(float cameraVertical, float cameraHorizontal, float backgroundVertical, float backgroundHorizontal){
		this.cameraVerticalOffset = cameraVertical;
		this.cameraHorizontalOffset = cameraHorizontal;
		
		this.backgroundVerticalOffset = backgroundVertical;
		this.backgroundHorizontalOffset = backgroundHorizontal;
		
		cameraInfo[0] = 0;
		cameraInfo[1] = 0;
		cameraInfo[2] = cameraHorizontalOffset;
		cameraInfo[3] = cameraVerticalOffset;
	}
	
	public float getLeftBound() {
		return leftBound;
	}

	public void setLeftBound(float leftBound) {
		this.leftBound = leftBound;
	}

	public float getRightBound() {
		return rightBound;
	}

	public void setRightBound(float rightBound) {
		this.rightBound = rightBound;
	}

	public float getTopBound() {
		return topBound;
	}

	public void setTopBound(float topBound) {
		this.topBound = topBound;
	}

	public float getBottomBound() {
		return bottomBound;
	}

	public void setBottomBound(float bottomBound) {
		this.bottomBound = bottomBound;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	
	public void disableDepthTesting(){
		GM.glDisableDepthTesting();
	}
	
	public void enableDepthTesting(){
		GM.glEnadlbeDepthTesting();
	}

	public boolean isFinishLevel() {
		return finishLevel;
	}

	public void setFinishLevel(boolean finishLevel) {
		this.finishLevel = finishLevel;
	}
	
	public float[] getCameraInfo(){
		return cameraInfo;
	}
	
	public void spawnBubble(Sprite bubble){
		if(bubbles != null)
			bubbles.addElement(bubble,true);
	}
	
	public VibratorHelper getVibrator(){
		return vibrator;
	}
	
	/**
	 * @see VibratorHelper#vibrate(long[], int)
	 * 
	 * @author Jani
	 * @param pattern
	 * @param repeat
	 */
	public void Vibrate(long[] pattern, int repeat){
		vibrator.vibrate(pattern, repeat);
	}
	
	public SoundPoolHelper getSoundPool(){
		return soundPool;
	}
	
	public void playSound(int soundID){
		soundPool.playSound(soundID);
	}
}

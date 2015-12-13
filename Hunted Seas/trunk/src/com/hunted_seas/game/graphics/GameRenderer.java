package com.hunted_seas.game.graphics;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static com.hunted_seas.game.world.acommon.CommonVariables.BACK_CLIP;
import static com.hunted_seas.game.world.acommon.CommonVariables.FOV;
import static com.hunted_seas.game.world.acommon.CommonVariables.FRONT_CLIP;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.hunted_seas.game.graphics.loading.LoadingScreenAnimation;
import com.hunted_seas.game.immersion.VibratorHelper;
import com.hunted_seas.game.util.MatrixHelper;
import com.hunted_seas.game.world.acommon.CommonVariables;
import com.hunted_seas.game.world.acommon.GameManager;
import com.hunted_seas.game.world.acommon.GameSettings;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.aone.Leve2_Pilot;
import com.hunted_seas.game.world.aone.Level0_Sandbox;
import com.hunted_seas.game.world.aone.Level1_Tutorial;
import com.hunted_seas.game.world.aone.Level3_Flow;
import com.hunted_seas.game.world.aone.Level4_Blowfish;
import com.hunted_seas.game.world.aone.Level5_Food;
import com.hunted_seas.game.world.aone.Level6_Flock;
import com.hunted_seas.game.world.btwo.Sandbox;

/**
 * Renderer for game.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class GameRenderer implements Renderer{
	private final Activity ACTIVITY;
	private final HUD GAME_HUD;
	private final Camera CAMERA_VIEW;
	
	private final float[] PROJECTION_MATRIX = new float[16];
	private final float[] VIEW_PROJECTION_MATRIX = new float[16];
	
	private int world;
	private int level;
	
	private float ratio = 0;

	
	private LoadingScreenAnimation loadingScreen;
	
	/*
	 * TEMP ************************************************
	 * *****************************************************
	 */

	GameManager gameManager;
	LevelManager levelManager;
	
	/*
	 * *******************************************
	 * *********END TEMP**************************
	 * *******************************************
	 */
	
	/**
	 * Array for storing FPS history. <br />
	 * {old old, old, current}
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	private int[] rendererFpsHistory = {0,0,0};
	private int[] gameFpsHistory = {0,0,0};
	
	/**
	 * Array for storing time that is needed to calculate FPS. <br />
	 * {old, current}
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	private long[] rendererFpsTime = {0,0};
	private long[] gameFpsTime = {0,0};

	
	
	/**
	 * Constructor.
	 * 
	 * @param CONTEXT Activity CONTEXT.
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public GameRenderer(Activity activity,HUD game_hud,LoadingScreenAnimation loadingScreen, int world, int level){
		this.ACTIVITY = activity;
		this.GAME_HUD = game_hud;
		this.CAMERA_VIEW = new Camera();
		
		this.world = world;
		this.level = level;
		
		this.loadingScreen = loadingScreen;
	}
	
	/**
	 * Draws new frame. <br />
	 * First it clears old frame.
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	@Override
	public void onDrawFrame(GL10 unused) {
		glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
		
		gameManager.movePlayerAndCamera();
		multiplyMM(VIEW_PROJECTION_MATRIX, 0, PROJECTION_MATRIX, 0, CAMERA_VIEW.getCameraM(), 0);
		gameManager.draw(VIEW_PROJECTION_MATRIX);
		
		GAME_HUD.updateRendererFps(calculateRendererFps(), gameFpsHistory[2]);
	}

	public void updatePlayerHealth(int hp){
		GAME_HUD.updatePlayerHealth(hp);
	}
	
	public void updateFoodEaten(int foodEaten){
		GAME_HUD.updateFoodEaten(foodEaten);
	}
	
	/**
	 * When surface is changed. <br />
	 */
	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		glViewport(0, 0, width, height);
		
		ratio = 1.0f * width / height;
		if(gameManager != null)
			gameManager.onConfigurationChanged();
		
		MatrixHelper.perspectiveM(PROJECTION_MATRIX, FOV, ratio ,FRONT_CLIP,BACK_CLIP);
		
		multiplyMM(VIEW_PROJECTION_MATRIX, 0, PROJECTION_MATRIX, 0, CAMERA_VIEW.getCameraM(), 0);
				
		
		//TEMP
		switch(world){
		case 1:
			switch(level){
			case 1:
				levelManager = new Level0_Sandbox(ACTIVITY);
				break;
			case 2:
				levelManager = new Level1_Tutorial(ACTIVITY);
				break;
			case 3:
				levelManager = new Leve2_Pilot(ACTIVITY);
				break;
			case 4:
				levelManager = new Level3_Flow(ACTIVITY);
				break;
			case 5:
				levelManager = new Level4_Blowfish(ACTIVITY);
				break;
			case 6:
				levelManager = new Level5_Food(ACTIVITY);
				break;
			case 7:
				levelManager = new Level6_Flock(ACTIVITY);
				break;
			default:
				levelManager = new Sandbox(ACTIVITY);
				break;
			}
			break;
			
		default:
			levelManager = new Sandbox(ACTIVITY);
			break;
		}
		
		gameManager = new GameManager(this,CAMERA_VIEW,levelManager);
	}

	/**
	 * When surface is created. <br />
	 */
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		 String s = unused.glGetString(GL10.GL_EXTENSIONS);

		 String debugType = "";
		 
	     if (s.contains("GL_IMG_texture_compression_pvrtc")){
	    	 Log.d("GameRenderer","Compression type: "+"GL_IMG_texture_compression_pvrtc: ");
	    	 CommonVariables.RENDERER_TYPE = CommonVariables.PVRTC;
	    	 debugType = "PVRTC";
	          //Use PVR compressed textures         
	     }else if (s.contains("GL_AMD_compressed_ATC_texture") || s.contains("GL_ATI_texture_compression_atitc")){
	    	 Log.d("GameRenderer","Compression type: "+"GL_AMD_compressed_ATC_texture || GL_ATI_texture_compression_atitc:");
	    	 debugType = "ATI";
	          //Load ATI Textures     
	    	 CommonVariables.RENDERER_TYPE = CommonVariables.ATI;
	     }else if (s.contains("GL_OES_texture_compression_S3TC") || s.contains("GL_EXT_texture_compression_s3tc")){
	    	 Log.d("GameRenderer","Compression type: "+"GL_OES_texture_compression_S3TC || GL_EXT_texture_compression_s3tc");
	    	 debugType = "S3TC";
	         //Use DTX Textures
	    	 CommonVariables.RENDERER_TYPE = CommonVariables.NVIDIA;
	     }else{
	    	 Log.d("GameRenderer","Compression type: "+"no texture support: "+s);
	    	 debugType = "OTHER, png";
	         //Handle no texture compression founded.
	    	 CommonVariables.RENDERER_TYPE = CommonVariables.PNG;
	     }
		
	     if(!GameSettings.compressTexture){
	    	 Log.d("GameRenderer","Compression disabled!");
	         //Handle no texture compression founded.
	    	 CommonVariables.RENDERER_TYPE = CommonVariables.PNG;
	    	 debugType += " disabled, png on";
	     }
	     
		GAME_HUD.setRendererType(debugType);
		
		
		glClearColor(0.0f,0.0f,1.0f,0.0f);
		
		// No culling of back faces
		glEnable(GL_CULL_FACE); //TODO poglej ce je treba dodat  tudi front/back face
		 
		// Depth testing speeds up drawing since we don't have to draw everything if it's hidden.
		glEnable(GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		 
		//Enable blending
		glEnable(GL_BLEND);
		
		if(CommonVariables.RENDERER_TYPE == CommonVariables.PNG){
			glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		}else{
			glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		}
		

	}
	
	public void glEnadlbeCulling(){
		glEnable(GL_CULL_FACE);
	}
	
	public void glDisableCulling(){
		glDisable(GL_CULL_FACE);
	}
	
	public void glEnadlbeDepthTesting(){
		glEnable(GL_DEPTH_TEST);
	}
	
	public void glDisableDepthTesting(){
		glDisable(GL_DEPTH_TEST);
	}
	
	/**
	 * Calculates FPS. <br />
	 * History is weighed by 0.6, 0.3, 0.1 <br />
	 * 
	 * Note: 600 = 1000 * 0.6 <br />
	 * 
	 * @return float current FPS
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	private int calculateRendererFps(){
		rendererFpsTime[1] = System.currentTimeMillis();
		rendererFpsHistory[2] = (int) (600/(rendererFpsTime[1] - rendererFpsTime[0]+0.0000000001)+ rendererFpsHistory[1] * 0.3f + rendererFpsHistory[0] * 0.1f);
		rendererFpsHistory[0] = rendererFpsHistory[1];
		rendererFpsHistory[1] = rendererFpsHistory[2];
		rendererFpsTime[0] = rendererFpsTime[1];
		
		return rendererFpsHistory[2];
	}
	
	public int calculateGameFps(){
		gameFpsTime[1] = System.currentTimeMillis();
		gameFpsHistory[2] = (int) (600/(gameFpsTime[1] - gameFpsTime[0]+0.0000000001)+ gameFpsHistory[1] * 0.3f + gameFpsHistory[0] * 0.1f);
		gameFpsHistory[0] = gameFpsHistory[1];
		gameFpsHistory[1] = gameFpsHistory[2];
		gameFpsTime[0] = gameFpsTime[1];
		
		return gameFpsHistory[2];
	}
	
	/**
	 * Returns camera object, which is used for moving cameraMatrix.
	 * 
	 * @return Camera
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public Camera getCamera(){
		return CAMERA_VIEW;
	}
	
	/**
	 * Returns GameManager.
	 * 
	 * @see GameManager
	 * 
	 * @return GameManager
	 */
	public GameManager getGameManager(){
		return gameManager;
	}
	
	
	/**
	 * Returns game ratio (height / width)
	 * @return ratio
	 */
	public float getRatio(){
		return ratio;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void accelerometerVector(float x, float y){
		if(gameManager != null)
			gameManager.accelerometerVector(x, y);
	}
	
	public void forceExit(){
		if(gameManager != null)
			gameManager.forceExit();
	}
	
	public void startLoadingScreen(){
		if(loadingScreen != null)
			loadingScreen.startAnimation();
	}
	
	public void stopLoadingScreen(){
		if(loadingScreen != null)
			loadingScreen.stopAnimation();
	}
}

package com.hunted_seas.debugging;

import android.util.Log;

/**
 * Logging class. Here you can easily turn logging on/off for different kind of classes
 * and log types.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class Logger {
	private static final boolean DEBUG_LOGGING_ON = true;
	
	
	private static final String SPRITE_TAG = "Sprite";
	private static final String TEXTURE_HELPER_TAG = "TextureHelper";
	private static final String BACKGROUND_BOX_TAG = "BackgroundBox";
	private static final String GAME_MANAGER_TAG = "GameManager";
	private static final String CAMERA_TAG = "Camera";
	private static final String GAME_SENSOR_LISTENER_TAG = "GameSensorListener";
	private static final String GAME_ACTIVITY_TAG = "GameActivity";	
	private static final String SHADER_HELPER_TAG = "ShaderHelper";	
	private static final String GAME_RENDERER_TAG = "GameRenderer";
	
	private static final boolean SPRITE_ON_WARN = true;
	private static final boolean SPRITE_ON_DEBUG = true;
	private static final boolean SPRITE_ON_INFO = true;
	private static final boolean SPRITE_ON_ERROR = true;
	private static final boolean TEXTURE_HELPER_ON_WARN = true;
	private static final boolean TEXTURE_HELPER_ON_DEBUG = true;
	private static final boolean TEXTURE_HELPER_ON_INFO = true;
	private static final boolean TEXTURE_HELPER_ON_ERROR = true;
	private static final boolean BACKGROUND_BOX_ON_WARN = true;
	private static final boolean BACKGROUND_BOX_ON_DEBUG = false;
	private static final boolean BACKGROUND_BOX_ON_INFO = true;
	private static final boolean BACKGROUND_BOX_ON_ERROR = true;
	private static final boolean GAME_MANAGER_ON_WARN = true;
	private static final boolean GAME_MANAGER_ON_DEBUG = false;
	private static final boolean GAME_MANAGER_ON_INFO = false;
	private static final boolean GAME_MANAGER_ON_ERROR = true;
	private static final boolean CAMERA_LOGGING_ON_WARN = false;
	private static final boolean CAMERA_LOGGING_ON_DEBUG = false;
	private static final boolean CAMERA_LOGGING_ON_INFO = false;
	private static final boolean CAMERA_LOGGING_ON_ERROR = false;
	private static final boolean GAME_SENSOR_LISTENER_LOGGING_ON_WARN = true;
	private static final boolean GAME_SENSOR_LISTENER_LOGGING_ON_DEBUG = true;
	private static final boolean GAME_SENSOR_LISTENER_LOGGING_ON_INFO = false;
	private static final boolean GAME_SENSOR_LISTENER_LOGGING_ON_ERROR = true;
	private static final boolean GAME_ACTIVITY_LOGGING_ON_WARN = false;
	private static final boolean GAME_ACTIVITY_LOGGING_ON_DEBUG = false;
	private static final boolean GAME_ACTIVITY_LOGGING_ON_INFO = false;
	private static final boolean GAME_ACTIVITY_LOGGING_ON_ERROR = false;
	private static final boolean GAME_RENDERER_LOGGIN_ON_DEBUG = true;
	private static final boolean GAME_RENDERER_LOGGIN_ON_WARN = true;
	private static final boolean GAME_RENDERER_LOGGIN_ON_INFO = false;
	private static final boolean GAME_RENDERER_LOGGIN_ON_ERROR = true;
	private static final boolean SHADER_HELPER_LOGGING_ON_WARN = true;
	private static final boolean SHADER_HELPER_LOGGING_ON_DEBUG = true;
	private static final boolean SHADER_HELPER_LOGGING_ON_INFO = true;
	private static final boolean SHADER_HELPER_LOGGING_ON_ERROR = true;
	
	public static void logRender(int type, String info){
		switch(type){
		case Log.DEBUG:
			if(DEBUG_LOGGING_ON && GAME_RENDERER_LOGGIN_ON_DEBUG)
				Log.d(GAME_RENDERER_TAG, info);
			break;
		case Log.WARN:
			if(DEBUG_LOGGING_ON && GAME_RENDERER_LOGGIN_ON_WARN)
				Log.w(GAME_RENDERER_TAG, info);
			break;
		case Log.INFO:
			if(DEBUG_LOGGING_ON && GAME_RENDERER_LOGGIN_ON_INFO)
				Log.i(GAME_RENDERER_TAG, info);
			break;
		case Log.ERROR:
			if(DEBUG_LOGGING_ON && GAME_RENDERER_LOGGIN_ON_ERROR)
				Log.e(GAME_RENDERER_TAG, info);
			break;
		}
			
	}
	
	public static void logShaderHelper(int type, String info){
		switch(type){
		case Log.DEBUG:
			if(DEBUG_LOGGING_ON && SHADER_HELPER_LOGGING_ON_DEBUG)
				Log.d(SHADER_HELPER_TAG,info);
			break;
		case Log.WARN:
			if(DEBUG_LOGGING_ON && SHADER_HELPER_LOGGING_ON_WARN)
				Log.w(SHADER_HELPER_TAG,info);
			break;
		case Log.INFO:
			if(DEBUG_LOGGING_ON && SHADER_HELPER_LOGGING_ON_INFO)
				Log.i(SHADER_HELPER_TAG,info);
			break;
		case Log.ERROR:
			if(DEBUG_LOGGING_ON && SHADER_HELPER_LOGGING_ON_ERROR)
				Log.e(SHADER_HELPER_TAG,info);			
			break;
		}
		
	}
	
	@SuppressWarnings("unused")
	public static void logGameActivity(int type, String info){
		switch(type){
		case Log.DEBUG:
			if(DEBUG_LOGGING_ON && GAME_ACTIVITY_LOGGING_ON_DEBUG)
				Log.d(GAME_ACTIVITY_TAG,info);
			break;
		case Log.WARN:
			if(DEBUG_LOGGING_ON && GAME_ACTIVITY_LOGGING_ON_WARN)
				Log.w(GAME_ACTIVITY_TAG,info);
			break;
		case Log.INFO:
			if(DEBUG_LOGGING_ON && GAME_ACTIVITY_LOGGING_ON_INFO)
				Log.i(GAME_ACTIVITY_TAG,info);
			break;
		case Log.ERROR:
			if(DEBUG_LOGGING_ON && GAME_ACTIVITY_LOGGING_ON_ERROR)
				Log.e(GAME_ACTIVITY_TAG,info);			
			break;
		}
	}
	
	public static void logGameSensorListener(int type, String info){
		switch(type){
		case Log.DEBUG:
			if(DEBUG_LOGGING_ON && GAME_SENSOR_LISTENER_LOGGING_ON_DEBUG)
				Log.d(GAME_SENSOR_LISTENER_TAG,info);
			break;
		case Log.WARN:
			if(DEBUG_LOGGING_ON && GAME_SENSOR_LISTENER_LOGGING_ON_WARN)
				Log.w(GAME_SENSOR_LISTENER_TAG,info);
			break;
		case Log.INFO:
			if(DEBUG_LOGGING_ON && GAME_SENSOR_LISTENER_LOGGING_ON_INFO)
				Log.i(GAME_SENSOR_LISTENER_TAG,info);
			break;
		case Log.ERROR:
			if(DEBUG_LOGGING_ON && GAME_SENSOR_LISTENER_LOGGING_ON_ERROR)
				Log.e(GAME_SENSOR_LISTENER_TAG,info);			
			break;
		}
	}
	
	@SuppressWarnings("unused")
	public static void logCamera(int type, String info){
		switch(type){
		case Log.DEBUG:
			if(DEBUG_LOGGING_ON && CAMERA_LOGGING_ON_DEBUG)
				Log.d(CAMERA_TAG,info);
			break;
		case Log.WARN:
			if(DEBUG_LOGGING_ON && CAMERA_LOGGING_ON_WARN)
				Log.w(CAMERA_TAG,info);
			break;
		case Log.INFO:
			if(DEBUG_LOGGING_ON && CAMERA_LOGGING_ON_INFO)
				Log.i(CAMERA_TAG,info);
			break;
		case Log.ERROR:
			if(DEBUG_LOGGING_ON && CAMERA_LOGGING_ON_ERROR)
				Log.e(CAMERA_TAG,info);			
			break;
		}
	}
	
	
	@SuppressWarnings("unused")
	public static void logGameManager(int type, String info){
		switch(type){
		case Log.DEBUG:
			if(DEBUG_LOGGING_ON && GAME_MANAGER_ON_DEBUG)
				Log.d(GAME_MANAGER_TAG,info);
			break;
		case Log.WARN:
			if(DEBUG_LOGGING_ON && GAME_MANAGER_ON_WARN)
				Log.w(GAME_MANAGER_TAG,info);
			break;
		case Log.INFO:
			if(DEBUG_LOGGING_ON && GAME_MANAGER_ON_INFO)
				Log.i(GAME_MANAGER_TAG,info);
			break;
		case Log.ERROR:
			if(DEBUG_LOGGING_ON && GAME_MANAGER_ON_ERROR)
				Log.e(GAME_MANAGER_TAG,info);			
			break;
		}
	}
	
	public static void logBackgroundBox(int type, String info){
		switch(type){
		case Log.DEBUG:
			if(DEBUG_LOGGING_ON && BACKGROUND_BOX_ON_DEBUG)
				Log.d(BACKGROUND_BOX_TAG,info);
			break;
		case Log.WARN:
			if(DEBUG_LOGGING_ON && BACKGROUND_BOX_ON_WARN)
				Log.w(BACKGROUND_BOX_TAG,info);
			break;
		case Log.INFO:
			if(DEBUG_LOGGING_ON && BACKGROUND_BOX_ON_INFO)
				Log.i(BACKGROUND_BOX_TAG,info);
			break;
		case Log.ERROR:
			if(DEBUG_LOGGING_ON && BACKGROUND_BOX_ON_ERROR)
				Log.e(BACKGROUND_BOX_TAG,info);			
			break;
		}
	}
	
	public static void logTextureHelper(int type, String info){
		switch(type){
		case Log.DEBUG:
			if(DEBUG_LOGGING_ON && TEXTURE_HELPER_ON_DEBUG)
				Log.d(TEXTURE_HELPER_TAG,info);
			break;
		case Log.WARN:
			if(DEBUG_LOGGING_ON && TEXTURE_HELPER_ON_WARN)
				Log.w(TEXTURE_HELPER_TAG,info);
			break;
		case Log.INFO:
			if(DEBUG_LOGGING_ON && TEXTURE_HELPER_ON_INFO)
				Log.i(TEXTURE_HELPER_TAG,info);
			break;
		case Log.ERROR:
			if(DEBUG_LOGGING_ON && TEXTURE_HELPER_ON_ERROR)
				Log.e(TEXTURE_HELPER_TAG,info);			
			break;
		}
	}
	
	public static void logSprite(int type, String info){
		switch(type){
		case Log.DEBUG:
			if(DEBUG_LOGGING_ON && SPRITE_ON_DEBUG)
				Log.d(SPRITE_TAG,info);
			break;
		case Log.WARN:
			if(DEBUG_LOGGING_ON && SPRITE_ON_WARN)
				Log.w(SPRITE_TAG,info);
			break;
		case Log.INFO:
			if(DEBUG_LOGGING_ON && SPRITE_ON_INFO)
				Log.i(SPRITE_TAG,info);
			break;
		case Log.ERROR:
			if(DEBUG_LOGGING_ON && SPRITE_ON_ERROR)
				Log.e(SPRITE_TAG,info);			
			break;
		}
	}
}

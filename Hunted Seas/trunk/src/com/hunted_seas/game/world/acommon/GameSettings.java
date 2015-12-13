package com.hunted_seas.game.world.acommon;

/**
 * Static class where all settings are stored.
 * 
 * Settings about performance, sound etc...
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class GameSettings {
	public final static float TARGET_FPS_RATE = 30f; //25 fps = 40ms per frame ; 30 fps = 33ms per frame
	public static final float GAME_FRAME_LENGTH = 1000f/TARGET_FPS_RATE; //in milliseconds
	
	public static int player_skin_id = 0;
	
	public static boolean compressTexture = false;
	
	public static boolean invertX = false;
	public static boolean invertY = false;
	public static boolean switchXY = false;
	
	public static double offsetX = 0;
	public static double offsetY = 0;
	public static double offsetZ = 0;
	
	public static int accelerometerSensitivity = 6;
	
	/**
	 * Gravity force in "pixels" units per second.
	 */
	public static float g = -5;
}

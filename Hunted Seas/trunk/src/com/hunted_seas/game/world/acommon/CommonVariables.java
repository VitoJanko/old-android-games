package com.hunted_seas.game.world.acommon;

/**
 * Variables that are used through whole application.
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class CommonVariables {
	public static final int BYTES_PER_FLOAT = 4;
	public static final int BYTES_PER_SHORT = 2;
	
	public static int androidVersion = 9;
	
	/**
	 * Objects closer than this won't get drawn.
	 */
	public static final float FRONT_CLIP = 1500;
	/**
	 * Objects further than this won't get drawn.
	 */
	public static final float BACK_CLIP = 4000;
	
	public static final float CAMERA_DEPTH = 2000f;
	
	public static final int FOV = 50;
	
	
	public static int RENDERER_TYPE = 0;
	public static final int PNG = 0;
	public static final int PVRTC = 1;
	public static final int ATI = 2;
	public static final int NVIDIA = 3;
	
	
	public static final boolean DEBUG = true;
}

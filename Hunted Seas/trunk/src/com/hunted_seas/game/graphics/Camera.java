package com.hunted_seas.game.graphics;

import static com.hunted_seas.debugging.Logger.logCamera;
import static com.hunted_seas.game.world.acommon.CommonVariables.CAMERA_DEPTH;
import static android.opengl.Matrix.setLookAtM;
import android.util.Log;

/**
 * Main view camera.
 * <br />
 * Note: This doesn't set field of view for camera. That is set in renderer class.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class Camera {
	private final float[] cameraMatrix = new float[16];
		
	private float x;
	private float y;
	private float z;
	
	/**
	 * Direction of camera, where is camera pointing at.
	 * <br />
	 * x y z
	 */
	private float directionVector[] = {0f, 1f, 0f};
	
	public Camera(){
		x = 0f;
		y = 0f;
		z = CAMERA_DEPTH;
		
		moveCamera();
	}
	
	public Camera(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
		
		moveCamera();
	}
	
	/**
	 * Moves camera. <br />
	 * Where camera is moved is set by moveX and moveY.
	 * 
	 * @see moveX
	 * @see moveY
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	private void moveCamera(){
		logCamera(Log.INFO, "x: "+x+"  y: "+y);
		setLookAtM(cameraMatrix, 0, x, y, z, x, y, 0f, directionVector[0], directionVector[1], directionVector[2]);
	}
	
	/**
	 * Moves camera horizontally.
	 * 
	 * @param distanceX
	 */
	public void moveX(float distanceX){
		this.x += distanceX;
		moveCamera();
	}
	
	/**
	 * Moves camera vertically.
	 * @param distanceY
	 */
	public void moveY(float distanceY){
		this.y += distanceY;
		moveCamera();
	}
	
	/**
	 * Moves camera depth wise.
	 * @param distanceZ
	 */
	public void moveZ(float distanceZ){
		this.z += distanceZ;
		moveCamera();
	}
	
	/**
	 * Move camera both horizontal and vertical.
	 * @param distanceX
	 * @param distanceY
	 */
	public void moveXY(float distanceX, float distanceY){
		this.x += distanceX;
		this.y += distanceY;
		
		moveCamera();
	}
	
	
	/**
	 * Move camera both horizontal and vertical to the designated coordinates.
	 * @param distanceX
	 * @param distanceY
	 */
	public void moveToXY(float distanceX, float distanceY){
		this.x = distanceX;
		this.y = distanceY;
		
		moveCamera();
	}
	
	/**
	 * Move camera in all three directions
	 * @param distanceX
	 * @param distanceY
	 * @param distanceZ
	 */
	public void moveXYZ(float distanceX, float distanceY, float distanceZ){
		this.x += distanceX;
		this.y += distanceY;
		this.z += distanceZ;
		moveCamera();
	}
	
	/**
	 * Move camera in all three directions.
	 * @param distanceXYZ
	 */
	public void moveXYZ(float[] distanceXYZ){
		this.x += distanceXYZ[0];
		this.y += distanceXYZ[1];
		this.z += distanceXYZ[2];
		
		moveCamera();
	}
	
	/**
	 * @return camera matrix. (mVp)
	 */
	public float[] getCameraM(){
		return cameraMatrix;
	}
	
	/**
	 * Return camera location.
	 * 
	 * @return [x,y,z]
	 */
	public float[] getCameraLocation(){
		return new float[]{x,y,z};
	}
}

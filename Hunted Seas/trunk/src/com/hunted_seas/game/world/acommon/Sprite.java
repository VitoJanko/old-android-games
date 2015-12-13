package com.hunted_seas.game.world.acommon;

import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import java.util.LinkedList;

import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.data.LineSegment;
import com.hunted_seas.game.objects.masters.BachedSpriteMaster;

/**
 * Each sprite object must compile to this interface.
 * <br />
 * While resources (texture, vertex data) are the same for all sprites of one kind, 
 * position, collision etc. are not. This basically holds these variables that are unique between
 * same types of sprites.
 * 
 * @see SpriteManagerInterface
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public abstract class Sprite implements ColidableObjectInterface{
	protected final float[] modelMatrix = new float[16];
	
	protected final float[] tempModelMatrix = new float[16];
	protected final float[] rotationModelMatrix = new float[16];
	protected final float[] scaleModelMatrix = new float[16];
	
	protected float x = 0;
	protected float y = 0;
	protected float z = 0;
	
	protected float alpha = 1.0f;
	
	protected int texture = -1;
	protected int model = -1;
	protected boolean collision = false;
	
	protected boolean draw = true;
	
	/**
	 * If on screen
	 */
	protected boolean visible = false;
	
	/**
	 * Angle of rotation
	 */
	public float angle = 0;
	protected float scale = 1;
	
	protected float offsetX = 0;
	protected float offsetY = 0;
	
	protected float speed = 0;
	/**
	 * Direction of movement
	 */
	protected float direction = 0;
	protected float radius = 0;
	protected BoundingBox[] boundingBox;
	
	/**
	 * State in which animation is. States go from 0 to the number of .raw and .texture models loaded.
	 */
	protected int animationState;
	
	private boolean dead = false;
//	protected SpriteMaster master;
	protected SpriteManagerInterface master;

	protected int flipped = 1;
	protected int flippedV = 1;
	
	protected int index = 0;
	
	private LinkedList<LinkedList<LineSegment>> lineSegments = new LinkedList<LinkedList<LineSegment>>();
	
	public Sprite(){};
	
	public Sprite(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Sprite(float x, float y, float z, float speed, float direction){
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
		this.speed = speed;
	}
	
	public void setAttributes(float x, float y, float z, float scale, float angle,boolean reflH,boolean reflV,int index){
		this.x = x;
		this.y = y;
		this.index = index;
		if(z!=-999)
			this.z = z;
		this.scale = scale;
		this.angle = (float) Math.toRadians(angle);
		if(reflH)
			flipped = -1;
		if(reflV)
			flippedV = -1;
	}
	
	public void reSpawn() {}
	
	public void setAlpha(float alpha){
		this.alpha = alpha;
	}
	
	public float getAlpha(){
		return alpha;
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public void setLocation(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void setBoundingBox(BoundingBox[] boundingBox){
		this.boundingBox=boundingBox;
		radius = (float) Math.sqrt( Math.pow(boundingBox[animationState].getWidth(), 2) + 
				Math.pow(boundingBox[animationState].getHeight(),2)) / 2f;
	}
	
	public void setMaster(SpriteManagerInterface master){
		this.master = master;
	}
	
	public void setMaster(BachedSpriteMaster master){
		this.master = master;
	}
	
	public float[] getModelMatrix() {
		return modelMatrix;
	}
	
	public void calculateModelMatrix(){
		setIdentityM(tempModelMatrix, 0);
		translateM(tempModelMatrix,0,tempModelMatrix,0,x - offsetX,y - offsetY,z);
		rotateM(tempModelMatrix, 0, angle*(180.0f/(float)Math.PI), 0, 0, -1);
		scaleM(modelMatrix,0,tempModelMatrix, 0, flipped*getScale(), flippedV*getScale(), 1f);
	}
	
	public float[] getPosition(){
		return new float[]{x,y,z};
	}
	
	public abstract boolean step(float stepScale);
	
	public boolean coarseCollisionDetection(ColidableObjectInterface sprite) {
		float distance = getDistanceToObject(sprite.getPosition());
		if(distance < radius * scale +sprite.getCoarseCollisionRadius())
			return true;
		return false;
	}
	

	protected float getDistanceToObject(float[] objectPosition){
		return (float) Math.sqrt( Math.pow(x - objectPosition[0], 2) + Math.pow(y - objectPosition[1], 2));
	}
	
	public boolean fineCollisionDetection(ColidableObjectInterface sprite){
		if(sprite == null)
			return false;
		
		LinkedList<LineSegment> otherSegments = sprite.getCurrentLineSegment();
		LinkedList<LineSegment> segments = getCurrentLineSegment();
		
		for(LineSegment seg : segments){
			seg.moveColidableObject(getModelMatrix());
		}
		
		
		for(LineSegment pSeg : otherSegments){
//			pSeg.moveColidableObject(sprite.getModelMatrix());
			for(LineSegment seg : segments){
				if(pSeg.doIntersect(seg)){
					resolveCollision(sprite, pSeg.getPointOfCollisoin());
					return true;
				}
			}
		}
		
		
		return false;	
	}
	
	public abstract void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision);
		
	public float getDirectionToPoint(float[] from, float[] to){
		float alpha = (float) Math.atan((float)(Math.abs(to[1]-from[1]))/Math.abs(to[0]-from[0]));
		if(from[0]<=to[0])
			if(from[1]<=to[1])
				return (float)Math.PI+alpha;
			else
				return (float)Math.PI-alpha;
		else
			if(from[1]<=to[1])
				return -alpha;
			else
				return alpha;
	}
	
	public float getCoarseCollisionRadius() {
		return radius;
	}

	public BoundingBox getCoarseCollisionSquare() {
		return boundingBox[animationState];
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public int getAnimationState() {
		return animationState;
	}

	public void setAnimationState(int stateType) {
		this.animationState = stateType;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	public void setLineSegments(LinkedList<LinkedList<LineSegment>> segments){
		this.lineSegments = segments;
	}
	
	public LinkedList<LineSegment> getCurrentLineSegment(){
		return getLineSegments().get(animationState);
	}

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public int getTexture() {
		return texture;
	}

	public void setTexture(int texture) {
		this.texture = texture;
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public BoundingBox[] getBoundBox() {
		return boundingBox;
	}

	public LinkedList<LinkedList<LineSegment>> getLineSegments() {
		return lineSegments;
	}
	
	public boolean draw(){
		return draw;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
	public boolean getVisible(){
		return visible;
	}
}

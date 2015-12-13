package com.hunted_seas.game.world.acommon;

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
public interface SpriteInterface {	
	public abstract int getState();
	public abstract float[] getPosition();
	public abstract void setRadius(float radius);
	public abstract boolean step(float stepScale);
	public abstract boolean coarseCollisionDetection(SpriteInterface sprite);
	public abstract boolean fineCollisionDetection(SpriteInterface sprite);
	public abstract float getCoarseCollisionRadius();
	public abstract float[] getCoarseCollisionSquare();
	public abstract float[] getModelMatrix();
	public abstract void reSpawn();
}

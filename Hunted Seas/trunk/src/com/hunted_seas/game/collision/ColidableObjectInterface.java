package com.hunted_seas.game.collision;

import java.util.LinkedList;

import com.hunted_seas.game.data.LineSegment;


public interface ColidableObjectInterface {
	public boolean coarseCollisionDetection(ColidableObjectInterface sprite);
	public boolean fineCollisionDetection(ColidableObjectInterface sprite);
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision);
	
	public float getCoarseCollisionRadius();
	
	public LinkedList<LineSegment> getCurrentLineSegment();
	
	public float[] getModelMatrix();
	public float[] getPosition();
}

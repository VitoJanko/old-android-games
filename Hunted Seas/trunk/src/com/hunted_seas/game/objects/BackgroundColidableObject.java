package com.hunted_seas.game.objects;

import java.util.LinkedList;

import android.content.Context;

import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.data.LineSegment;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.world.player.Player;

public class BackgroundColidableObject extends BackgroundRegularObject implements ColidableObjectInterface {
	
	
	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {}

	
	private float[] lastPPos = {0,0};
	
	@Override
	public boolean coarseCollisionDetection(ColidableObjectInterface sprite) {
		float[] position = sprite.getPosition();
		float radius = sprite.getCoarseCollisionRadius();
		
		if( position[0] > (x + boundingBox[0].left - radius) && (x + boundingBox[0].right + radius) > position[0])
			if( position[1] > (y + boundingBox[0].bottom - radius) && (y + boundingBox[0].top + radius) > position[1])
				return true;
		lastPPos = position;
		return false; //TODO test
	}

	@Override
	public boolean fineCollisionDetection(ColidableObjectInterface sprite) {
		return fineCollisionDetection(sprite, false);
	}
	
	public boolean fineCollisionDetection(ColidableObjectInterface sprite, boolean query) {
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
					if(!query)
						resolveCollision(sprite, pSeg.getPointOfCollisoin());
					return true;
				}
			}
		}
		
		lastPPos = sprite.getPosition();
		return false;	
	}
	
	public boolean fineCollisionDetection(ColidableObjectInterface sprite, float dx, float dy) {
		if(sprite == null)
			return false;
		
		LinkedList<LineSegment> otherSegments = sprite.getCurrentLineSegment();
		LinkedList<LineSegment> segments = getCurrentLineSegment();
		
		for(LineSegment seg : otherSegments){
			seg.moveColidableObject(dx,dy);
		}
		
		
		for(LineSegment pSeg : otherSegments){
//			pSeg.moveColidableObject(sprite.getModelMatrix());
			for(LineSegment seg : segments){
				if(pSeg.doIntersect(seg)){
					return true;
				}
			}
		}
		
		
		return false;	
	}

	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {
		if(sprite.getClass() == Player.class){
			Player player = (Player)sprite;
			player.lockMovement(true);
			float[] xy = player.getPosition();
//			float[] xy = lastPPos;
			
			
			double distance = Math.sqrt(Math.pow((xy[0]-lastPPos[0]), 2) + Math.pow((xy[1]-lastPPos[1]), 2));
			double alpha = getDirectionToPoint(sprite.getPosition(),lastPPos);
			
			if(distance < 0.005){
				distance = Math.sqrt(Math.pow((xy[0]-pointOfCollision[0]), 2) + Math.pow((xy[1]-pointOfCollision[1]), 2));
				alpha = getDirectionToPoint(sprite.getPosition(),pointOfCollision) + Math.PI;
			}
			
//			double razlika = player.getCoarseCollisionRadius() - distance;
			double razlika = distance;
			
//			if(razlika == 0){
//				player.lockMovement(false);
//				return;
//			}
			
			double dy = -(Math.sin(alpha) * razlika);
			double dx = -(Math.cos(alpha) * razlika);
			
			double[] criticalD = {dx,dy};
			boolean criticalMass = false;
			
			dx /= 2d; dy /= 2;
			

			double counterDX = 0;
			double counterDY = 0;
			

			for(int i=0; i < 10; i++){
				counterDX += dx;
				counterDY += dy;
				
				if(fineCollisionDetection(sprite, (float)dx,(float)dy)){//tudi na pol je collision
					dx = -(dx / 2d);
					dy = -(dy / 2d);
					criticalMass = true;
				}else{//ni collisiona na pol
					dx = (dx / 2d);
					dy = (dy / 2d);
					criticalMass = false;
					criticalD[0] = counterDX;
					criticalD[1] = counterDY;
				}

				if(Math.abs(dx) < 0.005 && Math.abs(dy) < 0.005)
					break;
			}
			if(criticalMass){
				dy = criticalD[0];
				dx = criticalD[1];
			}
			
			
			player.movePlayer((float)(counterDX), (float)(counterDY),true);
			player.lockMovement(false);
		}
	}
	
	public double getDirectionToPoint(float[] from, float[] to){
		double alpha =  Math.atan((Math.abs(to[1]-from[1]))/Math.abs(to[0]-from[0]));
		if(from[0]<=to[0])
			if(from[1]<=to[1])
				return Math.PI+alpha;
			else
				return Math.PI-alpha;
		else
			if(from[1]<=to[1])
				return -alpha;
			else
				return alpha;
	}

	@Override
	public float[] getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getCoarseCollisionRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LinkedList<LineSegment> getCurrentLineSegment() {
		return lineSegments;
	}

	@Override
	public float[] getModelMatrix() {
		return backgroundModelMatrixes.get(0); //TODO temp!
	}

}

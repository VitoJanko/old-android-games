package com.hunted_seas.game.world.acommon;

import java.util.LinkedList;

import android.util.Log;

import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.data.LineSegment;
import com.hunted_seas.game.world.player.Player;

public class SolidSprite extends Sprite{
	
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
			
//			double stepSize = razlika / 100d;
			
			double counterDX = 0;
			double counterDY = 0;
			
			Log.d("BackgroundColidableObject","--------------------------------------------------------");
			Log.d("BackgroundColidableObject", "Start: "+xy[0]+","+xy[1]+"  "+pointOfCollision[0]+","+pointOfCollision[1]+"   "+distance+"  rz: "+razlika);
			Log.d("BackgroundColidableObject"," before =  "+dx+" "+dy+"   : "+pointOfCollision[0]+","+pointOfCollision[1]+"  r: "+player.getCoarseCollisionRadius());


			for(int i=0; i < 10; i++){
//				double y = Math.sin(alpha) * stepSize;
//				double x = Math.cos(alpha) * stepSize;
				
//				player.movePlayer((float)(dx), (float)(dy),true);
				
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
				
				Log.d("BackgroundColidableObject","i: "+i+"   "+dx+" "+dy);
				
				if(Math.abs(dx) < 0.005 && Math.abs(dy) < 0.005)
					break;
				
//				counterDX += x;
//				counterDY += y;
//				
//				player.movePlayer((float)x, (float)y);
//				
//				if(!fineCollisionDetection(sprite, true)){
//					Log.d("BackgroundColidableObject","Collision step worked at: "+i);
//					return;
//				}
			}
			if(criticalMass){
				dy = criticalD[0];
				dx = criticalD[1];
				Log.d("BackgroundColidableObject","Critical mass:    "+dx+" "+dy);
			}
			
			
			player.movePlayer((float)(counterDX), (float)(counterDY),true);
//			lastPPos = player.getPosition();
			Log.d("BackgroundColidableObject"," ::  "+counterDX+"  "+counterDY+" | "+xy[0]+","+xy[1]+"  "+player.getPosition()[0]+","+player.getPosition()[1]);
			Log.d("BackgroundColidableObject"," ");
			
//			double y = Math.sin(alpha) * razlika - counterDY;
//			double x = Math.cos(alpha) * razlika - counterDX;
//			player.movePlayer((float)x, (float)y);
			
//			
//			player.graduallyMovePlayer(alpha, 0.5f, 1f);
			player.lockMovement(false);
		}
	}

	@Override
	public boolean step(float stepScale) {
		return false;
	}

}

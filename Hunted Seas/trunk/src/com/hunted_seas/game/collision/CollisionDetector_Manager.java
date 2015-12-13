package com.hunted_seas.game.collision;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import android.util.Log;

import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

public class CollisionDetector_Manager {
	private static final String  TAG = "CollisionDetector";
	
	private Player player;
	private LinkedList<ColidableObjectInterface> collidableSprites = new LinkedList<ColidableObjectInterface>();
	
	private Vector<ColidableObjectInterface> resolveCollisions = new Vector<ColidableObjectInterface>();
	
	CollisionDetectorThread threadOne;
//	CollisionDetectorThread threadTwo;
	
	public void setPlayer(Player player){
		this.player = player;
	}
	
	public void addColidableObject(ColidableObjectInterface sprite){
		collidableSprites.add(sprite);
	}
	
	public void removeColidableObject(ColidableObjectInterface sprite){
		collidableSprites.remove(sprite);
	}
	
	public void addColidableObjectSprite(Vector<Sprite> sprites){
		for(ColidableObjectInterface sprite : sprites){
			collidableSprites.add(sprite);
		}
	}
	
	public void addColidableObject(Vector<ColidableObjectInterface> sprites){
		for(ColidableObjectInterface sprite : sprites){
			collidableSprites.add(sprite);
		}
	}
	
	public void addColidableOjbect(LinkedList<ColidableObjectInterface> sprites){
		for(ColidableObjectInterface sprite : sprites){
			collidableSprites.add(sprite);
		}
	}
	
	public void addColidableObject(ArrayList<ColidableObjectInterface> sprites){
		for(ColidableObjectInterface sprite : sprites){
			collidableSprites.add(sprite);
		}
	}
	
	
	public boolean detectCollisions(){
		long t = System.currentTimeMillis();
		
		resolveCollisions.clear();
		
		
//		threadOne = new CollisionDetectorThread(0, collidableSprites.size()/2, collidableSprites, resolveCollisions, player);
//		threadTwo = new CollisionDetectorThread(collidableSprites.size()/2,collidableSprites.size(), collidableSprites, resolveCollisions, player);
		for(ColidableObjectInterface sprite : collidableSprites){
			if(sprite.coarseCollisionDetection(player)){
				resolveCollisions.add(sprite);
			}
		}
		
		
		try {
//			threadOne.join();
//			threadTwo.join();
			
//			if(resolveCollisions.size() == 1){
//				threadOne = new CollisionDetectorThread(0, 1, resolveCollisions, player);
//				threadOne.join();
//			}else if(resolveCollisions.size() > 1){
//				threadOne = new CollisionDetectorThread(0, resolveCollisions.size()/2, resolveCollisions, player);
//				threadTwo = new CollisionDetectorThread(resolveCollisions.size()/2,resolveCollisions.size(), resolveCollisions, player);
//				
//				threadOne.join();
//				threadTwo.join();
//			}
			
			if(resolveCollisions.size() > 4){
				threadOne = new CollisionDetectorThread(0, resolveCollisions.size()/2, resolveCollisions, player);
//				threadTwo = new CollisionDetectorThread(resolveCollisions.size()/2,resolveCollisions.size(), resolveCollisions, player);
				
				resolveCollisions(resolveCollisions.size()/2,resolveCollisions.size());
				
				threadOne.join();
//				threadTwo.join();
			}else{
				resolveCollisions();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
//		for(ColidableObjectInterface sprite : collidableSprites){
//			if(sprite.coarseCollisionDetection(player)){
//				resolveCollisions.add(sprite);
//			}
//		}
//				
//		resolveCollisions();
		long s = System.currentTimeMillis();
		
//		Log.d("CollisionDetector ","Collision time: "+(s-t));
		return true;
	}
	
	public boolean resolveCollisions(int start, int end){
		for(int i = start; i < end; i++){
			resolveCollisions.get(i).fineCollisionDetection(player);
		}
		
		return true;
	}
	
	public boolean resolveCollisions(){
//		for(ColidableObjectInterface sprite : resolveCollisions){
//			sprite.fineCollisionDetection(player);
//		}
//		
//		return true;
		
		return resolveCollisions(0, resolveCollisions.size());
	}
}

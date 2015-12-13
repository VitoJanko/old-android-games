package com.hunted_seas.game.collision;

import java.util.LinkedList;
import java.util.Vector;

import com.hunted_seas.game.world.player.Player;

public class CollisionDetectorThread extends Thread{
	private int startElement = 0;
	private int endElement = 0;
	private LinkedList<ColidableObjectInterface> sprites;
	private Vector<ColidableObjectInterface> resolveCollisions;
	private Player player;
	
	
	public boolean coarseCollision = true;
	
	public CollisionDetectorThread(int startElement, int endElement, Vector<ColidableObjectInterface> resolveCollisions, Player player){
		this.startElement = startElement;
		this.endElement = endElement;
		this.resolveCollisions = resolveCollisions;
		this.player = player;
		
		coarseCollision = false;
		
		this.start();
	}
	
	public CollisionDetectorThread(int startElement, int endElement, LinkedList<ColidableObjectInterface> sprites, Vector<ColidableObjectInterface> resolveCollisions, Player player){
		this.startElement = startElement;
		this.endElement = endElement;
		this.sprites = sprites;
		this.resolveCollisions = resolveCollisions;
		this.player = player;
		
		coarseCollision = true;
		
		this.start();
	}
	
	
	@Override
	public void run(){
		
		if(coarseCollision){
			for(int i=startElement; i < endElement; i++){
				if(sprites.get(i).coarseCollisionDetection(player)){
					resolveCollisions.add(sprites.get(i));
				}
			}
		}else{
			for(int i = startElement; i < Math.min(resolveCollisions.size(), endElement); i++){
				resolveCollisions.get(i).fineCollisionDetection(player);
			}
		}
	}
}

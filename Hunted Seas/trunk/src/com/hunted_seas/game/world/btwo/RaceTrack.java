package com.hunted_seas.game.world.btwo;

import java.util.LinkedList;
import java.util.Random;

import android.content.Context;

import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.objects.masters.SpriteMaster;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Lights;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;
import com.hunted_seas.game.world.cthree.Arrow;
import com.hunted_seas.game.world.cthree.Ring;

public class RaceTrack implements SpriteManagerInterface {

	private SpriteMaster rings;
	private SpriteMaster arrow;
	
	private LinkedList<Ring> trackRings;
	
	private LevelManager level;
	Arrow arrowSprite;
	
	int nextRing = 0;
	
	
	Random random;
	
	public RaceTrack(LevelManager level){
		this.level = level;
		
		trackRings = new LinkedList<Ring>();
		
		random = new Random();
		random.setSeed(1111);
	}
	
	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		rings = new SpriteMaster(level,true);
		rings.loadSprite(context, shaderProgram, Ring.texture, Ring.model,false,true);
		
		arrow = new SpriteMaster(level,false);
		arrow.loadSprite(context, shaderProgram, Arrow.texture, Arrow.model,false,false);		
	}

	public void spawnTrack(){
		spawnRings(16);
		spawnArrow();
	}
	
	public void spawnRings(int ammount){
		for(int i=0; i<ammount; i++){
			float width = level.getRightBound() - level.getLeftBound();
			float height = level.getTopBound() - level.getBottomBound();
			Ring u = new Ring(level.getLeftBound()+(float)(random.nextFloat()*width), level.getBottomBound() + (float)(random.nextFloat()*height), 0,level);
			rings.addElement(u);
			trackRings.add(u);
		}
		
		trackRings.getFirst().activate();
	}
	
	public void addNextRing(Ring ring){
		rings.addElement(ring);
		trackRings.add(ring);
		
		if(trackRings.size() == 1)
			ring.activate();
	}
	
	public void addNextRing(Ring ring, int position){
		if(position > trackRings.size())
			position = trackRings.size();
		if(position < 0)
			position = 0;
		
		if(position == 0){
			trackRings.getFirst().deactivate();
			ring.activate();
		}
		
		rings.addElement(ring);
		trackRings.add(ring);

	}
	
	public void spawnArrow(){
		arrowSprite = new Arrow(0, 0,500,level);
		arrow.addElement(arrowSprite);
	}
	
	@Override
	public void setUp_CollisionDetector(CollisionDetector_Manager collisionDetector) {
		rings.setUp_CollisionDetector(collisionDetector);
	}

	@Override
	public boolean step(float stepScale) {
		if(level.isFinishLevel()) return false;
		try{
			if(nextRing < trackRings.size()){
				if(trackRings.get(nextRing).wasReached()){
					nextRing++;
					trackRings.get(nextRing).activate();
				}
			}else{
				level.finish();
			}
			
			arrowSprite.setNextRingPosition(trackRings.get(nextRing).getPosition());
			
			rings.step(stepScale);
			arrow.step(stepScale);
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
			trackRings.clear();
			return false;
		}	
		return true;
	}

	@Override
	public void draw(float[] viewMatrix, Lights lights) {
		arrowSprite.move();
		
		rings.draw(viewMatrix, lights);
		arrow.draw(viewMatrix, lights);	
	}

	@Override
	public void pause() {		
	}

	@Override
	public void finish() {
		trackRings.clear();
	}

	@Override
	public void destroy() {
	}

	@Override
	public LevelManager getLvL() {
		return level;
	}

}

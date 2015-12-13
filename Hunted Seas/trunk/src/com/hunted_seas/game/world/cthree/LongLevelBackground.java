package com.hunted_seas.game.world.cthree;

import java.util.LinkedList;

import android.content.Context;

import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.objects.BackgroundRegularObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Lights;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;
import com.hunted_seas.game.world.aone.background.RockOne;
import com.hunted_seas.game.world.aone.background.SandThree;
import com.hunted_seas.game.world.aone.background.SandTwo;

public class LongLevelBackground implements SpriteManagerInterface {
	LinkedList<BackgroundRegularObject> sprites = new LinkedList<BackgroundRegularObject>();
	
	private RockOne rock1;
	private RockOne rock2;
	private RockOne rock3;
	
	private CollisionDetector_Manager collisionDetector;


	public LongLevelBackground(){
		rock1 = new RockOne(-1000,0,0);
		rock2 = new RockOne(5000,-1000,0);
		rock3 = new RockOne(10000,0,0);
		
		sprites.add(rock1);
		sprites.add(rock2);
		sprites.add(rock3);
		
//		for(int i=0; i < 8; i++){
//			if(Math.random() < 0.3)
//				sprites.add(new RockTwo((i*2500),-1100,-500));
//			if(Math.random() < 0.5)
//				sprites.add(new RockThree(3000,-1100,-500));
//		}
		
		for(int i=0; i < 4 ; i++){
//			float offsetTEMP = i * 6000; 
//			if(Math.random() < 0.6)
//			sprites.add(new SeaweedOne(-50+offsetTEMP,-600,-265));
//			if(Math.random() < 0.7)
//				sprites.add(new SeaweedThree(-150+offsetTEMP,-600,-265));
//			if(Math.random() < 0.4)
//				sprites.add(new SeaweedOne(850+offsetTEMP,-600,-265));
//			if(Math.random() < 0.4)
//				sprites.add(new SeaweedThree(750+offsetTEMP,-600,-260));
//			if(Math.random() < 0.6)
//				sprites.add(new SeaweedTwo(950+offsetTEMP,-600,-255));
//			if(Math.random() < 0.8)
//				sprites.add(new SeaweedTwo(50+offsetTEMP,-600,-255));
		}
		
		for(int i=0; i < 8; i++){
			if(Math.random() < 0.6)
			sprites.add(new SandTwo(-2700 * i,-1300,-240));
			if(Math.random() < 0.8)
				sprites.add(new SandTwo(2500 * i,-1300,-230));
			sprites.add(new SandThree(1000 * i,-1300,-210));
		}
	}
	
	
	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		for(BackgroundRegularObject sprite : sprites){
			sprite.loadSprite(context, shaderProgram);
		}

	}

	@Override
	public boolean step(float stepScale) {
		return true;
	}

	@Override
	public void pause() {
	}

	@Override
	public void finish() {
	}


	@Override
	public void setUp_CollisionDetector(CollisionDetector_Manager collisionDetector) {
		this.collisionDetector = collisionDetector;
		collisionDetector.addColidableObject(rock1);
		collisionDetector.addColidableObject(rock2);
		collisionDetector.addColidableObject(rock3);
	}


	@Override
	public void draw(float[] viewMatrix, Lights lights) {
		for(BackgroundRegularObject sprite : sprites){
			sprite.draw(viewMatrix, lights);
		}
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public LevelManager getLvL() {
		// TODO Auto-generated method stub
		return null;
	}

}

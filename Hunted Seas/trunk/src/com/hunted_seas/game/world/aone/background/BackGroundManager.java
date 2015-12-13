package com.hunted_seas.game.world.aone.background;

import java.util.LinkedList;

import android.content.Context;

import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.objects.BackgroundRegularObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Lights;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;

public class BackGroundManager implements SpriteManagerInterface {
	LinkedList<BackgroundRegularObject> sprites = new LinkedList<BackgroundRegularObject>();
	

	public BackGroundManager(){
		sprites.add(new RockOne(-3000,-1900,-500));
		sprites.add(new RockTwo(0,-1900,-500));
		sprites.add(new RockThree(3000,-1900,-500));
		
//		sprites.add(new Seabed_red(-700,-1900,-300));
//		sprites.add(new Seabed_red(800,-1900,-300));
		
		sprites.add(new SeaweedOne(-50,-1100,-265));
		sprites.add(new SeaweedThree(-150,-1100,-265));
		sprites.add(new SeaweedOne(850,-1100,-265));
		//sprites.add(new Sign(450,-900,-260));
		sprites.add(new SeaweedThree(750,-1100,-260));
		sprites.add(new SeaweedTwo(950,-1100,-255));
		sprites.add(new SeaweedTwo(50,-1100,-255));
		
		sprites.add(new SandTwo(-2700,-1900,-240));
		sprites.add(new SandTwo(2500,-1900,-230));
		sprites.add(new SandThree(1000,-1900,-210));
		
//		sprites.add(new SandTwoSmall(-3800,-1700, 50));
//		sprites.add(new SandTwoSmall(-3000,-1700, 50));
//		sprites.add(new SandTwoSmall(-2700,-1700, 50));
//		sprites.add(new SandTwoSmall(-2000,-1700, 50));
//		sprites.add(new SandTwoSmall(-1500,-1700, 50));
//		sprites.add(new SandTwoSmall(-1000,-1700, 50));
//		sprites.add(new SandTwoSmall(-100,-1700, 50));
//		sprites.add(new SandTwoSmall(2500,-1700, 50));
//		sprites.add(new SandTwoSmall(3000,-1700, 50));
//		sprites.add(new SandTwoSmall(4000,-1700, 50));
//		sprites.add(new SandTwoSmall(1000,-1700, 50));
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
		// TODO Auto-generated method stub
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

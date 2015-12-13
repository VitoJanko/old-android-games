package com.hunted_seas.game.world.aone.background;

import java.util.LinkedList;

import android.content.Context;

import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.objects.BackgroundRegularObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Lights;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;

public class ForeGroundManager implements SpriteManagerInterface {
	LinkedList<BackgroundRegularObject> sprites = new LinkedList<BackgroundRegularObject>();
	

	public ForeGroundManager(){
//		sprites.add(new SeaweedOne(-4500,-1600,40));
//		sprites.add(new SeaweedThree(-2000,-1600,40));
//		sprites.add(new SeaweedOne(-1800,-1600,40));
//		sprites.add(new SeaweedThree(1500,-1600,40));
//		sprites.add(new SeaweedTwo(3000,-1600,40));
//		sprites.add(new SeaweedOne(3100,-1600,40));
//
//		sprites.add(new SandTwoSmall(-4100,-1750, 50));
//		sprites.add(new SandTwoSmall(-3000,-1750, 50));
//		sprites.add(new SandTwoSmall(-2700,-1750, 50));
//		sprites.add(new SandTwoSmall(-2000,-1750, 50));
//		sprites.add(new SandTwoSmall(-1500,-1750, 50));
//		sprites.add(new SandTwoSmall(-1000,-1750, 50));
//		sprites.add(new SandTwoSmall(-100,-1750, 50));
//		sprites.add(new SandTwoSmall(2500,-1750, 50));
//		sprites.add(new SandTwoSmall(3000,-1750, 50));
//		sprites.add(new SandTwoSmall(4000,-1750, 50));
//		sprites.add(new SandTwoSmall(1000,-1750, 50));
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

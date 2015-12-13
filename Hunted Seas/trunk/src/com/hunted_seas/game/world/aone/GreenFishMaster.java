package com.hunted_seas.game.world.aone;

import android.content.Context;

import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.objects.masters.BachedSpriteMaster;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Lights;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;

public class GreenFishMaster implements SpriteManagerInterface{
	BachedSpriteMaster body;
	BachedSpriteMaster fin;
	BachedSpriteMaster tail;
	BachedSpriteMaster eye;
	BachedSpriteMaster pupil;

	int type;
	
	public GreenFishMaster(LevelManager level, boolean collision, int type){
		this.type = type;
		body = new BachedSpriteMaster(level, collision);
		fin = new BachedSpriteMaster(level, collision);
		tail = new BachedSpriteMaster(level, collision);
		eye = new BachedSpriteMaster(level, collision);
		pupil = new BachedSpriteMaster(level, collision);
	}
	
	public void addFish(Sprite sprite){
		body.addElement(sprite);
	}
	
	public void addFin(Sprite sprite){
		fin.addElement(sprite);
	}
	
	public void addTail(Sprite sprite){
		tail.addElement(sprite);
	}
	
	public void addEye(Sprite sprite){
		eye.addElement(sprite);
	}
	
	public void addPupil(Sprite sprite){
		pupil.addElement(sprite);
	}
	
	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		if(type==0){
			body.loadSprite(context, shaderProgram, GreenFish.texture, GreenFish.model,false);
			eye.loadSprite(context, shaderProgram, GreenFishEye.texture, GreenFishEye.model,false);
			pupil.loadSprite(context, shaderProgram, GreenFishPupil.texture, GreenFishPupil.model,false);
		}if(type==1){
			body.loadSprite(context, shaderProgram, RedFish.texture, RedFish.model,false);
			eye.loadSprite(context, shaderProgram, GreenFishEye.texture, GreenFishEye.model,false);
			pupil.loadSprite(context, shaderProgram, GreenFishPupil.texture, GreenFishPupil.model,false);
		}
		if(type==2){
			body.loadSprite(context, shaderProgram, GreenFish.textureAlt, GreenFish.model,false);
			eye.loadSprite(context, shaderProgram, GreenFishEye.texture, GreenFishEye.model,false);
			pupil.loadSprite(context, shaderProgram, GreenFishPupil.texture, GreenFishPupil.model,false);
		}
		if(type==3){
			body.loadSprite(context, shaderProgram, GreenFish.textureYellow, GreenFish.model,false);
			eye.loadSprite(context, shaderProgram, GreenFishEye.texture, GreenFishEye.model,false);
			pupil.loadSprite(context, shaderProgram, GreenFishPupil.texture, GreenFishPupil.model,false);
		}
		if(type==4){
			body.loadSprite(context, shaderProgram, PurpleFish.texture, GreenFish.model,false);
			eye.loadSprite(context, shaderProgram, GreenFishEye.texture, GreenFishEye.model,false);
			pupil.loadSprite(context, shaderProgram, GreenFishPupil.texture, GreenFishPupil.model,false);
		}
		

	}

	@Override
	public void setUp_CollisionDetector(CollisionDetector_Manager collisionDetector) {
		body.setUp_CollisionDetector(collisionDetector);
		fin.setUp_CollisionDetector(collisionDetector);
		tail.setUp_CollisionDetector(collisionDetector);
		eye.setUp_CollisionDetector(collisionDetector);
		pupil.setUp_CollisionDetector(collisionDetector);
	}

	@Override
	public boolean step(float stepScale) {
		body.step(stepScale);
		fin.step(stepScale);
		tail.step(stepScale);
		eye.step(stepScale);
		pupil.step(stepScale);
		return false;
	}

	@Override
	public void draw(float[] viewMatrix, Lights lights) {
		body.draw(viewMatrix, lights);
		fin.draw(viewMatrix, lights);
		tail.draw(viewMatrix, lights);
		eye.draw(viewMatrix, lights);
		pupil.draw(viewMatrix, lights);
	}

	@Override
	public void pause() {
		body.pause();
		fin.pause();
		tail.pause();
		eye.pause();
		pupil.pause();
	}

	@Override
	public void finish() {
		body.finish();
		fin.finish();
		tail.finish();
		eye.finish();
		pupil.finish();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public LevelManager getLvL(){
		return null;
	}
	
	

}

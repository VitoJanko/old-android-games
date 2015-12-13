package com.hunted_seas.game.world.awone.blowfish;

import android.content.Context;

import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.objects.masters.SpriteMaster;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Lights;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;

public class BlowFishMaster implements SpriteManagerInterface{
	private static final String FOLDER = "blowfish/";
	private static final String textureAtlas = FOLDER+"blowfish";
	private static int textureID = -1;
	
	private LevelManager level;
	private int type;
	
	SpriteMaster body;
	SpriteMaster eyes;
	SpriteMaster pupils;
	SpriteMaster fin;
	
	
	
	public BlowFishMaster(LevelManager level, boolean collision, int type){
		this.level = level;
		this.type = type;
		body = new SpriteMaster(level, collision);
		eyes = new SpriteMaster(level, false);
		pupils = new SpriteMaster(level, false);
		fin = new SpriteMaster(level, false);
	}


	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		if(type == 0){
			body.loadSprite(context, shaderProgram, new String[]{textureAtlas}, BlowfishBody.model, false,true);
			textureID = body.getTextureIDs()[0];
			eyes.loadSprite(context, shaderProgram, textureID, BlowfishEyes.model, false);
			pupils.loadSprite(context, shaderProgram, textureID, BlowfishPupils.model, false);
			fin.loadSprite(context, shaderProgram, textureID, BlowfishFins.model, false);
		}
		
		
	}

	public void addNewFish(BlowfishBody blowfish){
		body.addElement(blowfish);
		blowfish.loadBodyParts(this);
	}
	
	public void addEyes(Sprite blowfishEyes){
		eyes.addElement(blowfishEyes);
	}
	
	public void addPupils(Sprite blowfishPupils){
		pupils.addElement(blowfishPupils);
	}
	
	public void addFin(Sprite blowfishFin){
		fin.addElement(blowfishFin);
	}
	

	@Override
	public void setUp_CollisionDetector(CollisionDetector_Manager collisionDetector) {
		body.setUp_CollisionDetector(collisionDetector);
	}


	@Override
	public boolean step(float stepScale) {
		body.step(stepScale);
		eyes.step(stepScale);
		pupils.step(stepScale);
		fin.step(stepScale);
		
		
		return false;
	}


	@Override
	public void draw(float[] viewMatrix, Lights lights) {
		level.disableDepthTesting();
		fin.draw(viewMatrix, lights);	
		body.draw(viewMatrix, lights);	
		eyes.draw(viewMatrix, lights);	
		pupils.draw(viewMatrix, lights);	


		level.enableDepthTesting();
	}


	@Override
	public void pause() {
		body.pause();
		eyes.pause();
		pupils.pause();
		fin.pause();
	}


	@Override
	public void finish() {
		body.finish();
		eyes.finish();
		pupils.finish();
		fin.finish();
	}


	@Override
	public void destroy() {
		body.destroy();
		eyes.destroy();
		pupils.destroy();
		fin.destroy();
	}


	@Override
	public LevelManager getLvL() {
		return level;
	}

}

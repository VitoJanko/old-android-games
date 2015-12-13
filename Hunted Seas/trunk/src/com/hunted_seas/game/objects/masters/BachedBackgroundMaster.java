package com.hunted_seas.game.objects.masters;

import android.content.Context;

import com.hunted_seas.game.objects.BackgroundRectangleBachedObject;
import com.hunted_seas.game.programs.BachingTextureShaderProgram;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Sprite;

public class BachedBackgroundMaster extends BackgroundRectangleBachedObject {
	protected final int MAX_SPRITES = 128;
	String texture;
	public float[] bounds;
	
	public BachedBackgroundMaster(LevelManager level){
		this.level = level;
	}
	
	public BachedBackgroundMaster(LevelManager level, String texture, float[] bounds){
		this.level = level;
		this.texture = texture;
		this.bounds = bounds;
	}
	
	public void loadSprite(Context context,BachingTextureShaderProgram textureShaderProgram){
		loadSprite(context, textureShaderProgram, texture, bounds,0);
	}
	
	public void loadSprite(Context context,BachingTextureShaderProgram textureShaderProgram, String textureResourceId, float[] bounds){
		loadSprite(context, textureShaderProgram, textureResourceId, bounds,0);
	}
	
	public void loadSprite(Context context,BachingTextureShaderProgram textureShaderProgram, String textureResourceId, float[] bounds, float z){
		float[] tempDimensions = {bounds[0],bounds[1],bounds[3],bounds[2],z};
		this.backgroundDimensions = tempDimensions;
		tempDimensions = null;
		
		loadObject(context, textureShaderProgram, new String[]{textureResourceId});
	}
	
	public void addElement(Sprite sprite){
		addElement(sprite, false);
	}
	
	public void addElement(Sprite sprite, boolean screenLimit){
		if(spritesSize() < MAX_SPRITES || (screenLimit && getOnScreenSprites() < MAX_SPRITES)){
			addSprite(sprite);
			sprite.setMaster(this);
			if(boundingBox!=null)
				sprite.setBoundingBox(boundingBox);
		}
	}
	
	public void updateBoundingBox(){
		for(Sprite s: sprites){
			s.setBoundingBox(boundingBox);
		}
	}
	
	@Override
	public boolean step(float stepScale) {
		for(int i=0; i<spritesSize(); i++){
			Sprite bub = getSprite(i);
			bub.step(stepScale);
			bub.calculateModelMatrix();
			if(bub.isDead()){
				removeSprite(i);
				i--;
			}
		}
		return true;
	}
	
	@Override
	public LevelManager getLvL(){
		return level;
	}
	
	@Override
	public void pause() {
		
	}

	@Override
	public void finish() {
		clearSprites();
		
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
	}

	
	/**
	 * Returns number of sprites.
	 * 
	 * @return Number of sprites.
	 * 
	 * @author Jani
	 */
	public int size(){
		return spritesSize();
	}
}

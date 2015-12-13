package com.hunted_seas.game.objects.masters;

import android.content.Context;

import com.hunted_seas.game.objects.BachedObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Sprite;

public class BachedSpriteMaster extends BachedObject{
	protected final int MAX_SPRITES = 128;
	public boolean collision;
	private int[] model;
	private String[] texture;
	
	public BachedSpriteMaster(LevelManager level, boolean collision){
		this.level = level;
		this.collision=collision;
	}
	
	public BachedSpriteMaster(int[] model, String[] texture, LevelManager level, boolean collision){
		this.level = level;
		this.collision = collision;
		this.texture = texture;
		this.model = model;
	}

	public void addElement(Sprite sprite){
		if(spritesSize() < MAX_SPRITES){
			addSprite(sprite);
			if(collision && collisionDetector!=null)
				collisionDetector.addColidableObject(sprite);
			sprite.setMaster(this);
			if(boundingBox!=null)
				sprite.setBoundingBox(boundingBox);
			sprite.setLineSegments(lineSegments);
		}
	}
	
	public void updateBoundingBox(){
		for(Sprite s: sprites){
			s.setBoundingBox(boundingBox);
			s.setLineSegments(lineSegments);
		}
	}
	
	@Override
	public boolean step(float stepScale) {
		for(int i=0; i<spritesSize(); i++){
			Sprite bub = getSprite(i);
			if(bub.getBoundBox()==null)
				bub.setBoundingBox(boundingBox);
			if(bub.getLineSegments()==null)
				bub.setLineSegments(lineSegments);
			bub.step(stepScale);
			bub.calculateModelMatrix();
			if(bub.isDead()){
				removeSprite(i);
				if(collision)
					collisionDetector.removeColidableObject(bub);
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

	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		loadSprite(context,shaderProgram,texture,model,true);
	}

}

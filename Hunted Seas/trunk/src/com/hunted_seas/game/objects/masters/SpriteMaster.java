package com.hunted_seas.game.objects.masters;

import android.content.Context;
import android.util.Log;

import com.hunted_seas.game.objects.RegularObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Sprite;

public class SpriteMaster extends RegularObject{
	protected final int MAX_SPRITES = 128;
	public boolean collision;
	private int[] model;
	private String[] texture;
	
	public SpriteMaster(LevelManager level, boolean collision){
		this.level = level;
		this.collision = collision;
	}
	
	public SpriteMaster(int[] model, String[] texture, LevelManager level, boolean collision){
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
			if(boundingBox != null){
				sprite.setBoundingBox(boundingBox);
			}
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
			if(!bub.step(stepScale)){
				removeSprite(i);
				if(collision)
					collisionDetector.removeColidableObject(bub);
				i--;
				
				continue;
			}
			bub.calculateModelMatrix();
			
			//TODO zbrisi to, ce je mrtu naj step vrne false in to je to.
			//@Depreaced
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


	
	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		loadSprite(context,shaderProgram,texture,model,true);
	}

}

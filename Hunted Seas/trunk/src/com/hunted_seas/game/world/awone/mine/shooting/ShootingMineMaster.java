package com.hunted_seas.game.world.awone.mine.shooting;

import android.content.Context;

import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.objects.masters.SpriteMaster;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Lights;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;

public class ShootingMineMaster implements SpriteManagerInterface{
	public static final String FOLDER = "world_one/objects/shooting_mine/";
	public static final String[] TEXTURE_ATLAS = {FOLDER+"shooting_mine_atlas"};
	private static int textureID = -1;
	private static int bulletsTextureID = -1;
	
	
	SpriteMaster mines;
	SpriteMaster lights;
	SpriteMaster bullets; //mogoce bached object?
	
	LevelManager level;
	
	public ShootingMineMaster(LevelManager level){
		this.level = level;
		
		mines = new SpriteMaster(level, true);
		lights = new SpriteMaster(level,false);
		bullets = new SpriteMaster(level, true);
	}
	
	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		mines.loadSprite(context, shaderProgram, TEXTURE_ATLAS, ShootingMine.MODEL, true, true);
		textureID = mines.getTextureIDs()[0];
		
		lights.loadSprite(context, shaderProgram, textureID, ShootingMineLights.MODEL,false);
		
		bullets.loadSprite(context, shaderProgram, Bullet.TEXTURE_ATLAS, Bullet.MODEL, true, true);
		bulletsTextureID = bullets.getTextureIDs()[0];
	}

	public void addNewMine(ShootingMine newMine){
		mines.addElement(newMine);
		newMine.loadParts(this);
	}
	
	public void addLights(Sprite lights){
		this.lights.addElement(lights);
	}
	
	public void addNewBullet(Sprite bullet){
		this.bullets.addElement(bullet);
	}
	
	@Override
	public void setUp_CollisionDetector(CollisionDetector_Manager collisionDetector) {
		mines.setUp_CollisionDetector(collisionDetector);
		bullets.setUp_CollisionDetector(collisionDetector);
	}

	@Override
	public boolean step(float stepScale) {
		mines.step(stepScale);
		lights.step(stepScale);
		bullets.step(stepScale);
		
		return false;
	}

	@Override
	public void draw(float[] viewMatrix, Lights lights) {
		bullets.draw(viewMatrix, lights);
		
		mines.draw(viewMatrix, lights);
		this.lights.draw(viewMatrix, lights);
	}

	@Override
	public void pause() {
		mines.pause();
		lights.pause();
		bullets.pause();
	}

	@Override
	public void finish() {
		mines.finish();
		lights.finish();
		bullets.finish();
	}

	@Override
	public void destroy() {
		mines.destroy();
		lights.destroy();
		bullets.destroy();
	}

	@Override
	public LevelManager getLvL() {
		return level;
	}

}

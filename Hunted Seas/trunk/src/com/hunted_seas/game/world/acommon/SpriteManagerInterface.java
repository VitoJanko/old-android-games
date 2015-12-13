package com.hunted_seas.game.world.acommon;

import android.content.Context;

import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.programs.ShaderProgramInterface;

/**
 * All sprite types are grouped together under this interface.
 * It loads and holds all resources for one sprite (texture, vertex data ..).
 * This does not hold position as it is unique for each sprite.
 * 
 * @see Sprite
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public interface SpriteManagerInterface {
	
	public void loadSprite(Context context,ShaderProgramInterface shaderProgram);
	
	public void setUp_CollisionDetector(CollisionDetector_Manager collisionDetector);
	
	public boolean step(float stepScale);
	
	public void draw(float[] viewMatrix, Lights lights);
	
	public void pause();
	
	public void finish();
	public void destroy();
	
	public LevelManager getLvL();
}

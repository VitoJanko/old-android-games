package com.hunted_seas.game.world.acommon;
import android.content.Context;

import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.objects.BackgroundRectangleObject;
import com.hunted_seas.game.programs.TextureShaderProgram;

/**
 * Main Backgroud.
 * 
 * @see BackgroundRectangleObject
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class SkyBox extends BackgroundRectangleObject {
	
	float verticaloffset = 0;
	float horizontalOffset = 0;
	
	String textureResourceId  = "";
	
	public SkyBox(LevelManager level){
		this.level = level;
	}
	
	public SkyBox(LevelManager level, float verticalOffset, float horizontalOffset){
		this.level = level;
		this.verticaloffset = verticalOffset;
		this.horizontalOffset = horizontalOffset;
	}
	
	
	public void loadSkyBox(Context context,TextureShaderProgram textureShaderProgram, String textureResourceId, BoundingBox bounds){
		bounds.left -= horizontalOffset;
		bounds.right += horizontalOffset;
		bounds.top  += verticaloffset;
		bounds.bottom -= verticaloffset;
		this.backgroundDimensions = bounds;
		
		loadObject(context, textureShaderProgram, 2, 2, textureResourceId);
	}
	
	public void loadBackgroundBox(Context context,TextureShaderProgram textureShaderProgram){
		loadObject(context, textureShaderProgram, 2, 2, textureResourceId);
	}

	public void setBounds(BoundingBox bounds){
		this.backgroundDimensions = bounds;
	}
	
	public void setTexture(String texture){
		textureResourceId = texture;
	}
	
	@Override
	public void finish() {
		//TODO
	}
	
	
	
}

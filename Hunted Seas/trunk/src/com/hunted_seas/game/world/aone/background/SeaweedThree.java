package com.hunted_seas.game.world.aone.background;

import android.content.Context;

import com.hunted_seas.game.R;
import com.hunted_seas.game.objects.BackgroundRegularObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;

public class SeaweedThree extends BackgroundRegularObject {
	public static final String FOLDER = "world_one/background/seaweed/";
	public static final String texture = FOLDER + "seaweed3";
	
	
	public SeaweedThree(){
		addBackgroundObject(50,-1000,-250);
	}
	
	public SeaweedThree(float x, float y, float z){
		addBackgroundObject(x, y, z);
	}
	
	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		loadBackgroundResources(context, shaderProgram,texture, R.raw.seaweed_three);
	}
}

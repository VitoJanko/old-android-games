package com.hunted_seas.game.world.aone.background;

import android.content.Context;
import android.util.Log;

import com.hunted_seas.game.R;
import com.hunted_seas.game.objects.BackgroundRegularObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;

public class SeaweedOne extends BackgroundRegularObject {
	public static final String FOLDER = "world_one/background/seaweed/";
	public static final String texture = FOLDER + "seaweed1";
	
	public SeaweedOne(){
		addBackgroundObject(-50,-1000,-250);
	}
	
	public SeaweedOne(float x, float y, float z){
		addBackgroundObject(x, y, z);
	}
	
	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		loadBackgroundResources(context, shaderProgram, texture, R.raw.seaweed_one);
	}
}

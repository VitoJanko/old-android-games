package com.hunted_seas.game.world.aone.background;

import android.content.Context;

import com.hunted_seas.game.R;
import com.hunted_seas.game.objects.BackgroundRegularObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;

public class RockThree extends BackgroundRegularObject {
	public static final String FOLDER = "world_one/background/rock/";
	public static String texture = FOLDER + "rock3";
	
	public RockThree(){
		addBackgroundObject(3000,-1900,-500);
	}
	
	public RockThree(float x, float y, float z){
		addBackgroundObject(x, y, z);
	}
	
	
	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		loadBackgroundResources(context, shaderProgram, texture, R.raw.rock_3a);

	}



}

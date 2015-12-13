package com.hunted_seas.game.world.aone.background;

import android.content.Context;

import com.hunted_seas.game.R;
import com.hunted_seas.game.objects.BackgroundRegularObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;

public class RockTwo extends BackgroundRegularObject{
	public static final String FOLDER = "world_one/background/rock/";
	public static String texture = FOLDER + "rock2";
	
	
	public RockTwo(){
		addBackgroundObject(0,-1900,-500);
	}
	
	public RockTwo(float x, float y, float z){
		addBackgroundObject(x, y, z);
	}


	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		loadBackgroundResources(context, shaderProgram, texture , R.raw.rock_2a);
	}
}

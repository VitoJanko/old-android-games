package com.hunted_seas.game.world.aone.background;

import android.content.Context;

import com.hunted_seas.game.R;
import com.hunted_seas.game.objects.BackgroundRegularObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;

public class Seabed_red extends BackgroundRegularObject{
	public static final String FOLDER = "world_one/background/sand/";
	public static final String texture = FOLDER + "seabed";
	
	public Seabed_red(){
		addBackgroundObject(-700,-1900,-200);
	}
	
	public Seabed_red(float x, float y, float z){
		addBackgroundObject(x, y, z);
	}
	
	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		loadBackgroundResources(context, shaderProgram, texture, R.raw.seabed_red);
	}
}

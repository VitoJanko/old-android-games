package com.hunted_seas.game.world.aone.background;

import android.content.Context;

import com.hunted_seas.game.R;
import com.hunted_seas.game.objects.BackgroundRegularObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;

public class SandOne extends BackgroundRegularObject{
	public static final String FOLDER = "world_one/background/sand/";
	public static final String texture = FOLDER + "sand1";
	
	
	public SandOne(){
		addBackgroundObject(0,-2500,-230);
	}
	
	public SandOne(float x, float y, float z){
		addBackgroundObject(x, y, z);
	}
	
	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		loadBackgroundResources(context, shaderProgram, texture, R.raw.sand_one);
	}
}

package com.hunted_seas.game.world.aone.background;

import static com.hunted_seas.game.world.acommon.CommonVariables.ATI;
import static com.hunted_seas.game.world.acommon.CommonVariables.NVIDIA;
import static com.hunted_seas.game.world.acommon.CommonVariables.PVRTC;
import static com.hunted_seas.game.world.acommon.CommonVariables.RENDERER_TYPE;
import android.content.Context;

import com.hunted_seas.game.R;
import com.hunted_seas.game.objects.BackgroundColidableObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;

public class RockOne extends BackgroundColidableObject{	
	public static final String FOLDER = "world_one/background/rock/";
	public static String texture = FOLDER + "rock1";
	
	
	public RockOne(){
		addBackgroundObject(-3000,-1900,0);
	}
	
	public RockOne(float x, float y, float z){
		addBackgroundObject(x, y, z);
	}
	

	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
    		loadBackgroundResources(context, shaderProgram, texture, R.raw.rock_1a);
			
	}
	
}

package com.hunted_seas.game.world.aone.background;

import static com.hunted_seas.game.world.acommon.CommonVariables.ATI;
import static com.hunted_seas.game.world.acommon.CommonVariables.NVIDIA;
import static com.hunted_seas.game.world.acommon.CommonVariables.PVRTC;
import static com.hunted_seas.game.world.acommon.CommonVariables.RENDERER_TYPE;
import android.content.Context;

import com.hunted_seas.game.R;
import com.hunted_seas.game.objects.BackgroundRegularObject;
import com.hunted_seas.game.programs.ShaderProgramInterface;

public class SandTwo extends BackgroundRegularObject {
	public static final String FOLDER = "world_one/background/sand/";
	public static final String texture = FOLDER + "sand2";
	
	public SandTwo(){
		addBackgroundObject(-1000,-1900,-240);
	}
	
	public SandTwo(float x, float y, float z){
		addBackgroundObject(x, y, z);
	}
	
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {
		loadBackgroundResources(context, shaderProgram,texture, R.raw.sand_two);

	}
}

package com.hunted_seas.game.world.aone.background;

import com.hunted_seas.game.R;
import com.hunted_seas.game.world.acommon.SolidSprite;

public class Rock1 extends SolidSprite{	
	public static final String FOLDER = "world_one/background/rock/";
	public static String texture = FOLDER + "rock1";
	public static int model = R.raw.rock_1a;
	
	public Rock1(){
		collision = true;
	}

}

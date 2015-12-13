package com.hunted_seas.game.util;

import com.hunted_seas.game.data.BoundingBox;

public class OnScreenOptimizer {
	
	
	public static boolean visible(float[] camera, float[] spritePosition, BoundingBox spriteBox, float factor){	
		factor += 0.5;

		//		desni rob kamere				levi rob sprita
		if((camera[0] + camera[2] * factor) < (spritePosition[0] + spriteBox.left))
			return true;	
		//		levi rob kamere			desni rob sprita
		if((camera[0]-camera[2] * factor) > (spritePosition[0] + spriteBox.right))
			return true;
		//		zgornji rob kamere			spodnji rob sprita
		if((camera[1]+camera[3] * factor) < (spritePosition[1] + spriteBox.bottom))
			return true;
		if((camera[1]-camera[3] * factor) > (spritePosition[1] + spriteBox.top))
			return true;
		
		return false;
	}
}

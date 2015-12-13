package com.hunted_seas.game.immersion;

import android.content.Context;
import android.os.Vibrator;

public class VibratorHelper {
	Vibrator vibrator;
	
	public static long[] pattern_SOS = {0,50,25,50,25,50,25,100,25,100,25,100,25,50,25,50,25,50};
	public static long[] pattern_SHORT_INTERVALS = {0,30,15,30,15,30,15,30,15,30,15,30,15,30,15,30};
	public static long[] pattern_LONG_INTERVAL = {0,1000};
	
	public VibratorHelper(Context context){
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	/**
	 * 
	 * @param pattern
	 * @param repeat
	 * 
	 * @see Vibrator#vibrate(long[], int)
	 * 
	 * @author Jani
	 * 
	 */
	public void vibrate(long[] pattern, int repeat){
		vibrator.vibrate(pattern, repeat);
	}
}

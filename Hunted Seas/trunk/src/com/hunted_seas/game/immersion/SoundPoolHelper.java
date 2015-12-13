package com.hunted_seas.game.immersion;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPoolHelper {
	public static final String FOLDER = "sound/sound_effects/";
	SoundPool sp;
	
	public static int auu;
	public static int rocket;
	public static int bubbles;
	public static int mario_coin;
	
	public SoundPoolHelper(Context context){
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		
		AssetManager am = context.getAssets();
		
		try {
			auu = sp.load(am.openFd(FOLDER+"auu.wav"), 1);
			rocket = sp.load(am.openFd(FOLDER+"rocket_launch2.wav"), 1);
			mario_coin = sp.load(am.openFd(FOLDER+"mario_coin.wav"), 1);
			bubbles = sp.load(am.openFd(FOLDER+"bubbles.wav"), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void playSound(int soundID){
		sp.play(soundID, 1, 1, 0, 0, 1);
	}
}

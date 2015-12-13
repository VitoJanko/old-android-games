package com.igrargti;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;

public class MiscSound {
	private static MediaPlayer door;
	private static MediaPlayer fireplace;

	
	
	public MiscSound(Context context){
		door = MediaPlayer.create(context, R.raw.dorslide);
		door.setLooping(false);
		fireplace = MediaPlayer.create(context, R.raw.fireplace);
		fireplace.setLooping(true);
//		play();
	}
	
	public static void playFireplace(){
		fireplace.start();
	}
	
	public static void pauseFireplace(){
		fireplace.stop();
		try {
			fireplace.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void playDoor(){
		door.start();

	}
	
	public static void pauseDoor(){
		door.stop();
		try {
			door.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void release(){
		door.stop();
		if(door != null) door.release();
		door = null;
		
		fireplace.stop();
		if(fireplace != null) fireplace.release();
		fireplace = null;
	}
}

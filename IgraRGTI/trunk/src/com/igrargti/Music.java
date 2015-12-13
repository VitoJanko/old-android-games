package com.igrargti;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {
	private static MediaPlayer music;

	
	
	public Music(Context context){
		music = MediaPlayer.create(context, R.raw.aboveaverage);
		music.setLooping(true);
//		play();
	}
	
	public void play(){
		music.start();

	}
	
	public void pause(){
		music.stop();
		try {
			music.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void release(){
		music.stop();
		if(music != null) music.release();
		music = null;
	}
}

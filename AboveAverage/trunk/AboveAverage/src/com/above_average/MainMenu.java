package com.above_average;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainMenu extends  Activity{

	static int number=0;
	static float demage=0; 
	Menu mainView;
	Sound muzika;
	public static boolean sound = true;
	
	GoogleAnalyticsTracker tracker;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = new Menu(this,this);
        SharedPreferences settings = getSharedPreferences("Progress", 1);
        mainView.unlocked = settings.getInt("progress", 2);
        mainView.page=settings.getInt("page", 1);
        sound = settings.getBoolean("sound", true);
        for(int i=0; i<mainView.score.length; i++){
        	mainView.score[i]=settings.getInt("score"+i, 0);
	    }
        //score reset
//        mainView.unlocked=2;
//        for(int i=0; i<mainView.score.length; i++){
//        	mainView.score[i]=0;
//	    }
        mainView.patch1=settings.getBoolean("patch1", true);
        if(mainView.patch1==true){
        	mainView.score[14]=mainView.score[9];
        	mainView.score[9]=mainView.score[8];
        	mainView.score[8]=mainView.score[7];
        	mainView.score[7]=mainView.score[6];
        	mainView.score[6]=mainView.score[5];
        	mainView.score[5]=mainView.score[3];
        	mainView.score[3]=mainView.score[2];
        	mainView.score[2]=mainView.score[1];
        	mainView.score[1]=mainView.score[0];
        	mainView.score[0]=0;
        	mainView.patch1=false;
        }
        
        tracker = GoogleAnalyticsTracker.getInstance();

        // Start the tracker in manual dispatch mode...
        tracker.startNewSession("UA-25538274-1", this);
        tracker.trackPageView("Started");
        
        setContentView(mainView);
        
        muzika = new Sound(this);
    }

	public void zamenjajActivity(int stage){
		tracker.trackPageView(String.valueOf(stage));
		
		Main.stage=stage;
		Intent myIntent = new Intent(this, Main.class);
		startActivity(myIntent);
	}
	
	public void muzikaPlay(){
		muzika.play();
	}
	public void muzikaStop(){
		muzika.pause();
	}
	
	protected void onResume() {
		super.onResume();
		
		if(sound) muzika.play();
		
		mainView.stopRefresh = false;
		if(number!=0 && number!=mainView.lvlMax*5){
	        if (demage*100 > mainView.score[number-1])
	        	mainView.score[number-1] = (int)(demage*100);
			if (number==mainView.unlocked)
				mainView.unlocked=number+1;
		}
        if(number==mainView.lvlMax*5){
        	if(demage > mainView.score[number-1])
        		mainView.score[number-1] = (int)demage;
        	if (number==mainView.unlocked)
				mainView.unlocked=number+1;
        }
        if(mainView.started)
        	mainView.createSelector();
	}
	
	protected void onPause() {
        super.onPause();
        mainView.stopRefresh = true;
    }
	
	protected void onStop(){
		super.onStop();
		SharedPreferences settings = getSharedPreferences("Progress", 0); 
		SharedPreferences.Editor editor = settings.edit();
	    editor.putInt("progress", mainView.unlocked);
	    editor.putInt("page", mainView.page);
	    editor.putBoolean("patch1", mainView.patch1);
	    editor.putBoolean("sound", sound);
	    for(int i=0; i<mainView.score.length; i++){
	    	editor.putInt("score"+i, mainView.score[i]);
	    }
	    editor.commit();
	    tracker.dispatch();
	}
	
	protected void onDestroy(){
		super.onDestroy();
		mainView.destroy();
		muzika.release();
		
	    tracker.dispatch();
	    tracker.stopSession();
		
		System.gc();
	}
}
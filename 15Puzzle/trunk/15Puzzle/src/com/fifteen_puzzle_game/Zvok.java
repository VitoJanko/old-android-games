package com.fifteen_puzzle_game;

import android.content.Context;
import android.media.MediaPlayer;

public class Zvok {
	Context context;
	private MediaPlayer gumb2;// = MediaPlayer.create(context, R.raw.button19);
	private MediaPlayer gumb3;// = MediaPlayer.create(context, R.raw.button17);
	private MediaPlayer gumb4;// = MediaPlayer.create(context, R.raw.button16);
//	private long lastTime = 0;
	
	public Zvok(Context context){
		this.context = context;
		gumb2 = MediaPlayer.create(context, R.raw.button19);
		gumb3 = MediaPlayer.create(context, R.raw.button17);
		gumb4 = MediaPlayer.create(context, R.raw.button16);
	}
	
	public void gumb2(){
		try{
		if(!gumb2.isPlaying()){
			gumb2.start();
		}} catch(NullPointerException e){
			
		}
	}
	public void gumb3(){
       	try{
		if(!gumb3.isPlaying()){
       		gumb3.start();
       	}} catch(NullPointerException e){
       		
       	}
	}
	public void gumb4(){
       	try{
		if(!gumb4.isPlaying()){
       		gumb4.start();
       	}} catch(NullPointerException e){
       		
       	}
	}
//	
	public void razpustiZvok(){
		if(gumb2!=null)gumb2.release();
		if(gumb3!=null)gumb3.release();
		if(gumb4!=null)gumb4.release();
	}
	
	public void pripraviZvok(){		
		razpustiZvok();
		gumb2 = MediaPlayer.create(context, R.raw.button19);
		gumb3 = MediaPlayer.create(context, R.raw.button17);
		gumb4 = MediaPlayer.create(context, R.raw.button16);
	}
}

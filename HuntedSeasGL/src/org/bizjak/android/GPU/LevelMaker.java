package org.bizjak.android.GPU;

import android.util.Log;


public class LevelMaker {
	
	int lev;
	Sea host;
	boolean loaded = false;
	boolean created = false;
	
	LevelMaker(Sea host){
		loaded = false;
		created = false;
		this.host=host;
	}
	
	void initialize(){
		if(host.level==1) init1();
	}
	
	void load(){
		if(host.level==1) load1();
		loaded=true;
	}
	
	void step(){
		if(loaded){
			if(host.level==1) step1();
		}
	}
	
	void init1(){

				

	}
	
	void load1(){
		
		host.width = (int)host.renderer.offsetX*2;
		host.height = (int)host.renderer.offsetY*2;
		
		//host.xStart=-4000;
		//host.xEnd = 4000;
		//host.yStart=-1000;
		//host.yEnd = 1000;
		host.xStart=-2*host.width;
		host.xEnd = 2*host.width;
		host.yStart=-host.height;
		host.yEnd = host.height;

		//int width = host.xEnd-host.xStart;
		
		host.bg = new Background(-host.xStart,host.yStart,host.xEnd-host.xStart,host.yEnd-host.yStart,20,host,
				R.drawable.blues,false);
		host.fg = new Background(-host.xStart,host.yStart,host.xEnd-host.xStart,host.yEnd-host.yStart,-20,host,
				R.drawable.waves,true);
		Fish.load(host);
		Jelly.load(host);
		Bubble.load(host);
		
		host.width = (int)host.renderer.offsetX*2;
		host.height = (int)host.renderer.offsetY*2;
		host.marginX=(int)(host.width/4.0);
		host.marginY=(int)(host.height/4.0);
		host.wallX = (int)(host.width/8.0);
		host.wallUp = (int)(host.height/8.0);
		host.wallDown = (int)(host.height/8.0);
	}
	
	void step1(){
		if (!created){
			Jelly j = new Jelly(host, 0,0);
			host.instances.add(j);
			host.hero=j;
			created = true;
		}
	
//		if(Math.random()<host.timePassed*0.0002){
//		if(Math.random()<host.timePassed*0.002){
//			Log.d("LevelMaker","Around 80. I made a fish!");
//			Fish f = new Fish(host, (float)(host.xStart),
//					(float)Math.random()*(host.yEnd-host.yStart)+host.yStart);
//			host.instances.add(f);
//		}
//		if(Math.random()<host.timePassed*0.0002){
//		if(Math.random()<host.timePassed*0.002){
//			Log.d("LevelMaker","Around 90. I made a fish!");
//			float y = (float)Math.random()*(host.yEnd-host.yStart)+host.yStart;
//			Fish f = new Fish(host, (float)(host.xStart), y);
//			host.instances.add(f);
//			Fish f2 = new Fish(host, (float)(host.xStart)-150, y+150);
//			host.instances.add(f2);
//			Fish f3 = new Fish(host, (float)(host.xStart)-150, y-150);
//			host.instances.add(f3);
//		}
//		if(Math.random()<host.timePassed*0.0002){
////		if(Math.random()<host.timePassed*0.0002){
//			Log.d("LevelMaker","Around 100. I made a fish!");
//			float y = (float)Math.random()*(host.yEnd-host.yStart)+host.yStart;
//			Fish f = new Fish(host, (float)(host.xStart), y);
//			host.instances.add(f);
//			Fish f2 = new Fish(host, (float)(host.xStart)-150, y+150);
//			host.instances.add(f2);
//			Fish f3 = new Fish(host, (float)(host.xStart)-300, y+300);
//			host.instances.add(f3);
//			Fish f4 = new Fish(host, (float)(host.xStart)-500, y+450);
//			host.instances.add(f4);
//		}
//		if(Math.random()<host.timePassed*0.005){
//		if(Math.random()<host.timePassed*0.05){
		while(host.instances.size() < 500){
			Bubble b = new Bubble(host, (float)Math.random()*(host.xEnd-host.xStart)+host.xStart,
					host.yEnd);
			host.instances.add(b);
		}
		
		Log.d("LevelMaker","Around 110: Number of objects: "+host.instances.size());
	}
	

}

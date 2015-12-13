package com.example.huntedseasgl;

public class LevelMaker {
	
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
		host.xStart=-4000;
		host.xEnd = 4000;
		host.yStart=-1000;
		host.yEnd = 1000;

		host.width = (int)host.renderer.offsetX*2;
		host.height = (int)host.renderer.offsetY*2;
				

	}
	
	void load1(){
		host.bg = new Background(-host.xStart,host.yStart,host.xEnd-host.xStart,host.yEnd-host.yStart,20,host,
				R.drawable.blue2);
		Fish.load(host);
		Jelly.load(host);
		
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
		if(Math.random()<host.timePassed*0.001){
			System.out.println("I made a fish!");
			Fish f = new Fish(host, (float)Math.random()*3000-1500,
					(float)Math.random()*3000-1500);
			host.instances.add(f);
		}
	}
	

}

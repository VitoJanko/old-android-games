package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Liner extends Instance{
	
	int time;
	
	Liner(Darkness host){
		super(0,0,0,host);
		enemy=false;
		time=0;
	}
	
	protected void step(){
		time++;
		if(time<300){
			if(Math.random()<0.01) host.instances.add(new LinerMinion(host,0,0,host.width,0,0,1));
			if(Math.random()<0.01) host.instances.add(new LinerMinion(host,0,0,1,host.height,1,0));
		}
		if(time>300 && time<900){
			if(Math.random()<0.011) host.instances.add(new LinerMinion(host,0,0,host.width/2,0,0,1));
			if(Math.random()<0.012) host.instances.add(new LinerMinion(host,0,0,1,host.height/2,1,0));
			if(Math.random()<0.012) host.instances.add(new LinerMinion(host,host.width/2,0,host.width,0,0,1));
			if(Math.random()<0.012) host.instances.add(new LinerMinion(host,0,host.height/2,1,host.height,1,0));
		}
		if(time>900 && time<1500){
			if(Math.random()<0.01) host.instances.add(new LinerMinion(host,0,0,host.width/3,0,0,1));
			if(Math.random()<0.01) host.instances.add(new LinerMinion(host,0,0,1,host.height/3,1,0));
			if(Math.random()<0.01) host.instances.add(new LinerMinion(host,host.width/3,0,(int)(host.width*0.66),0,0,1));
			if(Math.random()<0.01) host.instances.add(new LinerMinion(host,0,host.height/3,1,(int)(host.height*0.66),1,0));
			if(Math.random()<0.01) host.instances.add(new LinerMinion(host,(int)(host.width*0.66),0,host.width,0,0,1));
			if(Math.random()<0.01) host.instances.add(new LinerMinion(host,0,(int)(host.height*0.66),1,host.height,1,0));
		}
		if(time>1200) dead=true;
	}
	
}


class LinerMinion extends Instance{
	
	int x1,x2,y1,y2;
	int dirX, dirY;
	int alpha;
	
	LinerMinion(Darkness host, int x1, int y1, int x2, int y2, int dirX, int dirY){
		super(0,0,0,host);
		onlyAlt=true;
		this.x1=x1;
		this.x2=x2;
		this.y1=y1;
		this.y2=y2;
		this.dirX=dirX;
		this.dirY=dirY;
		speed=3*host.ratio;
		alpha=50;
	}
	
	boolean altCollision(int xHero, int yHero, int radij){
		boolean collide = false;
		if (localCollision(xHero,yHero,radij,x1,y1,x2,y2)) collide = true;
		if (alpha!=255) collide=false;
		return collide;
	}
	
	protected void step(){
		alpha+=4;
		if(alpha>255) alpha=255;
		x1+=speed*dirX;
		x2+=speed*dirX;
		y1+=speed*dirY;
		y2+=speed*dirY;
		if(x1>host.width+20 || x1<-20) dead=true;
		if(y1>host.height+20 || y1<-20) dead=true;
	}
	
	protected void draw(Canvas c, Paint p){
		p.setColor(Color.argb(alpha,255,255,255));
		c.drawLine(x1,y1,x2,y2,p);
	}
	
}
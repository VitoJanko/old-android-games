package com.above_average;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Hedgehog extends Instance{
	
	int spikeDir;
	float dX, dY, dX2, dY2;
	boolean spikes;
	
	Hedgehog(int radij, Darkness host){
		super(0,0,radij,host);
		generateRandom();
		direction= (int)(Math.random()*360);
		speed = 3*host.ratio;
		spikeDir = (int)(Math.random()*360);
		spikes=true;
		left=70;
	}

	void step(){
		spikeDir+=5;
		if(spikeDir>360)spikeDir-=360;
		rotate(0.4f,10);
		x+= dirX;
		y+= dirY;
		dX = radij*(float)Math.cos(Math.toRadians(spikeDir));
		dY = radij*(float)Math.sin(Math.toRadians(spikeDir));
		int sP = spikeDir+90;
		if(sP>360) sP-=360;
		dX2 = radij*(float)Math.cos(Math.toRadians(sP));
		dY2 = radij*(float)Math.sin(Math.toRadians(sP));
		left--;
		if(hit){
			if(left>0) left=0;
			dead=true;
		}
		if(left==0){
			spikes=false;
			host.instances.add(new Bullet(x,y,x+dX,y+dY,8*host.ratio,false,host));
			host.instances.add(new Bullet(x,y,x-dX,y-dY,8*host.ratio,false,host));
			host.instances.add(new Bullet(x,y,x+dX2,y+dY2,8*host.ratio,false,host));
			host.instances.add(new Bullet(x,y,x-dX2,y-dY2,8*host.ratio,false,host));
		}
		deathWall();
	}
	
	boolean altCollision(int xHero, int yHero, int radij){
		boolean collide = false;
		if(spikes){
			if(localCollision(xHero,yHero,radij,x-dX,y-dY,x+dX,y+dY))collide=true;
			if(localCollision(xHero,yHero,radij,x-dX2,y-dY2,x+dX2,y+dY2))collide=true;
		}
		return collide;
	}
	
	void draw(Canvas c, Paint p){
		drawCircle(c,p);
		if(spikes){
			c.drawLine(x-dX,y-dY,x+dX,y+dY,p);
			c.drawLine(x-dX2,y-dY2,x+dX2,y+dY2,p);
		}
	}
	
}

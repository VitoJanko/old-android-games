package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Stalker extends Instance{
	
	Stalker(Darkness host, int width, int height, int radij){
		super(0,0,radij,host);
		if(Math.random()>0.5){
			int random = (int)(Math.random()*height);
			y = random;
			if(Math.random()>0.5) x = 0 - 2*radij;
			else x=width+2*radij;
		}
		else{
			int random = (int)(Math.random()*width);
			x=random;
			if(Math.random()>0.5) y = 0 - 2*radij;
			else y=height+2*radij;
		}
		left=240;
		speed=4*host.ratio;
	}
	
	protected void step(){
		follow(host.hero.x,host.hero.y);
		speed+=0.06*host.ratio;
		if(speed>6.5*host.ratio)speed=4*host.ratio;
		if(harmDone>35) dead=true;
		left--;
		if(left==0) dead=true;
		if(hit) dead=true;
	}
	
	protected void draw(Canvas c, Paint p){
		drawTriangle(host.hero.x,host.hero.y,c,p,Color.WHITE);
	}
}

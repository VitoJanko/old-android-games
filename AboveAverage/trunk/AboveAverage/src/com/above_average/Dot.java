package com.above_average;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Dot extends Instance{
	double dirX;
	double dirY;
	int width;
	int height;
	
	Dot(int radij, Darkness host){
		super(0,0,radij,host);
		this.width = host.width;
		this.height = host.height;
		dirX =(int)(Math.random()*4)-2;
		dirY =(int)(Math.random()*4)-2;
		generateRandom();
		if(host.grid!=null)
			host.makeCharge((int)x,(int)y);
		if(host.gridRed!=null)
			host.makeChargeRed((int)x,(int)y);
	}
	
	protected void step(){
		if(x+dirX>width+radij/2 || x+dirX<-radij/2) dead=true;
		if(y+dirY>height-radij/2 || y+dirY<-radij/2) dead=true;
		x+= dirX;
		y+= dirY;
		dirX+= Math.random()*1 -0.5;
		dirY+= Math.random()*1 -0.5;
		if(hit)dead=true;
	}
	
	protected void draw(Canvas c, Paint p){
		drawSquare(c,p);
	}
}

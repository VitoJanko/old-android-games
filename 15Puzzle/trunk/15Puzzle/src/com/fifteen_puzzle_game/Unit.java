package com.fifteen_puzzle_game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Unit {
	double x;
	double y;
	double dirX;
	double dirY;
	int width;
	int height;
	int radij;
	int color;
	Unit(int width, int height, int radij){
		this.width = width;
		this.height = height;
		this.radij = radij;
		dirX =(int)(Math.random()*4)-2;
		dirY =(int)(Math.random()*4)-2;
		x =(int)(Math.random()*width);
		y =(int)(Math.random()*height);
		int r = (int)(Math.random()*100) +155;
		int g = (int)(Math.random()*100) +155;
		int b = (int)(Math.random()*100) +155;
		color = Color.argb(255,r,g,b);
	}
	
	protected void step(){
		if(x+dirX>width-radij/2 || x+dirX<radij/2) dirX = 0;
		if(y+dirY>height-radij/2 || y+dirY<radij/2) dirY = 0;
		x+= dirX;
		y+= dirY;
		dirX+= Math.random()*1 -0.5;
		dirY+= Math.random()*1 -0.5;
	}
	
	protected void draw(Canvas c, Paint p){
		p.setColor(color);
		p.setStyle(Paint.Style.STROKE);
		c.drawRect((int)x-radij/2,(int)y-radij/2,(int)x+radij/2,(int)y+radij/2,p);
	}
}

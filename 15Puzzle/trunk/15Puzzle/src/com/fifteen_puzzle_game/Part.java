package com.fifteen_puzzle_game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Part {
	double x;
	double y;
	double dirX;
	double dirY;
	int width;
	int height;
	int red;
	int green;
	int blue;
	int alpha;
	Part(int x, int y, int radij, int color){
		this.y=y;
		this.x=x;
		this.width = radij;
		this.height = radij;
		dirX =(int)(Math.random()*2)-1;
		dirY =(int)(Math.random()*2)-1;
		this.red = Color.red(color);
		this.green = Color.green(color);
		this.blue = Color.blue(color);
		alpha = 255;
	}
	
	Part(int x, int y, int width, int height, int color){
		this(x,y,0,color);
		this.width = width;
		this.height = height;
	}
	
	Part(){
		x=0; y=0;
		alpha=0; red =0; green =0; blue=0;
		dirX=0; dirY=0;
		width = 1; height = 1;
	}
	
	protected void step(){
		x+= dirX;
		y+= dirY;
		alpha -= 5;
	}
	
	protected void draw(Canvas c, Paint p){
		p.setColor(Color.argb(alpha, red, green, blue));
		p.setStyle(Paint.Style.FILL);
		c.drawRect((int)x-width/2,(int)y-height/2,(int)x+width/2,(int)y+height/2,p);
	}
}

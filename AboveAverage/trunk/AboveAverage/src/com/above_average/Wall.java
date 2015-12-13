package com.above_average;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Wall extends Instance{

	int x1,y1,x2,y2;
	int color;
	
	Wall(int x1, int y1, int x2, int y2, int color, Darkness host){
		super(0,0,0,host);
		enemy=false;
		this.x1=x1;
		this.x2=x2;
		this.y1=y1;
		this.y2=y2;
		this.color=color;
	}
	
	void draw(Canvas c, Paint p){
		p.setColor(color);
		p.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x1,y1,x2,y2);
		c.drawRoundRect(r,9f,9f,p);
	}
	
	boolean altCollision(int xHero, int yHero, int radij){
		boolean collide = false;
		if (localCollision(xHero,yHero,radij,x1,y1,x2,y1)) collide = true;
		if (localCollision(xHero,yHero,radij,x1,y2,x2,y2)) collide = true;
		if (localCollision(xHero,yHero,radij,x1,y1,x1,y2)) collide = true;
		if (localCollision(xHero,yHero,radij,x2,y1,x2,y2)) collide = true;
		if(xHero>=x1 && xHero<=x2 && yHero>=y1 && yHero<=y2) collide=true;
		return collide;
	}
	
	void step(){
		for(int i=0; i<host.instances.size(); i++){
			Instance in = host.instances.get(i);
			if((in.enemy || in.solid)&& altCollision((int)in.x,(int)in.y,in.radij)){
				if(!in.immune)
					in.dead=true;
			}
		}
	}
}

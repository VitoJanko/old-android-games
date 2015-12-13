package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class Jack extends Instance{
	
	boolean close;
	int alpha;
	int mode;
	int angle;
	int maxSize;
	int size;
	
	float xStart1, xEnd1; 
	float xStart2, xEnd2;
	float yStart1, yEnd1;
	float yStart2, yEnd2;
	
	float xStart3, xEnd3; 
	float xStart4, xEnd4;
	float yStart3, yEnd3;
	float yStart4, yEnd4;
	
	Jack(int radij, int distance, Darkness host){
		super(0,0,radij,host);
		generateRandom();
		close=false;
		mode=0;
		maxSize=distance;
		size=0;
		angle=62;
	}

	void step(){
		Protagonist p = host.hero;
		int razdalja=host.razdalja(x,y,p.x,p.y);
		if(razdalja<p.radij*2+radij/2) close=true;
		else close=false;
		int razdaljaAlpha=razdalja;
		for(int i=0; i<host.maker.light.size(); i++){
			Firefly f = host.maker.light.get(i);
			int toF = host.razdalja(x, y, f.x, f.y);
			razdaljaAlpha=Math.min(razdaljaAlpha, toF);
			if(f.pacman && toF<radij/2+f.radij/2) dead=true;
		}
		if(close && !host.won){
			if(mode==0) mode=1;
		}
		if(mode==1){
			size+=host.ratio*5;
			if(size>=maxSize) mode=2;
			angle-=2;
		}
		if(mode==2){
			size-=7*host.ratio;
			if(size<=0) {
				size=0;
				mode=0;
				angle=62;
			}
		}
		if(razdaljaAlpha>p.radij*3) alpha=0;
		else alpha= 255 * (p.radij*3-razdaljaAlpha)/(p.radij*3);
		
		if(mode==1) alpha=255;
		
		if(hit){
			dead=true;
		}
	}
	
	int norm(int a){
		if(a<0)a+=360;;
		if(a>360)a-=360;
		return a;
	}
	
	boolean altCollision(int xHero, int yHero, int radij){
		boolean collide = false;
		if(mode!=0){
			if (localCollision(xHero,yHero,radij,xStart1,yStart1,xEnd1,yEnd1)) collide = true;
			if (localCollision(xHero,yHero,radij,xStart2,yStart2,xEnd2,yEnd2)) collide = true;
			if (localCollision(xHero,yHero,radij,xStart3,yStart3,xEnd3,yEnd3)) collide = true;
			if (localCollision(xHero,yHero,radij,xStart4,yStart4,xEnd4,yEnd4)) collide = true;
		}
		return collide;
	}
	
	void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		pp.setColor(Color.argb(alpha,255,255,255));
		pp.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, pp);
		if(size>0){
			int dir = getDirection(x,y,host.hero.x,host.hero.y);
			xStart1 = (float)(x+Math.cos(Math.toRadians(dir+90))*radij/2);
			yStart1 = (float)(y+Math.sin(Math.toRadians(dir+90))*radij/2);
			xStart2 = (float)(x+Math.cos(Math.toRadians(dir-90))*radij/2);
			yStart2 = (float)(y+Math.sin(Math.toRadians(dir-90))*radij/2);
			xEnd1 = (float)(xStart1+Math.cos(Math.toRadians(dir+angle))*size);
			yEnd1 = (float)(yStart1+Math.sin(Math.toRadians(dir+angle))*size);
			xEnd2 = (float)(xStart2+Math.cos(Math.toRadians(dir-angle))*size);
			yEnd2 = (float)(yStart2+Math.sin(Math.toRadians(dir-angle))*size);
			float xMeet = (float)(x+Math.cos(Math.toRadians(dir))*size*0.43);
			float yMeet = (float)(y+Math.sin(Math.toRadians(dir))*size*0.43);
			Path path = new Path();
			path.setFillType(Path.FillType.EVEN_ODD);
			path.moveTo(xStart1,yStart1);
			path.lineTo(xEnd1,yEnd1);
			path.lineTo(xMeet,yMeet);
			path.lineTo(xEnd2,yEnd2);
			path.lineTo(xStart2,yStart2);
			path.lineTo(xStart1,yStart1);
			path.close();
			c.drawPath(path, pp);
			
			for(int i=0; i<3; i++){
				drawTeeth(c,pp,xEnd1,yEnd1,xMeet,yMeet,1,i);
				drawTeeth(c,pp,xEnd2,yEnd2,xMeet,yMeet,-1,i);
			}
		}
	}
	
	void drawTeeth(Canvas c, Paint p, float startX, float startY, float endX, float endY ,int predznak,int part){
		float distance = host.razdalja(startX,startY,endX,endY);
		int angle = getDirection(startX,startY,endX,endY);
		float dis1 = (distance/4f)*part;
		float dis2 = (distance/4f)*(part+1);
		double kot1 = Math.cos(Math.toRadians(angle));
		double kot2 = Math.sin(Math.toRadians(angle));
		float x1 = (float)(startX+kot1*dis1);
		float y1 = (float)(startY+kot2*dis1);
		float xI = (float)(startX+kot1*(dis1/2+dis2/2));
		float yI = (float)(startY+kot2*(dis1/2+dis2/2));
		float x3 = (float)(startX+kot1*dis2);
		float y3 = (float)(startY+kot2*dis2);
		float x2 = (float)(xI+Math.cos(Math.toRadians(angle+90*predznak))*(dis2-dis1));
		float y2 = (float)(yI+Math.sin(Math.toRadians(angle+90*predznak))*(dis2-dis1));
		if(part==0 && predznak==1) {xStart3=x2; yStart3=y2;}
		if(part==2 && predznak==1) {xEnd3=x2; yEnd3=y2;}
		if(part==0 && predznak==-1) {xStart4=x2; yStart4=y2;}
		if(part==2 && predznak==-1) {xEnd4=x2; yEnd4=y2;}
		c.drawLine(x1,y1,x2,y2,p);
		c.drawLine(x2,y2,x3,y3,p);
	}
}

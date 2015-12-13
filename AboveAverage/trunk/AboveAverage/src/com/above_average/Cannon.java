package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;

public class Cannon extends Instance{
	
	static int type;
	
	int health;
	int red;
	int timer;
	int point1X,point2X,point3X,point4X;
	int point1Y,point2Y,point3Y,point4Y;
	
	Cannon(Darkness host, int radij, int type){
		super(0,0,radij,host);
		generateOutside();
		left=420;
		speed= 1f*host.ratio;
		timer = 100;
		direction=getDirection(x,y,host.width/2,host.height/2);
		red=255;
		health=5;
	}
	
	protected void step(){
		int desired = getDirection(x,y,host.hero.x,host.hero.y);
		if(Math.abs(direction-(desired+360))<Math.abs(direction-desired)) desired+=360;
		if(Math.abs(direction-(desired-360))<Math.abs(direction-desired)) desired-=360;
		if(Math.abs(direction-desired)<2) direction=desired;
		else if(direction>desired) direction-=2;
		else direction+=2;
		x+=Math.cos(Math.toRadians(direction))*speed;
		y+=Math.sin(Math.toRadians(direction))*speed;
		point1X=(int)(x+Math.cos(Math.toRadians(direction+30))*radij/2);
		point2X=(int)(x+Math.cos(Math.toRadians(direction-30))*radij/2);
		point3X=(int)(x+Math.cos(Math.toRadians(direction+180+45))*radij/1.5);
		point4X=(int)(x+Math.cos(Math.toRadians(direction+180-45))*radij/1.5);
		point1Y=(int)(y+Math.sin(Math.toRadians(direction+30))*radij/2);
		point2Y=(int)(y+Math.sin(Math.toRadians(direction-30))*radij/2);
		point3Y=(int)(y+Math.sin(Math.toRadians(direction+180+45))*radij/1.5);
		point4Y=(int)(y+Math.sin(Math.toRadians(direction+180-45))*radij/1.5);
		timer--;
		if(timer==0){
			if(type==0)
				host.instances.add(new Bouncy(x,y,(int)(radij*0.5),direction,host));
			if(type==1){
				host.instances.add(new Sticky(x,y,(int)(radij*0.5),direction,host));
			}
			timer=100;
		}
		if(red<255) red+=8;
		if(red>255) red=255;
		if(hit) {
			health--;
			if(health==0) dead=true;
			red=50;
			hit=false;
		}
		left--;
		if(left==0) 
			dead=true;
	}
	
	protected void draw(Canvas c, Paint p){
		p.setColor(Color.rgb(255,red,red));
		p.setStyle(Paint.Style.FILL);
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(point1X,point1Y);
		path.lineTo(point2X,point2Y);
		path.lineTo(point3X,point3Y);
		path.lineTo(point4X,point4Y);
		path.lineTo(point1X,point1Y);
		path.close();
		c.drawPath(path, p);
	}
}

class Sticky extends Instance{

	int anchored;
	int size;
	
	Sticky(float x, float y, int radij, int direction, Darkness host){
		super(x,y,radij,host);
		speed=9*host.ratio;
		this.direction=direction;
		immune=true;
		anchored=-1;
		size=-1;
	}
	
	boolean altCollision(int xHero, int yHero, int rHero){
		if(anchored==0)
			if(host.razdalja(x,y,xHero,yHero)<rHero/2+(radij+size)/2)
				if(host.razdalja(x,y,xHero,yHero)>(radij+size)/2-rHero/2)
					return true;
		return false;
	}
	
	void step(){
		if(anchored>0) anchored--;
		if(anchored==0){
			size+=4*host.ratio;
		}
		if(size>radij*6) dead=true;
		x+=Math.cos(Math.toRadians(direction))*speed;
		y+=Math.sin(Math.toRadians(direction))*speed;
		deathWall();
		for(int i=0; i<host.obstacle.size(); i++){
			Wall in = (Wall)(host.obstacle.get(i));
			if(in.altCollision((int)x, (int)y, radij)){
				if (anchored==-1) 
					anchored=45;
					speed=0;
			}
		}
	}
	
	protected void draw(Canvas c, Paint p){
		if(size>0){
			p.setColor(Color.WHITE);
			p.setStyle(Paint.Style.STROKE);
			RectF r = new RectF(x-(radij+size)/2, y-(radij+size)/2, x+(radij+size)/2, y+(radij+size)/2);
			c.drawOval(r, p);
		}
		else{
			double pointX1=x+Math.cos(Math.toRadians(direction))*radij/2;
			double pointY1=y+Math.sin(Math.toRadians(direction))*radij/2;
			double pointX2=x+Math.cos(Math.toRadians(direction+165))*radij/2;
			double pointY2=y+Math.sin(Math.toRadians(direction+165))*radij/2;
			double pointX3=x+Math.cos(Math.toRadians(direction+195))*radij/2;
			double pointY3=y+Math.sin(Math.toRadians(direction+195))*radij/2;
			p.setColor(Color.WHITE);
			p.setStyle(Paint.Style.FILL);
			Path path = new Path();
			path.setFillType(Path.FillType.EVEN_ODD);
			path.moveTo((float)pointX1,(float)pointY1);
			path.lineTo((float)pointX2,(float)pointY2);
			path.lineTo((float)pointX3,(float)pointY3);
			path.lineTo((float)pointX1,(float)pointY1);
			path.close();
			c.drawPath(path, p);
		}
	}
}

class Bouncy extends Instance{

	int bounced;
	int red;
	int health;
	
	Bouncy(float x, float y, int radij, int direction, Darkness host){
		super(x,y,radij,host);
		speed=8*host.ratio;
		this.direction=direction;
		immune=true;
		left=160;
		bounced=0;
		red=255;
		health=2;
	}
	
	void step(){
		if(bounced>0)bounced--;
		x+=Math.cos(Math.toRadians(direction))*speed;
		y+=Math.sin(Math.toRadians(direction))*speed;
		bounceWall();
		if(bounced==0){
			for(int i=0; i<host.obstacle.size(); i++){
				Wall in = (Wall)(host.obstacle.get(i));
				if(in.altCollision((int)x, (int)y, radij)){
					int dir=getDirection((in.x1+in.x2)/2,(in.y1+in.y2)/2,x,y);
					if(dir>360)dir-=360;
					if(dir<0)dir+=360;
					if(dir>45 && dir<135)
						direction=180+(180-direction);
					else if (dir>225 && dir<315){
						direction=180+(180-direction);
					}
					else if(dir>=0 && dir<=45){
						direction=270+(270-direction);
					}
					else{
						direction=270+(270-direction);
					}
					bounced=3;
				}
				
			}
		}
		if(red<255) red+=8;
		if(red>255) red=255;
		if(hit) {
			health--;
			if(health==0) dead=true;
			red=50;
			hit=false;
		}
		left--;
		if(left==0)dead=true;
	}
	
	protected void draw(Canvas c, Paint p){
		p.setColor(Color.rgb(255,red,red));
		p.setStyle(Style.FILL);
		RectF r= new RectF(x-radij/2,y-radij/2,x+radij/2,y+radij/2);
		c.drawOval(r, p);
	}
}


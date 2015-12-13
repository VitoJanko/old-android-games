package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Sniper extends Instance{
	
	int ammo;
	int timer;
	
	Sniper(Darkness host, int width, int height, int radij){
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
		speed= 1.5f*host.ratio;
		timer = 40;
		ammo = 3;
	}
	
	protected void step(){
		follow(host.hero.x,host.hero.y);
		timer--;
		if(timer==0){
			if(ammo==0){
				timer=40;
				ammo=3;
			}
			else{
				float vectorX = host.hero.x-x;
				float vectorY = host.hero.y-y;
				float norma = (float)Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY,2));
				float x1 = (vectorX*(float)(radij/5f))/norma;
				float y1 = (vectorY*(float)(radij/5f))/norma;
				host.instances.add(new Bullet(x,y,x+x1,y+y1,6*host.ratio,false,host));
				timer=8;
				ammo--;
			}
		}
		left--;
		if(left==0) dead=true;
		if(hit) dead=true;
	}
	
	protected void draw(Canvas c, Paint p){
		drawTriangle(host.hero.x,host.hero.y,c,p,Color.WHITE);
	}
}

class Bullet extends Instance{
	float diffX;
	float diffY;
	float speedX;
	float speedY;
	boolean good;
	
	Bullet(float x, float y, float endX, float endY, float speed, boolean good,Darkness host){
		super(x,y,0,host);
		diffX = endX-x;
		diffY = endY-y;
		float norma = (float)Math.sqrt(Math.pow(diffX,2)+Math.pow(diffY,2));
		speedX=speed*(diffX/norma);
		speedY=speed*(diffY/norma);
		x+=diffX;
		y+=diffY;
		radij = (int)norma;
		this.speed=speed;
		onlyAlt=true;
		left = 100;
		this.good=good;
		if(good) {
			solid=true;
			enemy=false;
		}
	}
	
	int razdalja(float x1,float y1,float x2,float y2){
		return (int)(Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2)));
	} 
	
	boolean altCollision(int xC, int yC, int rC){
		boolean collide = false;
		if(razdalja(x,y,xC,yC)<rC/2) collide = true;
		if(razdalja(x-diffX,y-diffY,xC,yC)<rC/2) collide = true;
		return collide;
	}
	
	void step(){
		x+=speedX;
		y+=speedY;
		if(good){
			for(int i=0; i<host.instances.size(); i++){
				Instance in = host.instances.get(i);
				if(in.enemy){
					if((!in.onlyAlt && host.razdalja(in.x,in.y,x,y)<radij/2+in.radij/2 )|| in.altCollision((int)x,(int)y,radij)){
						in.hit=true;
						dead=true;
					}
				}
			}
		}
		deathWall();
		left--;
		if(hit) dead=true;
		if(left==0) dead=true;
	}
	
	void draw(Canvas c, Paint p){
		p.setColor(Color.WHITE);
		if(good) p.setColor(Color.RED);
		c.drawLine(x,y,x-diffX,y-diffY,p);
	}
}

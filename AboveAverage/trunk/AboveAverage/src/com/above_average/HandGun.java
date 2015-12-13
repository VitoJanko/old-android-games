package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

public class HandGun extends Instance{
	
	int ammo;
	int timer;
	int mode;
	
	HandGun(Darkness host, int radij, int mode, int direction){
		super(0,0,radij,host);
		enemy=false;
		this.direction=direction;
		timer=30;
		ammo=6;
		this.mode=mode;
	}

	protected void step(){
		if(mode==0){
			int premik = 5;
			int desired = host.hero.direction+180;
			if(direction<0) direction+=360; if(direction>360) direction-=360;
			if(desired<0) desired+=360; if(desired>360) desired-=360;
			if(desired>=0 && desired <90 && direction>180 && direction<=360)desired+=360;
			if(direction>=0 && direction <90 && desired>180 && desired<=360)desired-=360;
			if(Math.abs(direction-desired)>120) premik=30;
			else if(Math.abs(direction-desired)>70) premik=15; 
			if(Math.abs(direction-desired)<5) direction=desired;
			else if(direction>desired) direction-=premik;
			else direction+=premik;
		}	
		if(mode==1){
			direction+=8;
		}
		if(mode==2){
			int distance=-1;
			for(int i=0; i<host.instances.size(); i++){
				Instance in = host.instances.get(i);
				if(in.enemy){
					int d = host.razdalja(host.hero.x, host.hero.y, in.x, in.y);
					if(distance==-1 || distance>d){
						distance=d;
						direction=getDirection(host.hero.x, host.hero.y, in.x, in.y);
					}
				}
			}
			if(distance==-1)
				direction+=8;
		}
		x=(float)(host.hero.x+Math.cos(Math.toRadians(direction))*host.hero.radij*0.75);
		y=(float)(host.hero.y+Math.sin(Math.toRadians(direction))*host.hero.radij*0.75);
		timer--;
		if(timer==0){
			if(ammo==0){
				timer=32;
				ammo=6;
				if(mode==1) ammo=9;
			}
			else{
				float vectorX = host.hero.x-x;
				float vectorY = host.hero.y-y;
				float norma = (float)Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY,2));
				float x1 = (vectorX*(float)(radij/5f))/norma;
				float y1 = (vectorY*(float)(radij/5f))/norma;
				host.instances.add(new Bullet(x+2*x1,y+2*y1,x,y,6*host.ratio,true,host));
				timer=6;
				if(mode==1) timer=4;
				ammo--;
			}
		}
	}
	
	protected void draw (Canvas c, Paint p){
		if(mode==0){
			//c.drawText(direction+"",20,20,p);
			drawTriangle(x+(float)Math.cos(Math.toRadians(direction)),y+(float)Math.sin(Math.toRadians(direction)),c,p,Color.RED);
		}
		if(mode==1)
			drawTriangle(x+(float)Math.cos(Math.toRadians(direction)),y+(float)Math.sin(Math.toRadians(direction)),c,p,Color.rgb(255,128,0));
		if(mode==2)
			drawTriangle(x+(float)Math.cos(Math.toRadians(direction)),y+(float)Math.sin(Math.toRadians(direction)),c,p,Color.YELLOW);
	}
}

class HandGunPickUp extends Instance{
	
	int mode;
	float x1, y1;
	int radijTri;
	
	HandGunPickUp(Darkness host, int radij, int radijTri, int mode){
		super(0,0,radij,host);
		this.mode=mode;
		generateRandom();
		direction=0;
		enemy=false;
		this.radijTri=radijTri;
	}
	
	protected void step(){
		if(mode==0){
			int premik = 5;
			int desired = host.hero.direction+180;
			if(direction<0) direction+=360; if(direction>360) direction-=360;
			if(desired<0) desired+=360; if(desired>360) desired-=360;
			if(desired>=0 && desired <90 && direction>180 && direction<=360)desired+=360;
			if(direction>=0 && direction <90 && desired>180 && desired<=360)desired-=360;
			if(Math.abs(direction-desired)>120) premik=30;
			else if(Math.abs(direction-desired)>70) premik=15; 
			if(Math.abs(direction-desired)<5) direction=desired;
			else if(direction>desired) direction-=premik;
			else direction+=premik;
		}	
		if(mode==1){
			direction+=10;
		}
		if(mode==2){
			int distance=-1;
			for(int i=0; i<host.instances.size(); i++){
				Instance in = host.instances.get(i);
				if(in.enemy){
					int d = host.razdalja(host.hero.x, host.hero.y, in.x, in.y);
					if(distance==-1 || distance>d){
						distance=d;
						direction=getDirection(host.hero.x, host.hero.y, in.x, in.y);
					}
				}
			}
			if(distance==-1)
				direction+=8;
		}
		x1=(float)(x+Math.cos(Math.toRadians(direction))*radij*0.75);
		y1=(float)(y+Math.sin(Math.toRadians(direction))*radij*0.75);
		if(host.razdalja(x,y,host.hero.x,host.hero.y)<host.hero.radij/2+radij/2){
			if(host.hero.weapon!=null) host.hero.weapon.dead=true;
			HandGun gun = new HandGun(host,radijTri,mode,0);
			host.hero.weapon=gun;
			host.instances.add(gun);
			dead=true;
		}
	}
	
	protected void draw (Canvas c, Paint p){
		if(mode==0) p.setColor(Color.RED);
		if(mode==1) p.setColor(Color.rgb(255,128,0));
		if(mode==2) p.setColor(Color.YELLOW);
		p.setStyle(Style.STROKE);
		RectF r = new RectF(x-radij/2,y-radij/2,x+radij/2,y+radij/2);
		c.drawOval(r,p);
		int temp = radij; radij=radijTri;
		float tempX = x; float tempY=y; x=x1; y=y1; 
		drawTriangle(x+(float)Math.cos(Math.toRadians(direction)),y+(float)Math.sin(Math.toRadians(direction)),c,p,p.getColor());
		radij=temp; x=tempX; y=tempY;
	}
}

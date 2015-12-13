package com.above_average;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;

public class WarStation{

	int teamSquare;
	int teamCircle;
	ArrayList<Soldier> soldiers;
	ArrayList<SpawnStation> stations;
	
	WarStation (Darkness host){
		soldiers=new ArrayList<Soldier>();
		stations=new ArrayList<SpawnStation>();
		int start=0;
		int diff=host.width/12;
		for(int i=0; i<12; i++){
			SpawnStation s1 = new SpawnStation(host,start,true,this);
			SpawnStation s2 = new SpawnStation(host,start,false,this);
			host.instances.add(s1); 
			host.instances.add(s2);
			stations.add(s1);
			stations.add(s2);
			start+=diff;
		}
	}
	
}

class SpawnStation extends Instance{
	
	WarStation war;
	int x1, x2, y1, y2;
	boolean up;
	int spawnY;
	
	SpawnStation(Darkness host, int width, boolean up, WarStation w){
		super(0,0,0,host);
		onlyAlt=true;
		war=w;
		this.up=up;
		x1=width;
		x2=width+host.width/12;
		if(up){
			y1=0;
			y2=host.height/20;
			w.teamSquare++;
			spawnY=-host.width/12;
		}
		else{
			y1=(host.height*19)/20;
			y2=host.height;
			w.teamCircle++;
			spawnY=host.height;
		}
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
	
	protected void step(){
		if(Math.random()<0.006){
			Soldier s= new Soldier(host,x1,spawnY,x2-x1,up,war);
			host.instances.add(s);
			war.soldiers.add(s);
		}
		if(!dead)
			for(int i=0; i<war.soldiers.size(); i++){
				Soldier s= war.soldiers.get(i);
				if(s.dead){
					war.soldiers.remove(i);
					i--;
				}
				else if(up && !s.up){
					if(s.x < x2 && s.x+s.width>x1)
						if(s.y<y2){
							s.dead=true;
							dead=true;
							war.teamSquare--;
						}
				}
				else if(!up && s.up){
					if(s.x < x2 && s.x+s.width>x1)
						if(s.y+s.width>y1){
							s.dead=true;
							dead=true;
							war.teamCircle--;
						}
				}
			}
	}
	
	protected void draw(Canvas c, Paint p){
		p.setColor(Color.WHITE);
		p.setStyle(Style.STROKE);
		Rect r=new Rect(x1,y1,x2,y2);
		c.drawRect(r,p);
		r=new Rect(x1+1,y1+1,x2-1,y2-1);
		c.drawRect(r,p);
	}
}

class Soldier extends Instance{
	
	boolean up;
	int width;
	WarStation war;
	
	Soldier(Darkness host, int x, int y, int width, boolean up, WarStation w){
		super(x,y,0,host);
		this.width=width;
		this.up=up;
		war=w;
		speed=4*host.ratio;
		onlyAlt=true;
		int dir1=0,dir2=0;
		if(!up){
			dir1=getDirection(x,y,0,0);
			dir2=getDirection(x,y,host.width,0);
		}
		else{
			dir1=getDirection(x,y,0,host.height);
			dir2=getDirection(x,y,host.width,host.height);
		}
		if(dir1<0)dir1+=360;
		if(dir2<0)dir2+=360;
		direction=dir1+(int)(Math.random()*(dir2-dir1));
	}
	
	boolean altCollision(int xHero, int yHero, int radij){
		boolean collide = false;
		if (localCollision(xHero,yHero,radij,x,y,x+width,y)) collide = true;
		if (localCollision(xHero,yHero,radij,x,y+width,x+width,y+width)) collide = true;
		if (localCollision(xHero,yHero,radij,x,y,x,y+width)) collide = true;
		if (localCollision(xHero,yHero,radij,x+width,y,x+width,y+width)) collide = true;
		if(xHero>=x && xHero<=x+width && yHero>=y && yHero<=y+width) collide=true;
		return collide;
	}
	
	protected void step(){
		x+=speed*Math.cos(Math.toRadians(direction));
		y+=speed*Math.sin(Math.toRadians(direction));
		for(int i=0; i<war.soldiers.size(); i++){
			Soldier s= war.soldiers.get(i);
			if(up!=s.up){
				if(host.razdalja(x,y,s.x,s.y)<3*width){
					direction=getDirection(x,y,s.x,s.y);
				}
				if(host.razdalja(x,y,s.x,s.y)<0.75*width){
					dead=true;
					s.dead=true;
					host.makeChargeRed((int)x,(int)y);
				}
			}
		}
		if(host.razdalja(x,y,host.width/2,host.height/2)>host.height*1.5) dead=true;
	}
	
	protected void draw(Canvas c, Paint p){
		RectF r= new RectF(x,y,x+width,y+width);
		p.setColor(Color.WHITE);
		p.setStyle(Style.STROKE);
		c.drawRect(r,p);
		if(up)
			c.drawOval(r, p);
		else{
			c.drawLine(x,y,x+width,y+width,p);
			c.drawLine(x+width,y,x,y+width,p);
		}
	}
}

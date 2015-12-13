package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Firefly extends Instance{
	
	boolean pacman;
	Maze master;
	
	Firefly(Darkness host, int radij){
		super(0,0,radij,host);
		generateRandom();
		pacman=false;
		enemy=false;
		speed=8*host.ratio;
		direction=getDirection(x,y,host.width/2,host.height/2);
		direction+=(int)(Math.random()*80)-40;
	}
	
	protected void step(){
		if(master==null){
			rotate(0.5f,14);
			x+= dirX;
			y+= dirY;
			host.instances.add(new TailF(x,y,radij,host));
			Protagonist p = host.hero;
			if(host.razdalja(x,y,p.x,p.y)<p.radij/2+radij/2 && !pacman){
				dead=true;
				p.charges+=3;
				host.maker.ffCount++;
				host.maker.ffDone=false;
				host.texter.addNow(host.maker.ffCount+"", 55);
			}
			if(pacman)
				bounceWall();
			else
				deathWall();
		}
		else{
			speed=6;
			int delay=90;
			int razdalja = host.razdalja(x,y,master.prizeX,master.prizeY);
			if(razdalja>master.size*0.32) delay=85;
			if(razdalja<master.size*0.28) delay=95;
			direction=getDirection(x,y,master.prizeX,master.prizeY);
			direction=norm(direction+delay);
			x+=Math.cos(Math.toRadians(direction))*speed;
			y+=Math.sin(Math.toRadians(direction))*speed;
			host.instances.add(new TailF(x,y,radij,host));
			Protagonist p = host.hero;
			if(host.razdalja(x,y,p.x,p.y)<p.radij/2+radij/2){
				master.won=true;
				dead=true;
				p.charges+=4;
			}
		}
	} 
	
	int norm(int a){
		if(a<0)a+=360;;
		if(a>360)a-=360;
		return a;
	}
	
	protected void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		pp.setColor(Color.argb(180, 255,255,0));
		pp.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, pp);
	}
}

class TailF extends Instance{

	int original;
	int originalAlpha;
	int alpha;
	
	TailF(float x, float y, int radij, Darkness host) {
		super(x, y, radij, host);
		enemy=false;
		original=radij;
		left = 30;
		alpha=200;
		originalAlpha=alpha;
	}
	
	void step(){
		left--;
		if(left==0) dead=true;
		radij=(int)((original/30.0)*left);
		alpha=(int)((originalAlpha/60.0)*left);
		//host.texter.addNow("radij: "+radij, 20);
	}
	
	protected void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		pp.setColor(Color.argb(alpha, 255,255,0));
		pp.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, pp);
	}

}

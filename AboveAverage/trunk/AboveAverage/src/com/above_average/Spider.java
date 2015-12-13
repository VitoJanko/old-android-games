package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Spider extends Instance{
	
	boolean close;
	int[] state;
	int[] predznak;
	int alpha;
	boolean small;
	
	Spider(int radij, Darkness host, boolean small){
		super(0,0,radij,host);
		generateRandom();
		this.small=small;
		speed = 6*host.ratio;
		state = new int[4];
		state[0]=(int)(Math.random()*50);
		state[1]=(int)(Math.random()*50);
		state[2]=(int)(Math.random()*50);
		state[3]=(int)(Math.random()*50);
		close=false;
		direction= (int)(Math.random()*360);
		if(small) speed= 4*host.ratio;
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
		for(int i=0; i<4; i++){
			state[i]+=8;
			if(state[i]>50)state[i]=0;
		}
		if(close && !small && !host.won){
			follow(host.hero.x,host.hero.y);
		}
		if(small){
			rotate(0.4f,10);
			x+= dirX;
			y+= dirY;
			bounceWall();
		}
		if(razdaljaAlpha>p.radij*3) alpha=0;
		else alpha= 255 * (p.radij*3-razdaljaAlpha)/(p.radij*3);
		if(hit){
			dead=true;
		}
		//deathWall();
	}
	
	int norm(int a){
		if(a<0)a+=360;;
		if(a>360)a-=360;
		return a;
	}
	
	void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		pp.setColor(Color.argb(alpha,255,255,255));
		pp.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, pp);
		if(close || small){
			int dir = getDirection(x,y,host.hero.x,host.hero.y);
			if(small) dir=direction;
			int[] start = new int[4];
			start[0]= norm(dir+60);
			start[1]= norm(dir+150);
			start[2]= norm(dir-60);
			start[3]= norm(dir-150);
			drawArm(dir,start[0],1,0,c,pp);
			drawArm(dir,start[1],1,1,c,pp);
			drawArm(dir,start[2],-1,2,c,pp);
			drawArm(dir,start[3],-1,3,c,pp);
		}
	}
	
	void drawArm(int generalDir, int startDir,int bodySide,int index,Canvas c, Paint p){
		double startX=x+Math.cos(Math.toRadians(startDir))*radij/2;
		double startY=y+Math.sin(Math.toRadians(startDir))*radij/2;
		int odklon = norm(generalDir+bodySide*(state[index]+60));
		double endX=startX+Math.cos(Math.toRadians(odklon))*radij*0.75;
		double endY=startY+Math.sin(Math.toRadians(odklon))*radij*0.75;
		c.drawLine((float)startX,(float)startY,(float)endX,(float)endY,p);
		int odklonN = norm(odklon+bodySide*120);
		double endNX=endX+Math.cos(Math.toRadians(odklonN))*radij/2;
		double endNY=endY+Math.sin(Math.toRadians(odklonN))*radij/2;
		c.drawLine((float)endX,(float)endY,(float)endNX,(float)endNY,p);
	}
	
}

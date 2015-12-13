package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Bat extends Instance{
	
	boolean close;
	int[] state;
	int predznak;
	int alpha;
	boolean small;
	boolean raging;
	int red;
	double colR1x,colR1y,colR2x,colR2y,colR3x,colR3y,colL1x,colL1y,colL2x,colL2y,colL3x,colL3y;
	
	Bat(int radij, Darkness host, boolean small){
		super(0,0,radij,host);
		generateRandom();
		this.small=small;
		speed = 6*host.ratio;
		state = new int[2];
		state[0]=(int)(Math.random()*30);
		state[1]=state[0];
		close=false;
		speed= 4.5f*host.ratio;
		predznak=1; if(Math.random()<0.5) predznak=-1;
		raging=false;
		red=255;
		left=-1;
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
		for(int i=0; i<2; i++){
			state[i]+=2;
			if(state[i]>30)state[i]=0;
		}
		if(razdaljaAlpha>p.radij*3) {
			speed=10*host.ratio;
			alpha=0;
		}
		else {
			alpha= 255 * (p.radij*3-razdaljaAlpha)/(p.radij*3);
			speed= 4.5f*host.ratio;
		}
		if(!raging){
			direction=getDirection(x,y,host.hero.x,host.hero.y);
			direction=norm(direction+predznak*65);
		}
		else{
			rotate(0.4f,14);
		}
		if(left>0){
			direction=getDirection(host.hero.x,host.hero.y,x,y);
			deathWall();
		}
		x+=Math.cos(Math.toRadians(direction))*speed;
		y+=Math.sin(Math.toRadians(direction))*speed;
		if(raging && Math.random()<0.03) raging=false;
		if(!raging && Math.random()<0.0085) raging=true;
		if(host.won){
			raging=true;
			deathWall();
		}
		if(hit) {
			red=150;
			left=20;
			hit=false;
		}
		left--;
		if(left==0) dead=true;
	}
	
	boolean altCollision(int xHero, int yHero, int radij){
		boolean collide = false;
		if (localCollision(xHero,yHero,radij,(float)colR1x,(float)colR1y,(float)colR2x,(float)colR2y)) collide = true;
		if (localCollision(xHero,yHero,radij,(float)colR2x,(float)colR2y,(float)colR3x,(float)colR3y)) collide = true;
		if (localCollision(xHero,yHero,radij,(float)colL1x,(float)colL1y,(float)colL2x,(float)colL2y)) collide = true;
		if (localCollision(xHero,yHero,radij,(float)colL2x,(float)colL2y,(float)colL3x,(float)colL3y)) collide = true;
		return collide;
	}
	
	int norm(int a){
		if(a<0)a+=360;;
		if(a>360)a-=360;
		return a;
	}
	
	void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		pp.setColor(Color.argb(alpha,255,red,red));
		pp.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, pp);
		//int dir = getDirection(x,y,host.hero.x,host.hero.y);
		int dir=direction;
		int[] start = new int[4];
		start[0]= norm(dir+45);
		start[1]= norm(dir+90);
		start[2]= norm(dir-45);
		start[3]= norm(dir-90);
		drawWing(dir,start[0],start[1],0,1,c,pp);
		drawWing(dir,start[2],start[3],1,-1,c,pp);
	}	
	
	void drawWing(int generalDir, int startDir, int endDir, int index ,int bodySide, Canvas c, Paint p){
		double startX=x+Math.cos(Math.toRadians(startDir))*radij/2;
		double startY=y+Math.sin(Math.toRadians(startDir))*radij/2;
		int odklon = norm(generalDir+bodySide*(50+state[index]));
		double end1X=startX+Math.cos(Math.toRadians(odklon))*radij*0.75;
		double end1Y=startY+Math.sin(Math.toRadians(odklon))*radij*0.75;
		c.drawLine((float)startX,(float)startY,(float)end1X,(float)end1Y,p);
		odklon = norm(odklon+bodySide*90);
		double end2X=end1X+Math.cos(Math.toRadians(odklon))*radij;
		double end2Y=end1Y+Math.sin(Math.toRadians(odklon))*radij;
		c.drawLine((float)end1X,(float)end1Y,(float)end2X,(float)end2Y,p);
		odklon = norm(odklon+bodySide*135);
		double end3X=end2X+Math.cos(Math.toRadians(odklon))*radij/2;
		double end3Y=end2Y+Math.sin(Math.toRadians(odklon))*radij/2;
		c.drawLine((float)end2X,(float)end2Y,(float)end3X,(float)end3Y,p);
		odklon = norm(odklon-bodySide*125);
		double end4X=end3X+Math.cos(Math.toRadians(odklon))*radij/2;
		double end4Y=end3Y+Math.sin(Math.toRadians(odklon))*radij/2;
		c.drawLine((float)end3X,(float)end3Y,(float)end4X,(float)end4Y,p);
		odklon = norm(odklon+bodySide*115);
		double end5X=end4X+Math.cos(Math.toRadians(odklon))*radij/2;
		double end5Y=end4Y+Math.sin(Math.toRadians(odklon))*radij/2;
		c.drawLine((float)end4X,(float)end4Y,(float)end5X,(float)end5Y,p);
		odklon = norm(odklon-bodySide*95);
		double end6X=end5X+Math.cos(Math.toRadians(odklon))*radij/2;
		double end6Y=end5Y+Math.sin(Math.toRadians(odklon))*radij/2;
		c.drawLine((float)end5X,(float)end5Y,(float)end6X,(float)end6Y,p);
		double endX=x+Math.cos(Math.toRadians(endDir))*radij/2;
		double endY=y+Math.sin(Math.toRadians(endDir))*radij/2;
		c.drawLine((float)end6X,(float)end6Y,(float)endX,(float)endY,p);
		if(bodySide==1){
			colR1x=end1X;
			colR1y=end1Y;
			colR2x=end2X;
			colR2y=end2Y;
			colR3x=end6X;
			colR3y=end6Y;
		}
		if(bodySide==-1){
			colL1x=end1X;
			colL1y=end1Y;
			colL2x=end2X;
			colL2y=end2Y;
			colL3x=end6X;
			colL3y=end6Y;
		}
	}
	
}

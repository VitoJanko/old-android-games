package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Prison extends Instance{

	static int ringNumber;
	static int prisonNumber;
	
	int alpha;
	int startSize;
	int counter;
	int rings;
	
	Prison(int radij, int rings, Darkness host) {
		super(0,0, radij, host);
		this.rings=rings;
		prisonNumber++;
		ringNumber++;
		alpha = 0;
		speed=2*host.ratio;
		counter=0;
		onlyAlt=true;
		direction=(int)(Math.random()*360);
		enemy = false;
		startSize=radij;
		x=host.hero.x;
		y=host.hero.y;
	}

	boolean altCollision(int xHero, int yHero, int rHero){
		if(host.razdalja(x,y,xHero,yHero)<rHero/2+(radij+(rings-1)*5)/2)
			if(host.razdalja(x,y,xHero,yHero)>radij/2-rHero/2)
				return true;
		return false;
	}
	
	protected void step(){
		alpha +=5;
		if(alpha>255){
			enemy=true;
			alpha=255;
		}
		counter++;
		if(counter==2){
			radij--;
			counter=0;
		}
		if(alpha==255){
			rotate(0.2f,10);
			bounce();
			x+= dirX;
			y+= dirY;
		}
		else{
			x=host.hero.x;
			y=host.hero.y;
		}
		if(radij<startSize/2){
			if(!dead){
				dead=true;
				prisonNumber--;
			}
		}
	} 
	
	protected void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		pp.setColor(Color.argb(alpha,255,255,255));
		pp.setStyle(Paint.Style.STROKE);
		int tRadij=radij;
		for(int i=0; i<rings; i++){
			RectF r = new RectF(x-tRadij/2, y-tRadij/2, x+tRadij/2, y+tRadij/2);
			c.drawOval(r, pp);
			tRadij+=10;
		}
	}
}

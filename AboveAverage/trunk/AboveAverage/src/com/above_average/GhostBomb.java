package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class GhostBomb extends Instance{
	
	boolean close;
	int alpha;
	boolean bullet;
	
	GhostBomb(int radij, boolean bullet, Darkness host){
		super(0,0,radij,host);
		generateRandom();
		close=false;
		this.bullet=bullet;
		speed=7*host.ratio;
		direction=(int)(Math.random()*360);
	}

	void step(){
		Protagonist p = host.hero;
		int razdalja=host.razdalja(x,y,p.x,p.y);
		if(razdalja<p.radij*1.25+radij/2) close=true;
		else close=false;
		int razdaljaAlpha=razdalja;
		for(int i=0; i<host.maker.light.size(); i++){
			Firefly f = host.maker.light.get(i);
			int toF = host.razdalja(x, y, f.x, f.y);
			razdaljaAlpha=Math.min(razdaljaAlpha, toF);
			if(f.pacman && toF<radij/2+f.radij/2) dead=true;
		}
		if(hit){
			dead=true;
			close=true;
		}
		if(!bullet && close && !host.won){
			for(int i=0; i<7; i++){
				GhostBomb gb = new GhostBomb(radij/2,true,host);
				host.instances.add(gb);
				gb.x=x;
				gb.y=y;
			}
			dead=true;
		}
		if(bullet){
			x+=Math.cos(Math.toRadians(direction))*speed;
			y+=Math.sin(Math.toRadians(direction))*speed;
		}
		
		if(razdaljaAlpha>p.radij*3) alpha=0;
		else alpha= 255 * (p.radij*3-razdaljaAlpha)/(p.radij*3);
		
		deathWall();	}
	
	void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		pp.setColor(Color.argb(alpha,255,255,255));
		pp.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, pp);
	}
	
}

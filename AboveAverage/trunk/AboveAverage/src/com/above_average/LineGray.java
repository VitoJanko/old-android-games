package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LineGray extends Instance{
	int xStart;
	int yStart;
	int dolzina;
	int alpha;
	
	LineGray(int xStart, int yStart, int dolzina, Darkness host){
		super(xStart+dolzina/2,yStart+dolzina/2,0,host);
		this.xStart=xStart;
		this.yStart=yStart;
		this.dolzina=dolzina;
		alpha=0;
		enemy=false;
	}
	
	void draw(Canvas c, Paint p){
		p.setColor(Color.argb(alpha,50,100,150));
		c.drawLine(xStart,yStart,xStart+dolzina,yStart,p);
		c.drawLine(xStart,yStart,xStart,yStart+dolzina,p);
	}
	
	void step(){
		Protagonist p = host.hero;
		int razdaljaAlpha=host.razdalja(x,y,p.x,p.y);
		for(int i=0; i<host.maker.light.size(); i++){
			Firefly f = host.maker.light.get(i);
			razdaljaAlpha=Math.min(razdaljaAlpha, host.razdalja(x, y, f.x, f.y));
		}
		if(razdaljaAlpha>p.radij*3) 
			alpha=0;
		else 
			alpha= 255 * (p.radij*3-razdaljaAlpha)/(p.radij*3);
		alpha=Math.max(60, alpha);
		alpha=alpha/2;
	}
}

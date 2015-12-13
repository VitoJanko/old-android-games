package com.example.huntedseas;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.FloatMath;

public class Squid extends Instance{
	
	float speed;
	float direction;
	
	Squid(int x, int y, Sea s, int depth, float speed, float direction){
		super(x,y,s,s.loader.food, depth);
		this.speed=speed;
		this.direction=direction;
	}
	
	protected void step(){
		x+=FloatMath.cos(direction)*speed;
		y+=FloatMath.sin(direction)*speed;
		if (y<0 || x<0 || x>host.realWidth || y>host.realHeight){
			dead=true;
		}
		if (collide(getBox(),host.hero.getBox())){
			host.hero.food+=1;
			dead=true;
		}
	}
	
	
	
	protected void draw(Canvas c, Paint p){
		c.drawBitmap(picture, x-w/2+host.premikX, y-h/2+host.premikY, p);
	}

}

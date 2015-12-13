package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Glow extends Instance{
	int red;
	int green;
	int blue;
	int alpha;
	
	Glow(int x, int y, int radij, int color){
		super(x, y, radij, null);
		this.red=Color.red(color);
		this.blue=Color.blue(color);
		this.green=Color.green(color);
		alpha = 255;
		enemy=false;
		persistant=true;
	}
	
	void step(){
		alpha-=5;
		radij+=2;
		if(alpha<0) {
			alpha=0;
			dead = true;
		}
	}
	
	protected void draw(Canvas c, Paint p){
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		Paint pp = new Paint();
		pp.setColor(Color.argb(alpha,red,green,blue));;
		pp.setStyle(Paint.Style.STROKE);
		c.drawOval(r, pp);
	}
}

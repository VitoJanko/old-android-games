package com.example.huntedseas;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Bubble extends Instance{
	
	float move;
	
	Bubble(int x, int y, Sea s, int depth, float move, int index){
		super(x,y,s,s.loader.bubble1, depth);
		if(index==1) picture= host.loader.bubble1;
		if(index==2) picture= host.loader.bubble2;
		if(index==3) picture= host.loader.bubble3;
		this.move=move;
	}
	
	protected void step(){
		y+=move;
		if (y<0){
			dead=true;
		}
	}
	
	protected void draw(Canvas c, Paint p){
		c.drawBitmap(picture, x-w/2+host.premikX, y-h/2+host.premikY, p);
	}

}

package com.example.huntedseas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Background extends Instance{
	
	Background(int x, int y, Sea s, Bitmap b, int depth){
		super(x,y,s,b, depth);
	}
	
	protected void step(){

	}
	
	protected void draw(Canvas c, Paint p){
		c.drawBitmap(picture, x+host.premikX, y+host.premikY, p);
	}

}

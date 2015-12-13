package com.example.huntedseas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

public class SuperFish extends Instance{
	
	float move;
	boolean dying;
	
	SuperFish(int x, int y, Sea s, Bitmap b, int depth, float move){
		super(x,y,s,b, depth);
		this.move=move;
		dying=false;
	}
	
	protected void step(){
		
		if (!dying) {
			x+=move;
			float dist = distance(x,y,host.hero.x,host.hero.y);
			if (dist<250 && host.hero.y>y)
				y+=4;
			if (dist<250 && host.hero.y<y)
				y-=4;
		}
		else 
			y+=5;
		if (x < -100 ||x >host.realWidth+100){
			dead=true;
		}
		
		
		if (collide(getBox(),host.hero.getBox()) && !dying){
			demage(8,0.3f,4);
			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);
			picture = Bitmap.createBitmap(picture, 0, 0, w, h, matrix, false);
			dying=true;

		}
		if (y>host.realHeight)
			dead=true;
	}
	

	
	protected void draw(Canvas c, Paint p){
		ColorMatrix colorMatrix = new ColorMatrix();
        //float[] fields = {1.5f,0,0,0,0,  0,1.5f,0,0,0,   0,0,0.5f,0,0,  0,0,0,1,0};
        //colorMatrix.set(fields);
        colorMatrix.setSaturation(0.2f); 
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);                          
        c.drawBitmap(picture, x-w/2+host.premikX, y-h/2+host.premikY, paint);
	}

}

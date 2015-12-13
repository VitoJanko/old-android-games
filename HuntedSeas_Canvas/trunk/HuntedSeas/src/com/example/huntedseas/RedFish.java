package com.example.huntedseas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class RedFish extends Instance{
	
	float move;
	float naturalHeight;
	float minHeight;
	boolean threatned;
	
	RedFish(int x, int y, Sea s, Bitmap b, int depth, float move){
		super(x,y,s,b, depth);
		this.move=move;
		threatned=false;
		naturalHeight=y;
		minHeight=host.realHeight-80;
	}
	
	protected void step(){
		
		if (!threatned) {
			x+=move;
			if(y>naturalHeight)
				y-=8;
		}
		else{
			x+=move*1.5;
			if(y<minHeight)
				y+=8;
		}
		float dist = distance(x,y,host.hero.x,host.hero.y);
		if (dist<210 && host.hero.y<y+100)
			threatned=true;
		if(dist>400)
			threatned=false;
		
		if (x < -100 ||x >host.realWidth+100){
			dead=true;
		}
		if (collide(getBox(),host.hero.getBox())){
			host.hero.food+=1;
			dead=true;
		}

	}
	

	
	protected void draw(Canvas c, Paint p){
		ColorMatrix colorMatrix = new ColorMatrix();
        float[] fields = {2,0,0,0,0,  0,0.8f,0,0,0,   0,0,0.8f,0,0,  0,0,0,1,0};
        colorMatrix.set(fields);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);                          
        c.drawBitmap(picture, x-w/2+host.premikX, y-h/2+host.premikY, paint);
	}

}

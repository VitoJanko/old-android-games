package com.example.huntedseas;

import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.FloatMath;

public class SinusFish extends Instance{
	
	float progression;
	float ySpeed;
	float xSpeed;
	int frameLength;
	int frame;
	boolean flipped;
	
	SinusFish(int x, int y, Sea s, int depth, float xSpeed, float delay, boolean flipped){
		super(x,y,s,s.loader.fishSmall[0], depth);
		this.xSpeed=xSpeed;
		ySpeed=3;
		progression=delay;
		frameLength=0;
		frame=0;
		this.flipped=flipped;
	}
	
	protected void step(){
		
		frameLength+=1;
		if(frameLength==5){
			frameLength=0;
			frame+=1;
			if(frame==4)
				frame=0;
			picture = host.loader.fishSmall[frame];
			if(flipped)
				picture = host.loader.fishSmallF[frame];
		}
		
		x+=xSpeed;
		y+=ySpeed*FloatMath.sin(progression);
		progression+=(2*Math.PI)/100;
				
		
		if (x < -160 ||x >host.realWidth+100){
			dead=true;
		}
		if (collide(getBox(),host.hero.getBox())){
			demage(6,0.2f,1);
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

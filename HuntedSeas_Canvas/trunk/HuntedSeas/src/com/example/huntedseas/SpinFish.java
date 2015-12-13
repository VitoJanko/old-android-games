package com.example.huntedseas;

import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.FloatMath;

public class SpinFish extends Instance{
	
	float speed;
	float direction;
	float dirSpeed;
	int frameLength;
	int frame;
	
	SpinFish(int x, int y, Sea s, int depth, float speed, float direction, float dirSpeed){
		super(x,y,s,s.loader.fishSmall[0], depth);
		this.direction=direction;
		this.dirSpeed=dirSpeed;
		this.speed=speed;
		frameLength=0;
		frame=0;
	}
	
	protected void step(){
		
		frameLength+=1;
		if(frameLength==5){
			frameLength=0;
			frame+=1;
			if(frame==4)
				frame=0;
			picture = host.loader.fishSmall[frame];
		}
		
		direction-=dirSpeed;
		if (direction<0) direction = 2*(float)Math.PI-direction;
		
		x+=speed*FloatMath.cos(direction);
		y+=speed*FloatMath.sin(direction);
				
		
		if (x < -160 ||x >host.realWidth+100){
			dead=true;
		}
		
		if (y < -160 ||y >host.realHeight+100){
			dead=true;
		}
		
		if (collide(getBox(),host.hero.getBox())){
			demage(6,0.2f,1);
			dead=true;
		}

	}
	

	
	protected void draw(Canvas c, Paint p){
		
//		float d =  (float)(direction);
//		if(direction>Math.PI){
//			d = 2*(float)Math.PI-direction;
//		}
//		d /= (float)Math.PI*2;
//		d+=0.25;
//		ColorMatrix colorMatrix = new ColorMatrix();
//        float[] fields = {d,d,d,0,0,  d,d,d,0,0,   d,d,d,0,0,  0,0,0,1,0};
		ColorMatrix colorMatrix = new ColorMatrix();
        float[] fields = {2,0,0,0,0,  0,0.8f,0,0,0,   0,0,0.8f,0,0,  0,0,0,1,0};
        colorMatrix.set(fields);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);
        
        Matrix rotate = new Matrix();
		rotate.setRotate((float)((direction+Math.PI)*360/(2*Math.PI)),w/2,h/2);
		rotate.postTranslate(x-w/2+host.premikX,y-h/2+host.premikY);
		c.drawBitmap(picture, rotate, paint);
        
	}

}

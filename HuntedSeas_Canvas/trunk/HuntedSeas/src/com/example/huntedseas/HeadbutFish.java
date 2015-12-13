package com.example.huntedseas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.FloatMath;

public class HeadbutFish extends Instance{
	
	Bitmap[] animation;
	int frameLength;
	int frame;
	boolean flipped;
	float direction;
	float speed;
	boolean frenzy;
	float speedFrenzy;
	
	
	HeadbutFish(int x, int y, Sea s, int depth, boolean flipped){
		super(x,y,s,s.loader.fishSmall[0], depth);
		direction=(float)Math.PI/2;
		this.flipped=flipped;
		if (flipped) direction=0;
		frameLength=0;
		frame=0;
		speed=host.calibrate(4);
		speedFrenzy=host.calibrate(7);
		frenzy=false;
	}
	
	protected void step(){
		frameLength+=1;
		if(frameLength==5){
			frameLength=0;
			frame+=1;
			if(frame==4)
				frame=0;
			picture = host.loader.fishSmall[frame];
			if (flipped)
				picture = host.loader.fishSmallF[frame];
		}
		
		x+=FloatMath.cos(direction)*speed;
		y+=FloatMath.sin(direction)*speed;
		
		if(!frenzy && distance(x,y,host.hero.x,host.hero.y)<300){
			frenzy=true;
			direction = (float)(Math.atan2((host.hero.x - x),(host.hero.y - y))-Math.PI/2);
			speed=speedFrenzy;
		}
		
		
		if (x < -100 ||x >host.realWidth+150){
			dead=true;
		}
		if (y>host.realHeight || y<0)
			dead=true;
	}
	

	
	protected void draw(Canvas c, Paint p){
		Matrix rotate = new Matrix();
		float angle = (float) Math.toDegrees(direction);
		rotate.setRotate(-angle,w/2,h/2);
		rotate.postTranslate(x-w/2+host.premikX,y-h/2+host.premikY);
		
		ColorMatrix colorMatrix = new ColorMatrix();
        float[] fields = {2,0,0,0,0,  0,0.8f,0,0,0,   0,0,0.8f,0,0,  0,0,0,1,0};
        colorMatrix.set(fields);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);                          
		
		c.drawBitmap(picture, rotate, paint);
		//ColorMatrix colorMatrix = new ColorMatrix();
        //colorMatrix.setSaturation(0f); 
        //ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        
        //Paint paint = new Paint();
        //paint.setColorFilter(colorFilter);  
		//paint.setAlpha(160);                             //you can set your transparent value here    
		//c.drawBitmap(shadow, x-w/2+host.premikX, host.realHeight-85+host.premikY, paint);
	}

}

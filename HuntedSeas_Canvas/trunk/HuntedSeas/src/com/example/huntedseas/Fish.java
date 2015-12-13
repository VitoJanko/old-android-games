package com.example.huntedseas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Fish extends Instance{
	
	float move;
	boolean dying;
	Bitmap[] animation;
	int frameLength;
	int frame;
	boolean fliped;
	
	
	Fish(int x, int y, Sea s, int depth, float move, boolean fliped){
		super(x,y,s,s.loader.fish[0], depth);
		this.move=move;
		dying=false;
		//this. animation = animation;
		frameLength=0;
		frame=0;
		this.fliped=fliped;
	}
	
	protected void step(){
		if(!dying){
			frameLength+=1;
			if(frameLength==5){
				frameLength=0;
				frame+=1;
				if(frame==4)
					frame=0;
				picture = host.loader.fish[frame];
				if (fliped)
					picture = host.loader.fishF[frame];
			}
		}
		
		if (!dying) x+=move;
		else y+=5;
		if (x < -100 ||x >host.realWidth+150){
			dead=true;
		}
		if (collide(getBox(),host.hero.getBox()) && !dying){
			demage(8,0.3f,2);
			if(host.hero.food>=4){
				Matrix matrix = new Matrix();
				matrix.preScale(1, -1);
				picture = Bitmap.createBitmap(picture, 0, 0, w, h, matrix, false);
				dying=true;
			}
		}
		if (y>host.realHeight)
			dead=true;
	}
	

	
	protected void draw(Canvas c, Paint p){
		c.drawBitmap(picture, x-w/2+host.premikX, y-h/2+host.premikY, p);
		
		//ColorMatrix colorMatrix = new ColorMatrix();
        //colorMatrix.setSaturation(0f); 
        //ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        
        //Paint paint = new Paint();
        //paint.setColorFilter(colorFilter);  
		//paint.setAlpha(160);                             //you can set your transparent value here    
		//c.drawBitmap(shadow, x-w/2+host.premikX, host.realHeight-85+host.premikY, paint);
	}

}

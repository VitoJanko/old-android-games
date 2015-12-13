package com.example.huntedseas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.FloatMath;

public class Blowfish extends Instance{
	
	float direction;
	float speed;
	Bitmap[] animation;
	int frameLength;
	int frame;
	
	
	Blowfish(int x, int y, Sea s, Bitmap b, int depth, float move, Bitmap[] animation){
		super(x,y,s,b, depth);
		speed=move;
		direction = (float)(Math.random()*Math.PI*2);
		this.animation = animation;
		frameLength=0;
		frame=0;
	}
	
	protected void step(){
		x+=FloatMath.cos(direction)*speed;
		y+=FloatMath.sin(direction)*speed;
		direction += Math.PI/100;
		if(distance(x,y,host.hero.x,host.hero.y)<200 && frameLength==0){
			picture=animation[1];
			h=picture.getHeight();
			frameLength+=1;
		}
		if(distance(x,y,host.hero.x,host.hero.y)<200 && frameLength!=0){	
			frameLength+=1;
			if(frameLength==10){
				picture=animation[2];
				h=picture.getHeight();
			}
			
		}
		if(distance(x,y,host.hero.x,host.hero.y)>200 ){
			frameLength=0;
			picture=animation[0];
			h=picture.getHeight();
		}
		

		if (collide(getBox(),host.hero.getBox())){
			demage(8,0.3f,2);
		}

	}
	

	
	protected void draw(Canvas c, Paint p){
		c.drawBitmap(picture, x-w/2+host.premikX, y-h/2+host.premikY, p);
		
		ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f); 
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);  
		paint.setAlpha(160);                             //you can set your transparent value here    
		//c.drawBitmap(shadow, x-w/2+host.premikX, host.realHeight-85+host.premikY, paint);
	}

}

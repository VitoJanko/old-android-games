package com.example.huntedseas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.FloatMath;
import android.util.Log;

/**
 * TODO Put here a description of what this class does.
 *
 * @author Jani.
 *         Created 28. okt. 2012.
 */
public class BlowfishFollower extends Instance {
	
	float direction;
	float speed;
	Bitmap[] animation;
	int frameLength;
	int frame;
	
	
	BlowfishFollower(int x, int y, Sea s, Bitmap b, int depth, float move, Bitmap[] animation) {		
		super(x,y,s,b, depth);
		
		speed=move;
		direction = (float)(Math.random()*Math.PI*2);
		this.animation = animation;
		frameLength=0;
		frame=0;
	}
	
	/*
	 *   (x,y)/|x,y|   * speed    kjer je x=  hero.x-riba.x   in y simetrièno
	 * 
	 */
	
	protected void step(){
		if(distance(x,y,host.hero.x,host.hero.y)>250 && frameLength==0){
			x+=FloatMath.cos(direction)*speed;
			y+=FloatMath.sin(direction)*speed;
			direction += Math.PI/100;
		}else{
			x += (host.hero.x-x) / Math.abs(host.hero.x-x) * speed;
			y += (host.hero.y-y) / Math.abs(host.hero.y-y) * speed;
		}
		if(distance(x,y,host.hero.x,host.hero.y)<130 && frameLength==0){
			picture=animation[1];
			h=picture.getHeight();
			frameLength+=1;
		}
		if(distance(x,y,host.hero.x,host.hero.y)<130 && frameLength!=0){	
			frameLength+=1;
			if(frameLength==10){
				picture=animation[2];
				h=picture.getHeight();
			}
			
		}
		if(distance(x,y,host.hero.x,host.hero.y)>100 ){
			frameLength=0;
			picture=animation[0];
			h=picture.getHeight();
		}
		

		if (collide(getBox(),host.hero.getBox())){
			demage(8,0.3f,2);
		}

	}
	
	protected void draw(Canvas c, Paint p){	
		Matrix rotate = new Matrix();
		float angle = (float) Math.toDegrees(Math.atan2((host.hero.x - x),(host.hero.y - y)))+90;
		//rotate.setRotate(((float) Math.toDegrees(angle)+90),w/2,h/2); lol keri efekt rata ce dam to
		rotate.setRotate(-angle,w/2,h/2);
		Matrix matrix = new Matrix();
		matrix.set(rotate);
		matrix.postTranslate(x-w/2+host.premikX,y-h/2+host.premikY);
		c.drawBitmap(picture, matrix, p);
		
		
		ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f); 
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);  
		paint.setAlpha(160);                             //you can set your transparent value here    
		//c.drawBitmap(shadow, x-w/2+host.premikX, host.realHeight-85+host.premikY, paint);
	}
}

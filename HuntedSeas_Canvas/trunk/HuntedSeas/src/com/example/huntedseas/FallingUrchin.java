package com.example.huntedseas;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class FallingUrchin extends Instance{
	float move;
	float moveDown;
	boolean atFloor;
	Matrix rotate;
	
	FallingUrchin(int x, int y, Sea s, int depth){
		super(x,y,s,s.loader.urchin, depth);
		move = host.calibrate(2);
		if(Math.random()<0.5)
			move=-move;
		moveDown = host.calibrate(4);
		rotate = new Matrix();
		rotate.setRotate(0,w/2,h/2);
		atFloor=false;
	}
	
	protected void step(){
		if(!atFloor){
			y+=moveDown;
			if(y>host.realHeight-host.wallDown)
				atFloor=true;
			if(Math.random()<0.15)
				host.addInstance(new Bubble((int)x,(int)y,host,4 ,host.calibrate(-1*(1+(int)(Math.random()*4))),3));
		}
		else{
			x+=move;
			double obseg = w*Math.PI;
			float ammount = (float)(move/obseg)*360;
			if (move>0)
				rotate.postRotate(ammount,w/2,h/2);
			else
				rotate.postRotate(ammount,w/2,h/2);
		}
		
		if (x < 50 ||x >host.realWidth+50){
			dead=true;
		}
		if (collide(getBox(),host.hero.getBox())){
			demage(14,0.5f,3);
		}
	}
	
	protected void draw(Canvas c, Paint p){
		Matrix matrix = new Matrix();
		if(atFloor){
			matrix.set(rotate);
		}
		matrix.postTranslate(x-w/2+host.premikX,y-h/2+host.premikY);
		c.drawBitmap(picture, matrix, p);
	}

}

package com.example.huntedseas;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Urchin extends Instance{
	float move;
	float maxMove;
	Matrix rotate;
	
	Urchin(int x, int y, Sea s, int depth){
		super(x,y,s,s.loader.urchin, depth);
		move = host.calibrate(2);
		this.maxMove=move;
		rotate = new Matrix();
		rotate.setRotate(0,w/2,h/2);
	}
	
	protected void step(){
		x+=move;
		double obseg = w*Math.PI;
		float ammount = (float)(move/obseg)*360;
		if (move>0)
			rotate.postRotate(ammount,w/2,h/2);
		else
			rotate.postRotate(ammount,w/2,h/2);
		//picture = Bitmap.createBitmap(picture, 0, 0, 2*w,2*h, matrix, true);
		if (Math.random()<host.calibrateProb(0.02))
			move=-move;
		if (x < 0 ||x >host.realWidth){
			move=-move;
		}
		if (collide(getBox(),host.hero.getBox())){
			demage(14,0.5f,5);
		}
	}
	
	protected void draw(Canvas c, Paint p){
		Matrix matrix = new Matrix();
		matrix.set(rotate);
		matrix.postTranslate(x-w/2+host.premikX,y-h/2+host.premikY);
		c.drawBitmap(picture, matrix, p);
		
		//int[] box = getBox();
		//p.setColor(Color.BLACK);
		//c.drawLine(box[0]+host.premikX, box[1]+host.premikY, box[0]+host.premikX, box[3]+host.premikY, p);
		//c.drawLine(box[2]+host.premikX, box[1]+host.premikY, box[2]+host.premikX, box[3]+host.premikY, p);
		//c.drawLine(box[0]+host.premikX, box[1]+host.premikY, box[2]+host.premikX, box[1]+host.premikY, p);
		//c.drawLine(box[0]+host.premikX, box[3]+host.premikY, box[2]+host.premikX, box[3]+host.premikY, p);
	}

}

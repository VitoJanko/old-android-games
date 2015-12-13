package com.example.huntedseas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.FloatMath;


public class Instance {

	Bitmap picture;
	Bitmap shadow;
	float x;
	float y;
	Sea host;
	int w;
	int h;
	Boolean dead;
	int depth;
	
	Instance(float x, float y, Sea host, Bitmap b, int depth){
		this.x = x;
		this.y = y;
		this.host=host;
		dead=false;
		picture=b;
		h = picture.getHeight();
		w = picture.getWidth();
		this.depth=depth;
		
		//Matrix matrix = new Matrix();
		//matrix.preScale(1, -1);
		//shadow = Bitmap.createBitmap(picture, 0, h/2, w, h/2, matrix, false);
	}
	
	protected boolean collide(int[] b1, int[] b2){
		boolean vertical = false;
		boolean horizontal = false;
		if (b2[0]>=b1[0] && b2[0]<=b1[2])
			horizontal = true;
		if (b1[0]>=b2[0] && b1[0]<=b2[2])
			horizontal = true;
		if (b2[1]>=b1[1] && b2[1]<=b1[3])
			vertical = true;
		if (b1[1]>=b2[1] && b1[1]<=b2[3])
			vertical = true;
		return (horizontal && vertical);
	}
	
	protected void demage(float recoil, float diminish, int lose){
		host.hero.dirX=host.hero.x-x;
		host.hero.dirY=host.hero.y-y;
		host.hero.recoil=host.calibrate(recoil);
		host.hero.diminish=host.calibrate(diminish);
		host.hero.food-=lose;
		if(host.hero.food<0) host.hero.food=0;
	}
	
	protected int[] getBox(){
		int[] bounds = new int[4];
		bounds[0]=(int)(x-(w/2)*0.85);
		bounds[1]=(int)(y-(h/2)*0.85);
		bounds[2]=(int)(x+(w/2)*0.85);
		bounds[3]=(int)(y+(h/2)*0.85);
		return bounds;
	}
	
	protected int[] getBox(float xN, float yN){
		int[] bounds = new int[4];
		bounds[0]=(int)(x-(w/2)*0.7+xN);
		bounds[1]=(int)(y-(h/2)*0.7+yN);
		bounds[2]=(int)(x+(w/2)*0.7+xN);
		bounds[3]=(int)(y+(h/2)*0.7+yN);
		return bounds;
	}
	
	protected Rect getRect(){
		int[] bounds = new int[4];
		bounds[0]=(int)(x-(w/2)*0.85);
		bounds[1]=(int)(y-(h/2)*0.85);
		bounds[2]=(int)(x+(w/2)*0.85);
		bounds[3]=(int)(y+(h/2)*0.85);
		return new Rect(bounds[0],bounds[1],bounds[2],bounds[3]);
	}
	
	protected float distance(float x1, float y1, float x2, float y2){
		return FloatMath.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}
	
	void step(){}
	void draw(Canvas c, Paint p){}
}
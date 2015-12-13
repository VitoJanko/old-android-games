package com.example.huntedseas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.FloatMath;



public class Protagonist extends Instance{
	
	float recoil;
	float diminish;
	float dirX;
	float dirY;
	int food;
	int stadij;
	int bigger;
	
	Protagonist(int x, int y, Sea s, Bitmap b, int depth){
		super(x,y,s,b, depth);
		recoil=0;
		food=0;
		stadij=0;
		bigger=0;
	}
	
	protected void step(){
		//input
		float planedX = host.calibrate(host.angleY*3);
		float planedY = host.calibrate((host.angleX-host.nagib)*3);
		//recoil
		if(recoil!=0){
			float size = FloatMath.sqrt(dirX*dirX+dirY*dirY);
			planedX=(float)(dirX/size)*recoil;
			planedY=(float)(dirY/size)*recoil;
			recoil-=diminish;
			if(recoil<0)recoil=0;
		}
		//collison
		
		for (int i=0; i<host.obstacles.size(); i++){
			
			Rect o = host.obstacles.get(i);
			//System.out.println(o.left+" " + o.right+" " + o.top +" "+ o.bottom);
			//boolean collision=o.intersect(getRect());
			int[] cord = {o.left,o.top,o.right,o.bottom};
			if (collide(getBox(planedX,planedY),cord)){
				System.out.println("Boom");
				int okX=0;
				int okY=0;
				while(true){
					boolean trialX=false;
					boolean trialY=false;
					if(planedX>=1){
						okX+=1;
						if (collide(getBox(okX,okY),cord))
							okX-=1;
						else{
							planedX-=1;
							trialX=true;
						}	
					}
					if(planedX<=-1){
						okX-=1;
						if (collide(getBox(okX,okY),cord))
							okX+=1;
						else{
							planedX+=1;
							trialX=true;
						}	
					}
					if(planedY>=1){
						okY+=1;
						if (collide(getBox(okX,okY),cord))
							okY-=1;
						else{
							planedY-=1;
							trialY=true;
						}	
					}
					if(planedY<=-1){
						okY-=1;
						if (collide(getBox(okX,okY),cord))
							okY+=1;
						else{
							planedY+=1;
							trialY=true;
						}	
					}
					if(!trialX && !trialY){
						planedX=okX;
						planedY=okY;
						break;
					}
				}
//				
//				if(planedX>=0 && x+w/2+planedX>o.left && x+w/2+planedX<o.right )
//					planedX=o.left-(x+w/2);
//				if(planedX<=0 && x-w/2+planedX>o.left && x-w/2+planedX<o.right )
//					planedX=(x-w/2)-o.right;
//				
//				if(planedY>=0 && y+h/2+planedY>o.top && y+h/2+planedY<o.bottom )
//					planedY=o.top-(y+h/2);
//				if(planedY<=0 && y-h/2+planedY>o.top && y-h/2+planedY<o.bottom )
//					planedY=(y-h/2)-o.bottom;
			}
		}
		
		//scrolling horizontal
		if(x+planedX>host.wallX && x+planedX<host.realWidth-host.wallX){
			x+=planedX;
			if(x+host.premikX<host.marginX)
				host.premikX+=host.marginX-(x+host.premikX);
			if(x+host.premikX>host.width-host.marginX)
				host.premikX-=x+host.premikX-(host.width-host.marginX);
			if(host.premikX>0) host.premikX=0;
			if(host.premikX<-(host.realWidth-host.width)) host.premikX=-(host.realWidth-host.width);
		}
		//scrolling vertical
		if(y+planedY>host.wallUp && y+planedY<host.realHeight-host.wallDown-host.hero.h/2){
			y+=planedY;
			if(y+host.premikY<host.marginY)
				host.premikY+=host.marginY-(y+host.premikY);
			if(y+host.premikY>host.height-host.marginY)
				host.premikY-=y+host.premikY-(host.height-host.marginY);
			if(host.premikY>0) host.premikY=0;
			if(host.premikY<-(host.realHeight-host.height)) host.premikY=-(host.realHeight-host.height);
		}
		//growth
		stadij = food/5;
		if(stadij>3) stadij=3;
		if(bigger<stadij*14)
			bigger+=1;
		if(bigger>stadij*14)
			bigger-=1;
	}
	
	protected void draw(Canvas c, Paint p){
		if(host.gameMode==host.EMPTY){
			p.setColor(Color.BLACK);
			c.drawText(host.premikX+"", 100, 100, p);
			c.drawText(host.premikY+"", 100, 200, p);
	
		}
		int prevWidth = picture.getWidth();
		int prevHeight = picture.getHeight();
		Bitmap pic =  Bitmap.createScaledBitmap(picture,(int)(prevWidth*(0.5+bigger/200.0)),
				(int)(prevHeight*(0.5+bigger/200.0)), true);
		w = pic.getWidth();
		h = pic.getHeight();
		
//		int[] box = getBox();
//		p.setColor(Color.BLACK);
//		c.drawLine(box[0]+host.premikX, box[1]+host.premikY, box[0]+host.premikX, box[3]+host.premikY, p);
//		c.drawLine(box[2]+host.premikX, box[1]+host.premikY, box[2]+host.premikX, box[3]+host.premikY, p);
//		c.drawLine(box[0]+host.premikX, box[1]+host.premikY, box[2]+host.premikX, box[1]+host.premikY, p);
//		c.drawLine(box[0]+host.premikX, box[3]+host.premikY, box[2]+host.premikX, box[3]+host.premikY, p);
		
		c.drawBitmap(pic, x-w/2+host.premikX, y-h/2+host.premikY, p);
	}
}

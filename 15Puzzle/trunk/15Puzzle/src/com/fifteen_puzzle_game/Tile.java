package com.fifteen_puzzle_game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Tile {
	
	int width;
	int strWidth;
	int strHeight;
	float x;
	float y;
	float xStrech;
	float yStrech;
	int x1;
	int y1;
	int i;
	int j;
	int number;
	int color;
	boolean empty;
	int gameNumber;
	Bitmap pictureB;
	
	Tile(int i, int j, int x1, int x2, int y1, int y2, int gameNumber, int sWidth, int sHeight){
		width = (y2-y1)/gameNumber;
		x = x1+i*width;
		y = y1+j*width;
		this.gameNumber = gameNumber;
		this.strWidth = (sWidth-2)/gameNumber;
		this.strHeight = (sHeight-2)/gameNumber;
		xStrech = i*strWidth+1;
		yStrech = j*strHeight+1;
		this.x1 = x1;
		this.y1 = y1;
		this.i=i;
		this.j=j;
		number = i + j*gameNumber +1;
		empty = false;
		if(number == gameNumber*gameNumber) empty = true;
		int r=0; int b =0; int g=0;
		while(r+g+b<60){
			r = (int)(Math.random()*80);
			g = (int)(Math.random()*80);
			b = (int)(Math.random()*80);
		}
		color = Color.argb(255,r,g,b);
	}
	
	Tile(int i, int j, int x1, int x2, int y1, int y2, int color, int number, int gameNumber, int sWidth, int sHeight){
		width = (y2-y1)/gameNumber;
		x = x1+i*width;
		y = y1+j*width;
		this.gameNumber = gameNumber;
		this.strWidth = (sWidth-2)/gameNumber;
		this.strHeight = (sHeight-2)/gameNumber;
		xStrech = i*strWidth+1;
		yStrech = j*strHeight+1;
		this.x1 = x1;
		this.y1 = y1;
		this.i=i;
		this.j=j;
		this.number = number;
		empty = false;
		if(number == gameNumber*gameNumber) empty = true;
		this.color = color;
	}
	
	void update(int sWidth, int sHeight){
		this.strWidth = (sWidth-2)/gameNumber;
		this.strHeight = (sHeight-2)/gameNumber;
		xStrech = i*strWidth+1;
		yStrech = j*strHeight+1;
	}
	
	protected void setPicture(Bitmap b){
		pictureB = b;
	}
	
	protected void goTo(int i, int j){
		this.i=i;
		this.j=j;
		x = x1+i*width;
		y = y1+j*width;
		xStrech = i*strWidth+1;
		yStrech = j*strHeight+1;
	}
	
	protected void draw(Canvas c, Paint p){
		if(!empty){
			if(pictureB == null){
				if(!TopSecret.strech){
					p.setStyle(Paint.Style.FILL);
					p.setColor(color);
					c.drawRect(x,y,x+width,y+width,p);
					
					p.setStyle(Paint.Style.STROKE);
					p.setColor(Color.GRAY);
					c.drawRect(x,y,x+width,y+width,p);
					
					p.setColor(Color.WHITE);
					int delay = 6;
					if(number>9) delay = 9;
					c.drawText(number+"",x+width/2-delay,y+width/2+4,p);
				}
				else{
					p.setStyle(Paint.Style.FILL);
					p.setColor(color);
					c.drawRect(xStrech,yStrech,xStrech+strWidth,yStrech+strHeight,p);
					
					p.setStyle(Paint.Style.STROKE);
					p.setColor(Color.GRAY);
					c.drawRect(xStrech,yStrech,xStrech+strWidth,yStrech+strHeight,p);
					
					p.setColor(Color.WHITE);
					int delay = 6;
					if(number>9) delay = 9;
					c.drawText(number+"",xStrech+strWidth/2-delay,yStrech+strHeight/2+4,p);
				}
			}
			else{
				if(!TopSecret.strech)
					c.drawBitmap(pictureB,x,y,p);
				else
					c.drawBitmap(pictureB,xStrech,yStrech,p);
			}
			if(TopSecret.tips){
				p.setColor(Color.RED);
				p.setTextSize(p.getTextSize()*2);
				if(!TopSecret.strech){
					p.setColor(Color.RED);
					int delay = 12;
					if(number>9) delay = 18;
					c.drawText(number+"",x+width/2-delay,y+width/2+4,p);
				}
				else{
					p.setColor(Color.RED);
					int delay = 12;
					if(number>9) delay = 18;
					c.drawText(number+"",xStrech+strWidth/2-delay,yStrech+strHeight/2+4,p);
				}
				p.setTextSize(p.getTextSize()/2);
			}
		}
	}
	
	protected void makePrice(Part[] price, int index){
		if(!TopSecret.strech){
			float xUnit = x;
			float yUnit = y;
			int radij = width/gameNumber;
			int priceColor = color;
			if(pictureB!=null){
				priceColor = pictureB.getPixel(1, 1);
			}
			for(int i = index; i<index+gameNumber*gameNumber; i++){
				price[i] = new Part((int)xUnit+radij/2,(int)yUnit+radij/2,radij,priceColor);
				xUnit+=radij;
				if(xUnit==x+gameNumber*radij){
					xUnit = x;
					yUnit += radij;
				}
			}
		}
		else{
			float xUnit = xStrech;
			float yUnit = yStrech;
			int radijX = strWidth/gameNumber;
			int radijY = strHeight/gameNumber;
			int priceColor = color;
			if(pictureB!=null){
				priceColor = pictureB.getPixel(1, 1);
			}
			for(int i = index; i<index+gameNumber*gameNumber; i++){
				price[i] = new Part((int)xUnit+radijX/2,(int)yUnit+radijY/2,radijX,radijY,priceColor);
				xUnit+=radijX;
				if(xUnit==xStrech+gameNumber*radijX){
					xUnit = xStrech;
					yUnit += radijY;
				}
			}
		}
	}
}

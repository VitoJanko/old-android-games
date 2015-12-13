package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LineGreen extends Instance{
	int xStart;
	int yStart;
	int dolzina;
	int green;
	int charge;
	boolean charging;
	boolean vertical;
	LineGreen next;
	
	LineGreen(int xStart, int yStart, int dolzina, boolean vertical){
		super(0,0,0,null);
		this.vertical=vertical;
		this.xStart=xStart;
		this.yStart=yStart;
		this.dolzina=dolzina;
		charging=false;
		green=40;
		enemy=false;
	}
	
	void draw(Canvas c, Paint p){
		int col = Math.min(200,charge/2);
		p.setColor(Color.rgb(0,green+col,0));
		if(vertical)
			c.drawLine(xStart,yStart,xStart,yStart+dolzina,p);
		else
			c.drawLine(xStart,yStart,xStart+dolzina,yStart,p);
	}
	
	void step(){
		if(charge>110 && charging) {
			charging=false;
			if(next!=null)
				next.charging=true;
		}
		if(!charging)
			charge-=10;
		if(charging)
			charge+=65;
		if(charge<0)charge=0;	
	}
}

class GreenCharger extends Instance{
	
	LineGreen[] list;
	
	GreenCharger(LineGreen[] list){
		super(0,0,0,null);
		this.list=list;
		enemy=false;
	}
	
	void step(){
		if(Math.random()<0.1){
			list[(int)(Math.random()*list.length)].charging=true;
		}
	}
}

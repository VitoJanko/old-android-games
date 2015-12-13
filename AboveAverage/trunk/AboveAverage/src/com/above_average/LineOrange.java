package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LineOrange extends Instance{
	int xStart;
	int yStart;
	int dolzina;
	int red;
	int charge;
	boolean chargingD;
	int delay;
	boolean charging;
	boolean vertical;
	LineOrange next;
	
	LineOrange(int xStart, int yStart, int dolzina, boolean vertical){
		super(0,0,0,null);
		this.vertical=vertical;
		this.xStart=xStart;
		this.yStart=yStart;
		this.dolzina=dolzina;
		charging=false;
		red=40;
		enemy=false;
	}
	
	void draw(Canvas c, Paint p){
		int col = Math.min(200,charge/2);
		p.setColor(Color.rgb(red+col,(red+col)/2,0));
		if(vertical)
			c.drawLine(xStart,yStart,xStart,yStart+dolzina,p);
		else
			c.drawLine(xStart,yStart,xStart+dolzina,yStart,p);
	}
	
	void step(){
		if(chargingD && delay==0) {charging=true; chargingD=false;}
		if(chargingD && delay!=0) delay--;
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

class OrangeCharger extends Instance{
	
	LineOrange[] list;
	
	OrangeCharger(LineOrange[] list){
		super(0,0,0,null);
		this.list=list;
		enemy=false;
	}
	
	void step(){
		if(Math.random()<0.02){
			int time=0;
			for(int i=0; i<list.length; i++){
				time+=Math.random()*10-5;
				list[i].chargingD=true;
				list[i].delay=Math.max(0, time);
			}
		}
	}
}

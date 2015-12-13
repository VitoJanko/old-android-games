package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Line extends Instance{
	int xStart;
	int yStart;
	int dolzina;
	int blue;
	int charge;
	Line up;
	Line down;
	Line left;
	Line right;
	
	
	Line(int xStart, int yStart, int dolzina){
		super(0,0,0,null);
		this.xStart=xStart;
		this.yStart=yStart;
		this.dolzina=dolzina;
		blue=40;
		enemy=false;
	}
	
	void draw(Canvas c, Paint p){
		int col = Math.min(200,charge);
		p.setColor(Color.rgb(0,0,blue+col));
		c.drawLine(xStart,yStart,xStart+dolzina,yStart,p);
		c.drawLine(xStart,yStart,xStart,yStart+dolzina,p);
	}
	
	void step(){
		if(up!=null && charge-up.charge>50 ) {up.charge+=20; charge-=20;}
		if(right!=null && charge-right.charge>50) {right.charge+=20; charge-=20;}
		if(down!=null && charge-down.charge>50) {down.charge+=20; charge-=20;}
		if(left!=null && charge-left.charge>50) {left.charge+=20; charge-=20;}
		charge-=5;
		if(charge<0)charge=0;
		
	}
}

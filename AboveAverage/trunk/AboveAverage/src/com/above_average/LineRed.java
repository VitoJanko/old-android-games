package com.above_average;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class LineRed extends Instance{
	
	static int index = 0;
	
	int xStart;
	int yStart;
	int dolzina;
	int red;
	int charge;
	boolean charging;
	LineRed up;
	LineRed down;
	LineRed left;
	LineRed right;
	ArrayList<Integer> list; 
	
	LineRed(int xStart, int yStart, int dolzina){
		super(0,0,0,null);
		this.xStart=xStart;
		this.yStart=yStart;
		this.dolzina=dolzina;
		red=40;
		charge=0;
		charging=false;
		enemy=false;
		list=new ArrayList<Integer>();
	}
	
	void draw(Canvas c, Paint p){
		int col = Math.min(200,charge);
		p.setColor(Color.rgb(red+col,0,0));
		c.drawLine(xStart,yStart,xStart+dolzina,yStart,p);
		c.drawLine(xStart,yStart,xStart,yStart+dolzina,p);
	}
	
	void addCharge(int index){
		if(!list.contains(index)){
			list.add(index);
			charging=true;
			if(list.size()>10)
				list.remove(0);
		}
	}
	
	void step(){
		if(charging)charge+=7;
		if(!charging)charge-=7;
		if(charge<0)charge=0;
		if(charge>200){
			charge=200;
			charging=false;
			int id = list.get(list.size()-1);
			if(up!=null) up.addCharge(id);
			if(down!=null) down.addCharge(id);
			if(left!=null) left.addCharge(id);
			if(right!=null) right.addCharge(id);
		}
	}
}


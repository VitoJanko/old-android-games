package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Leaf extends Instance{
	int directionR;
	int directionA;
	float x1, x2, x3, y1, y2, y3;
	
	Leaf(Darkness host, int direction, int radij){
		super(0,0,radij,host);
		y=host.height+radij;
		x=(float)(Math.random()*host.width);
		left=240;
		speed=4*host.ratio;
		directionR=270;
		directionA=(int)(Math.random()*360);
	}
	
	protected void step(){
		if(Math.random()<0.1){
			directionR+=(int)(Math.random()*20-10);
		}
		if(directionR<0)directionR=360+directionR;
		if(directionR>360) directionR=directionR-360;
		if(Math.random()<0.3){
			directionA+=(int)(Math.random()*30-20);
		}
		if(directionA<0)directionA=360+directionA;
		if(directionA>360) directionA=directionA-360;
		x += speed*(float)Math.cos(Math.toRadians(directionR));
		y += speed*(float)Math.sin(Math.toRadians(directionR));
		x1 = radij/2*(float)Math.cos(Math.toRadians(directionA));
		y1 = radij/2*(float)Math.sin(Math.toRadians(directionA));
		x2 = (float)(x1*Math.cos(2*Math.PI/3)-y1*Math.sin(2*Math.PI/3));
		y2 = (float)(x1*Math.sin(2*Math.PI/3)+y1*Math.cos(2*Math.PI/3));
		x3 = (float)(x1*Math.cos(4*Math.PI/3)-y1*Math.sin(Math.PI/3));
		y3 = (float)(x1*Math.sin(4*Math.PI/3)+y1*Math.cos(Math.PI/3));

		left--;
		if(left==0) dead=true;
	}
	
	protected void draw(Canvas c, Paint p){
		p.setColor(Color.WHITE);
		p.setStyle(Paint.Style.FILL);
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(x+x1,y+y1);
		path.lineTo(x+x2, y+y2);
		path.lineTo(x+x3, y+y3);
		path.lineTo(x+x1, y+y1);
		path.close();
		c.drawPath(path, p);
	}
}

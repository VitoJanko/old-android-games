package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Flame extends Instance{
	
	Boss target;
	int stage;
	int origin;
	int green;
	int red;
	
	Flame(Darkness host, int radij, Boss target){
		super(0,0,radij,host);
		generateRandom();
		this.target=target;
		stage=0;
		origin=radij;
		green=255;
		red=255;
		enemy=false;
		left=190;
	}
	
	protected void step(){
		if(stage==0){
			Protagonist p = host.hero;
			if(host.razdalja(x,y,p.x,p.y)<p.radij/2+radij/2){
				stage=1;
			}
		}
		if(stage==1){
			radij+=1;
			if(radij>origin*1.5){
				stage=2;
				target.health-=10;
				target.red=50;
			}
		}
		if(stage==2){
			if(green>45) green-=45;
			else{
				green=0;
				red-=45;
			}
			if(red<70) dead=true;
		}
		left--;
		if(left<0 && stage==0) dead=true;
		if(target.dead) dead=true;
	}
	
	protected void draw(Canvas c, Paint p){
		if(stage!=2)
			drawTriangle(target.x,target.y,c,p,Color.RED);
		else{
			float vectorX = target.x-x;
			float vectorY = target.y-y;
			float norma = (float)Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY,2));
			float x1 = (vectorX*radij/2)/norma;
			float y1 = (vectorY*radij/2)/norma;
			float x2 = (float)(x1*Math.cos(2*Math.PI/3)-y1*Math.sin(2*Math.PI/3));
			float y2 = (float)(x1*Math.sin(2*Math.PI/3)+y1*Math.cos(2*Math.PI/3));
			float x3 = (float)(x1*Math.cos(4*Math.PI/3)-y1*Math.sin(4*Math.PI/3));
			float y3 = (float)(x1*Math.sin(4*Math.PI/3)+y1*Math.cos(4*Math.PI/3));
			p.setColor(Color.rgb(red, green, 0));
			p.setStyle(Paint.Style.FILL);
			Path path = new Path();
			path.setFillType(Path.FillType.EVEN_ODD);
			path.moveTo(target.x,target.y);
			path.lineTo(x+x2, y+y2);
			path.lineTo(x+x3, y+y3);
			path.lineTo(target.x, target.y);
			path.close();
			c.drawPath(path, p);
		}
	}
}

package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Bomb extends Instance{

	float radijCount;
	int radijMax;
	
	Bomb(int radijMax, Darkness host){
		super(0,0,3,host);
		radijCount=3;
		this.radijMax=radijMax;
		generateRandom();
	}
	
	protected void step(){
		radijCount+=0.25*host.ratio;
		radij=(int)(radijCount);
		if(Math.random()<0.1)
			host.instances.add(new Ghost(this));
		if(radij>radijMax){
			dead = true;
			for(int i=0; i<8; i++)
				host.instances.add(new Shrapnel((int)x,(int)y,radij/2,host));
		}
	}
	
	protected void draw(Canvas c, Paint p){
		drawCircle(c,p);
	}
}

class Shrapnel extends Instance{
	int width;
	int height;
	
	Shrapnel(int x, int y, int radij, Darkness host){
		super(x,y,radij,host);
		this.width = host.width;
		this.height = host.height;
		direction =(int)(Math.random()*360);
		speed = 8*host.ratio;
	}
	
	protected void step(){
		float dirX = speed*(float)Math.cos(Math.toRadians(direction));
		float dirY = speed*(float)Math.sin(Math.toRadians(direction));
		if(x+dirX>width+radij/2 || x+dirX<-radij/2) dead=true;
		if(y+dirY>height-radij/2 || y+dirY<-radij/2) dead=true;
		x+= dirX;
		y+= dirY;
	}
	
	protected void draw(Canvas c, Paint p){
		drawCircle(c,p);
	}
}

class Ghost extends Instance{
	Bomb master;
	int alpha;
	
	Ghost(Bomb master){
		super(0,0,0,null);
		this.master=master;
		radij=master.radij/2;
		double startDir = Math.random()*360;
		x = master.x + master.radij*3*(float)Math.cos(Math.toRadians(startDir));
		y = master.y + master.radij*3*(float)Math.sin(Math.toRadians(startDir));
		direction=(int)startDir+180;
		if(direction>360)direction-=360;
		speed = 2*master.host.ratio;
		alpha = 80+(int)(Math.random()*60);
	}
	
	protected void step(){
		float dirX = speed*(float)Math.cos(Math.toRadians(direction));
		float dirY = speed*(float)Math.sin(Math.toRadians(direction));
		x+= dirX;
		y+= dirY;
		if(master.dead) dead = true;
		else if(master.host.razdalja(x,y,master.x,master.y)<speed)dead=true;		
	}
	
	protected void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		pp.setColor(Color.argb(alpha, 255,255,255));
		pp.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, pp);
	}
}

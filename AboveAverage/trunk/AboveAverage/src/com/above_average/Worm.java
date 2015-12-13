package com.above_average;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Worm extends Instance{
	Tail tail;
	
	Worm(Darkness host, int radij){
		super(0,0,radij,host);
		generateRandom();
		speed=5*host.ratio;
		double diffY = (y-host.height/2.0);
		double diffX = (x-host.width/2.0);
		double k = diffY /diffX;
		direction=(int)(Math.toDegrees(Math.atan(k))+Math.random()*180-90);
		tail=new Tail(x,y,radij,host);
		host.instances.add(tail);
	}
	
	protected void step(){
		rotate(0.2f,10);
		x+= dirX;
		y+= dirY;
		if(host.razdalja(x,y,tail.x,tail.y)>radij){
			tail=new Tail(x,y,radij,host);
			host.instances.add(tail);
		}
		deathWall();
	} 
	
	protected void draw(Canvas c, Paint p){
		drawCircle(c,p);
	}
}

class Tail extends Instance{

	Tail(float x, float y, int radij, Darkness host) {
		super(x, y, radij, host);
		left = 90; 
	}
	
	void step(){
		left--;
		if(left==0) dead=true;
	}
	
	protected void draw(Canvas c, Paint p){
		drawCircle(c,p);
	}

}

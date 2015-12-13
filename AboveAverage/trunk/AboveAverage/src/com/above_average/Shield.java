package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Shield extends Instance{
	
	int radijMax;
	boolean timeToDie;
	
	Shield(Darkness host, int radij){
		super(0,0,radij,host);
		generateRandom();
		radijMax=radij;
		this.radij=0;
		enemy=false;
		host.makeChargeRed((int)x, (int)y);
		timeToDie=false;
	}
	
	protected void step(){
		if(!timeToDie)
			radij+=host.ratio;
		else{
			radij-=2*host.ratio;
			if(radij<0)
				dead=true;
		}
		if(radij>radijMax){
			generateRandom();
			radij=0;
			host.makeChargeRed((int)x, (int)y);
			if(timeToDie) dead=true;
		}
		if(Math.random()<0.03) host.instances.add(new ShieldRing(host,this));
		for(int i=0; i<host.instances.size(); i++){
			Instance in = host.instances.get(i);
			if(in.enemy && host.razdalja(x,y,in.x,in.y)<radij/2+in.radij/2){
				in.dead=true;
			}
		}
	} 
	
	protected void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		pp.setColor(Color.argb(50, 255,255,0));
		pp.setStyle(Paint.Style.FILL);
		int rad = radij;
		RectF r = new RectF(x-rad/2, y-rad/2, x+rad/2, y+rad/2);
		c.drawOval(r, pp);
		pp.setColor(Color.argb(255, 255,255,0));
		pp.setStyle(Paint.Style.STROKE);
		c.drawOval(r, pp);
	}
}

class ShieldRing extends Instance{
	
	int radijMax;
	Shield owner;
	
	ShieldRing(Darkness host, Shield owner){
		super(owner.x,owner.y,0,host);
		enemy=false;
		this.owner=owner;
	}
	
	protected void step(){
		radij+=2*host.ratio;
		if(owner!=null && radij>owner.radij)
			dead=true;
		if(owner==null || owner.dead){
			dead=true;
		}
	} 
	
	protected void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		pp.setColor(Color.argb(255, 255,255,0));
		pp.setStyle(Paint.Style.STROKE);
		c.drawOval(r, pp);
	}
}

package com.above_average;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Net extends Instance{
	
	static int netNumber;
	static int maximum ;
	
	static float chanse(float spawnBase){
		if(maximum/2>netNumber)
			spawnBase*=(maximum/2-netNumber);
		else if (maximum/2<netNumber)
			spawnBase/=(float)(netNumber-maximum/2);
		if(netNumber>maximum) spawnBase=0;
		return spawnBase;
	}
	
	int width;
	int height;
	int nodes;
	int pointX;
	int pointY;
	ArrayList<Net> list;
	boolean spike;
	int timer;
	
	Net(int radij, Darkness host, int nodes, boolean spike, boolean original){
		super(0,0,radij,host);
		this.width = host.width;
		this.height = host.height;
		this.nodes=nodes;
		this.spike=spike;
		netNumber++;
		list=new ArrayList<Net>();
		direction=(int)(Math.random()*360);
		speed=2*host.ratio;
		timer=60;
		left=250;
		generateRandom();
		if(host.grid!=null & original)
			host.makeCharge((int)x,(int)y);
	}
	
	boolean altCollision(int xHero, int yHero, int radij){
		boolean collide = false;
		for(int i=0; i<list.size(); i++) {
			Net net = list.get(i);
			if(localCollision(xHero,yHero,radij,x,y,net.x,net.y)) collide=true;
		}
		return collide;
	}
	
	protected void step(){
		timer--;
		if(timer==0 && nodes>0 && !(pointX>0 || pointY>0)){
			timer=60;
			pointX=(int)x;
			pointY=(int)y;
		}
		if(pointX>0 || pointY>0){
			if(host.razdalja(x,y,pointX,pointY)>3*radij){
				Net n;
				if(spike){
					n = new Net(radij,host,0,false,false);
					nodes--;
				}
				else{
					n = new Net(radij,host,nodes-1,false,false);
					nodes=0;
				}
				n.x=pointX;
				n.y=pointY;
				if(host.grid!=null)
					host.makeCharge((int)n.x,(int)n.y);
				pointX=0; pointY=0;
				list.add(n);
				host.instances.add(n);
			}
		}
		rotate(0.2f,10);
		left--;
		if(left==0){
			netNumber--;
			dead=true;
		}
//		if(x+dirX>width || x+dirX<0) {dirX-=dirX; direction+=180;}
//		else if(y+dirY>height || y+dirY<0) {dirY-=dirY; direction+=180;}
		bounce();
		x+= dirX;
		y+= dirY;
	}
	
	protected void draw(Canvas c, Paint p){
		p.setColor(Color.WHITE);
		p.setStyle(Paint.Style.STROKE);
		if(pointX>0 || pointY>0) c.drawLine(x,y,pointX,pointY,p);
		for(int i=0; i<list.size(); i++) {
			Net n = list.get(i);
			if(n.dead) {list.remove(i); i--;}
			else c.drawLine(x,y,n.x,n.y,p);
		}
		c.drawRect((int)x-radij/2,(int)y-radij/2,(int)x+radij/2,(int)y+radij/2,p);
	}
}


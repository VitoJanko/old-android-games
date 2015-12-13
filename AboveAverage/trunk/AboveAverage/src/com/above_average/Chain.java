package com.above_average;

import android.graphics.Canvas; 
import android.graphics.Paint;

public class Chain {

	Chain(Darkness host, int radij, int links){
		int xStart1; int yStart1;
		int xStart2; int yStart2;
		int length = 2*radij+(radij/2)*links;
		int side = (int)(Math.random()*4);
		if(side==0){
			xStart1=(int)(Math.random()*(host.width-length));
			xStart2=xStart1+length-radij;
			yStart1=-radij;
			yStart2=-radij;
		}
		else if(side==1){
			xStart1=(int)(Math.random()*(host.width-length));
			xStart2=xStart1+length-radij;
			yStart1=host.height+radij;
			yStart2=host.height+radij;
		}
		else if(side==2){
			yStart1=(int)(Math.random()*(host.height-length));
			yStart2=yStart1+length-radij;
			xStart1=-radij;
			xStart2=-radij;
		}
		else{
			yStart1=(int)(Math.random()*(host.height-length));
			yStart2=yStart1+length-radij;
			xStart1=host.width+radij;
			xStart2=host.width+radij;
		}
		ChainHead left = new ChainHead(xStart1,yStart1,radij,host,length-radij);
		ChainHead right = new ChainHead(xStart2,yStart2,radij,host,length-radij);
		ChainLink[] l = new ChainLink[links];
		
		float vectorX = right.x-left.x;
		float vectorY = right.y-left.y;
		float norma = (float)Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY,2));
		float dirX = (vectorX*(radij/2))/norma;
		float dirY = (vectorY*(radij/2))/norma;
		int firstX = (int)(left.x+(vectorX*(radij*0.75))/norma);
		int firstY = (int)(left.y+(vectorY*(radij*0.75))/norma);
		host.instances.add(left);
		host.instances.add(right);
		left.pair = right;
		right.pair = left;
		for(int i=0; i<links; i++){
			l[i]=new ChainLink(firstX,firstY,radij/2);
			host.instances.add(l[i]);
			if(i!=0){
				l[i].left = l[i-1];
				l[i-1].right = l[i];
			}
			firstX+=dirX;
			firstY+=dirY;
		}
		l[0].first = true;
		l[0].head=left;
		l[links-1].first = false;
		l[links-1].head = right;
		left.first = true;
		left.link = l[0];
		right.first = false;
		right.link = l[links-1];
	}
}

class ChainHead extends Instance{
	int length;
	ChainHead pair;
	ChainLink link;
	boolean first;
	
	ChainHead(int x, int y, int radij, Darkness host, int length){
		super(x,y,radij,host);
		this.length = length;
		speed=4*host.ratio;
		double diffY = (host.height/2.0-y);
		double diffX = (host.width/2.0-x);
		double k = diffY /diffX;
		direction=(int)(Math.toDegrees(Math.atan(k)));
		if(diffX<0 && diffY>0) direction=180+direction;
		if(diffX<0 && diffY<0) direction=180+direction;
		direction+=(int)(Math.random()*80)-40;
		left = 240;
	}
	
	void handle(){
		float vectorX = x-link.x;
		float vectorY = y-link.y;
		float norma = (float)Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY,2));
		x=link.x+(vectorX*(link.radij/2+radij/2))/norma;
		y=link.y+(vectorY*(link.radij/2+radij/2))/norma;
	}
	
	void step(){
		left--;
		if(left==0) dead=true;
		while(true){
			if(Math.random()<0.1){
				direction+=(int)(Math.random()*10-5);
			}
			if(direction<0)direction=360+direction;
			if(direction>360) direction=direction-360;
			float dirX = speed*(float)Math.cos(Math.toRadians(direction));
			float dirY = speed*(float)Math.sin(Math.toRadians(direction));
			x+= dirX;
			y+= dirY;
			if(host.razdalja(x,y,pair.x,pair.y)<=length)
				break;
			else{
				x-=dirX;
				y-=dirY;
			}
		}
		link.handle(first);
	}
	
	protected void draw(Canvas c, Paint p){
		drawCircle(c,p);
	}
	
}

class ChainLink extends Instance{
	ChainLink left;
	ChainLink right;
	ChainHead head;
	boolean first;
	int life;
	
	ChainLink(int x, int y, int radij){
		super(x,y,radij,null);
		life=240;
	}
	
	void step(){
		life--;
		if(life==0)dead=true;
	}
	
	void handle(boolean leftToRight){
		if(head==null && leftToRight){
			if(calculate(left,right))
				right.handle(leftToRight);
		}
		else if (head==null){
			if(calculate(right,left))
				left.handle(leftToRight);
		}
		else if(leftToRight==true && first==true){
			if(calculate(head,right))
				right.handle(leftToRight);
		}
		else if(leftToRight==false && first==true){
			if(calculate(right,head))
				head.handle();
		}
		else if(leftToRight==true && first==false){
			if(calculate(left,head))
				head.handle();
		}
		else{
			if(calculate(head,left))
				left.handle(leftToRight);
		}
	}
		
	boolean calculate(Instance a, Instance b){
		float vectorX = b.x-a.x;
		float vectorY = b.y-a.y;
		float norma = (float)Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY,2));
		x=a.x+(vectorX*(a.radij/2+radij/2))/norma;
		y=a.y+(vectorY*(a.radij/2+radij/2))/norma;
		return razdalja(a,b)>a.radij/2+radij+b.radij/2;
	}
		
	
	float razdalja(Instance a, Instance b){
		return (float)Math.sqrt(Math.pow((a.x-b.x),2)+Math.pow(a.y-b.y,2));
	}
	
	protected void draw(Canvas c, Paint p){
		drawCircle(c,p);
	}
}

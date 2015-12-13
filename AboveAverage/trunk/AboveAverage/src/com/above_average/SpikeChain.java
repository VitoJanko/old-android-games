 package com.above_average;

import android.graphics.Canvas;
import android.graphics.Paint;

public class SpikeChain {
		
	SpikeChain(int radij, int links, Darkness host){
		SpikeLink.number++;
		int xStart; 
		int yStart;
		int dirX;
		int dirY;
		int length = links*radij;
		int side = (int)(Math.random()*4);
		if(side==0){
			xStart=(int)(Math.random()*(host.width-length));
			yStart=-radij;
			dirX=1; dirY=0;
		}
		else if(side==1){
			xStart=(int)(Math.random()*(host.width-length));
			yStart=host.height+radij;
			dirX=1; dirY=0;
		}
		else if(side==2){
			yStart=(int)(Math.random()*(host.height-length));
			xStart=-radij;
			dirX=0; dirY=1;
		}
		else{
			yStart=(int)(Math.random()*(host.height-length));
			xStart=host.width+radij;
			dirX=0; dirY=1;
		}
		SpikeLink[] l = new SpikeLink[links];
		for(int i=0; i<links; i++){
			boolean head = false;
			int radijT=radij;
			xStart+=(radijT/2)*dirX;
			yStart+=(radijT/2)*dirY;
			if(i==0 || i==links-1){
				head=true;
				radijT=2*radij;
			}
			l[i] = new SpikeLink(xStart, yStart, radijT, head, host);
			host.instances.add(l[i]); 
			xStart+=(radijT/2)*dirX;
			yStart+=(radijT/2)*dirY;
		}
		for(int i=0; i<links; i++){
			if(i!=0)l[i-1].right=l[i];
			if(i!=links-1)l[i+1].left=l[i];
		}
	}
}

class SpikeLink extends Instance{
	
	static int number;
	
	static float chanse(float spawnBase){
		if(number!=0)
			spawnBase=spawnBase/(3*number);
		return spawnBase;
	}
	
	SpikeLink left;
	SpikeLink right;
	boolean head;
	boolean anchored;
	int time;
	float tryX;
	float tryY;
	
	SpikeLink(int x, int y, int radij, boolean head, Darkness host){
		super(x,y,radij,host);
		this.head = head;
		speed=4*host.ratio;
		double diffY = (host.height/2.0-y);
		double diffX = (host.width/2.0-x);
		double k = diffY /diffX;
		direction=(int)(Math.toDegrees(Math.atan(k)));
		if(diffX<0 && diffY>0) direction=180+direction;
		if(diffX<0 && diffY<0) direction=180+direction;
		direction+=(int)(Math.random()*80)-40;
		time = 240;
		anchored=false;
		immune=true;
	}
	
	void step(){
		time--;
		if(time==0) {
			if(head && left==null) number--;
			Hedgehog h = new Hedgehog(radij,host);
			host.instances.add(h);
			h.x=x;
			h.y=y;
			dead=true;
		}
		if(head && !anchored){
			rotate(0.2f, 5);
			tryX = x + dirX;
			tryY = y + dirY;
			if(left!=null) left.handle(false);
			if(right!=null) right.handle(true);
		}
		for(int i=0; i<host.obstacle.size(); i++){
			Instance in = host.obstacle.get(i);
			if(in.altCollision((int)x, (int)y, radij)){
				anchored=true;
			}	
		}
	}
	
	void handle(boolean leftToRight){
		boolean success = false;
		SpikeLink next;
		SpikeLink prev;
		if(leftToRight){
			next=right; 
			prev=left;
		}
		else{
			next=left; 
			prev=right;
		}
		if(anchored || head){
			tryX=x;
			tryY=y;
			success = razdalja(this,prev)<=radij/2+prev.radij/2;
		}
		else{
			next.tryX=next.x; next.tryY=next.y;
			success=!calculate(prev,next);
		}
		if (success){
			x=tryX;
			y=tryY;
			prev.handleBack(!leftToRight, success);
		}
		else if(head || anchored) prev.handleBack(!leftToRight, success);
		else  next.handle(leftToRight);	
	}
	
	boolean calculate(SpikeLink a, SpikeLink b){
		float vectorX = b.tryX-a.tryX;
		float vectorY = b.tryY-a.tryY;
		float norma = (float)Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY,2));
		tryX=a.tryX+(vectorX*(a.radij/2+radij/2))/norma;
		tryY=a.tryY+(vectorY*(a.radij/2+radij/2))/norma;
		return razdalja(a,b)>a.radij/2+radij+b.radij/2;
	}
	
	float razdalja(SpikeLink a, SpikeLink b){
		return (float)Math.sqrt(Math.pow((a.tryX-b.tryX),2)+Math.pow(a.tryY-b.tryY,2));
	}
	
	void handleBack(boolean leftToRight, boolean success){
		if(success){
			x=tryX;
			y=tryY;
		}
		if(!head){
			if(leftToRight) right.handleBack(leftToRight,success);
			if(!leftToRight) left.handleBack(leftToRight,success);
		}
		if(head && !success) direction+=(int)(Math.random()*90-45);
	}
	
	protected void draw(Canvas c, Paint p){
		drawCircle(c,p);
		if(left!=null && right!=null){
			float k= (left.y-right.y)/(left.x-right.x);
			float dir = (float) Math.atan(k);
			dir+=(Math.PI/2);
			float sX = (float) (radij*Math.cos(dir));
			float sY = (float) (radij*Math.sin(dir));
			c.drawLine(x-sX,y-sY,x+sX,y+sY,p);
		}
		else{
			float k;
			if(left!=null) k= (left.y-y)/(left.x-x);
			else k= (right.y-y)/(right.x-x);
			float dir = (float) Math.atan(k);
			float sX2 = (float) (radij*Math.cos(dir));
			float sY2 = (float) (radij*Math.sin(dir));
			dir+=(Math.PI/2);
			float sX = (float) (radij*Math.cos(dir));
			float sY = (float) (radij*Math.sin(dir));
			c.drawLine(x-sX,y-sY,x+sX,y+sY,p);
			c.drawLine(x-sX2,y-sY2,x+sX2,y+sY2,p);
		}
	}
}

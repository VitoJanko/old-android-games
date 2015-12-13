package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Protagonist extends Instance{
	
	float ratioX;
	float ratioY;
	int demage;
	int color;
	int charges;
	int direction;
	int impact;
	HandGun weapon;
	
	Protagonist(int x, int y, int radij, int width, int height, Darkness d){
		super(x,y,radij,d);
		ratioX = (float)(360.0/width);
		ratioY = (float)(1.0/height);
		enemy = false;
		persistant=true;
		demage = 0;
		charges=0;
	}
	
	boolean collision(){
		boolean collide = false;
		for(int i=0; i<host.instances.size(); i++){
			Instance in = host.instances.get(i);
			if (in.enemy && 
					((!in.onlyAlt && host.razdalja(in.x,in.y,x,y)<radij/2+in.radij/2 )|| in.altCollision((int)x,(int)y,radij))){
						collide=true;
						in.harmDone+=in.demageDealt;
						if(in.demageDealt>impact) impact= in.demageDealt;
			}
		}
		return collide;
	}
	
	int makeColor(){
		float L=(100+demage)*(1f/255f);
		float H=x*ratioX;
		float H2=H/60f;
		float S=y*ratioY;
		float C=(1-Math.abs(2*L-1))*S;
		float X=C*(1-Math.abs(H2%2-1));
		float R1=0, G1=0, B1=0;
		switch ((int)H2){
			case 0:
				R1=C;
				G1=X;
				break;
			case 1:
				R1=X;
				G1=C;
				break;
			case 2:
				G1=C;
				B1=X;
				break;
			case 3:
				B1=C;
				G1=X;
				break;
			case 4:
				R1=X;
				B1=C;
				break;
			case 5:
				R1=C;
				B1=X;
				break;
				
		}
		float m = (float)(L-0.5*C);
		int R = (int)((R1+m)*255);
		int G = (int)((G1+m)*255);
		int B = (int)((B1+m)*255);
		return Color.rgb(R,G,B);
	}
	
	void step(){
		impact=0;
		if(demage<0)demage=0;
		color=makeColor();
		int a = (int)(Math.random()*15);
		if (a==0 && (host.gameState==host.PAUSE || host.controlType==host.GYRO)){
			Glow g = new Glow((int)x,(int)y,radij,color);
			host.instances.add(g);
		}
		if(host.controlType==host.GYRO && host.gameState==host.GAME){
			float premikX = host.main.angleX*4.5f*host.ratio;
			x-= premikX;
			for(int i=0; i<host.obstacle.size(); i++){
				Instance in = host.obstacle.get(i);
				while(Math.abs(premikX)>1 && in.altCollision((int)x, (int)y, radij)){
					x+=premikX;
					premikX/=2;
					x-=premikX;
				}
				if(in.altCollision((int)x, (int)y, radij)) x+=premikX;
			}
			float premikY = host.main.angleY*4.5f*host.ratio;
			y+=premikY;
			for(int i=0; i<host.obstacle.size(); i++){
				Instance in = host.obstacle.get(i);
				while(Math.abs(premikY)>1 && in.altCollision((int)x, (int)y, radij)){
					y-=premikY;
					premikY/=2;
					y+=premikY;
				}
				if(in.altCollision((int)x, (int)y, radij)) y-=premikY;
			}
			if(x<0)x=host.width;
			if(y<0)y=host.height;
			if(x>host.width)x=0;
			if(y>host.height)y=0;
			if(premikX!=0 || premikY!=0)
				direction=getDirection(0,0,-premikX,premikY);
		}
		if(Math.random()<0.24 && host.gameState==host.GAME && charges>0){
			host.instances.add(new Bonus(this));
			charges--;
		}
		if(collision()&& host.gameState==host.GAME){
			if(!host.won){			
				demage+=impact;
				host.texter.addNow("Ouch", 30);
			}
		}
		if(demage>150) {
			demage=155;
			host.gameState=host.LOST;
			if(host.stage==15){
				host.altWon=true;
				MainMenu.number=host.stage;
				MainMenu.demage=host.maker.time/33;
			}
		}
	}
	
	protected void draw(Canvas c, Paint p){
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		p.setColor(color);
		p.setStyle(Paint.Style.FILL);
		c.drawOval(r, p);
		p.setColor(Color.WHITE);
		p.setStyle(Paint.Style.STROKE);
		c.drawOval(r, p);
	}
}

class Bonus extends Instance{
	Protagonist master;
	int alpha;
	int color;
	
	Bonus(Protagonist master){
		super(0,0,0,null);
		this.master=master;
		radij=master.radij/3;
		color=master.makeColor();
		double startDir = Math.random()*360;
		x = master.x + master.radij*(float)Math.cos(Math.toRadians(startDir));
		y = master.y + master.radij*(float)Math.sin(Math.toRadians(startDir));
		speed = 4;
		enemy=false;
		alpha = 80+(int)(Math.random()*60);
	}
	
	protected void step(){
		float vectorX = master.x-x;
		float vectorY = master.y-y;
		float norma = (float)Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY,2));
		float dirX = (vectorX*speed)/norma;
		float dirY = (vectorY*speed)/norma;
		speed+=0.3;
		x+= dirX;
		y+= dirY;
		if(master.host.razdalja(x,y,master.x,master.y)<speed){
			master.demage-=4;
			dead=true;		
		}
	}
	
	protected void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		pp.setColor(Color.argb(alpha, Color.red(color),Color.green(color),Color.blue(color)));
		pp.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, pp);
	}
}

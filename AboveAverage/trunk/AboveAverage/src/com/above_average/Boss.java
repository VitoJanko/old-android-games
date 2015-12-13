package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Boss extends Instance{
		
	int razmak;
	int innerRadij;
	int handDirection;
	int handSpeed;
	int health;
	int state;
	int timer;
	int ammo;
	boolean ready;
	Target target;
	int lx1,ly1,lx2,ly2,lx3,ly3;
	int cx1,cx2,cx3,cy1,cy2,cy3;
	int red;
	
	Boss(int radij, Darkness host){
		super(0,0,radij,host);
		generateRandom();
		direction= (int)(Math.random()*360);
		handDirection= (int)(Math.random()*360);
		speed = 3*host.ratio;
		razmak = (int)(radij/4.0);
		innerRadij=radij-razmak;
		health=255;
		state=0;
		handSpeed=4;
		target=null;
		timer=10;
		ammo=6;
		ready=true;
		red=255;
	}

	boolean altCollision(int xHero, int yHero, int radij){
		boolean collide = false;
		if (localCollision(xHero,yHero,radij,x,y,lx1,ly1)) collide = true;
		if (localCollision(xHero,yHero,radij,x,y,lx2,ly2)) collide = true;
		if (localCollision(xHero,yHero,radij,x,y,lx3,ly3)) collide = true;
		if(state!=3) collide=false;
		if(host.razdalja(xHero,yHero,cx1,cy1)<radij/2+razmak/2)	collide=true;
		if(host.razdalja(xHero,yHero,cx2,cy2)<radij/2+razmak/2)	collide=true;
		if(host.razdalja(xHero,yHero,cx3,cy3)<radij/2+razmak/2)	collide=true;
		return collide;
	}
	
	void step(){
		if(red<255)red+=20;
		if(red>255)red=255;
		if(state==0){
			handSpeed=4;
			if(Math.random()<0.06f)
				host.instances.add(new Leaf(host,1,(int)(15*host.ratio)));
			if(health<230) state++;
		}
		if(state==1){
			if(target==null){
				target=new Target(radij/2,host);
				host.instances.add(target);
			}
			else{
				if(target.left<55){
					speed = 12*host.ratio;
					double diffY = target.y-y;
					double diffX = target.x-x;
					double k = diffY /diffX;
					direction=(int)(Math.toDegrees(Math.atan(k)));
					if(diffX<0 && diffY>0) direction=180+direction;
					if(diffX<0 && diffY<0) direction=180+direction;
				}
				if(host.razdalja(x,y,target.x,target.y)<target.radij/2+radij/2){
					speed = 3*host.ratio;
					target.dead=true;
					target=null;
				}
			}
			handSpeed=3;
			if(Math.random()<0.03f)
				host.instances.add(new Leaf(host,1,(int)(15*host.ratio)));
			if(Math.random()<0.02f)
				host.instances.add(new Hedgehog((int)(18*host.ratio),host));
			if(health<160) state++;
		}
		if(state==2){
			target=null;
			speed = 3*host.ratio;
			handSpeed=2;
			if(Math.random()<0.02f)
				host.instances.add(new Leaf(host,1,(int)(15*host.ratio)));
			if(Math.random()<0.013f)
				host.instances.add(new Stalker(host,host.width,host.height,(int)(15*host.ratio)));
			if(health<100) state++;
		}
		if(state==3){
			handSpeed=1;
			if(Math.random()<0.01f)
				host.instances.add(new Leaf(host,1,(int)(15*host.ratio)));
			if(Math.random()<0.008f)
				host.instances.add(new Stalker(host,host.width,host.height,(int)(15*host.ratio)));
			if(Math.random()<0.008f)
				host.instances.add(new Hedgehog((int)(18*host.ratio),host));
		}
		if(Math.random()<0.009f)
			host.instances.add(new Flame(host,(int)(15*host.ratio),this));
		if(Math.random()<0.002f)
			host.instances.add(new Health((int)(25*host.ratio),host));
		
		rotate(0.4f,10);
		x+= dirX;
		y+= dirY;
		handDirection+=handSpeed;
		if(handDirection>360)handDirection-=360;
		bounceWall();
		if(health<10) {
			host.maker.end();
			dead=true; 
		}
	}
	
	void drawHand(Canvas c, Paint p, int argument){
		float dx = (float)(x+Math.cos(Math.toRadians(handDirection))*(innerRadij+razmak/2));
		float dy = (float)(y+Math.sin(Math.toRadians(handDirection))*(innerRadij+razmak/2));
		RectF rr = new RectF(dx-razmak/2, dy-razmak/2, dx+razmak/2, dy+razmak/2);
		c.drawOval(rr, p);
		if(argument==1) {cx1=(int)dx; cy1=(int)dy;}
		if(argument==2) {cx2=(int)dx; cy2=(int)dy;}
		if(argument==3) {cx3=(int)dx; cy3=(int)dy;}
		if(state==2){
			if(ready){
				float vectorX = host.hero.x-dx;
				float vectorY = host.hero.y-dy;
				float norma = (float)Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY,2));
				float x1 = (vectorX*(float)(razmak/5f))/norma;
				float y1 = (vectorY*(float)(razmak/5f))/norma;
				host.instances.add(new Bullet(dx,dy,dx+x1,dy+y1,6*host.ratio,false,host));
				host.makeCharge((int)dx,(int)dy);
				ammo--;
			}
		}
		if(state==3){
			float vectorX = dx-x;
			float vectorY = dy-y;
			float norma = (float)Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY,2));
			float x1 = 1.5f*host.height*(vectorX*radij/2)/norma;
			float y1 = 1.5f*host.height*(vectorY*radij/2)/norma;
			c.drawLine(x,y,x+x1,y+y1,p);
			if(argument==1) {lx1=(int)(x+x1); ly1=(int)(y+y1);}
			if(argument==2) {lx2=(int)(x+x1); ly2=(int)(y+y1);}
			if(argument==3) {lx3=(int)(x+x1); ly3=(int)(y+y1);}
		}
		handDirection+=120;
		if(handDirection>360)handDirection-=360;
	}
	
	void drawHealth(Canvas c, Paint p){
		float dx = (float)(x+Math.cos(Math.toRadians(handDirection))*(innerRadij+razmak/2));
		float dy = (float)(y+Math.sin(Math.toRadians(handDirection))*(innerRadij+razmak/2));
		drawTriangle(dx,dy,c,p,Color.argb(health,255,red,red));
	}
	
	void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		pp.setColor(Color.argb(255,255,255,255));
		pp.setStyle(Paint.Style.STROKE);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, pp);
		RectF rr = new RectF(x-innerRadij/2, y-innerRadij/2, x+innerRadij/2, y+innerRadij/2);
		c.drawOval(rr, pp);
		if(state==2){
			timer--;
			ready=false;
			if(timer==0){
				ready=true;
				if(ammo==0){
					timer=90;
					ammo=6;
				}
				else timer = 8;
			}
		}
		drawHand(c,pp,1);
		drawHand(c,pp,2);
		drawHand(c,pp,3);
		drawHealth(c,pp);
	}
	
}

class Target extends Instance{
	
	Target(int radij, Darkness host){
		super(0,0,radij,host);
		generateRandom();
		left=100;
		host.makeCharge((int)x,(int)y);
	}
	
	void step(){
		left--;
		if(left==0) dead=true;
	}
	
	void draw(Canvas c, Paint p){
		p.setColor(Color.WHITE);
		p.setStyle(Paint.Style.STROKE);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, p);
		c.drawLine(x-(radij*3)/4,y,x+(radij*3)/4,y,p);
		c.drawLine(x,y-(radij*3)/4,x,y+(radij*3)/4,p);
	}
}

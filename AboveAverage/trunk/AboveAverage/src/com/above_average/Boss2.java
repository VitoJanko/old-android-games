package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class Boss2 extends Instance{
	
	int handDirection;
	int handSpeed;
	boolean laser;
	boolean shield;
	int health;
	int healthShield;
	int red;
	int redShield;
	boolean fireing;
	int laserAlpha;
	int countDownToLaser;
	boolean hasBall;
	PrivateBall orianna;
	float x1,x2,x7,x8;
	float y1,y2,y7,y8;
	int trueRadij;
	
	Boss2(int radij, Darkness host){
		super(0,0,radij,host);
		generateRandom();
		direction= (int)(Math.random()*360);
		handDirection= (int)(Math.random()*360);
		speed = 3*host.ratio;
		handSpeed=2;
		laser=false;
		countDownToLaser=90;
		shield=true;
		fireing=false;
		health=255;
		healthShield=255;
		red=255;
		redShield=255;
		hasBall=true;
		int ballDirection= (int)(Math.random()*360);
		int ballRadij=radij/3;
		float xBall= (float)(x+Math.cos(Math.toRadians(ballDirection))*radij*radij/2+ballRadij/2);
		float yBall= (float)(y+Math.sin(Math.toRadians(ballDirection))*radij*radij/2+ballRadij/2);
		orianna=new PrivateBall(xBall,yBall,ballRadij,this,host);
		host.instances.add(orianna);
		trueRadij=radij;
	}
	
	boolean altCollision(int xHero, int yHero, int radij){
		boolean collide = false;
		if(fireing && laserAlpha>60){
			if (localCollision(xHero,yHero,radij,x1,y1,x7,y7)) collide = true;
			if (localCollision(xHero,yHero,radij,x2,y2,x8,y8)) collide = true;
		}
		return collide;
	}
	
	protected void step(){
		countDownToLaser--;
		if(countDownToLaser==0) laser=true;
		int desired=getDirection(x,y,host.hero.x,host.hero.y);
		handDirection=(int)(converge(handDirection,desired,handSpeed));
		if(handDirection==desired && laser && !fireing){
			fireing=true;
		}
		if(fireing && laserAlpha<255){
			laserAlpha+=30;
		}
		if(laserAlpha>=255){
			laserAlpha=0;
			fireing=false;
		}
		if(!shield){
			if(Math.random()<0.018)
				host.instances.add(new Plane(x,y,trueRadij/3,handDirection,host));
		}
		if(shield && healthShield<120){
			if(Math.random()<0.02)
				host.instances.add(new Bombardier(x,y,trueRadij/5,true,host));
		}
		if(orianna!=null && hasBall){
			if(Math.random()<0.035){
				orianna.possesed=false;
				hasBall=false;
				orianna.untouched=3;
				int dir1=orianna.stickDirection;
				int dir2=getDirection(orianna.x,orianna.y,host.hero.x,host.hero.y);
				if(dir1<0) dir1+=360; if(dir1>360) dir1-=360;
				if(dir2<0) dir2+=360; if(dir2>360) dir2-=360;
				if(dir2>=0 && dir2 <90 && dir1>180 && dir1<=360)dir2+=360;
				if(dir1>=0 && dir1 <90 && dir2>180 && dir2<=360)dir2-=360;
				if(Math.abs(dir1-dir2)<120){
					orianna.direction=dir2;
					orianna.mode=2;
				}
				else{
					orianna.direction=dir1+(int)(Math.random()*80)-40;
					orianna.mode=2;
				}
			}
		}
		rotate(0.4f,10);
		x+= dirX;
		y+= dirY;
		//handDirection+=handSpeed;
		if(handDirection>360)handDirection-=360;
		bounceWall();
		if(red<255)red+=20;
		if(red>255)red=255;
		if(redShield<255)redShield+=20;
		if(redShield>255)redShield=255;
		if(hit){
			if(shield){
				healthShield-=4;
				redShield=40;
			}
			else{
				health-=4;
				red=40;
			}
			hit=false;
		}
		if(shield && healthShield<10){
			if(orianna!=null){
				orianna.dead=true;
				orianna=null;
			}
			host.instances.add(new Health((int)(25*host.ratio),host));
			host.instances.add(new Health((int)(25*host.ratio),host));
			radij=(int)(radij*0.7);
			shield=false;
		}
		
		if(health<30) {
			host.maker.end();
			dead=true; 
		}
	}
	
	protected void draw(Canvas c, Paint p){
		if(shield){
			p.setColor(Color.argb(healthShield,255,redShield,redShield));
			p.setStyle(Paint.Style.STROKE);
			for(int i=0; i<4; i++){
				RectF r = new RectF(x-trueRadij/2, y-trueRadij/2, x+trueRadij/2, y+trueRadij/2);
				c.drawOval(r, p);
				trueRadij--;
			}
			trueRadij+=4;
		}
		Paint pp = new Paint();
		pp.setColor(Color.argb(health,255,red,red));
		pp.setStyle(Paint.Style.FILL);
		x1 = (float)(x+Math.cos(Math.toRadians(handDirection+90))*trueRadij*0.1);
		y1 = (float)(y+Math.sin(Math.toRadians(handDirection+90))*trueRadij*0.1);
		float x4 = (float)(x+Math.cos(Math.toRadians(handDirection-90))*trueRadij*0.1);
		float y4 = (float)(y+Math.sin(Math.toRadians(handDirection-90))*trueRadij*0.1);
		float x2 = (float)(x1+Math.cos(Math.toRadians(handDirection+25))*trueRadij*0.2);
		float y2 = (float)(y1+Math.sin(Math.toRadians(handDirection+25))*trueRadij*0.2);
		float x3 = (float)(x4+Math.cos(Math.toRadians(handDirection-25))*trueRadij*0.2);
		float y3 = (float)(y4+Math.sin(Math.toRadians(handDirection-25))*trueRadij*0.2);
		float x5 = (float)(x1+Math.cos(Math.toRadians(180+handDirection-45))*trueRadij*0.3);
		float y5 = (float)(y1+Math.sin(Math.toRadians(180+handDirection-45))*trueRadij*0.3);
		float x6 = (float)(x4+Math.cos(Math.toRadians(180+handDirection+45))*trueRadij*0.3);
		float y6 = (float)(y4+Math.sin(Math.toRadians(180+handDirection+45))*trueRadij*0.3);
		if(fireing){
			x7=(float)(x1+Math.cos(Math.toRadians(handDirection))*host.height*2);
			y7=(float)(y1+Math.sin(Math.toRadians(handDirection))*host.height*2);
			x8=(float)(x4+Math.cos(Math.toRadians(handDirection))*host.height*2);
			y8=(float)(y4+Math.sin(Math.toRadians(handDirection))*host.height*2);
			Paint ppp = new Paint();
			ppp.setColor(Color.argb(laserAlpha,255,255,255));
			Path path = new Path();
			path.setFillType(Path.FillType.EVEN_ODD);
			path.moveTo(x1,y1);
			path.lineTo(x7,y7);
			path.lineTo(x8,y8);
			path.lineTo(x4,y4);
			path.lineTo(x1,y1);
			path.close();
			c.drawPath(path, ppp);
		}
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(x1,y1);
		path.lineTo(x2,y2);
		path.lineTo(x3,y3);
		path.lineTo(x4,y4);
		path.lineTo(x6,y6);
		path.lineTo(x5,y5);
		path.lineTo(x1,y1);
		path.close();
		c.drawPath(path, pp);
	}

}

class PrivateBall extends Instance{
	
	Boss2 master;
	boolean possesed;
	int stickDirection;
	int mode;
	int untouched;
	int undirected;
	int red;
	int health;
	
	PrivateBall(float x, float y, int radij, Boss2 master, Darkness host){
		super(x,y,radij,host);
		this.master=master;
		possesed=true;
		stickDirection=getDirection(master.x,master.y,x,y);
		speed=11*host.ratio;
		untouched=0;
		undirected=0;
		red=255;
		health=255;
	}
	
	void step(){
		if(red<255)red+=20;
		if(red>255)red=255;
		if(hit){
			health-=4;
			red=40;
			hit=false;
		}
		if(health<10){
			dead=true;
			master.orianna=null;
			host.instances.add(new Health((int)(25*host.ratio),host));
		}
		if(untouched>0) untouched--;
		if(undirected>0) undirected--;
		if(possesed){
			x=(float)(master.x+Math.cos(Math.toRadians(stickDirection))*(master.radij/2+radij/2));
			y=(float)(master.y+Math.sin(Math.toRadians(stickDirection))*(master.radij/2+radij/2));
		}
		else{
			x+=(float)(Math.cos(Math.toRadians(direction))*speed);
			y+=(float)(Math.sin(Math.toRadians(direction))*speed);
			if((x-radij/2<0 || y-radij/2<0 || x+radij/2>host.width || y+radij/2>host.height) && undirected==0){
				undirected=3;
				if(mode==2) {
					mode=1;
					direction=getDirection(x,y,host.hero.x,host.hero.y);
				}
				else if(mode==1){
					direction=getDirection(x,y,master.x,master.y);
				}
			}
			if(host.razdalja(x,y,master.x,master.y)<radij/2+master.radij/2 && untouched==0){
				possesed=true;
				mode=0;
				stickDirection= getDirection(master.x,master.y,x,y);
				master.hasBall=true;
			}
		}	
	}
	
	void draw(Canvas c, Paint p){
		p.setColor(Color.argb(health,255,red,red));
		p.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, p);
	}
	
}

class Bombardier extends Instance{
	
	boolean bomb;
	
	Bombardier(float x, float y, int radij,boolean bomb ,Darkness host){
		super(x,y,radij,host);
		this.bomb=bomb;
		this.speed=5;
		if(!bomb) speed=8;
		left=(int)(Math.random()*52)+30;
		direction=getDirection(x,y,host.hero.x,host.hero.y);
		if(!bomb){
			direction=(int)(Math.random()*360);
		}
	}
	
	void step(){
		left--;
		if(left==0){
			dead=true;
			if(bomb){
				for(int i=0; i<3; i++){
					host.instances.add(new Bombardier(x,y,radij/2,false,host));
				}
			}
		}
		x+=(float)(Math.cos(Math.toRadians(direction))*speed);
		y+=(float)(Math.sin(Math.toRadians(direction))*speed);
		deathWall();
	}
	
	void draw(Canvas c, Paint p){
		drawCircle(c,p);
	}
	
}

class Plane extends Instance{
	
	float dir;
	float odmik;
	boolean honing;
	float x1,y1,x2,y2,x3,y3;
	
	Plane(float x, float y, int radij, int direction, Darkness host){
		super(x,y,radij,host);
		this.dir=direction;
		honing=true;
		odmik=1.5f+(float)(Math.random()*3.5);
		speed=6;
		left=120;
	}
	
	void step(){
		if(honing){
			int desired=getDirection(x,y,host.hero.x,host.hero.y);
			dir=converge(dir,desired,odmik);
		}
		x+=(float)(Math.cos(Math.toRadians(dir))*speed);
		y+=(float)(Math.sin(Math.toRadians(dir))*speed);
		left--;
		if(left==0){
			honing=false;
			speed=11;
		}
		if(!honing){
			deathWall();
		}
	}
	
	void draw(Canvas c, Paint p){
		x1 = (float)(x+Math.cos(Math.toRadians(dir))*radij/2);
		y1 = (float)(y+Math.sin(Math.toRadians(dir))*radij/2);
		float x4 = (float)(x+Math.cos(Math.toRadians(dir+180+20))*radij/4);
		float y4 = (float)(y+Math.sin(Math.toRadians(dir+180+20))*radij/4);
		float x5 = (float)(x+Math.cos(Math.toRadians(dir+180-20))*radij/4);
		float y5 = (float)(y+Math.sin(Math.toRadians(dir+180-20))*radij/4);
		x2 = (float)(x+Math.cos(Math.toRadians(dir+180+60))*radij/2);
		y2 = (float)(y+Math.sin(Math.toRadians(dir+180+60))*radij/2);
		x3 = (float)(x+Math.cos(Math.toRadians(dir+180-60))*radij/2);
		y3 = (float)(y+Math.sin(Math.toRadians(dir+180-60))*radij/2);
		float x6 = (float)(x+Math.cos(Math.toRadians(dir+180+40))*radij/6);
		float y6 = (float)(y+Math.sin(Math.toRadians(dir+180+40))*radij/6);
		float x7 = (float)(x+Math.cos(Math.toRadians(dir+180-40))*radij/6);
		float y7 = (float)(y+Math.sin(Math.toRadians(dir+180-40))*radij/6);
		p.setColor(Color.WHITE);
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(x1,y1);
		path.lineTo(x2,y2);
		path.lineTo(x6,y6);
		path.lineTo(x4,y4);
		path.lineTo(x5,y5);
		path.lineTo(x7,y7);
		path.lineTo(x3,y3);
		path.lineTo(x1,y1);
		path.close();
		c.drawPath(path, p);
	}
	
}

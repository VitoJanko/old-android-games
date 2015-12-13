package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class Instance {
	boolean dead;
	boolean enemy;
	boolean persistant;
	boolean onlyAlt;
	boolean immune;  //ga zid ne ubije
	int harmDone; //2 Newtonov zakon :)
	boolean hit; //vstreljen
	int demageDealt; //kolko zbije
	boolean solid; //nemore cez zid
	float x;
	float y;
	int radij;
	Darkness host;
	
	//optional
	float speed;
	int direction;
	int left;
	float dirX;
	float dirY;
	
	Instance(float x, float y, int radij, Darkness host){
		this.x = x;
		this.y = y;
		this.radij=radij;
		this.host=host;
		onlyAlt=false;
		dead = false;
		enemy = true;
		persistant = false;
		immune=false;
		harmDone=0;
		demageDealt=3;
		hit=false;
		solid=false;
	}
	void step(){};
	void draw(Canvas c, Paint p){};
	
	boolean altCollision(int x, int y, int radij){
		return false;
	}
	
	void drawCircle(Canvas c, Paint p){
		p.setColor(Color.WHITE);
		p.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, p);
	}
	
	void drawSquare(Canvas c, Paint p){
		p.setColor(Color.WHITE);
		p.setStyle(Paint.Style.STROKE);
		c.drawRect((int)x-radij/2,(int)y-radij/2,(int)x+radij/2,(int)y+radij/2,p);
	}
	
	void rotate(float chanse, float ammount){
		if(Math.random()<chanse){
			direction+=(int)(Math.random()*2*ammount-ammount);
		}
		if(direction<0)direction=360+direction;
		if(direction>360) direction=direction-360;
		dirX = speed*(float)Math.cos(Math.toRadians(direction));
		dirY = speed*(float)Math.sin(Math.toRadians(direction));
	}
	
	float converge(float oldDir, int desired, float ammount){
		if(oldDir<0) oldDir+=360; if(oldDir>360) oldDir-=360;
		if(desired<0) desired+=360; if(desired>360) desired-=360;
		if(desired>=0 && desired <90 && oldDir>180 && oldDir<=360)desired+=360;
		if(oldDir>=0 && oldDir <90 && desired>180 && desired<=360)desired-=360;
		if(Math.abs(oldDir-desired)<ammount) oldDir=desired;
		else if(oldDir>desired) oldDir-=ammount;
		else oldDir+=ammount;
		return oldDir;
	}
	
	void bounce(){
		if(x+radij/2+dirX>host.width && dirX>0){dirX=-dirX; direction+=180;}
		if(x-radij/2+dirX<0 && dirX<0){dirX=-dirX; direction+=180;}
		if(y+radij/2+dirY>host.height && dirY>0){dirY=-dirY; direction+=180;}
		if(y-radij/2+dirY<0 && dirY<0){dirY=-dirY; direction+=180;}
	}
	
	void deathWall(){
		if(x+dirX>host.width+radij/2 || x+dirX<-radij/2) dead=true;
		if(y+dirY>host.height-radij/2 || y+dirY<-radij/2) dead=true;
	}
	
	void bounceWall(){
		if(x-radij/2<0 || y-radij/2<0 || x+radij/2>host.width || y+radij/2>host.height){
			double diffY = (host.height/2.0-y);
			double diffX = (host.width/2.0-x);
			double k = diffY /diffX;
			direction=(int)(Math.toDegrees(Math.atan(k)));
			if(diffX<0 && diffY>0) direction=180+direction;
			if(diffX<0 && diffY<0) direction=180+direction;
			direction+=(int)(Math.random()*80)-40;
		}
	}
	
	void follow(float pointX, float pointY){
		float vectorX = pointX-x;
		float vectorY = pointY-y;
		float norma = (float)Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY,2));
		dirX = (vectorX*speed)/norma;
		dirY = (vectorY*speed)/norma;
		x+= dirX;
		y+= dirY;
	}
	
	void drawTriangle(float pointX, float pointY,Canvas c,Paint p, int color){
		float vectorX = pointX-x;
		float vectorY = pointY-y;
		float norma = (float)Math.sqrt(Math.pow(vectorX,2)+Math.pow(vectorY,2));
		float x1 = (vectorX*radij/2)/norma;
		float  y1 = (vectorY*radij/2)/norma;
		float x2 = (float)(x1*Math.cos(2*Math.PI/3)-y1*Math.sin(2*Math.PI/3));
		float y2 = (float)(x1*Math.sin(2*Math.PI/3)+y1*Math.cos(2*Math.PI/3));
		float x3 = (float)(x1*Math.cos(4*Math.PI/3)-y1*Math.sin(4*Math.PI/3));
		float y3 = (float)(x1*Math.sin(4*Math.PI/3)+y1*Math.cos(4*Math.PI/3));
		p.setColor(color);
		p.setStyle(Paint.Style.FILL);
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(x+x1,y+y1);
		path.lineTo(x+x2, y+y2);
		path.lineTo(x+x3, y+y3);
		path.lineTo(x+x1, y+y1);
		path.close();
		c.drawPath(path, p);
	}
	
	boolean localCollision(int xC, int yC, int radij, float x1, float y1, float x2, float y2){
		int r = radij/2;
		if(xC+r> Math.min(x1,x2)&&xC-r<Math.max(x1,x2)&&yC+r> Math.min(y1,y2)&&yC-r<Math.max(y1,y2)){ 
			float k = (y2-y1)/(x2-x1);
			float n= (y1-yC)-k*(x1-xC);
			float eval = k*k*r*r+r*r-n*n;
			if(eval>=0)
				return true;
		}
		return false;
	}
	
	int getDirection(float fromX, float fromY, float targetX, float targetY){
		double diffY = targetY-fromY;
		double diffX = targetX-fromX;
		double k = diffY /diffX;
		int dir=(int)(Math.toDegrees(Math.atan(k)));
		if(diffX<0 && diffY>0) dir=180+dir;
		if(diffX<0 && diffY<0) dir=180+dir;
		return dir;
	}
	
	private boolean avoidHost(int x, int y){
		if(host.razdalja(x,y,host.hero.x,host.hero.y)<host.hero.radij*1.5)return false;
		for(int i=0; i<host.obstacle.size(); i++){
			Instance in = host.obstacle.get(i);
			if(in.altCollision((int)x, (int)y, radij)){
				return false;
			}	
		}
		return true;
	}
	
	void generateOutside(){
		if(Math.random()>0.5){
			int random = (int)(Math.random()*host.height);
			y = random;
			if(Math.random()>0.5) x = 0 - 2*radij;
			else x=host.width+2*radij;
		}
		else{
			int random = (int)(Math.random()*host.width);
			x=random;
			if(Math.random()>0.5) y = 0 - 2*radij;
			else y=host.height+2*radij;
		}
	}
	
	protected void generateRandom(){
		x =(int)(Math.random()*host.width);
		y =(int)(Math.random()*host.height);
		while(!avoidHost((int)x,(int)y)){
			x =(int)(Math.random()*host.width);
			y =(int)(Math.random()*host.height);
		}
	}
}

package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class Slime extends Instance{
	
	static int number;
	static int holdBack;
	
	boolean flying;
	int cycle;
	Slime brother;
	int alpha;
	int gray;
	
	Slime(int radij, Darkness host){
		super(0,0,radij,host);
		generateRandom();
		direction= (int)(Math.random()*360);
		speed = 2*host.ratio;
		flying=false;
		cycle=(int)(Math.random()*30);
		left=90;
		brother=null;
		number++;
		alpha=(int)(Math.random()*175)+80;
		gray=(int)(Math.random()*100)+100;
	}

	void step(){
		rotate(0.4f,10);
		x+= dirX;
		y+= dirY;
		left--;
		if(left==0 && !flying) {
			flying=true;
			speed=7*host.ratio;
			direction=getDirection(x,y,host.hero.x,host.hero.y);
		}
		cycle--;
		if(cycle==0 && !flying){
			brother = new Slime(radij,host);
			host.instances.add(brother);
			brother.x=x;
			brother.y=y;
		}
		if(brother!=null){
			if(host.razdalja(x,y,brother.x,brother.y)>3*radij){
				brother=null;
				cycle=(int)(20+Math.random()*5*(number+holdBack));
			}
		}
		if(flying)
			deathWall();
		else bounceWall();
		if(dead) number--;
	}
	
	void draw(Canvas c, Paint p){
		Paint pp = new Paint();
		pp.setColor(Color.argb(alpha, gray,gray,gray));
		pp.setStyle(Paint.Style.FILL);
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		c.drawOval(r, pp);
		if(brother!= null && !brother.dead && host.razdalja(x,y,brother.x,brother.y)>radij){
			pp.setStyle(Paint.Style.FILL);
			Path path = new Path();
			path.setFillType(Path.FillType.EVEN_ODD);
			int dir = getDirection(x,y,brother.x,brother.y)+90;
			int xStart= (int)(x+radij/2*Math.cos(Math.toRadians(dir)));
			int yStart= (int)(y+radij/2*Math.sin(Math.toRadians(dir)));
			int xStartB= (int)(brother.x+radij/2*Math.cos(Math.toRadians(dir)));
			int yStartB= (int)(brother.y+radij/2*Math.sin(Math.toRadians(dir)));
			dir+=180;
			int xNext= (int)(x+radij/2*Math.cos(Math.toRadians(dir)));
			int yNext= (int)(y+radij/2*Math.sin(Math.toRadians(dir)));
			int xNextB= (int)(brother.x+radij/2*Math.cos(Math.toRadians(dir)));
			int yNextB= (int)(brother.y+radij/2*Math.sin(Math.toRadians(dir)));
			path.moveTo(xStart,yStart);
			float middleX=x+(brother.x-x)/2;
			float middleY=y+(brother.y-y)/2;
			path.lineTo(xNext, yNext);
			path.cubicTo(middleX,middleY,middleX,middleY,xNextB,yNextB);
			path.lineTo(xStartB, yStartB);
			path.cubicTo(middleX,middleY,middleX,middleY,xStart,yStart);
			path.close();
			c.drawPath(path, pp);
		}
	}
	
}

package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class LevelSelect {
	int x;
	int y;
	int radij;
	int number;
	int color;
	int bestRun;
	int progress;
	String name;
	Menu host;
	
	LevelSelect(int x, int y, int radij, int number, int color, String name, Menu host){
		this.x=x;
		this.y=y;
		this.radij=radij;
		this.number=number;
		this.color=Color.rgb(Color.red(color)/2,Color.green(color)/2,Color.blue(color)/2);
		this.name=name;
		this.host=host;
		bestRun=0;
		progress=0;
	}
	
	protected void draw(Canvas c, Paint p){
		RectF r = new RectF(x-radij/2, y-radij/2, x+radij/2, y+radij/2);
		p.setColor(color);
		p.setStyle(Paint.Style.FILL);
		c.drawOval(r, p);
		p.setColor(Color.WHITE);
		p.setStyle(Paint.Style.STROKE);
		c.drawOval(r, p);
		p.setTextSize(host.getResources().getDimension(R.dimen.menu));
		if(bestRun!=0)
			if(number!=host.lvlMax*5)
				c.drawText(name+"   ("+bestRun+"%)",x+radij,y,p);
			else{
				c.drawText(name,x+radij,y,p);
				c.drawText("    Record:  "+bestRun+" seconds",x+radij,y+(int)(radij*0.8),p);
			}
		else c.drawText(name+"",x+radij,y,p);
	}
}

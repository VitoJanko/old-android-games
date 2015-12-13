package com.example.huntedseas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class MenuChoice {
	int x1;
	int x2;
	int y1;
	int y2;
	String text;
	int level;
	boolean active;
	
	public MenuChoice(int x1, int y1, int x2, int y2, String text, int level, boolean active) {
		super();
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.text = text;
		this.level = level;
		this.active=active;
	}
	
	protected void draw(Canvas c, Paint p){
		p.setColor(Color.BLACK);
		p.setStrokeWidth(5);
		p.setStyle(Paint.Style.STROKE);
		
		c.drawRoundRect(new RectF(x1,y1,x2,y2), 10, 10, p);
		if (!active){
			p.setColor(Color.RED);
			c.drawLine(x1,y1,x2,y2,p);
			c.drawLine(x1,y2,x2,y1,p);
		}
		else{
			Paint pp = new Paint();
			pp.setTextSize(16);
			c.drawText(text,x1+(x2-x1)/8,(y2+y1)/2,pp);
		}
	}

}

package com.above_average;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Health extends Instance{
	
	int colorNumber;
	float ratioX;
	float ratioY;
	int demage;
	int color;
	
	Health (int radij, Darkness d){
		super(0,0,radij,d);
		generateRandom();
		ratioX = (float)(360.0/d.width);
		ratioY = (float)(1.0/d.height);
		enemy = false;
		color=makeColor();
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
		Protagonist p = host.hero;
		if(host.razdalja(x,y,p.x,p.y)<p.radij/2+radij/2){
			dead=true;
			p.charges+=7;
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

package com.example.huntedseas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class HUD{
	
	Sea host;
	
	HUD(Sea host){
		this.host=host;

	}
	
	protected void step(){
		
	}
	

	
	protected void draw(Canvas c, Paint p){
		p.setColor(Color.BLACK);
	    //c.drawLine(0,host.realHeight-host.wallDown+host.premikY,host.width,host.realHeight-host.wallDown+host.premikY,p);
		p.setTextSize(28);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		c.drawText(host.hero.food+"/25",host.width-140, 40, p);
		if(host.level==4){
			c.drawText("Time: "+(int)host.maker.timer,host.width-140, 70, p);
		}
		//c.drawText("Level: "+host.level,host.width-110, 70, p);
		//c.drawText("x: "+host.hero.x,host.width-110, 100, p);
		//c.drawText("y "+host.hero.y,host.width-110, 130, p);
		//c.drawText("y "+host.hero.y,host.width-110, 130, p);

	}

}

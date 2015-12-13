package com.above_average;

import java.util.ArrayList;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Texter extends Instance{
	ArrayList<Timer> que;
	ArrayList<Timer> display;
	
	Texter(Darkness host){
		super(0,0,0,host);
		enemy=false;
		persistant = true;
		que = new ArrayList<Timer>();
		display = new ArrayList<Timer>();
	}
	
	void addQue(String text, int delay, int remaining){
		que.add(new Timer(text,delay,remaining,host.width,host.height,host.paint));
	}
	
	void addNow(String text, int remaining){
		display.add(new Timer(text,0,remaining,host.width,host.height,host.paint));
		if(display.size()>8)display.remove(0);
	}
	
	void reset(){
		que = new ArrayList<Timer>();
	}
	
	void step(){
		if(!que.isEmpty()){
			Timer t = que.get(0);
			t.delay--;
			if(t.delay==0){
				display.add(t);
				que.remove(0);
			}
		}
		for(int i=0; i<display.size(); i++){
			Timer t = display.get(i);
			t.remaining--;
			if(t.remaining==0){
				display.remove(i);
				i--;
			}
		}
	}
	
	void draw(Canvas c, Paint p){
		if(host.gameState!=host.GAME){
			Paint pp = new Paint();
			pp.setColor(Color.argb(90,0,0,0));
			pp.setStyle(Paint.Style.FILL);
			c.drawRect(0,0,host.width,host.height,pp);
		}
		
		p.setColor(Color.WHITE);
		//p.setTextSize(21);

		p.setTextSize(host.getResources().getDimension(R.dimen.message));
		for(int i=0; i<display.size(); i++){
			Timer t = display.get(i);
			c.drawText(t.text,t.x,t.y,p);
		}
		if(host.gameState==host.PAUSE){
			if(host.stage==1){
				if(Math.abs(host.main.angleX)<3 && Math.abs(host.main.angleY)<3){
					String text = "Press anywhere to continue";
					c.drawText(text,(host.width-p.measureText(text))/2,host.height/7,p);
				}
				else{
					String text = "Place your phone horizontaly";
					c.drawText(text,(host.width-p.measureText(text))/2,host.height/7,p);
					text = "and than press anywhere to continue";
					c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				}
			}
			else{
				c.drawText("PAUSED: Click on the dot to resume",host.width/6,host.height/7,p);
			}
		}
		else if(host.won){
			String text = "VICTORY";
			c.drawText(text,(host.width-p.measureText(text))/2,host.height/7,p);
			text = "Press the screen to finish";
			c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
		}
		else if(host.gameState==host.LOST){
			if(display.size()!=0)
				display=new ArrayList<Timer>();
			String text = "GAME OVER";
			c.drawText(text,(host.width-p.measureText(text))/2,host.height/7,p);
			if(host.stage==2){
				text = "They want you to fail, ";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "because they don't want to be alone";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
			}
			if(host.stage==3){
				text = "Why struggle and suffer?, ";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "...when you can sleep";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
			}
//			if(host.stage==4){
//				text = "You lost";
//				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
//				text = "just like everyone...";
//				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
//			}
			if(host.stage==4){
				text = "Better give up,";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "some are not meant to win";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
			}
			if(host.stage==5){
				text = "Try again, ";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "fail better";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
			}
			if(host.stage==6){
				text = "Quit now, ";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "and no one will remeber you";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
			}
			if(host.stage==7){
				text = "Fall seven times, ";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "get up eight!";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
			}
			if(host.stage==8){
				text = "You tried so hard, you got so far, ";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "but in the end...";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
			}
			if(host.stage==9){
				text = "They never gave you a choice, ";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "you never took it";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
			}
			if(host.stage==10){
				text = "Nobody made it";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "...you are nobody";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
			}
			if(host.stage==11){
				text = "Atleast you tried";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "...time to go home...";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
			}
			if(host.stage==12){
				text = "So it ends...";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
			}
			if(host.stage==13){
				text = "Tip 1: move slowly";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "Tip 2: stop as you get a firefly";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
			}
			if(host.stage==14){
				text = "You came out second,";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "in a two player game...";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
			}
			if(host.stage==15){
				text = "No one survives life, ";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+40,p);
				text = "so why bother trying?";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+70,p);
				text = "Time: "+host.maker.time/33+" seconds";
				c.drawText(text,(host.width-p.measureText(text))/2,host.height/7+110,p);
			}
		}
	}
}

class Timer{
	String text;
	int delay;
	int remaining;
	int x;
	int y;
	Timer(String text, int delay, int remaining, int width, int height, Paint p){
		this.text=text;
		this.delay=delay;
		this.remaining=remaining;
		x = Math.max(0,(int)(Math.random()*(width-p.measureText(text)*2)));
		//x = (int)(width-p.measureText(text)*1.5);
		y = (int)(Math.random()*height*0.66)+ (int)(height*0.2);
	}
}
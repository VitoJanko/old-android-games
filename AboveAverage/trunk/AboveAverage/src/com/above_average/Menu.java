package com.above_average;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

public class Menu extends View{

	int height;
	int width;
	Bitmap bitmap;
	Canvas canvas;
	Paint paint;
	boolean patch1;
	int unlocked;
	int[] score;
	boolean stopRefresh;
	ArrayList<LevelSelect> list;
	MainMenu host;
	boolean started;
	int endY;
	int needToMove;
	boolean leftArrow;
	boolean rightArrow;
	int page;
	int lvlMax;
	boolean pressed;
	float pressX;
	
	Bitmap sound;
	Bitmap noSound;
	
	private RefreshHandler redrawHandler = new RefreshHandler(this);
	
	public void destroy(){
		bitmap = null;
		canvas = null;
		paint = null;
		score = null;
		list = null;
		host = null;	
		Bitmap sound = null;
		Bitmap noSound = null;
		redrawHandler = null;
	}
	
	/**
	 * 
	 * @author Jani
	 * To je treba poštudirat zakaj je tako prou. Ampak to je pravilno in naj
	 * ne bi povzroèalo memory leaka.
	 */
	static class RefreshHandler extends Handler{
		WeakReference<Menu> mview;
		
		RefreshHandler(Menu aFragment){
			mview = new WeakReference<Menu>(aFragment); // I have no idea what I'm doing. (slika od unga psa)
		}
		
		@Override
		public void handleMessage(Message msg){
			Menu mmview = mview.get();
			mmview.update();
			mmview.invalidate();
		}
		
		public void sleep(long delayMillis){
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0),delayMillis);
		}
	};
	
	protected void update(){
		if(page<lvlMax && unlocked>page*5) rightArrow=true; else rightArrow=false;
		if(page>1)leftArrow=true; else leftArrow=false;
		if(needToMove>0){
			needToMove-=width/8;
			for(int i=0; i<list.size(); i++){
				list.get(i).x-=width/8;
			}
			if(needToMove<0) needToMove=0;
		}
		if(needToMove<0){
			needToMove+=width/8;
			for(int i=0; i<list.size(); i++){
				list.get(i).x+=width/8;
			}
			if(needToMove>0) needToMove=0;
		}
		drawAll();
		
		if(MainMenu.sound){
			canvas.drawBitmap(sound, 15,15, paint);
		}else{
			canvas.drawBitmap(noSound, 15,15, paint);
		}
		
		if(!stopRefresh){
			redrawHandler.sleep(30);
		}
	}
	
	protected void drawAll(){
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		Rect r = new Rect(0,0,width,height);
		canvas.drawRect(r,paint);
		paint.setColor(Color.WHITE);
		paint.setTextSize(getResources().getDimension(R.dimen.title));
		String text = "Above Average";
		canvas.drawText(text,(width-paint.measureText(text))/2,height/7,paint);
		for(int i=0; i<list.size(); i++){
			list.get(i).draw(canvas,paint);
		}
		if(rightArrow){
			int startY=endY-height/14;
			int space=height-startY;
			int startX=(int)(width*3.0/4.0);
			int endX=(int)(width*5.0/6.0);
			canvas.drawLine(startX,startY+space/2,endX,startY+space/2,paint);
			canvas.drawLine(startX,startY+space/2+space/8,endX,startY+space/2+space/8,paint);
			canvas.drawLine(startX,startY+space/2,startX,startY+space/2+space/8,paint);
			canvas.drawLine(endX,startY+space/2,endX,startY+space/2-space/8,paint);
			canvas.drawLine(endX,startY+space/2+space/8,endX,startY+space/2+2*space/8,paint);
			canvas.drawLine(endX,startY+space/2-space/8,endX+width/12,startY+space/2+space/16,paint);
			canvas.drawLine(endX,startY+space/2+2*space/8,endX+width/12,startY+space/2+space/16,paint);
		}
		if(leftArrow){
			int startY=endY-height/14;
			int space=height-startY;
			int startX=(int)(width/4.0);
			int endX=(int)(width/6.0);
			canvas.drawLine(startX,startY+space/2,endX,startY+space/2,paint);
			canvas.drawLine(startX,startY+space/2+space/8,endX,startY+space/2+space/8,paint);
			canvas.drawLine(startX,startY+space/2,startX,startY+space/2+space/8,paint);
			canvas.drawLine(endX,startY+space/2,endX,startY+space/2-space/8,paint);
			canvas.drawLine(endX,startY+space/2+space/8,endX,startY+space/2+2*space/8,paint);
			canvas.drawLine(endX,startY+space/2-space/8,endX-width/12,startY+space/2+space/16,paint);
			canvas.drawLine(endX,startY+space/2+2*space/8,endX-width/12,startY+space/2+space/16,paint);
		}
	}
	
	public Menu(Context context, MainMenu host) {
		super(context);
		this.host=host;
		score = new int[16];
		started=false;
		this.setOnTouchListener(touch);
		endY=0;
		needToMove=0;
		page=1;
		leftArrow=true;
		rightArrow=true;
		lvlMax=3;
		sound = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sound);
		noSound = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.nosound);
	}
	
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    	height = View.MeasureSpec.getSize(heightMeasureSpec);
	        width = View.MeasureSpec.getSize(widthMeasureSpec);
	        setMeasuredDimension(width,height);
	        paint = new Paint();
	        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	        canvas = new Canvas(bitmap);
	        createSelector();
	        started=true;
	        //stopRefresh=false;
	        
	    }
	
	 void createSelector(){
		 list = new ArrayList<LevelSelect>();
		 int radij = height/14;
		 int startY=radij*5;
		 if(unlocked>=1){list.add(new LevelSelect(width/6,startY,radij,1,Color.BLUE,"Tutorial",this)); startY+=radij*1.5;}
		 if(unlocked>=2){list.add(new LevelSelect(width/5,startY,radij,2,Color.BLUE,"Pilot",this)); startY+=radij*1.5;}
		 if(unlocked>=3){list.add(new LevelSelect(width/7,startY,radij,3,Color.RED,"Exile",this)); startY+=radij*1.5;}
		 if(unlocked>=4){list.add(new LevelSelect(width/6,startY,radij,4,Color.BLUE,"Hunter's net",this)); startY+=radij*1.5;}
		 if(unlocked>=5){list.add(new LevelSelect(width/4,startY,radij,5,Color.BLUE,"Big, bad and evil",this)); startY+=radij*1.5;}
		 endY=startY;
		 startY=radij*5;
		 if(unlocked>=6){list.add(new LevelSelect(width/6,startY,radij,6,Color.GREEN,"New Hope",this)); startY+=radij*1.5;}
		 if(unlocked>=7){list.add(new LevelSelect(width/5,startY,radij,7,Color.GRAY,"Hive",this)); startY+=radij*1.5;}
		 if(unlocked>=8){list.add(new LevelSelect(width/7,startY,radij,8,Color.GREEN,"Arena",this)); startY+=radij*1.5;}
		 if(unlocked>=9){list.add(new LevelSelect(width/6,startY,radij,9,Color.GRAY,"Alone in the dark",this)); startY+=radij*1.5;}
		 if(unlocked>=10){list.add(new LevelSelect(width/4,startY,radij,10,Color.RED,"Apocalypse",this)); startY+=radij*1.5;}
		 startY=radij*5;
		 if(unlocked>=11){list.add(new LevelSelect(width/6,startY,radij,11,Color.rgb(255,128,0),"Wild west",this)); startY+=radij*1.5;}
		 if(unlocked>=12){list.add(new LevelSelect(width/5,startY,radij,12,Color.GRAY,"Booby trap",this)); startY+=radij*1.5;}
		 if(unlocked>=13){list.add(new LevelSelect(width/7,startY,radij,13,Color.GRAY,"Maze",this)); startY+=radij*1.5;}
		 if(unlocked>=14){list.add(new LevelSelect(width/6,startY,radij,14,Color.rgb(255,128,0),"Duel",this)); startY+=radij*1.5;}
		 if(unlocked>=15){list.add(new LevelSelect(width/4,startY,radij,15,Color.WHITE,"Until the bitter end",this)); startY+=radij*1.5;}
		 for(int i=0; i<list.size(); i++){
			 list.get(i).bestRun=score[i];
		 }
		 for(int i=0; i<list.size(); i++){
			 int difference= (i/5+1)-page;
			 list.get(i).x+=difference*width;
		 }
		 update();
	 }
	
	 boolean inBox(LevelSelect in, MotionEvent m){
		boolean collide=false;
		if(m.getX()>in.x-in.radij && m.getX()<in.x+2*in.radij+paint.measureText(in.name))
			if(m.getY()>in.y-in.radij*0.6 && m.getY()<in.y+in.radij*0.6)
				collide=true;
		return collide; 
	 }
	 
	 void preveriNaprej(MotionEvent m){
		 int startY=endY-height/14;
		 int space=height-startY;
		 if(m.getX()>=(int)(width*3.0/4.0) && m.getX()<=(int)(width*5.0/6.0)+width/12){
			 if(m.getY()>=startY+space/2-space/8 && m.getY()<=startY+space/2+2*space/8){
				 needToMove=width;
				 pressed=false;
				 page++;
			 }
		 }
	 }
	 
	 void preveriNazaj(MotionEvent m){
		 int startY=endY-height/14;
		 int space=height-startY;
		 if(m.getX()>=(int)(width/6.0) && m.getX()<=(int)(width/4.0)+width/12){
			 if(m.getY()>=startY+space/2-space/8 && m.getY()<=startY+space/2+2*space/8){
				 needToMove=-width;
				 pressed=false;
				 page--;
			 }
		 }
	 }
	 
	 private OnTouchListener touch = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent m) {
				if(m.getAction()==MotionEvent.ACTION_DOWN){
					LevelSelect izbrani=null;
					for(int i=0; i<list.size(); i++){
						LevelSelect in = list.get(i);
//						if(Math.sqrt(Math.pow(in.x-m.getX(),2)+Math.pow(in.y-m.getY(),2))<in.radij/2){
						if(inBox(in,m)){
							izbrani=in;
							break;
						}
					}
					if(izbrani!=null){
						host.zamenjajActivity(izbrani.number);
					}
					pressed=true;
					pressX=m.getX();
					if(rightArrow)
						preveriNaprej(m);
					if(leftArrow)
						preveriNazaj(m);
				}
				if(m.getAction()==MotionEvent.ACTION_UP){
					pressed=false;
					if(m.getX() < 70 && m.getY() < 70){
						if(MainMenu.sound) {
							MainMenu.sound = false;
							host.muzikaStop();
						}
						else{ 
							MainMenu.sound = true;
							host.muzikaPlay();
						}
					}
				}
				if(pressed){
					float diffX=pressX-m.getX();
					if(rightArrow && diffX>0 && needToMove==0){
						needToMove=width;
						page++;
						pressed=false;
					}
					if(leftArrow && diffX<0 && needToMove==0){
						 needToMove=-width;
						 page--;
						 pressed=false;
					}
				}
				return true;
			}
			
		};
	 
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

}

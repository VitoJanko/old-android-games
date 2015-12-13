package com.example.huntedseas;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO Put here a description of what this class does.
 *
 * @author Jani.
 *         Created 27. okt. 2012.
 */
public class MenuView extends View {
	private int height;
	private int width;
	private Paint paint;
	private Bitmap bitmap;
	private Canvas canvas;
	private MenuActivity menuActivity;
	ArrayList<MenuChoice> choice;
	
	private boolean stopUpdate;
	
	private RefreshHandler redrawHandler = new RefreshHandler(this);
	
	
	public MenuView(Context context,MenuActivity menuActivity){
		super(context);
		choice = new ArrayList<MenuChoice>();
		this.menuActivity = menuActivity;
		this.setOnTouchListener(touch);
		
	}
	
	protected void makeMenu(){
		int cWidth=width/6;
		int cHeight=height/5;
		int x1 = width/8;
		int x2 = x1+cWidth+width/24;
		int x3 = x2+cWidth+width/24;
		int x4 = x3+cWidth+width/24;
		int y1 = height/7;
		int y2 = y1+cHeight+width/24;
		int y3 = y2+cHeight+width/24;
		choice.add(new MenuChoice(x1,y2,x1+cWidth,y2+cHeight,"Warm welcome",1,true));
		choice.add(new MenuChoice(x2,y2,x2+cWidth,y2+cHeight,"School of fish",2,true));
		choice.add(new MenuChoice(x3,y2,x3+cWidth,y2+cHeight,"Narrow passage",3,true));
		choice.add(new MenuChoice(x2,y1,x2+cWidth,y1+cHeight,"Hunting season",4,true));
		choice.add(new MenuChoice(x2,y3,x2+cWidth,y3+cHeight,"In nick of time",5,false));
		choice.add(new MenuChoice(x4,y2,x4+cWidth,y2+cHeight,"Lair",6,false));
	}
	
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    	height = View.MeasureSpec.getSize(heightMeasureSpec);
	        width = View.MeasureSpec.getSize(widthMeasureSpec);
	        setMeasuredDimension(width,height);
	        paint = new Paint();
	        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	        canvas = new Canvas(bitmap);
	        
	        stopUpdate = false;
	        makeMenu();
	        update();
	    }
	
	 protected void destroy(){
		 stopUpdate=true;
		 //paint=null;
		 //bitmap = null;
		 //canvas = null;
		 //menuActivity=null;
	 }
	 
	 private void draw(){
		 paint.setColor(Color.BLUE);
		 paint.setStyle(Paint.Style.FILL);
		 canvas.drawRect(0,0,width,height, paint);
		 paint.setTextSize(30);
		 paint.setColor(Color.BLACK);
		 canvas.drawText("Hunted Seas", width/3,height/9,paint);
		 
		 for (int i=0; i<choice.size(); i++)
			 choice.get(i).draw(canvas,paint);
		 
	 }
	 
	 private void update(){
		draw();
		
		if(!stopUpdate)
			redrawHandler.sleep(30);
	 }
	 
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap,0,0,paint);
	}
	
	 private OnTouchListener touch = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent m) {
				if(m.getAction()==MotionEvent.ACTION_DOWN){
					float y = m.getY();
					float x = m.getX();
					for (int i=0; i<choice.size(); i++){
						MenuChoice c = choice.get(i);
						if (x>=c.x1 && x<=c.x2 && y>=c.y1 && y<=c.y2){
							menuActivity.zamenjajActivity(c.level);
							break;
						}
					}				
				}
				if(m.getAction()==MotionEvent.ACTION_UP){
					//TODO
				}
				return true;
			}
			
		};
	
	static class RefreshHandler extends Handler{
		WeakReference<MenuView> mview;
		
		RefreshHandler(MenuView aFragment){
			mview = new WeakReference<MenuView>(aFragment); // I have no idea what I'm doing. (slika od unga psa)
		}
		
		@Override
		public void handleMessage(Message msg){
			MenuView mmview = mview.get();
			mmview.update();
			mmview.invalidate();
		}
		
		public void sleep(long delayMillis){
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0),delayMillis);
		}
	};
	
}

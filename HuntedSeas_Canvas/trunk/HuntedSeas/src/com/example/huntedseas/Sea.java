package com.example.huntedseas;


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

public class Sea extends View{
	
	int useless=0;
	
	final int EMPTY=0;
	final int NORMAL=1;
	final int CORRIDOR =2;
	final int SURVIVAL=3;
	final int TIMER = 4;
	final int BOSS = 5;
	
	HUD hud;
	
	int gameMode = NORMAL;
	int level = MenuActivity.selectedLvL;
	int world =1;
	LevelMaker maker;
	
	float nagib=4;
	
	int width;
	int height;
	int realWidth;
	int realHeight;
	int marginX;
	int marginY;
	int wallX;
	int wallUp;
	int wallDown;
	
	float angleX;
	float angleY;
	float angleZ;
	
	Paint paint;
	Bitmap bitmap;
	Canvas canvas;
	References loader;
	
	private RefreshHandler redrawHandler = new RefreshHandler(this);
	boolean started;
	boolean ready;
	boolean stopUpdate;
	
	int premikX;
	int premikY;
	
	Protagonist hero;
	ArrayList<Instance> instances;
	ArrayList<Rect> obstacles;
	
	
	int framerate = 25;
	int framelength; //Se nastavi sam glede na prejsnjo vrednost
	long past;
	
	public Sea(Context context) {
	        super(context);
	        setKeepScreenOn(true);
			this.setOnTouchListener(touch);
			started=false;
	 }
	
	protected void destroy(){
		hud = null;
		maker = null;
		paint = null;
		bitmap = null;
		canvas = null;
		hero = null;
		instances = null;
		stopUpdate = true;
	}
	
	static class RefreshHandler extends Handler{
		WeakReference<Sea> mview;
		
		RefreshHandler(Sea aFragment){
			mview = new WeakReference<Sea>(aFragment); 
		}
		
		@Override
		public void handleMessage(Message msg){
			Sea mmview = mview.get();
			if (mmview!=null){
				mmview.update();
				mmview.invalidate();
			}
		}
		
		public void sleep(long delayMillis){
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0),delayMillis);
		}
	};
	
	protected void setState(){
		
	}
	
	protected double calibrateProb(double input){
		double output = (input*20.0)/framerate;
		return output;
	}
	
	protected float calibrate(float input){
		float output = (float)(input/50.0)*framelength;
		return output;
	}
	
	
	
	protected void update(){
		//Ozadje
		paint.setColor(Color.argb(255,50,50,230));
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(premikX,premikY,realWidth,realHeight,paint);
		//canvas.drawBitmap(ozadje, premikX,premikY, paint);

		maker.step();
		hud.draw(canvas, paint);
		
		//Action
		//System.out.print(instances.size());
		for(int i=0; i<instances.size(); i++){
			Instance in = instances.get(i);
			in.step();
			in.draw(canvas,paint);
			if(in.dead){
				in.host=null;
				//in.picture.recycle();
				instances.remove(i);
				i--;
			}
		}
		
		//Ospredje
		//canvas.drawBitmap(floor, premikX,premikY, paint);
		
		//Measure time
		long current=System.currentTimeMillis();
		long diff = (current-past);
		past = current;
		int delay = Math.max(0,framelength-(int)diff);
		if(diff>40){
			System.out.println("DAT LAG: "+(diff-40));
		}
		//System.out.println("diff: "+diff);
		//System.out.println("delay: "+delay);
		
		if(!stopUpdate)
			redrawHandler.sleep(delay);
	}
	
	protected Bitmap scale(Bitmap b, int width){
		int prevWidth = b.getWidth();
		int prevHeight = b.getHeight();
		int newHeight =(width*prevHeight)/prevWidth;
		return Bitmap.createScaledBitmap(b,width,newHeight, true);
	}
	
	protected Bitmap scale(Bitmap b, int width, int height){
		return Bitmap.createScaledBitmap(b,width,height, true);
	}
	
	protected void addInstance(Instance add){
		boolean inserted=false;
		for(int i=0; i<instances.size(); i++){
			Instance in = instances.get(i);
			if (in.depth<=add.depth){
				instances.add(i, add);
				inserted=true;
				break;
			}
		}
		if(!inserted){
			instances.add(add);
		}
	}
	
	protected void prepareField(){
		//Set time
		framelength = 1000/framerate;
		past = System.currentTimeMillis();
		//Make list and hero
		instances = new ArrayList<Instance>();
		obstacles = new ArrayList<Rect>();
		Bitmap blob = BitmapFactory.decodeResource(getResources(), R.drawable.blob);
		blob = scale(blob,140);
		hero = new Protagonist(width/2,(int)(realHeight*0.75),this,blob,3);
		addInstance(hero);
		hud = new HUD(this);
		//addInstance(hud);

		//Last prepartions
		loader = new References();
		maker =new LevelMaker(this);
		maker.inicialize();
		premikX=0;
		premikY=0;
		ready=true;
		
		update();
	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width=w;
        height=h;
		if(level==3){
			gameMode=CORRIDOR;
		}
        
        if(gameMode!=CORRIDOR){
	        realWidth=2*width;
	        realHeight=2*height;
	        marginX=width/3;
	    	marginY=height/3;
	    	wallX=width/20;
	    	wallUp=width/20;
	    	wallDown=width/12;
        }
        else{
	        realWidth=6*width;
	        realHeight=height;
	        marginX=width/3;
	    	marginY=height/3;
	    	wallX=width/20;
	    	wallUp=width/20;
	    	wallDown=width/16;
        }
        
        paint = new Paint();
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        if (started==false){
        	started=true;
        	prepareField();
        }
    }
	
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }
	
	
	 private OnTouchListener touch = new OnTouchListener() {
		 public boolean onTouch(View v, MotionEvent m) {
			 return true;
		 }
	 };
}

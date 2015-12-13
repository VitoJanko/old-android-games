package org.bizjak.android.GPU;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Sea extends GLSurfaceView{
	
	int useless=0;
	
	final int EMPTY=0;
	final int NORMAL=1;
	final int CORRIDOR =2;
	final int SURVIVAL=3;
	final int TIMER = 4;
	final int BOSS = 5;
	
	Jelly hero; 
	Background bg;
	Background fg;
	
	int gameMode = NORMAL;
	int level = 1;
	int world = 1;
	LevelMaker maker;
	
	float nagib=4;
	
	int width;
	int height;
	int xStart;
	int yStart;
	int xEnd;
	int yEnd;
	
	int marginX;
	int marginY;
	int wallX;
	int wallUp;
	int wallDown;
	
	float angleX;
	float angleY;
	float angleZ;
	
	
	private RefreshHandler redrawHandler = new RefreshHandler(this);
	boolean started;
	boolean ready;
	boolean stopUpdate;
	
	int premikX;
	int premikY;
	
	ArrayList<Instance> instances;
	ArrayList<Rect> obstacles;
	
	int framerate = 25;
	int framelength; //Se nastavi sam glede na prejsnjo vrednost
	long past;
	long timePassed;
	
	public Context context;
	final Render renderer;
	
	public Sea(Context context,AttributeSet attrs) {
	        super(context,attrs);
	        setKeepScreenOn(true);
			this.setOnTouchListener(touch);
			started=false;
			
			this.context = context;
			
			setEGLContextClientVersion(2);
			
			renderer = new Render(this);
			setRenderer(renderer);
	 }
	
	protected void destroy(){
		maker = null;
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
	
	protected void update(){
//		maker.step();

		
		long current=System.currentTimeMillis();
		timePassed = (current-past);
		past = current;
		
		maker.step();
		if(fg!=null)
			fg.step();
		for(int i=0; i<instances.size(); i++){
			Instance in = instances.get(i);
			in.step();
			if(in.dead){
				in.host=null;
				instances.remove(i);
				i--;
			}
		}


		long finish=System.currentTimeMillis();
		long dTime =(finish-current);
		
		int delay = Math.max(0,framelength-(int)dTime);
		if(timePassed>50){
			Log.d("Sea", "Around 147. Lag: "+(timePassed-50));
		}

		
		if(!stopUpdate)
			redrawHandler.sleep(delay);
	}
	
//	protected void addInstance(Instance add){
//		boolean inserted=false;
//		for(int i=0; i<instances.size(); i++){
//			Instance in = instances.get(i);
//			if (in.depth<=add.depth){
//				instances.add(i, add);
//				inserted=true;
//				break;
//			}
//		}
//		if(!inserted){
//			instances.add(add);
//		}
//	}
	
	protected void prepareField(){
		//Set time
		framelength = 1000/framerate;
		past = System.currentTimeMillis();
		//Make list and re
		instances = new ArrayList<Instance>();
		obstacles = new ArrayList<Rect>();
		
		maker = new LevelMaker(this);
		maker.initialize();
		premikX=0;
		premikY=0;
		ready=true;
		
		update();
	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //width=w;
        //height=h;
        if (started==false){
        	started=true;
        	prepareField();
        }
    }
	
	
	 private OnTouchListener touch = new OnTouchListener() {
		 public boolean onTouch(View v, MotionEvent m) {
			 return true;
		 }
	 };
}

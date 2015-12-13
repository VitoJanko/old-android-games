package com.above_average;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;


public class Darkness extends View{

	Protagonist hero;
	int height;
	int width;
	Bitmap bitmap;
	Canvas canvas;
	Paint paint;
	int gameState;
	final int PAUSE = 0;
	final int GAME = 1;
	final int LOST = 2;
	boolean won;
	boolean altWon;
	ArrayList<Instance> instances;
	ArrayList<Instance> obstacle;
	private RefreshHandler redrawHandler = new RefreshHandler(this);
	Texter texter;
	LevelMaker maker;
	int dolzinaGrid;
	Line[][] grid;
	LineRed[][] gridRed;
	Main main;
	int stage;
	float ratio;
	
	boolean stopUpdate;
	boolean ready;
	
	int controlType;
	final int DRAG = 0;
	final int GYRO = 1;
	final int FOLLOW = 2;
	
	float spawnDot;
	float spawnStalker;
	float spawnWorm;
	float spawnBomb;
	float spawnChain;
	float spawnLeaf;
	float spawnNet;
	float spawnPrison;
	float spawnHealth;
	float spawnHedgehog;
	float spawnSniper;
	float spawnSpikeChain;
	float spawnFirefly;
	float spawnBoss;
	float spawnCannon;
	float spawnGhostBomb;
	float spawnBat;
	float spawnSpider;
	
	
	public void destroy(){
		hero = null;
		bitmap = null;
		canvas = null;
		paint = null;
		instances = null;
		obstacle = null;
		redrawHandler = null;
		texter = null;
		maker = null;
		grid = null;
		gridRed = null;
		main = null;	
	}
	
	/**
	 * 
	 * @author Jani
	 * To je treba poštudirat zakaj je tako prou. Ampak to je pravilno in naj
	 * ne bi povzroèalo memory leaka.
	 */
	static class RefreshHandler extends Handler{
		WeakReference<Darkness> mview;
		
		RefreshHandler(Darkness aFragment){
			mview = new WeakReference<Darkness>(aFragment); // I have no idea what I'm doing. (slika od unga psa)
		}
		
		@Override
		public void handleMessage(Message msg){
			Darkness mmview = mview.get();
			mmview.update();
			mmview.invalidate();
		}
		
		public void sleep(long delayMillis){
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0),delayMillis);
		}
	};
	
	public Darkness(Context context, Main main) {
		super(context);
		this.main=main;
		ready=false;
		setKeepScreenOn(true);
		this.setOnTouchListener(touch);
	}
	 
	void update(){
		if(maker.time==0)maker.step();
		if(gameState==GAME){
			if(Math.random()<spawnDot) 
				instances.add(new Dot((int)(7*ratio),this));
			if(Math.random()<spawnStalker) 
				instances.add(new Stalker(this,width,height,(int)(15*ratio)));
			if(Math.random()<spawnWorm)
				instances.add(new Worm(this,(int)(13*ratio)));
			if(Math.random()<spawnBomb)
				instances.add(new Bomb((int)(25*ratio),this));
			if(Math.random()<spawnChain)
				new Chain(this,(int)(30*ratio),25);
			if(Math.random()<spawnLeaf)
				instances.add(new Leaf(this,1,(int)(15*ratio)));
			if(spawnNet>0 && Math.random()<Net.chanse(spawnNet)){
				if(Math.random()<0.5)
					instances.add(new Net((int)(18*ratio),this,5,false,true));
				else instances.add(new Net((int)(18*ratio),this,3,true,true));
			}
			if(Math.random()<spawnPrison){
				if(Prison.prisonNumber==0)
					instances.add(new Prison((int)(500*ratio),Prison.ringNumber,this));
			}
			if(Math.random()<spawnHealth)
				instances.add(new Health((int)(25*ratio),this));
			if(Math.random()<spawnHedgehog)
				instances.add(new Hedgehog((int)(18*ratio),this));
			if(Math.random()<spawnSniper)
				instances.add(new Sniper(this,width,height,(int)(15*ratio)));
			if(Math.random()<SpikeLink.chanse(spawnSpikeChain))
				new SpikeChain((int)(15*ratio),20,this);
			if(Math.random()<spawnFirefly){
				Firefly f=new Firefly(this,(int)(12*ratio));
				instances.add(f);
				maker.light.add(f);
			}
			if(Math.random()<spawnBoss)
				instances.add(new Boss((int)(90*ratio),this));
			if(Math.random()<spawnCannon)
				instances.add(new Cannon(this,(int)(32*ratio),Cannon.type));
			if(Math.random()<spawnGhostBomb)
				instances.add(new GhostBomb((int)(26*ratio),false,this));
			if(Math.random()<spawnBat)
				instances.add(new Bat((int)(20*ratio),this,false));
			if(Math.random()<spawnSpider)
				instances.add(new Spider((int)(15*ratio),this,true));
		}
		paint.setColor(Color.argb(255,0,0,0));
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(0,0,width,height,paint);
		for(int i=0; i<instances.size(); i++){
			Instance in = instances.get(i);
			if(in.persistant || gameState==GAME)
				in.step();
			in.draw(canvas,paint);
			if(in.dead){
				in.host=null;
				instances.remove(i);
				i--;
			}
		}
		Instance in = texter;
		if(in.persistant || gameState==GAME) in.step();
		in.draw(canvas,paint);
		
		if(!stopUpdate)
			redrawHandler.sleep(30);
	}
	
	void makeChargeRed (int x, int y){
		int xOS= x/dolzinaGrid;
		int yOS= y/dolzinaGrid;
		if(xOS<gridRed.length && yOS<gridRed[0].length){
			gridRed[xOS][yOS].addCharge(LineRed.index);
			LineRed.index++;
			if(LineRed.index>100)LineRed.index=0;
		}
	}
	
	void makeCharge (int x, int y){
		int xOS= x/dolzinaGrid;
		int yOS= y/dolzinaGrid;
		if(xOS<grid.length && yOS<grid[0].length){
			grid[xOS][yOS].charge+=4500;
		}
	}
	
	int razdalja(float x1,float y1,float x2,float y2){
		return (int)(Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2)));
	}
	
	private OnTouchListener touch = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent m) {
			if(controlType==DRAG){
				if(m.getAction()==MotionEvent.ACTION_UP){
					gameState=PAUSE;
				}
				else if(gameState==PAUSE){
					if(m.getAction()==MotionEvent.ACTION_DOWN && razdalja(m.getX(),m.getY(),hero.x,hero.y)<hero.radij)
		    			gameState=GAME;
				}
				else if(gameState==GAME){
					hero.x=m.getX();
					hero.y=m.getY();
				}
			}
			if(controlType==GYRO){
				if(m.getAction()==MotionEvent.ACTION_DOWN){
					if(won && gameState==GAME)
						main.zamenjajActivity(stage,(float)(150-hero.demage)/150f);
					else if (altWon){
						main.zamenjajActivity(stage,maker.time/33);
					}
					else if (gameState==LOST){
						main.finish();
					}
					else if(gameState==GAME)
						gameState=PAUSE;
					else if(gameState==PAUSE){
						if(stage!=1 || (Math.abs(main.angleX)<3 && Math.abs(main.angleY)<3)){
							gameState=GAME;
							texter.reset();
						}
					}
				}
			}
			return true;
		}
		
	};
	
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    	height = View.MeasureSpec.getSize(heightMeasureSpec);
	        width = View.MeasureSpec.getSize(widthMeasureSpec);
	        setMeasuredDimension(width,height);
	        paint = new Paint();
	        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	        canvas = new Canvas(bitmap);
	        prepareField();
	    }
	 
	void makeGrid(){
		int dolzina = (int)(width/10);
		dolzinaGrid=dolzina;
		int yNumber = (int)(height/dolzina);
		grid = new Line[10][yNumber];
		for(int i=0; i<grid.length; i++){
			for(int j=0; j<grid[i].length; j++){
				grid[i][j]=new Line(i*dolzina,j*dolzina,dolzina);
				instances.add(0,grid[i][j]);
			}
		}
		for(int i=0; i<grid.length; i++){
			for(int j=0; j<grid[i].length; j++){
				if(i!=0)grid[i][j].left=grid[i-1][j];
				if(i!=grid.length-1)grid[i][j].right=grid[i+1][j];
				if(j!=0)grid[i][j].up=grid[i][j-1];
				if(j!=grid[0].length-1)grid[i][j].down=grid[i][j+1];
			}
		}
	}
	
	void makeGridOrange(boolean blocks){
		int dolzina = (int)(width/10);
		dolzinaGrid=dolzina;
		int yNumber = (int)(height/dolzina);
		LineOrange[][] vertical = new LineOrange[10][yNumber];
		LineOrange[][] horizontal = new LineOrange[10][yNumber];
		LineOrange[] border = new LineOrange[yNumber*2];
		int k=0;
		for(int i=0; i<10; i++){
			for(int j=0; j<yNumber; j++){
				vertical[i][j] = new LineOrange(i*dolzina,j*dolzina,dolzina,true);
				horizontal[i][j] = new LineOrange(i*dolzina,j*dolzina,dolzina,false);
				instances.add(0,vertical[i][j]);
				instances.add(0,horizontal[i][j]);
				if(i!=0) {
					vertical[i-1][j].next = vertical[i][j];
					horizontal[i-1][j].next = horizontal[i][j];
				}
				else {
					border[k] = vertical[i][j]; k++;	
					border[k] = horizontal[i][j]; k++;
				}
			}
		}
		instances.add(new OrangeCharger(border));
		if(blocks){
			for(int i=0; i<4; i++){
				int a = (int)(Math.random()*8 +1)*dolzina;
				int b = (int)(Math.random()*(yNumber-2) + 1)*dolzina;
				Wall w = new Wall(a,b,a+dolzina,b+dolzina,Color.rgb(120, 70, 0),this);
				if(!w.altCollision((int)hero.x, (int)hero.y, hero.radij)){
					instances.add(w);
					obstacle.add(w);
				}
			}
		}
	}
	
	void makeGridGreen(){
		int dolzina = (int)(width/10);
		dolzinaGrid=dolzina;
		int yNumber = (int)(height/dolzina);
		LineGreen[][] vertical = new LineGreen[10][yNumber];
		LineGreen[][] horizontal = new LineGreen[10][yNumber];
		LineGreen[] border = new LineGreen[10+yNumber];
		int k=0;
		for(int i=0; i<10; i++){
			for(int j=0; j<yNumber; j++){
				vertical[i][j] = new LineGreen(i*dolzina,j*dolzina,dolzina,true);
				horizontal[i][j] = new LineGreen(i*dolzina,j*dolzina,dolzina,false);
				instances.add(0,vertical[i][j]);
				instances.add(0,horizontal[i][j]);
				if(j!=0) vertical[i][j-1].next = vertical[i][j];
				else {border[k] = vertical[i][j]; k++;}
				if(i!=0) horizontal[i-1][j].next = horizontal[i][j];
				else {border[k] = horizontal[i][j]; k++;}
			}
		}
		instances.add(new GreenCharger(border));
		for(int i=0; i<5; i++){
			int a = (int)(Math.random()*8 +1)*dolzina;
			int b = (int)(Math.random()*(yNumber-2) + 1)*dolzina;
			Wall w = new Wall(a,b,a+dolzina,b+dolzina,Color.rgb(0,120,70),this);
			if(!w.altCollision((int)hero.x, (int)hero.y, hero.radij)){
				instances.add(w);
				obstacle.add(w);
			}
		}
	}
	
	void makeGridGray(){
		int dolzina = (int)(width/10);
		dolzinaGrid=dolzina;
		int yNumber = (int)(height/dolzina);
		LineGray[][] gridGray = new LineGray[10][yNumber];
		for(int i=0; i<gridGray.length; i++){
			for(int j=0; j<gridGray[i].length; j++){
				gridGray[i][j]=new LineGray(i*dolzina,j*dolzina,dolzina,this);
				instances.add(0,gridGray[i][j]);
			}
		}
	}
	
	void makeGridRed(){
		int dolzina = (int)(width/10);
		dolzinaGrid=dolzina;
		int yNumber = (int)(height/dolzina);
		gridRed = new LineRed[10][yNumber];
		for(int i=0; i<gridRed.length; i++){
			for(int j=0; j<gridRed[i].length; j++){
				gridRed[i][j]=new LineRed(i*dolzina,j*dolzina,dolzina);
				instances.add(0,gridRed[i][j]);
			}
		}
		for(int i=0; i<gridRed.length; i++){
			for(int j=0; j<gridRed[i].length; j++){
				if(i!=0)gridRed[i][j].left=gridRed[i-1][j];
				if(i!=gridRed.length-1)gridRed[i][j].right=gridRed[i+1][j];
				if(j!=0)gridRed[i][j].up=gridRed[i][j-1];
				if(j!=gridRed[0].length-1)gridRed[i][j].down=gridRed[i][j+1];
			}
		}
	}
	
	private void prepareField(){
		ratio = (float)width/480f;
		hero = new Protagonist(width/2,height/2,(int)(70*ratio),width,height,this);
		texter= new Texter(this);
		instances = new ArrayList<Instance>();
		obstacle = new ArrayList<Instance>();
		stage=Main.stage;
		won=false;
		altWon=false;
		maker = new LevelMaker(this); 
		instances.add(hero);
		//instances.add(texter);
		instances.add(maker);
		gameState = PAUSE;
		controlType = GYRO;
		ready=true;
		update();
	}
	
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

}



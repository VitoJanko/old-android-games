package com.fifteen_puzzle_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;


public class TopSecret extends View {
	
	static int prev = -2;
	static boolean strech = false; 
	
	//Ustavi refresh ko zapreš activity
	static boolean stopRefresh=true;
	
	//nastavitve
	static boolean sound = true;
	static boolean ozadje = true;
	static int picture = 4;
	static boolean keepLast = false;
	static Bitmap custom = null;
	static boolean inProgress;
	static int tezavnost = 1;
	static final int TILE =4;
	static final int PICTURE = 5;
	static final int GALLERY = 6;
	final int EASY = 0;
	final int MEDIUM = 1;
	final int HARD = 2;
	final int IMPOSSIBLE = 3;
	static boolean tips;
	
	//zvok
	Zvok zvok;
	
	//celoten zaslon 
	int _height;
	int _width;
		
	//mesanje
	int shuffleTimes;
	float shuffleSpeed;
	int shuffleState;
	final int PICK = 0;
	final int MOVE = 1;
	int shuffleDirection;
	final int LEFT = 0;
	final int RIGHT = 1;
	final int UP = 2;
	final int DOWN = 3;
	float shuffleAmmount;
	boolean forceStop;
	
	//upper menu
	RectF noSound;
	RectF noSoundExtended;
	RectF noOzadje;
	Bitmap isSound;
	Bitmap isNotSound;
	
	//risanje
	Bitmap _bitmap;
	Canvas _canvas;
	Paint _paint;
	Context context;
	Main host;
	 
	//objekti
	int gameNumber;
	Unit[] life;
	Part[] price;
	Tile[][] tile;
	int unitRadij;
	
	//board koordinate
	int x1;
	int x2;
	int y1;
	int y2;
	
	int[] empty;
	
	//animacija
	int timePressed;
	boolean selected;
	int[] follower;
	float currentX;
	float currentY;
	float limit;
	int smer;
	final int NAVPICNO = 0;
	final int VODORAVNO = 1;
	float razmerjeX;
	float razmerjeY;
	
	//stanje igre
	int gameState = 1;	
	final int WATCHING = 0;
	final int PREPARING = 1;
	final int PLAYING = 2;
	final int WINNING = 3;
	String[] navodilo;
	boolean firstTime;
	
	//timers
	int timer0; //po koncani igri
	int timer1;
	
	//score
	int moves;
	int time;
	int best;
	boolean scoreDisplay;
	//static int highScore = 0;
	
	//saveState
	Bundle restore;
	boolean restored;
	
	private RefreshHandler redrawHandler = new RefreshHandler();
	
	class RefreshHandler extends Handler {
		
		@Override
	    public void handleMessage(Message msg) {
	        TopSecret.this.update();
	        TopSecret.this.invalidate();
	    }

	    public void sleep(long delayMillis) {
	    	this.removeMessages(0);
	    	
//	    	if(stopRefresh){
	    		sendMessageDelayed(obtainMessage(0), delayMillis);
//	    	}
	    }
	};
	
	protected void restoreState(){
		shuffleTimes = restore.getInt("shuffleTimes");
		shuffleSpeed = restore.getFloat("shuffleSpeed");
		shuffleSpeed = x2-x1;
		gameState = restore.getInt("state");
		timer0 = restore.getInt("timer0");
		empty = restore.getIntArray("empty");
		gameNumber = restore.getInt("gameNumber");
		moves = restore.getInt("moves");
		time = restore.getInt("time");
		int[] color = restore.getIntArray("tileColor");
		int[] number = restore.getIntArray("tileNumber");
		int i=0; int j=0;
		for(int k=0; k<color.length; k++){
			tile[i][j] = new Tile(i,j,x1,x2,y1,y2,color[k],number[k],gameNumber,_width,_height);
			j++;
			if(j==tile.length){
				j=0; i++;
			}
		}
		if(gameState == WINNING){
			price = new Part[1];
			price[0] = new Part();
		}
        update();
	}
	
	protected void saveState(Bundle bundle){
		bundle.putInt("shuffleTimes", shuffleTimes);
		bundle.putFloat("shuffleSpeed",shuffleSpeed);
		int[] number = new int[tile.length*tile.length];
		int[] color = new int[tile.length*tile.length];
		int counter = 0;
		for(int i=0; i<tile.length; i++){
			for(int j=0; j<tile.length; j++){
				number[counter] = tile[i][j].number;
				color[counter] = tile[i][j].color;
				counter++;
			}
		}
		bundle.putIntArray("tileColor", color);
		bundle.putIntArray("tileNumber", number);
		bundle.putIntArray("empty",empty);
		bundle.putInt("gameNumber",gameNumber);
		bundle.putInt("state",gameState);
		bundle.putInt("timer0",timer0);		
		bundle.putInt("moves",moves);
		bundle.putInt("time", time);
	}
	
	private void t0(){
		if(timer0==0){
			gameState = WINNING;
			price = new Part[(int)Math.pow(tile.length,4)];
			int index = 0;
			for(Tile[] row: tile){
				for(Tile t: row){
					t.draw(_canvas,_paint);
					t.makePrice(price,index);
					index+=tile.length*tile.length;
				}
			}
		}
		timer0--;
	}
	
	private void t1(){
		if(timer1==0){
			tips = false;
		}
		timer1--;
	}
	
	private void handleOzadje(){
		if(ozadje){
			for(Unit u: life){
				u.step();
				u.draw(_canvas,_paint);
			}
		}
	}
	
	//Barvanje ozadja
	//TODO
	int Rc = 0 ;
	int Gc = 0 ;
	int Bc = 0 ;
	int konst = 1 ;
	int konstG = 5 ;
	int stevec2 = 0;
	
	private void update(){
//		stevec2 ++;
//		if(stevec2 <= 35){
//			stevec2 = 0;
//			if(Gc>100 && konstG>0) konstG=-konstG;
//			if(Gc<0 && konstG<0) konstG=-konstG;
//			if(Bc>100 || Bc<0) {konst = -konst; Gc+=konstG;}
//			Bc+=konst;
//		}
//		if(Gc<0) Gc=0;
//		if(Bc<0) Bc=0;
//		if(Gc>255) Gc=255;
//		if(Bc>255) Bc=255;
		_paint.setColor(Color.argb(255,Rc,Gc,Bc));
		_paint.setStyle(Paint.Style.FILL);
		_canvas.drawRect(0,0,_width,_height,_paint);
		_paint.setColor(Color.WHITE);
		if(!TopSecret.strech){
			if(sound)_canvas.drawBitmap(isSound,noSound.left,noSound.top,_paint);
			if(!sound)_canvas.drawBitmap(isNotSound,noSound.left,noSound.top,_paint);
		}

		//_canvas.drawRect(noSound, _paint);
		//_canvas.drawRect(noOzadje, _paint);
		//_canvas.drawText(timePressed+"",10,20,_paint);
		//_paint.setTextSize(19);
		if(timer0>=0) t0();
		if(timer1>=0) t1();
		if(timePressed>=0)timePressed++;
		handleOzadje();
		int txtWidth = 8*navodilo[gameState].length();
    	_paint.setColor(Color.WHITE);
    	_paint.setTextSize(getResources().getDimension(R.dimen.numbers));
    	if(!TopSecret.strech)
    		_canvas.drawText(navodilo[gameState], (_width-txtWidth)/2, y1-20, _paint);
    	//_paint.setTextSize(18);
		if(gameState == PLAYING || gameState == PREPARING || gameState == WATCHING){
			time+=30;
			for(Tile[] row: tile){
				for(Tile t: row){
					t.draw(_canvas,_paint);
				}
			}
	        drawBoard();
		}
		
		if(gameState == PREPARING){
			if(restored){
//				int width = (x2-x1)/4;
//				int a = shuffleDirection;
//				int b = shuffleTimes;
//				int c = shuffleState;
//				float d = shuffleSpeed;
//				float e = shuffleAmmount;
//				_canvas.drawText(shuffleDirection+"",10,40,_paint);
			}
			if(shuffleTimes>0){
				if(shuffleSpeed>(x2-x1)/(0.75*gameNumber)){
					shuffle(shuffleTimes);
					shuffleTimes = 0;
					gameState = PLAYING;
				}
				else{
					shuffleStep();
				}
			}
			else 
				gameState = PLAYING;
		}
			
		if(gameState == WINNING){
			if(price[0].alpha>0){
				handleOzadje();
				for(Part u: price){
					u.draw(_canvas,_paint);
					u.step();
				}
			}
			else{
				drawHighscore();
			}
		}
		if(stopRefresh){
			redrawHandler.sleep(30);
		}
	}
	
	private void drawHighscore(){
		scoreDisplay = true;
		Paint _p = new Paint();
		_p.setColor(Color.WHITE);
    	_p.setTextSize(getResources().getDimension(R.dimen.HighScore));
    	_p.setTextSkewX((float) -0.4);
    	int razmik = _height/12;
    	int delay = (int)(razmik*1.5);
    	_canvas.drawText("WELL DONE!",_width/6, _height*4/20, _p);
    	String diff =""; 
    	double teza = 1;
    	if(tezavnost==EASY && picture==TILE) {diff = "Easy"; teza = 0.5;}
    	if(tezavnost==MEDIUM && picture==TILE) {diff = "Medium"; teza = 5.5;}
    	if(tezavnost==HARD && picture==TILE) {diff = "Hard"; teza = 28;}
    	if(tezavnost==EASY && picture!=TILE) {diff = "Easy"; teza = 1;}
    	if(tezavnost==MEDIUM && picture!=TILE) {diff = "Medium"; teza = 14;}
    	if(tezavnost==HARD && picture!=TILE) {diff = "Hard"; teza = 90;}
    	_canvas.drawText("Difficulty:  "+diff, _width/6, _height*4/20+delay, _p);
    	int realTime = time/1000;
    	delay+=razmik;
    	_canvas.drawText("Time:  "+realTime, _width/6, _height*4/20+delay, _p);
    	delay+=razmik;
    	_canvas.drawText("Moves:   "+moves, _width/6, _height*4/20+delay, _p);
    	int score = 1000000/(realTime*moves);
    	score*=teza;
    	delay+=razmik;
    	_canvas.drawText("Score:   "+score, _width/6, _height*4/20+delay, _p);
    	delay+=(int)(razmik*1.5);
    	if(score>MenuView.highScore){
    		best = 1;
    		MenuView.highScore = score;
//    		host.highScore((String.valueOf(score))); TODO izklopljeno ker ni server konfiguriran
    		
    	}
    	if(best==1)    
    		_canvas.drawText("NEW HIGHSCORE!", _width/6, _height*4/20+delay, _p);
    	delay+=razmik;
    	_canvas.drawText("HighScore:   "+MenuView.highScore, _width/6, _height*4/20+delay, _p);
	}
	
	private void menuSound(){
		float optWidth = _width/13;
		noOzadje = new RectF(optWidth*3,optWidth*1,optWidth*4,optWidth*2);
		noSound = new RectF(optWidth*10,optWidth,optWidth*11,optWidth*2);
		noSoundExtended = new RectF((float)(optWidth*9.5),(float)(optWidth*0.5),(float)(optWidth*11.5),(float)(optWidth*2.5));
		isSound = BitmapFactory.decodeResource(getResources(),R.drawable.sound);
		isNotSound = BitmapFactory.decodeResource(getResources(),R.drawable.nosound);
		isSound = Bitmap.createScaledBitmap(isSound, (int)noSound.width(), (int)noSound.width(), true);
		isNotSound = Bitmap.createScaledBitmap(isNotSound, (int)noSound.width(), (int)noSound.width(), true);
	}
	
	private void drawBoard() {
		_paint.setColor(Color.GRAY);
		_paint.setStyle(Paint.Style.STROKE);
		if(!TopSecret.strech){
			_canvas.drawRect(x1,y1,x2,y2,_paint);
			_canvas.drawRect(x1-1,y1-1,x2+1,y2+1,_paint);
		}
		else{
			_canvas.drawRect(1,1,_width-1,_height-1,_paint);
			_canvas.drawRect(2,2,_width-2,_height-2,_paint);
		}
	}
	
	protected void setUpBoard(){
		restored = true;
		x1 = _width/12;
		x2 = (_width/12)*11;
		y1 = (_height-(x2-x1))/2;
		y2 = y1 +(x2-x1);
		if(_width>_height){
			y1 = _height/12;
			y2 = (_height/12)*11;
			x1 = (_width-(y2-y1))/2;
			x2 = x1 +(y2-y1);
		}
		if(tezavnost==EASY){
			gameNumber=3;
			shuffleTimes = 60;
		}
		if(tezavnost==MEDIUM){
			gameNumber=4;
			shuffleTimes = 120;
		}
		if(tezavnost==HARD){
			gameNumber=5;
			shuffleTimes = 150;
		}
		x2-=(x2-x1)%gameNumber;
		y2-=(y2-y1)%gameNumber;
		unitRadij = (x2-x1)/(gameNumber*gameNumber);
		shuffleSpeed = unitRadij;
		tile = new Tile[gameNumber][gameNumber];
		for(int i=0; i<gameNumber; i++){
			for(int j=0; j<gameNumber; j++){
				tile[i][j] = new Tile(i,j,x1,x2,y1,y2,gameNumber,_width,_height);
			}
		}
		razmerjeX = (float)((_width-2)/(x2-x1));
		razmerjeY = (float)((_height-2)/(y2-y1));
		menuSound();
		gameState = PREPARING;
		shuffleState = PICK;
		shuffleDirection = -1;
		forceStop = false;
		empty = new int[2]; 
		empty[0] = gameNumber-1; empty[1] = gameNumber-1;
		selected = false;
		timer0 = -1;
		time = 0;
		moves = 0;
		best = 0;
		scoreDisplay = false;
		timePressed = -1;
		if(picture==PICTURE){
			makePicture();
			gameState = WATCHING;
		}
		if(picture==GALLERY){
			if(TopSecret.custom!=null){
				if(TopSecret.strech)
					makeGalleryStreched();
				else
					makeGallery();
				gameState = WATCHING;
			}
			else if (!TopSecret.inProgress){
				TopSecret.picture = TopSecret.TILE;
				host.gallery();
			}
		}
		navodilo = new String[4];
		navodilo[0] = "Press to shuffle";
		navodilo[1] = "Shuffling... press to skip";
		navodilo[2] = ""; navodilo[3] = "";
		
        generate();
        if(restore!=null) restoreState();
        update();
	}
	
	void fixTiles(){
		for(int i=0; i<tile.length; i++){
			for(int j=0; j<tile.length; j++){
				tile[i][j].update(_width,_height);
			}
		}
	}
	
	void makeGalleryStreched(){
		int fragWidth = (_width-2)/gameNumber;
		int fragHeight = (_height-2)/gameNumber;
		for(int i=0; i<tile.length; i++){
			for(int j=0; j<tile.length; j++){
				int n = tile[i][j].number-1;
				tile[i][j].setPicture
					(Bitmap.createBitmap(custom, fragWidth*(n%gameNumber), fragHeight*(n/gameNumber), fragWidth, fragHeight));
			}
		}
	}
	
	void makeGallery(){
		Bitmap celota = custom;
		if(custom.getHeight()<custom.getWidth())
			celota = Bitmap.createBitmap(celota, 0,0,celota.getHeight(),celota.getHeight());
		else
			celota = Bitmap.createBitmap(celota, 0, 0, celota.getWidth(), celota.getWidth());
		celota = Bitmap.createScaledBitmap(celota, (int)(x2-x1), (int)(x2-x1), true);
		int fragWidth = (x2-x1)/gameNumber;
		for(int i=0; i<tile.length; i++){
			for(int j=0; j<tile.length; j++){
				int n = tile[i][j].number-1;
				tile[i][j].setPicture
					(Bitmap.createBitmap(celota, fragWidth*(n%gameNumber), fragWidth*(n/gameNumber), fragWidth, fragWidth));
			}
		}
	}
	
	private void makePicture(){
		int a = (int)(Math.random()*13);
		while(a==prev){
			a = (int)(Math.random()*13);
		}
		if(!keepLast)
			prev = a;
		else a=prev;
		Bitmap celota = null;
		switch (a){
			case 0: celota=BitmapFactory.decodeResource(getResources(),R.drawable.wall);  break;
			case 1: celota=BitmapFactory.decodeResource(getResources(),R.drawable.ana);  break;
			case 2: celota=BitmapFactory.decodeResource(getResources(),R.drawable.drev);  break;
			case 3: celota=BitmapFactory.decodeResource(getResources(),R.drawable.emu);  break;
			case 4: celota=BitmapFactory.decodeResource(getResources(),R.drawable.jaki);  break;
			case 5: celota=BitmapFactory.decodeResource(getResources(),R.drawable.karte);  break;
			case 6: celota=BitmapFactory.decodeResource(getResources(),R.drawable.krompir_hard);  break;
			case 7: celota=BitmapFactory.decodeResource(getResources(),R.drawable.miniraf);  break;
			case 8: celota=BitmapFactory.decodeResource(getResources(),R.drawable.mojca);  break;
			case 9: celota=BitmapFactory.decodeResource(getResources(),R.drawable.ognjemet); break;
			case 10: celota=BitmapFactory.decodeResource(getResources(),R.drawable.pes); break;
			case 11: celota=BitmapFactory.decodeResource(getResources(),R.drawable.sonce); break;
			case 12: celota=BitmapFactory.decodeResource(getResources(),R.drawable.suricate); break;
		}
		try{
		celota = Bitmap.createScaledBitmap(celota, (int)(x2-x1), (int)(x2-x1), true);
		}catch(NullPointerException e){
			celota = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ana), (int)(x2-x1), (int)(x2-x1), true);
		}
		Bitmap[][] fragments = new Bitmap[gameNumber][gameNumber];
		int fragWidth = (x2-x1)/gameNumber;
		for(int i=0; i<fragments.length; i++){
			for(int j=0; j<fragments.length; j++){
				fragments[i][j] = Bitmap.createBitmap(celota, fragWidth*i, fragWidth*j, fragWidth, fragWidth);
				tile[i][j].setPicture(fragments[i][j]);
			}
		}
	}
	
	private void shuffleStep(){
		if(shuffleState == PICK){
			boolean done = false;
			while(!done){
				done = true;
				int a = (int)(Math.random()*4);
				if(a==0 && empty[0]!=tile.length-1 && shuffleDirection!=RIGHT) shuffleDirection = LEFT;
				else if(a==1 && empty[0]!=0 && shuffleDirection!=LEFT) shuffleDirection = RIGHT;
				else if(a==2 && empty[1]!=tile.length-1 && shuffleDirection!=DOWN) shuffleDirection = UP;
				else if(a==3 && empty[1]!=0 && shuffleDirection!=UP) shuffleDirection = DOWN;
				else done = false;
			}
			shuffleAmmount = (x2-x1)/tile.length;
			if(TopSecret.strech){
				if(shuffleDirection==RIGHT || shuffleDirection==LEFT)
					shuffleAmmount = (_width-2)/tile.length;
				else
					shuffleAmmount = (_height-2)/tile.length;
			}
			shuffleState = MOVE;
		}
		if(shuffleState == MOVE){
			float moveX = 0;
			float moveY = 0;
			Tile t = null;
			int[] tt = new int[2];
			if(shuffleDirection == LEFT){
				tt[0] = empty[0]+1; tt[1] = empty[1];
				t = tile[empty[0]+1][empty[1]];
				moveX = -Math.min(shuffleSpeed,shuffleAmmount);
			}
			if(shuffleDirection == RIGHT){
				tt[0] = empty[0]-1; tt[1] = empty[1];
				t = tile[empty[0]-1][empty[1]];
				moveX = Math.min(shuffleSpeed,shuffleAmmount);
			}
			if(shuffleDirection == UP){
				tt[0] = empty[0]; tt[1] = empty[1]+1;
				t = tile[empty[0]][empty[1]+1];
				moveY = -Math.min(shuffleSpeed,shuffleAmmount);
			}
			if(shuffleDirection == DOWN){
				tt[0] = empty[0]; tt[1] = empty[1]-1;
				t = tile[empty[0]][empty[1]-1];
				moveY = Math.min(shuffleSpeed,shuffleAmmount);
			}
			shuffleAmmount -= shuffleSpeed;
			if(!TopSecret.strech){
				t.x+=moveX;
				t.y+=moveY;
				t.xStrech+=moveX*razmerjeX;
				t.yStrech+=moveY*razmerjeY;
			}
			else{
				t.x+=moveX/razmerjeX;
				t.y+=moveY/razmerjeY;
				t.xStrech+=moveX;
				t.yStrech+=moveY;				
			}
			if(shuffleAmmount<0){
				shuffleState = PICK;
				change(tt[0],tt[1],empty[0],empty[1]);
				shuffleSpeed++;
				shuffleTimes--;
				if(forceStop)
					shuffleSpeed = (x2-x1);
			}
		}
	}
	
	private void shuffle(int times){
		int prev = -1;
		for(int i=0; i<times; i++){
			int[] move = new int[2];
			boolean done = false;
			int a = -1;
			while(!done){
				done = true;
				a = (int)(Math.random()*4);
				if(a==0 && empty[0]!=tile.length-1 && prev!= 1) {move[0]=empty[0]+1; move[1] = empty[1];}
				else if(a==1 && empty[0]!=0 && prev!= 0 ) {move[0]=empty[0]-1; move[1] = empty[1];}
				else if(a==2 && empty[1]!=tile.length-1 && prev!= 3) {move[1]=empty[1]+1; move[0] = empty[0];}
				else if(a==3 && empty[1]!=0 && prev!= 2) {move[1]=empty[1]-1; move[0] = empty[0];}
				else done = false;
			}
			prev = a;
			change(move[0],move[1],empty[0],empty[1]);
		}
	}
	
	private void change(int oldI, int oldJ, int newI, int newJ){
		tile[oldI][oldJ].goTo(newI, newJ);
		tile[newI][newJ].goTo(oldI,oldJ);
		Tile temp = tile[oldI][oldJ];
		tile[oldI][oldJ] = tile[newI][newJ];
		tile[newI][newJ] = temp;
		empty[0] = oldI;
		empty[1] = oldJ;
		if (gameState==PLAYING ) {
			moves++;
			if(sound)
				zvok.gumb2();
		}
	}
	
	private void generate(){
		life = new Unit[30];
		for(int i=0; i<30; i++){
			life[i] = new Unit(_width,_height,(int)(8*((float)_width/480f)));
		}
	}
	
	protected int[] findFollower(float x, float y){
		if(!TopSecret.strech){
			int delayX = ((int)x-x1)/((x2-x1)/tile.length);
			int delayY = ((int)y-y1)/((y2-y1)/tile.length);
			int[] result = {delayX, delayY};
			return result;
		}
		else{
			int delayX = ((int)x-1)/((_width-2)/tile.length);
			int delayY = ((int)y-1)/((_height-2)/tile.length);
			int[] result = {delayX, delayY};
			return result;
		}
	}
	
	public static boolean stikalo=true;
	private OnTouchListener touch = new OnTouchListener() {
	    public boolean onTouch(View v, MotionEvent m) {
	    	if(stopRefresh && stikalo){
	    		stopRefresh=true;
	    		stikalo=false;
	    		update();
	    	}
	    	try{
	    	if(noSoundExtended.contains(m.getX(),m.getY()) && !TopSecret.strech){
	    		if(m.getAction()==MotionEvent.ACTION_DOWN)
	    			sound = !sound;
	    			MenuView.sound = sound;

	    	}
	    	else if(noOzadje.contains(m.getX(),m.getY())){
	    		if(m.getAction()==MotionEvent.ACTION_DOWN)
	    			ozadje = !ozadje;
	    	}
	    	else if(gameState == PREPARING){
	    		if(m.getAction()==MotionEvent.ACTION_DOWN)
	    			forceStop = true;
	    	}
	    	else if(gameState == WATCHING){
	    		if(m.getAction()==MotionEvent.ACTION_DOWN)
	    			gameState = PREPARING;
	    	}
	    	else if(gameState == WINNING){
	    		if(scoreDisplay){
	    			if(m.getAction()==MotionEvent.ACTION_DOWN){
	    				host.openOptionsMenu();
	    			}
	    		}
	    	}
	    	else if(gameState == PLAYING && timer0<=0){
		    	if(m.getAction()==MotionEvent.ACTION_DOWN){
		    		float x = m.getX();
		    		float y = m.getY();
		    		if((x>x1 && x<x2 && y>y1 && y<y2) || TopSecret.strech){
		    			selected = true;
		    			follower = findFollower(x,y);
		    			Tile f = tile[follower[0]][follower[1]];
		    			if (!f.empty){
		    				timePressed = 0;
			    			if(empty[0] == follower[0]){
			    				smer = NAVPICNO;
			    				if(empty[1] == follower[1]+1){
			    					if(!TopSecret.strech)limit = f.width;
			    					if(TopSecret.strech) limit = f.strHeight;
			    				}
			    				else if(empty[1] == follower[1]-1){
			    					if(!TopSecret.strech)limit = -f.width;
			    					if(TopSecret.strech) limit = -f.strHeight;
			    				}
			    				else {selected = false; follower = null;}
			    			}
			    			else if(empty[1] == follower[1]){
			    				smer = VODORAVNO;
			    				if(empty[0] == follower[0]+1){
			    					if(!TopSecret.strech)limit = f.width;
			    					if(TopSecret.strech) limit = f.strWidth;
			    				}
			    				else if(empty[0] == follower[0]-1){
			    					if(!TopSecret.strech)limit = -f.width;
			    					if(TopSecret.strech) limit = -f.strWidth;
			    				}
			    				else {selected = false; follower = null;}
			    			}
			    			else{
			    				follower = null;
			    				selected = false;
			    			}
		    			}
		    			else{
		    				follower = null;
		    				selected = false;
		    			}
		    			currentX = m.getX();
		    			currentY = m.getY();
		    		}
		    	}
		    	if(m.getAction()==MotionEvent.ACTION_UP){
		    		if(selected){
		    			Tile t = tile[follower[0]][follower[1]];
		    			int realWidth = t.width;
			    		if(TopSecret.strech && smer==NAVPICNO) realWidth = t.strHeight;
			    		if(TopSecret.strech && smer==VODORAVNO) realWidth = t.strWidth;
		    			if (timePressed>0 && timePressed<15 && Math.abs(limit)<realWidth*0.9){
		    				change(t.i,t.j,empty[0],empty[1]);
		    			}
		    			else if(Math.abs(limit)>realWidth/2){
		    				t.goTo(t.i, t.j);
		    			}
		    			else{
		    				change(t.i,t.j,empty[0],empty[1]);
		    			}
		    		}
		    		check();
		    		selected = false;
		    		follower = null;
		    		timePressed = -1;
		    	}
		    	if(selected){
		    		int width = tile[follower[0]][follower[1]].width;
		    		int realWidth = width;
		    		if(TopSecret.strech && smer==NAVPICNO) realWidth = tile[follower[0]][follower[1]].strHeight;
		    		if(TopSecret.strech && smer==VODORAVNO) realWidth = tile[follower[0]][follower[1]].strWidth;
		    		float diffX = m.getX()-currentX;
		    		float diffY = m.getY()-currentY;
		    		if(smer == NAVPICNO){
		    			if (limit>0){
		    				if(limit-diffY <= 0) {
		    					diffY = limit; 
		    					change(follower[0],follower[1],follower[0],follower[1]+1);
		    					selected = false;
		    					}
		    				if(limit-diffY > realWidth) diffY = limit-realWidth;
		    			}
		    			if (limit<0){
		    				if(limit-diffY >= 0) {
		    					diffY = limit; 
		    					change(follower[0],follower[1],follower[0],follower[1]-1);
		    					selected = false;
		    					}
		    				if(Math.abs(limit-diffY) > realWidth) diffY = realWidth+limit;
		    			}
		    			limit-=diffY;
		    			if(!TopSecret.strech){
		    				tile[follower[0]][follower[1]].y+=diffY;
		    				tile[follower[0]][follower[1]].yStrech+=diffY*razmerjeY;
		    			}
		    			else{
		    				tile[follower[0]][follower[1]].y+=diffY/razmerjeY;
		    				tile[follower[0]][follower[1]].yStrech+=diffY;
		    			}
		    				
		    		}
		    		if(smer == VODORAVNO){
		    			if (limit>0){
		    				if(limit-diffX <= 0) {
		    					diffX = limit; 
		    					change(follower[0],follower[1],follower[0]+1,follower[1]);
		    					selected = false;
		    					}
		    				if(limit-diffX > realWidth) diffX = limit-realWidth;
		    			}
		    			if (limit<0){
		    				if(limit-diffX >= 0) {
		    					diffX = limit; 
		    					change(follower[0],follower[1],follower[0]-1,follower[1]);
		    					selected = false;
		    					}
		    				if(Math.abs(limit-diffX) > realWidth) diffX = realWidth+limit;
		    			}
		    			limit-=diffX;
		    			if(!TopSecret.strech){
		    				tile[follower[0]][follower[1]].x+=diffX;
		    				tile[follower[0]][follower[1]].xStrech+=diffX*razmerjeX;
		    			}
		    			else{
		    				tile[follower[0]][follower[1]].x+=diffX/razmerjeX;
		    				tile[follower[0]][follower[1]].xStrech+=diffX;
		    			}
		    		}
		    		if(!selected) {
		    			follower = null;
		    			check();
		    		}
		    		currentX=m.getX();
		    		currentY=m.getY();
		    	}
	    	}
	    	}catch(ArrayIndexOutOfBoundsException e){
	    		
	    	}
	    	return true;
	    }
	};
	
	protected void check(){
		boolean won = true;
		for(int i=0; i<tile.length; i++){
			for(int j=0; j<tile.length; j++){
				int st = tile[i][j].number-1;
				if(i!=st%tile.length || j!=st/tile.length)
					won = false;
			}
		}
		if(won){
			timer0 = 50;
			tile[empty[0]][empty[1]].empty = false;
			tile[empty[0]][empty[1]].goTo(empty[0], empty[1]);
			
			host.vibrator();
		}
	}
	
    public TopSecret(Context context,Zvok zvok) {
        super(context);
        this.context=context;
        _paint = new Paint();
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setColor(Color.BLUE);
        this.zvok = zvok;
        this.setOnTouchListener(touch);
        firstTime=true;
    }
 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	_height = View.MeasureSpec.getSize(heightMeasureSpec);
        _width = View.MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(_width, _height);
        
        _bitmap = Bitmap.createBitmap(_width, _height, Bitmap.Config.ARGB_8888);
        _canvas = new Canvas(_bitmap);
        if(firstTime){
        	firstTime=false;
        	setUpBoard();
        }
        else if(picture==GALLERY){
        	fixTiles();
        	makeGalleryStreched();
        }
        else if(picture==TILE){
        	fixTiles();
        }
    }
    
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
        canvas.drawBitmap(_bitmap, 0, 0, _paint);
        if(stopRefresh){
        	update();
        }
    }
}

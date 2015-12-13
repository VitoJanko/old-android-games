package com.fifteen_puzzle_game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MenuView extends View {
	Paint paint = new Paint();
	Bitmap bitmap;
	Canvas canvas;
	
	private RefreshHandler redrawHandler = new RefreshHandler();
	
	//velièina texta
	Resources res;
	float newgame;
	float menu;
	float choice;
	
	public static int height;
	public static int width;
	
	Context context;
	PrvaStran prvaStran;
	Zvok zvok;
	
	//USTAVI UPDATE IN MEMORY LEAK! :D:D:D
	public static boolean stopUpdate= true;
	
	//Premikanje prsta
	float prejsnjiX = 0;
	
	//Klikanje
	long startTime = 0;
	long endTime = 0;
	boolean click = false;
	float totalSprememba = 0;
	
	//Nastavitve:
	public static int difficulty = 1;
	public static int background = TopSecret.PICTURE;
	public static boolean sound = false;
	public static int highScore = 0;
	
	//DIFFICULTY
	int alphaEasy = 150;
	int alphaMedium = 255;
	int alphaHard = 150;
	float zamikDifficulty = 0;
	int enotaDifficulty;
	//int difficulty = 1;
	
	//BACKGROUND
	int alphaTile = 150;
	int alphaPicture = 155;
	int alphaGallery = 150;
	float zamikBackground = 0;
	int enotaBackground;
	
	//SOUND
	int alphaON = 150;
	int alphaOFF = 255;
	float zamikSound = 0;
	int enotaSound;
	
	public MenuView(Context context,PrvaStran prvaStran,Zvok zvok) {
		super(context);
		this.context = context;	
		this.prvaStran = prvaStran;
		this.zvok = zvok;
        
		res = getResources();
		
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
		
		Typeface font = Typeface.createFromAsset(res.getAssets(), "segoesc.ttf");
        paint.setTypeface(font);
		paint.setShadowLayer(2, 2,2, Color.rgb(104, 171, 239));
        
		stopUpdate=true;
        
	}
	
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	height = View.MeasureSpec.getSize(heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        
        setMeasuredDimension(width, height);
        
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        
        zacetneNastavitve();
        //nastavi velikost texta
    	newgame = res.getDimension(R.dimen.new_game);
    	menu = res.getDimension(R.dimen.menu_item);
    	choice = res.getDimension(R.dimen.choiche);
    	//-<
        stopUpdate= true;
        ustvariMenu();
        this.setOnTouchListener(touch);
        
        update();
        
    }
    
    public void zacetneNastavitve(){
    	stopUpdate = true;
    	if(sound){
    		zamikSound = 0;
    		alphaON = 255;
    		alphaOFF = 150;
    		
    	}else{
    		zamikSound = -width/5+1;
    		alphaON = 150;
    		alphaOFF = 255;
    	}
    	
    	if(background == TopSecret.PICTURE){
    		zamikBackground = 0;
    		alphaPicture = 255;
    		alphaTile = 150;
    		alphaGallery = 150;
    	}else if(background == TopSecret.TILE){
    		zamikBackground = +width/4+1;
    		alphaPicture = 150;
    		alphaTile = 255;
    		alphaGallery = 150;
    	}else{
    		zamikBackground = -width/4+1;
    		alphaPicture = 150;
    		alphaTile = 150;
    		alphaGallery = 255;
    	}
    	
    	if(difficulty == 1){
    		zamikDifficulty = 0;
    		alphaEasy = 150;
    		alphaMedium = 255;
    		alphaHard = 150;
    		
    	}else if(difficulty == 0){
    		zamikDifficulty = width/4-1;
    		alphaEasy = 255;
    		alphaMedium = 150;
    		alphaHard = 150;
    		
    	}else{
    		zamikDifficulty = -width/4+1;
    		alphaEasy = 150;
    		alphaMedium = 150;
    		alphaHard = 255;
    	}
    }
    
    public void ustvariMenu(){
    	stopUpdate= true;
    	paint.setColor(Color.WHITE);
    	paint.setTextSize(newgame);
    	paint.setTextSkewX((float) -0.4);
    	canvas.drawText("NEW GAME", width/2-width/5, height*6/20, paint);
    	
    	//TODO
    	paint.setTextSize(menu);
    	paint.setTextSkewX((float) -0.6);
    	canvas.drawText("DIFFICULTY:", width/2-width/6, height*8/20, paint);
    	paint.setTextSkewX((float) -0.4);
    	paint.setTextSize(choice);
    	paint.setAlpha(alphaEasy);
    	canvas.drawText("Easy", width/2-width/3-width/20+zamikDifficulty, height*9/20, paint);
    	paint.setAlpha(alphaMedium);
    	canvas.drawText("Medium", width/2-width/8-width/20+zamikDifficulty, height*9/20, paint);
    	paint.setAlpha(alphaHard);
    	canvas.drawText("Hard", width/2+width/5-width/20+zamikDifficulty, height*9/20, paint);
    	
//    	//TODO
//    	paint.setAlpha(255);
//    	paint.setTextSize(26);
//    	paint.setTextSkewX((float) -0.6);
//    	canvas.drawText("BOARDSTYLE:",width/2-width/5,height*11/20,paint);
//    	paint.setTextSize(32);
//    	paint.setTextSkewX((float) -0.4);
//    	paint.setAlpha(alphaTile);
//    	canvas.drawText("Tile", width/2-width/7+zamikBackground, height*12/20, paint);
//    	paint.setAlpha(alphaPicture);
//    	canvas.drawText("Picture", width/2+width/8+zamikBackground, height*12/20, paint);
    	paint.setAlpha(255);
    	paint.setTextSize(menu);
    	paint.setTextSkewX((float) -0.6);
    	canvas.drawText("BOARDSTYLE:", width/2-width/6, height*11/20, paint);
    	paint.setTextSkewX((float) -0.4);
    	paint.setTextSize(choice);
    	paint.setAlpha(alphaTile);
    	canvas.drawText("Tile", width/2-width/3-width/20+zamikBackground, height*12/20, paint);
    	paint.setAlpha(alphaPicture);
    	canvas.drawText("Picture", width/2-width/8-width/20+zamikBackground, height*12/20, paint);
    	paint.setAlpha(alphaGallery);
    	canvas.drawText("Gallery", width/2+width/5-width/20+zamikBackground, height*12/20, paint);
    	
    	
    	//TODO
    	paint.setAlpha(255);
    	paint.setTextSize(menu);
    	paint.setTextSkewX((float) -0.6);
    	canvas.drawText("SOUND:", width/2-width/8, height*14/20, paint);
    	paint.setTextSize(choice);
    	paint.setTextSkewX((float) -0.4);
    	paint.setAlpha(alphaON);
    	canvas.drawText("ON", width/2-width/7+zamikSound, height*15/20, paint);
    	paint.setAlpha(alphaOFF);
    	canvas.drawText("OFF", width/2+width/8+zamikSound, height*15/20, paint);
    	
    	paint.setAlpha(255);
    	paint.setTextSize(res.getDimension(R.dimen.try_new_game));
    	
//    	if(!PrvaStran.VOTE){
//    		canvas.drawText("Rank it on market", width/3-width/7, height*17/20, paint);
//    	}else if(PrvaStran.AA){				
//    		canvas.drawText("Share with friends!", width/3-width/7, height*17/20, paint);
//    	}else{
    		canvas.drawText("Try NEW game Above Average!", 0, height*17/20, paint);
//    	}
    	String hsk = "High Score: "+String.valueOf(highScore);
    	canvas.drawText(hsk, 0, height*19/20, paint);
    }

	
	protected void onDraw(Canvas canvas){
		canvas.drawBitmap(bitmap ,0 ,0 , paint);
	}

	
	private OnTouchListener touch = new OnTouchListener() {
	    public boolean onTouch(View v, MotionEvent m) {
	    	float X = m.getX();
	    	float Y = m.getY();
	    	
	    	if(m.getAction() == MotionEvent.ACTION_DOWN){
	    		prejsnjiX = X;
	    		totalSprememba = X;
	    		click = false;
	    		startTime = System.currentTimeMillis();
	    	}else if(m.getAction() == MotionEvent.ACTION_UP){
	    		endTime = System.currentTimeMillis();
	    		if((endTime-startTime)<250 && (Math.abs(totalSprememba-X)) < width/6){
	    			click = true;
	    		}
	    	}
	    	
	    	
	    	//NEW GAME
	    	if(Y > height*4/20 && Y < height*7/20){
	    		if(sound && m.getAction() == MotionEvent.ACTION_DOWN){
	    			zvok.gumb2();
	    		}
	    		prvaStran.zamenjajActivity();
	    	} 
	    	//DIFICULTY
	    	else if(Y > height*8/20 && Y < height*10/20){
	    		if(!click){
		    		if(Math.abs(zamikDifficulty) < width/4){
		    			zamikDifficulty += X-prejsnjiX;
		    			prejsnjiX=X;
		    			if(zamikDifficulty > width /4){
		    				zamikDifficulty = width/4-1;
		    			}else if(zamikDifficulty < -width/4){
		    				zamikDifficulty = -width/4+1;
		    			}
		    		}else if(zamikDifficulty < width/4 && (X-prejsnjiX) > 0){
		    			zamikDifficulty += X-prejsnjiX;
		    			prejsnjiX=X;
		    		}else if(zamikDifficulty > width/4 && (X-prejsnjiX) < 0){
		    			zamikDifficulty += X-prejsnjiX;
		    			prejsnjiX=X;
		    		}
		    		if(Math.abs(zamikDifficulty)< width/6){
		    			alphaEasy = 150;
		    			alphaMedium = 255;
		    			alphaHard = 150;
			    		if(sound && difficulty != 1){
			    			zvok.gumb3();
			    		}
		    			difficulty = 1;	    			
		    		}else if(zamikDifficulty > width/6){
		    			alphaEasy = 255;
		    			alphaMedium = 150;
		    			alphaHard = 150;
			    		if(sound && difficulty != 0){
			    			zvok.gumb3();
			    		}
		    			difficulty = 0;
		    		}else{
		    			alphaEasy = 150;
		    			alphaMedium = 150;
		    			alphaHard = 255;
			    		if(sound && difficulty != 2){
			    			zvok.gumb3();
			    		}
		    			difficulty = 2;
		    		}
	    		}else{//CLICK
	    			if( (width/2-width/3-width/20+zamikDifficulty) < X && X < (width/2-width/8-width/20+zamikDifficulty)){
	    				alphaEasy = 255;
	    				alphaMedium = 150;
	    				alphaHard = 150;
			    		if(sound && difficulty != 0){
			    			zvok.gumb3();
			    		}
	    				difficulty = 0;
	    				zamikDifficulty = width/4-1;
	    			}else if((width/2+width/5-width/20+zamikDifficulty) < X){
	    				alphaEasy = 150;
	    				alphaMedium = 150;
	    				alphaHard = 255;
			    		if(sound && difficulty != 2){
			    			zvok.gumb3();
			    		}
	    				difficulty = 2;
	    				zamikDifficulty = -width/4;
	    			}else if(X < (width/2+width/5-width/20+zamikDifficulty) && X > (width/2-width/3-width/20+zamikDifficulty) ){
	    				alphaEasy = 150;
	    				alphaMedium = 255;
	    				alphaHard = 150;
			    		if(sound && difficulty != 1){
			    			zvok.gumb3();
			    		}
	    				difficulty = 1;
	    				zamikDifficulty = 0;
	    			}
	    		}

	    	}
	    	//BACKGROUND
	    	else if(Y > height*11/20 && Y < height*13/20){
		    	
//		    		if(zamikBackground <= 0 && zamikBackground > -width/4){
//		    			zamikBackground += X-prejsnjiX;
//		    			prejsnjiX=X;
//		    			if(zamikBackground > 0){
//		    				zamikBackground = -1;
//		    			}else if(zamikBackground < -width/4){
//		    				zamikBackground = -width/4+1;
//		    			}
//		    		}else if(zamikBackground < -width/4 && (X-prejsnjiX) > 0){
//		    			zamikBackground += X-prejsnjiX;
//		    			prejsnjiX=X;
//		    		}else if(zamikBackground > 0 && (X-prejsnjiX) < 0){
//		    			zamikBackground += X-prejsnjiX;
//		    			prejsnjiX=X;
//		    		}
//		    		
//		    		//Spreminjanje barve
//		    		if(Math.abs(zamikBackground) < width/6){
//		    			alphaTile = 255;
//		    			alphaPicture = 150;
//			    		if(sound && !background){
//			    			zvok.gumb3();
//			    		}
//		    			background = true;
//		    		}else{
//		    			alphaTile = 150;
//		    			alphaPicture = 255;
//			    		if(sound && background){
//			    			zvok.gumb3();
//			    		}
//		    			background = false;
//		    		}
//		    	}else{//CLICK
//	    			if(X <= width/2){
//	    				alphaTile = 255;
//	    				alphaPicture = 155;
//	    				zamikBackground=0;
//			    		if(sound && !background){
//			    			zvok.gumb3();
//			    		}
//	    				background = true;
//	    			}else{
//	    				alphaTile = 150;
//	    				alphaPicture = 255;
//	    				zamikBackground= -width/4+1;
//			    		if(sound && background){
//			    			zvok.gumb3();
//			    		}
//	    				background = false;
//	    			}
//		    	}//TODO
		    		if(!click){
			    		if(Math.abs(zamikBackground) < width/4){
			    			zamikBackground += X-prejsnjiX;
			    			prejsnjiX=X;
			    			if(zamikBackground > width /4){
			    				zamikBackground = width/4-1;
			    			}else if(zamikBackground < -width/4){
			    				zamikBackground = -width/4+1;
			    			}
			    		}else if(zamikBackground < width/4 && (X-prejsnjiX) > 0){
			    			zamikBackground += X-prejsnjiX;
			    			prejsnjiX=X;
			    		}else if(zamikBackground > width/4 && (X-prejsnjiX) < 0){
			    			zamikBackground += X-prejsnjiX;
			    			prejsnjiX=X;
			    		}
			    		if(Math.abs(zamikBackground)< width/6){
			    			alphaTile = 150;
			    			alphaPicture = 255;
			    			alphaGallery = 150;
				    		if(sound && TopSecret.PICTURE != background){
				    			zvok.gumb3();
				    		}
			    			background = TopSecret.PICTURE;	    			
			    		}else if(zamikBackground > width/6){
			    			alphaTile = 255;
			    			alphaPicture = 150;
			    			alphaGallery = 150;
				    		if(sound && TopSecret.TILE != background){
				    			zvok.gumb3();
				    		}
			    			background = TopSecret.TILE;
			    		}else{
			    			alphaTile = 150;
			    			alphaPicture = 150;
			    			alphaGallery = 255;
				    		if(sound && TopSecret.GALLERY != background){
				    			zvok.gumb3();
				    		}
			    			background = TopSecret.GALLERY;
			    		}
		    		}else{//CLICK
		    			if( (width/2-width/3-width/20+zamikBackground) < X && X < (width/2-width/8-width/20+zamikBackground)){
		    				alphaTile = 255;
		    				alphaPicture = 150;
		    				alphaGallery = 150;
				    		if(sound && background != TopSecret.TILE){
				    			zvok.gumb3();
				    		}
		    				background = TopSecret.TILE;
		    				zamikBackground = width/4-1;
		    			}else if((width/2+width/5-width/20+zamikBackground) < X){
		    				alphaTile = 150;
		    				alphaPicture = 150;
		    				alphaGallery = 255;
				    		if(sound && background != TopSecret.GALLERY){
				    			zvok.gumb3();
				    		}
		    				background = TopSecret.GALLERY;
		    				zamikBackground = -width/4;
		    			}else if(X < (width/2+width/5-width/20+zamikBackground) && X > (width/2-width/3-width/20+zamikBackground) ){
		    				alphaTile = 150;
		    				alphaPicture = 255;
		    				alphaGallery = 150;
				    		if(sound && background != TopSecret.PICTURE){
				    			zvok.gumb3();
				    		}
		    				background = TopSecret.PICTURE;
		    				zamikBackground = 0;
		    			}
		    		
		    	}
		    	
	    			
	    	}
	    	//SOUND
	    	else if(Y > height*14/20 && Y < height*16/20){
	    		if(!click){
		    		if(zamikSound <= 0 && zamikSound > -width/5){
		    			zamikSound += X-prejsnjiX;
		    			prejsnjiX=X;
		    			if(zamikSound > 0){
		    				zamikSound = -1;
		    			}else if(zamikSound < -width/5){
		    				zamikSound = -width/5+1;
		    			}
		    		}else if(zamikSound < -width/5 && (X-prejsnjiX) > 0){
		    			zamikSound += X-prejsnjiX;
		    			prejsnjiX=X;
		    		}else if(zamikSound > 0 && (X-prejsnjiX) < 0){
		    			zamikSound += X-prejsnjiX;
		    			prejsnjiX=X;
		    		}
		    		
		    		//Spreminjanje barve
		    		if(Math.abs(zamikSound) < width/6){
		    			alphaON = 255;
		    			alphaOFF = 150;
			    		if(!sound){
			    			zvok.gumb3();
			    		}
		    			sound = true;
		    		}else{
		    			alphaON = 150;
		    			alphaOFF = 255;
		    			sound = false;
		    		}
	    		}else{
	    			if(X <= width/2){
	    				alphaON = 255;
	    				alphaOFF = 155;
	    				zamikSound=0;
			    		if(!sound){
			    			zvok.gumb3();
			    		}
	    				sound = true;
	    			}else{
	    				alphaON = 155;
	    				alphaOFF = 255;
	    				sound = false;
	    				zamikSound = -width/6;
	    			}
	    		}
	    		
	    	}
	    	//ABOUT
	    	if(Y > height*16/20 && Y < height*18/20 && m.getAction() == MotionEvent.ACTION_DOWN){
	    		prvaStran.popup();
	    		if(sound && m.getAction() == MotionEvent.ACTION_DOWN){
	    			zvok.gumb2();
	    		}
	    	}
	    	
	    	//HighScore
	    	if(Y > height*18/20  && m.getAction() == MotionEvent.ACTION_DOWN){
	    		prvaStran.highscoreView();
	    		if(sound && m.getAction() == MotionEvent.ACTION_DOWN){
	    			zvok.gumb2();
	    		}
	    	}
	    	
	    	return true;
	    }
	    


	};
	
	public void update() {
		canvas.drawRGB(0, 0, 0);
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, 0, width, height, paint);
		ustvariMenu();
		redrawHandler.sleep(60);
	}
	
	
	class RefreshHandler extends Handler {
		
		@Override
	    public void handleMessage(Message msg) {
	        MenuView.this.update();
	        MenuView.this.invalidate();
	    }

	    public void sleep(long delayMillis) {
	    	this.removeMessages(0);
	        
	    	if(stopUpdate){
	        	sendMessageDelayed(obtainMessage(0), delayMillis);
	        }
	    }
	};
}

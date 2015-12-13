package com.mindblast;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

public class MenuView extends View{
	MainMenu menu;
	
	
	Paint paint = new Paint();
	Paint paintSelected = new Paint();
	Bitmap bitmap;
	Canvas canvas;
	
	Resources res;
	
	Bitmap ozadje;
	Rect ekran;
	
	Bitmap ticTacToe;
	Rect ticTacRect;

	
	//Mere ekrana
	public int height;
	public int width;
	
	public static int gameplay = 2;
	
	//Konec/pauza animacije
	public boolean stopUpdate  = false; //Ce false potem animacija tece,drugace ne.
	//Selected animacija
	private int selected = 0;
	
	boolean anim = true;
	int animacija = 1;
	boolean gametype = true;
	
	
	private RefreshHandler redrawHandler = new RefreshHandler();
	
	//////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	
	/**
	 * Konstruktor
	 **/
	public MenuView(Context context,MainMenu menu){
		super(context);
		
		this.menu = menu;
				
		stopUpdate = false;
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		height = View.MeasureSpec.getSize(heightMeasureSpec);
		width = View.MeasureSpec.getSize(widthMeasureSpec);
		
		setMeasuredDimension(width,height);
		
		ekran = new Rect(0, 0, width, height);
		
		
		res = getResources();
		
		Typeface font = Typeface.createFromAsset(res.getAssets(), "Harngton.ttf");
		paint.setTypeface(font);
		paint.setShadowLayer(res.getDimension(R.dimen.dva), res.getDimension(R.dimen.ena),res.getDimension(R.dimen.ena), Color.rgb(104, 171, 239));
		paint.setTextAlign(Align.CENTER);
		
		paintSelected.setTypeface(font);
		paintSelected.setShadowLayer(1.5f, 1, 1, Color.rgb(255, 0, 0));
		paintSelected.setTextAlign(Align.CENTER);
		selected = 0;
		
		
		
		bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);

		canvas = new Canvas(bitmap);
		
		this.setBackgroundResource(R.drawable.aluminijozadje);
		
		
		if(gametype){
			ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ena);
		}else{
			ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.dvanajst);
		}
		ticTacToe = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.tictactoe);

		
		
		this.setOnTouchListener(touchlistener);
		
		
		
		update();
	}
	

	public boolean back(){
		if(gametype){
			if(anim) return true;
			else{
				resetAnimation();
				return false;
			}
		}else{
			resetAnimation();
			return false;
		}
	}
	
	public void ticTacToe(){
		if(gametype){
		switch(animacija){
			case 1:
				ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.dva);
				animacija++;
				break;
			case 2:
				ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.tri);
				animacija++;
				break;
			case 3:
				ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.stiri);
				animacija++;
				break;
			case 4:
				ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pet);
				animacija++;
				break;
			case 5:
				ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sest);
				animacija++;
				break;
			case 6:
				ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sedem);
				animacija++;
				break;
			case 7:
				ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.osem);
				animacija++;
				break;
			case 8:
				ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.devet);
				animacija++;
				break;
			case 9:
				ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.deset);
				animacija++;
				break;
			case 10:
				ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.enajst);
				animacija++;

				break;
			case 11:
				ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.dvanajst);
				gametype=false;
				break;
		}
		}else{
			paint.setTextAlign(Align.CENTER);
			
			
			switch(selected){
				case 0:
					paint.setTextSize(res.getDimension(R.dimen.player_vs_computer));
					canvas.drawText(res.getString(R.string.player_vs_computer), width/2, height/2, paint);
					
					paint.setTextSize(res.getDimension(R.dimen.player_vs_player));
					canvas.drawText(res.getString(R.string.player_vs_player), width/2, height/2+res.getDimension(R.dimen.petnajst)+res.getDimension(R.dimen.player_vs_computer), paint);
					
					paint.setTextSize(res.getDimension(R.dimen.multiplayer_online));
					canvas.drawText(res.getString(R.string.multiplayer_online), width/2, height/2+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.player_vs_computer)+res.getDimension(R.dimen.player_vs_player), paint);
					break;
				case 1:
					paintSelected.setTextSize(res.getDimension(R.dimen.player_vs_computer)+res.getDimension(R.dimen.dva));
					canvas.drawText(res.getString(R.string.player_vs_computer), width/2, height/2, paintSelected);
					
					paint.setTextSize(res.getDimension(R.dimen.player_vs_player));
					canvas.drawText(res.getString(R.string.player_vs_player), width/2, height/2+res.getDimension(R.dimen.petnajst)+res.getDimension(R.dimen.player_vs_computer), paint);
					
					paint.setTextSize(res.getDimension(R.dimen.multiplayer_online));
					canvas.drawText(res.getString(R.string.multiplayer_online), width/2, height/2+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.player_vs_computer)+res.getDimension(R.dimen.player_vs_player), paint);
					break;
				case 2:
					paint.setTextSize(res.getDimension(R.dimen.player_vs_computer));
					canvas.drawText(res.getString(R.string.player_vs_computer), width/2, height/2, paint);
					
					paintSelected.setTextSize(res.getDimension(R.dimen.player_vs_player)+res.getDimension(R.dimen.dva));
					canvas.drawText(res.getString(R.string.player_vs_player), width/2, height/2+res.getDimension(R.dimen.petnajst)+res.getDimension(R.dimen.player_vs_computer), paintSelected);
					
					paint.setTextSize(res.getDimension(R.dimen.multiplayer_online));
					canvas.drawText(res.getString(R.string.multiplayer_online), width/2, height/2+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.player_vs_computer)+res.getDimension(R.dimen.player_vs_player), paint);
					break;
				case 3:
					paint.setTextSize(res.getDimension(R.dimen.player_vs_computer));
					canvas.drawText(res.getString(R.string.player_vs_computer), width/2, height/2, paint);
					
					paint.setTextSize(res.getDimension(R.dimen.player_vs_player));
					canvas.drawText(res.getString(R.string.player_vs_player), width/2, height/2+res.getDimension(R.dimen.petnajst)+res.getDimension(R.dimen.player_vs_computer), paint);
					
					paintSelected.setTextSize(res.getDimension(R.dimen.multiplayer_online)+res.getDimension(R.dimen.dva));
					canvas.drawText(res.getString(R.string.multiplayer_online), width/2, height/2+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.player_vs_computer)+res.getDimension(R.dimen.player_vs_player), paintSelected);
					break;
			}
		
		}
		
	}
	
	
	public void resetAnimation(){
		ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ena);
		gametype = true;
		anim = true;
		animacija = 1;
	}
	
	/**
	 * Metoda update, ki refresha sliko menuja vsake 40ms = 25fps
	 * Metoda klièe narisiMenu
	 */
	public void update(){
		//canvas.drawBitmap(ozadje, 0, 0, paint);
		canvas.drawBitmap(ozadje, null, ekran, null);
	
		narisiMenu();
		
		redrawHandler.sleep(25); // 40 = 25fps
	}
	
	
	@SuppressWarnings("static-access")
	public void narisiMenu(){
		if(anim){
			canvas.drawBitmap(ticTacToe,(width-ticTacToe.getWidth())/2,height/2, paint);
		}else{
			ticTacToe();
		}
		
		
		paint.setTextSize(res.getDimension(R.dimen.nickname));
		paintSelected.setTextSize(res.getDimension(R.dimen.nickname)+res.getDimension(R.dimen.dva));
		switch(selected){
		case 4:
			canvas.drawText(menu.IME, width/2, height-res.getDimension(R.dimen.pet), paintSelected);
			break;
		default:
				canvas.drawText(menu.IME, width/2, height-res.getDimension(R.dimen.pet), paint);
				break;
		}
		
	}
	
	
	/**
	 * Touch listenerji
	 */
	private OnTouchListener touchlistener = new OnTouchListener() {
	    public boolean onTouch(View v, MotionEvent m) {
	    	if(m.getAction() == MotionEvent.ACTION_DOWN){
	    		if(gametype){
	    			anim = false;
	    		}else{
//	    			float X=m.getX();
	    			float Y=m.getY();
	    			
	    			if(Y > height/2-R.dimen.player_vs_computer && Y < height/2+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.player_vs_computer)){
	    				//Player vs AI
	    				selected = 1;
	    				
	    			}else if(Y > height/2+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.player_vs_computer) && Y < height/2+res.getDimension(R.dimen.petindvajset)+res.getDimension(R.dimen.player_vs_computer)+res.getDimension(R.dimen.player_vs_player)){
	    				//PvP
	    				selected = 2;
	    			}else if( Y > height/2+res.getDimension(R.dimen.petindvajset)+res.getDimension(R.dimen.player_vs_computer)+res.getDimension(R.dimen.player_vs_player) && Y < height-res.getDimension(R.dimen.petnajst)-res.getDimension(R.dimen.nickname)){
	    				//Online
	    				selected = 3;
	    			}else if(Y > height-res.getDimension(R.dimen.petnajst)){
	    				selected = 4;
	    			}else{
	    				selected = 0;
	    			}
	    		}
	    	
	    	}else if(m.getAction() == MotionEvent.ACTION_MOVE){
	    		if(gametype){
	    			anim = false;
	    		}else{
//	    			float X=m.getX();
	    			float Y=m.getY();
	    			
	    			if(Y > height/2-R.dimen.player_vs_computer && Y < height/2+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.player_vs_computer)){
	    				//Player vs AI
	    				selected = 1;
	    				
	    			}else if(Y > height/2+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.player_vs_computer) && Y < height/2+res.getDimension(R.dimen.petindvajset)+res.getDimension(R.dimen.player_vs_computer)+res.getDimension(R.dimen.player_vs_player)){
	    				//PvP
	    				selected = 2;
	    			}else if( Y > height/2+res.getDimension(R.dimen.petindvajset)+res.getDimension(R.dimen.player_vs_computer)+res.getDimension(R.dimen.player_vs_player) && Y < height-res.getDimension(R.dimen.petnajst)-res.getDimension(R.dimen.nickname)){
	    				//Online
	    				selected = 3;
	    			}else if(Y > height-res.getDimension(R.dimen.petnajst)){

		    			selected = 4;
	    			}else{
	    				selected = 0;
	    			}
	    		}	
	    	}else if(m.getAction() == MotionEvent.ACTION_UP){
	    		//TODO 
	    		//zaenkrat gre samo v igro ko klikneš
	    		//menu.newGame();
	    		if(gametype){
	    			anim = false;
	    		}else{
//	    			float X=m.getX();
	    			float Y=m.getY();
	    			
	    			if(Y > height/2-R.dimen.player_vs_computer && Y < height/2+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.player_vs_computer)){
	    				//Player vs AI
	    				gameplay = 2;
	    				menu.newGame(true);
	    				
	    			}else if(Y > height/2+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.player_vs_computer) && Y < height/2+res.getDimension(R.dimen.petindvajset)+res.getDimension(R.dimen.player_vs_computer)+res.getDimension(R.dimen.player_vs_player)){
	    				//PvP
	    				gameplay = 1;
	    				menu.newGame(true);
	    			}else if(Y > height/2+res.getDimension(R.dimen.petindvajset)+res.getDimension(R.dimen.player_vs_computer)+res.getDimension(R.dimen.player_vs_player) && Y < height-res.getDimension(R.dimen.petnajst)-res.getDimension(R.dimen.nickname)){
	    				//Online
	    				gameplay = 3;
	    				menu.newGame(false);
	    			}else if(Y > height-res.getDimension(R.dimen.petnajst)-res.getDimension(R.dimen.nickname)){
		    			menu.enterYourName();
		    			selected = 0;
	    			}
	    		}
	    	}
	    	
	    	return true;
	    }
	};
	
	
	protected void onDraw(Canvas canvas){
		canvas.drawBitmap(bitmap,0,0,paint);
	}
	
	
	/**
	 * Poskrbi za updejtanje risalne plosce
	 *
	 */
	class RefreshHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg){
			MenuView.this.update();
			MenuView.this.invalidate();
		}
		
		public void sleep(long delayMillis){
			this.removeMessages(0);
			
			if(!stopUpdate){
				sendMessageDelayed(obtainMessage(0),delayMillis);
			}
		}
	};
}

package com.mindblast;

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

public class LobbyView extends View{
	Lobby lobby;
	ArrayList<String> seznami = new ArrayList<String>();
	LoadLobby pl;
	
	
	Paint paint = new Paint();
	Bitmap bitmap;
	Canvas canvas;
	
	//Mere ekrana
	public int height;
	public int width;
	
	//Konec/pauza animacije
	public boolean stopUpdate  = false; //Ce false potem animacija tece,drugace ne.
	
	private RefreshHandler redrawHandler = new RefreshHandler();
	
	/**
	 * Ustavi thread, ki prejema posodobitve lobbya v razredu
	 * LoadLobby
	 */
	public void ustaviLoadLobby(){
		pl.active = false;
	}
	
	
	/**
	 * Konstruktor
	 **/
	public LobbyView(Context context,Lobby lobby, Handler lobbyHandler){
		super(context);
		
		this.lobby = lobby;
		seznami.add("loading lobby");
		stopUpdate = false;
//		pl = new LoadLobby("188.230.131.244",17525,this,lobbyHandler);
//		pl.start();
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		height = View.MeasureSpec.getSize(heightMeasureSpec);
		width = View.MeasureSpec.getSize(widthMeasureSpec);
		
		setMeasuredDimension(width,height);
		
		bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		
		this.setOnTouchListener(touchlistener);
		
		update();
	}

	
	public void narisilobby(){
		paint.setColor(Color.WHITE);
		paint.setTextSize(15);
//		canvas.drawText("Loading lobby", width/8, height/3, paint);
		int temph = 15;
		for(int i=0;i<seznami.size();i++){
			canvas.drawText(seznami.get(i), 0,temph,paint);
			temph+=16;
		}
	}
	
	/**
	 * Touch listenerji
	 */
	private OnTouchListener touchlistener = new OnTouchListener() {
	    public boolean onTouch(View v, MotionEvent m) {
	    	if(m.getAction() == MotionEvent.ACTION_DOWN){
	    		//TODO 
	    		//zaenkrat gre samo v igro ko klikneš
	    		
	    	}
	    	
	    	return true;
	    }
	};
	
	
	/**
	 * Metoda update, ki refresha sliko menuja vsake 40ms = 25fps
	 * Metoda klièe narisiMenu
	 */
	public void update(){
		canvas.drawRGB(95, 164, 233);
//		paint.setColor(Color.BLACK);
//		canvas.drawRect(0,0,width,height, paint);
		
		narisilobby();
		redrawHandler.sleep(40); // 40 = 25fps
	}
	
	
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
			LobbyView.this.update();
			LobbyView.this.invalidate();
		}
		
		public void sleep(long delayMillis){
			this.removeMessages(0);
			
			if(!stopUpdate){
				sendMessageDelayed(obtainMessage(0),delayMillis);
			}
		}
	};
}

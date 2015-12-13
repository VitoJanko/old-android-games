package com.mindblast;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class TicTacToeView extends View {
	TicTacToe ticTacToe;
	Player player;
	TicTacToeAI AI;
	
	public int gameplay = 1; //1 = singelplayer , 2 = vs AI , 3 = vs player
	
	int[][] igralnaPlosca = {{0,0,0},
							{0,0,0},
							{0,0,0}};
	
	int[][] zmagaNiz = new int[3][3];
	
	ArrayList<float[]> rdeca = new ArrayList<float[]>();
	ArrayList<float[]> modra = new ArrayList<float[]>();
	ArrayList<float[]> zelena = new ArrayList<float[]>();
	ArrayList<float[]> rumena = new ArrayList<float[]>();
	ArrayList<float[]> magenta = new ArrayList<float[]>();
	ArrayList<float[]> siva = new ArrayList<float[]>();
	
	Paint paint = new Paint();
	Paint paint2 = new Paint();
	Bitmap bitmap;
	Canvas canvas;
	
	Resources res;
	Bitmap ozadje;
	Rect ekran;
	
	Bitmap kriz;
	Bitmap kroz;
	
	Rect ena;
	Rect dva;
	Rect tri;
	Rect stiri;
	Rect pet;
	Rect sest;
	Rect sedem;
	Rect osem;
	Rect devet;
	
	int sirinaCelice = 0;
	
	
	/////////////////////////////////////////////////
	////////Crte igrisca ///////////////////////////
	////////////////////////////////////////////////
	
	float odmikZg;
	float odmikSp;
	float prvaNavpicna;
	float drugaNavpicna;
	float prvaVodoravna;
	float drugaVodoravna;
	float tretjaVodoravna;
	
	float rdecaX;
	float rdecaY;
	int rdecaSmer = 0;
	int hitrost = 25;
	int hitrost2 = 50;
	int alfa = 255;
	
	float modraX = 0;
	float modraY = 0;
	int modraSmer = 2;
	
	float zelenaX = 0;
	float zelenaY = 0;
	int zelenaSmer = 0;
	
	float sivaX = 0;//to je svetlo plava
	float sivaY = 0;
	int sivaSmer = 3;
	
	float rumenaX = 0;
	float rumenaY = 0;
	int rumenaSmer = 0;
	
	float magentaX = 0;
	float magentaY = 0;
	int magentaSmer = 0;
	
	////////////////////////////////////////////
	/////////////Konec crt igrisca//////////////
	////////////////////////////////////////////
	
	///////////////////////////////
	/////END ANIMACIJA////////////
	int hitrostAnimacije = 0;
	int vrt = 0;
	int stl = 0;
	boolean endAnimacija = true;
	/////////////////////////////
	/////////////////////////////
	
	///////////////////////////////
	/////////NOVA IGRA/////////////
	//////////////////////////////
	public boolean playAgain = false;
	public boolean waiting = false;
	public int waitingStevec1 = 0;
	public int waitingStevec2 = 0;
	
	///////////////////////////////
	////////////CHAT///////////////
	///////////////////////////////
	private String msg = "";
	private boolean newMsg = false;
	public boolean unreadMsg = false;
	private int chat = 0;
	
	private int misli = 0; //animacija za misli ko AI isce potezo
	
	public boolean krizec = false;
	private boolean player1start = false;
	
	//Mere ekrana
	public int height;
	public int width;
	
	float getX;
	float getY;
	
	public boolean END = false;
	private int WON = 0; //0 = draw , 1 = player 1  WON, 2 = player 2 WON
	
	//Konec/pauza animacije
	public boolean stopUpdate  = false; //Ce false potem animacija tece,drugace ne.
	
	private RefreshHandler redrawHandler = new RefreshHandler();
	
	
	private String player1 = MainMenu.IME;
	private String player2 = "Player 2";
	private int player1Score = 0;
	private int player2Score = 0;
	private int game = 1;
	
	///////////////////////////
	///////MULTIPLAYER/////////
	///////////////////////////
	private long myLastTime = 0;
	private int Red = 255;
	private int Green = 0;
	private int Blue = 0;
	private boolean toastIsShown = false;
	private boolean vibrator1 = false;
	private boolean vibrator2 = false;
	private boolean vibrator3 = false;
	
	private long playAgainTime = 0;
	private int timeLeft = 60;

	
	
	public boolean thinking = true;
	
	/////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	//////////////////////KONEC SPREMENLJIVK/////////////////////////////////
	//////////////////////Zacatej Programa///////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	
	/**
	 * Konstruktor
	 **/
	public TicTacToeView(Context context,TicTacToe ticTacToe,int gameplay){
		super(context);
		
		this.ticTacToe = ticTacToe;
		this.gameplay = gameplay;
		
		stopUpdate = false;
		
		switch(this.gameplay){
		case 3://ONLINE
			player = new Player("188.230.131.244",Lobby.PORT,this);
			player.start();
			player2 = Lobby.player2;
			break;
		case 2://AI
			AI = new TicTacToeAI(this);
			AI.setPriority(4);
			AI.start();
			krizec = true;
			player2 = "AI";
			int temp2 = (int)(Math.random()*10);
			if(temp2 > 4){
				krizec = true;
				player1start = true;
			}else{
				krizec = false;
				player1start = false;
			}
//			krizec = true;
			break;
		default ://Player vs Player 1 phone
			int temp = (int)(Math.random()*10);
			player1="X";
			player2="O";
			if(temp > 4){
				krizec = true;
				player1start = true;
			}else{
				krizec = false;
				player1start = false;
			}
		}
		
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		height = View.MeasureSpec.getSize(heightMeasureSpec);
		width = View.MeasureSpec.getSize(widthMeasureSpec);
		
		setMeasuredDimension(width,height);
		
		ekran = new Rect(0,0,width,height);		
		
		res = getResources();
		
		Typeface font = Typeface.createFromAsset(res.getAssets(), "Harngton.ttf");
		paint.setTypeface(font);
		
		this.setBackgroundResource(R.drawable.aluminijozadje);
		ozadje = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.aluminijozadje);
		kriz = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.krizec);
		kroz = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.krogec);

		
		bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		
		this.setOnTouchListener(touchlistener);
	
		pripraviOkvir();
		update();
	}
	
	public void pripraviOkvir(){
		odmikZg = height/15;
		odmikSp = 6*height/20;
		
		getX = width / 3;
		getY = (height-odmikZg-odmikSp) / 3;
		
		prvaVodoravna = (height-odmikZg-odmikSp)/3;
		drugaVodoravna = 2*prvaVodoravna;//-odmikZg;
		tretjaVodoravna = 3*prvaVodoravna;//-2*odmikZg;
		prvaVodoravna += odmikZg;
		drugaVodoravna += odmikZg;
		tretjaVodoravna += odmikZg;
		
		prvaNavpicna = width/3;
		drugaNavpicna = 2*prvaNavpicna;	
		
		ena = new Rect(0,(int)odmikZg,(int)prvaNavpicna,(int)prvaVodoravna);
		dva = new Rect((int)prvaNavpicna,(int)odmikZg,(int)drugaNavpicna,(int)prvaVodoravna);
		tri = new Rect((int)drugaNavpicna,(int)odmikZg,(int)width,(int)prvaVodoravna);
		
		stiri = new Rect(0,(int)prvaVodoravna,(int)prvaNavpicna,(int)drugaVodoravna);
		pet = new Rect((int)prvaNavpicna,(int)prvaVodoravna,(int)drugaNavpicna,(int)drugaVodoravna);
		sest = new Rect((int)drugaNavpicna,(int)prvaVodoravna,(int)width,(int)drugaVodoravna);
		
		sedem = new Rect(0,(int)drugaVodoravna,(int)prvaNavpicna,(int)tretjaVodoravna);
		osem = new Rect((int)prvaNavpicna,(int)drugaVodoravna,(int)drugaNavpicna,(int)tretjaVodoravna);
		devet = new Rect((int)drugaNavpicna,(int)drugaVodoravna,(int)width,(int)tretjaVodoravna);
		
		rdecaX = 0;
		rdecaY = odmikZg;
		rdecaSmer = 0;
		
		modraX = width;
		modraY = tretjaVodoravna;
		modraSmer = 2;
		
		zelenaX = drugaNavpicna;
		zelenaY = odmikZg;
		zelenaSmer = 1;
		
		sivaX = prvaNavpicna;
		sivaY = tretjaVodoravna;
		sivaSmer = 3;
		
		rumenaX = 0;
		rumenaY = drugaVodoravna;
		rumenaSmer = 3;
		
		magentaX = width;
		magentaY = prvaVodoravna;
		magentaSmer = 0;
		
		hitrost = (int) res.getDimension(R.dimen.trinajst);
		hitrost2 = (int) res.getDimension(R.dimen.petindvajset);
		
		if(!krizec && gameplay == 2){
			int prviX = (int)(Math.random()*3);
			int prviY = (int)(Math.random()*3);
			zamenjajPlosco(prviX,prviY,2);
			krizec = true;
		}
	}
	
	public void newGame(){
		for(int i=0;i<igralnaPlosca.length;i++){
			for(int j=0;j<igralnaPlosca[i].length;j++){
				igralnaPlosca[i][j] = 0;
			}
		}
		
		krizec = !player1start;
		player1start = !player1start;
		
		END = false;
		endAnimacija = true;
		WON = 0;
		
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				zmagaNiz[i][j] = 0;
			}
		}
		vrt = 0;
		stl = 0;
		
		if(!krizec && gameplay == 2){
			int prviX = (int)(Math.random()*3);
			int prviY = (int)(Math.random()*3);
			zamenjajPlosco(prviX,prviY,2);
			krizec = true;
		}
		
		if(gameplay == 3 && !krizec && !END){
			startTimer();
		}
		
	}
	
	/**
	 * Preveri ali je kdo konèal igro. Preveri tako da zmnoži vrednosti v polju. Èe so 3 enke je rezultat 1 èe so tri 2kje je rezultat 8. 
	 * Èe je kaj drugega umes je rezultat vedno drugaèen.
	 */
	public void checkEndgame(){
		int win = 0;
		int temp = 0;
		
		//vrstice
		for(int i=0;i<3;i++){
			temp=igralnaPlosca[i][0]*igralnaPlosca[i][1]*igralnaPlosca[i][2];
			if(temp == 1){
				win = 1;
//				zmagaNiz[0][0] = i;
//				zmagaNiz[0][1] = 0;
//				zmagaNiz[1][0] = i;
//				zmagaNiz[1][1] = 1;
//				zmagaNiz[2][0] = i;
//				zmagaNiz[2][1] = 2;
				
				zmagaNiz[i][0] = 1;
				zmagaNiz[i][1] = 1;
				zmagaNiz[i][2] = 1;
				
			}else if(temp == 8){
				win = 8;
				zmagaNiz[i][0] = 1;
				zmagaNiz[i][1] = 1;
				zmagaNiz[i][2] = 1;
			}
		}
		
		//stolpci
		for(int i=0;i<3;i++){
			temp=igralnaPlosca[0][i]*igralnaPlosca[1][i]*igralnaPlosca[2][i];
			if(temp == 1){
				win = 1;
//				zmagaNiz[0][0] = 0;
//				zmagaNiz[0][1] = i;
//				zmagaNiz[1][0] = 1;
//				zmagaNiz[1][1] = i;
//				zmagaNiz[2][0] = 2;
//				zmagaNiz[2][1] = i;
				
				zmagaNiz[0][i] = 1;
				zmagaNiz[1][i] = 1;
				zmagaNiz[2][i] = 1;
				
			}else if(temp == 8){
				win = 8;
				zmagaNiz[0][i] = 1;
				zmagaNiz[1][i] = 1;
				zmagaNiz[2][i] = 1;
			}
		}
		
		temp=igralnaPlosca[0][0]*igralnaPlosca[1][1]*igralnaPlosca[2][2];
		if(temp == 1){
			win = 1;
			zmagaNiz[0][0] = 1;
			zmagaNiz[1][1] = 1;
			zmagaNiz[2][2] = 1;
		}else if(temp == 8){
			win = 8;
			zmagaNiz[0][0] = 1;
			zmagaNiz[1][1] = 1;
			zmagaNiz[2][2] = 1;
		}
		temp=igralnaPlosca[0][2]*igralnaPlosca[1][1]*igralnaPlosca[2][0];
		if(temp == 1){
			win = 1;
			zmagaNiz[0][2] = 1;
			zmagaNiz[1][1] = 1;
			zmagaNiz[2][0] = 1;
		}else if(temp == 8){
			win = 8;
			zmagaNiz[0][2] = 1;
			zmagaNiz[1][1] = 1;
			zmagaNiz[2][0] = 1;
		}
		
		if(win== 1){
//			krizec = false;
			END = true;
			WON = 1;
			player1Score++;
			game++;
		}else if(win == 8){
//			krizec = false;
			END = true;
			WON = 2;
			player2Score++;
			game++;
		}else{
			boolean tempDraw = true;
			
			for(int i=0;i < igralnaPlosca.length;i++){
				for(int j=0;j<igralnaPlosca[i].length;j++){
					if(igralnaPlosca[i][j] == 0){
						tempDraw = false;
						i=3;
						break;
					}
				}
			}
			
			if(tempDraw){
				END = true;
				WON = 0;
				game++;
			}
			
		}
	}
	
	/**
	 * Zakljuèi activity
	 */
	public void endGame(){
		ticTacToe.endGame();
	}
	
	/**
	 * Konca igro, ko pritisnemo na tipko back.
	 */
	public void backEnd(){
		if(gameplay == 3){
			player.end();
		}else{
			endGame();
		}
	}
	
	/**
	 * 
	 * @param X
	 * @param Y
	 * @param vrednost
	 * 
	 * Ko nam nasprotnik pošlje koordinate, se klièe ta metoda, da jih izriše na ekran.
	 */
	public void zamenjajPlosco(int X,int Y,int vrednost){
		if(gameplay == 3 && !krizec){
			startTimer();
		}
		
		igralnaPlosca[X][Y] = vrednost;
		krizec=true;
		checkEndgame();
		thinking = true;
	}
	
	/**
	 * Nariše namizje (aka èrte)
	 */
	private void narisiIgro(){
		if(gameplay == 3 && krizec && !END){
			timer();
		}
		paint.setColor(Color.BLUE);
		
		paint.setTextSize(res.getDimension(R.dimen.pvp));
		paint.setAntiAlias(true);
//		paint.setTextAlign(Align.CENTER);
//		canvas.drawText(player1+" "+String.valueOf(player1Score)+":"+String.valueOf(game)+":"+String.valueOf(player2Score)+" "+player2, width/2, odmikZg-res.getDimension(R.dimen.pet), paint);
		paint.setTextAlign(Align.LEFT);
		canvas.drawText(player1+" ", 0, odmikZg-res.getDimension(R.dimen.pet), paint);
		
		paint.setTextAlign(Align.RIGHT);
		canvas.drawText(" "+player2, width, odmikZg-res.getDimension(R.dimen.pet), paint);
		
		paint.setTextAlign(Align.CENTER);
		canvas.drawText(String.valueOf(player1Score)+":"+String.valueOf(game)+":"+String.valueOf(player2Score), width/2, odmikZg-res.getDimension(R.dimen.pet), paint);
		
		
		
		
//		paint.setStyle(Style.STROKE);
		
		
//		float tempSirina = (height-odmikZg-odmikSp)/3;
//		float tempVis = width/3;
		
		
//		canvas.drawLine(0,odmikZg, width,odmikZg, paint); //zgornja
//		for(int i=1;i<3;i++){
//			canvas.drawLine(0, tempSirina+odmikZg, width, tempSirina+odmikZg, paint); //vodoravne
//			canvas.drawLine(tempVis, 0+odmikZg, tempVis, height-odmikSp, paint); //vertikalne
//			
//			tempSirina *= 2;
//			tempVis *= 2;
//		}
//		canvas.drawLine(0, tempSirina-odmikSp, width, tempSirina-odmikSp, paint); // spodnja crta
//		
		
		if(!END){
			paint.setTextSize(res.getDimension(R.dimen.nextturn));
			if(gameplay != 2){//PLAYER VS PLAYER/ONLINE
			if(krizec){
				if(gameplay == 3){
					canvas.drawText(res.getString(R.string.it_is)+" "+res.getString(R.string.your)+" "+res.getString(R.string.turn), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset), paint);
				}else{
					canvas.drawText(res.getString(R.string.it_is)+" "+player1+" "+res.getString(R.string.turn), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset), paint);					
				}
			}else{
				canvas.drawText(res.getString(R.string.it_is)+" "+player2+" "+res.getString(R.string.turn), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset), paint);
			}
			}else{//AI
				if(krizec){
					canvas.drawText(res.getString(R.string.it_is)+" "+player1+" "+res.getString(R.string.turn), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset), paint);
					misli = 0;
				}else{
					if(misli < 5){
						canvas.drawText(res.getString(R.string.ai_thinking1), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset), paint);
						misli ++;
					}else if(misli < 10){
						canvas.drawText(res.getString(R.string.ai_thinking2), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset), paint);
						misli++;
					}else if(misli < 15){
						canvas.drawText(res.getString(R.string.ai_thinking3), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset), paint);
						misli++;
					}else{
						canvas.drawText(res.getString(R.string.ai_thinking1), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset), paint);
						misli = 1;
					}
				}
			}
		}else{
			paint.setTextSize(res.getDimension(R.dimen.endmessage));
			switch(WON){
			case 0:
				canvas.drawText(res.getString(R.string.draw), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset), paint);
				break;
			case 1:
				canvas.drawText(player1+" "+res.getString(R.string.has_won), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset), paint);
				break;
			case 2:
				canvas.drawText(player2+" "+res.getString(R.string.has_won), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset), paint);
				break;
			}
			
			if(!playAgain){
			////////////////
			///PLAY AGAIN///
			////////////////
				if(gameplay != 3){
					paint.setTextSize(res.getDimension(R.dimen.playagain));
					canvas.drawText(res.getString(R.string.play_again), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.pet)+res.getDimension(R.dimen.endmessage), paint);
					paint.setTextSize(res.getDimension(R.dimen.yes));
					canvas.drawText(res.getString(R.string.yes), width/4, tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.endmessage)+res.getDimension(R.dimen.playagain), paint);
					paint.setTextSize(res.getDimension(R.dimen.no));
					canvas.drawText(res.getString(R.string.no), width*3/4, tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.endmessage)+res.getDimension(R.dimen.playagain), paint);
				}else{
					long tempTime = System.currentTimeMillis()-playAgainTime;
					if(tempTime > 999){
						timeLeft --;
						playAgainTime = System.currentTimeMillis();
					}
					
					paint.setTextSize(res.getDimension(R.dimen.playagain));
					canvas.drawText(res.getString(R.string.play_again)+" ("+String.valueOf(timeLeft)+")", width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.pet)+res.getDimension(R.dimen.endmessage), paint);
					paint.setTextSize(res.getDimension(R.dimen.yes));
					canvas.drawText(res.getString(R.string.yes), width/4, tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.endmessage)+res.getDimension(R.dimen.playagain), paint);
					paint.setTextSize(res.getDimension(R.dimen.no));
					canvas.drawText(res.getString(R.string.no), width*3/4, tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.endmessage)+res.getDimension(R.dimen.playagain), paint);
				
					if(timeLeft <= 0){
						player.end();
					}
				}
			}else{
			////////////////////////
			///PLAY AGAIN REQUEST///
			////////////////////////
				if(!waiting){
					long tempTime = System.currentTimeMillis()-playAgainTime;
					if(tempTime > 999){
						timeLeft --;
						playAgainTime = System.currentTimeMillis();
					}
					
					paint.setTextSize(res.getDimension(R.dimen.wants_to_play_again));
					canvas.drawText(player2+" "+res.getString(R.string.wants_to_play_again)+" ("+String.valueOf(timeLeft)+")", width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.pet)+res.getDimension(R.dimen.endmessage), paint);
					paint.setTextSize(res.getDimension(R.dimen.yes));
					canvas.drawText(res.getString(R.string.yes), width/4, tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.endmessage)+res.getDimension(R.dimen.playagain), paint);
					paint.setTextSize(res.getDimension(R.dimen.no));
					canvas.drawText(res.getString(R.string.no), width*3/4, tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.endmessage)+res.getDimension(R.dimen.playagain), paint);
				
					if(timeLeft <= 0){
						player.end();
					}
				}else{
					paint.setTextSize(res.getDimension(R.dimen.waiting));
					switch(waitingStevec1){
					case 0:
						canvas.drawText(res.getString(R.string.waiting_opponent)+" "+player2+" "+res.getString(R.string.ena_pika), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.pet)+res.getDimension(R.dimen.endmessage), paint);
						waitingStevec2++;
						break;
					case 1:
						canvas.drawText(res.getString(R.string.waiting_opponent)+" "+player2+" "+res.getString(R.string.dve_pike), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.pet)+res.getDimension(R.dimen.endmessage), paint);
						waitingStevec2++;
						break;
					default:
						canvas.drawText(res.getString(R.string.waiting_opponent)+" "+player2+" "+res.getString(R.string.tri_pike), width/2, tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.pet)+res.getDimension(R.dimen.endmessage), paint);
						waitingStevec2++;
						break;	
					}
					
					if(waitingStevec2 > 10){
						waitingStevec2 = 0;
						if(waitingStevec1 != 2){
							waitingStevec1++;
						}else{
							waitingStevec1 = 0;
						}
					}
					
				}
			}
		}
		
		narisiFigure();
		
		if(gameplay == 3)	obdelajMsg(false,"");
		
		drawRdeca();
		drawModra();
		drawRumena();
		drawZelena();
		drawSiva();
		drawMagenta();
		if(END){
			endAnimacija();
//			if(!krizec && gameplay == 2){
//				AI.end();
//			}
		}
		
		
		if(!krizec && gameplay == 2 && !END && thinking){
			thinking = false;
			AI.makeMove(igralnaPlosca);
		}
	}
	
	public void startTimer(){
		Red = 255;
		Green = 255;
		Blue = 255;
		myLastTime = System.currentTimeMillis()+15000;
		toastIsShown = false;
		vibrator1 = true;
		vibrator2 = true;
		vibrator3 = true;
	}
	
	private void timer(){
		long temp = myLastTime - System.currentTimeMillis();
		float procent = width* temp/15000;
		
		if(Green > 0){
			Green --;
			Blue --;
		}
		
		if(temp <= 2000){
			if(vibrator1){
				ticTacToe.vibrate(1000);
				vibrator1 = false;
			}
		}else if (temp <= 3500){
			if(vibrator2){
				ticTacToe.vibrate(500);
				vibrator2 = false;
			}
		}else if(temp <= 5000){
			ticTacToe.cancelDialog();
			if(vibrator3){
				ticTacToe.vibrate(100);
				vibrator3 = false;
			}
			
			if(!toastIsShown){
				Toast.makeText(getContext(), res.getString(R.string.fife_s_left), Toast.LENGTH_SHORT).show();
				toastIsShown = true;
			}
		}
		if(temp <= 0){
			//TODO NI VEÈ ÈASA
			Toast.makeText(getContext(), "Your time is up!", Toast.LENGTH_SHORT).show();
			player.end();
			
		}else{
			paint.setColor(Color.rgb(Red, Green, Blue));
//			paint.setColor(Color.RED);
			canvas.drawRect(0, 0, procent, odmikZg, paint);
		}
	}
	
	public synchronized void obdelajMsg(boolean player,String msge){
		paint.setTextSize(res.getDimension(R.dimen.chat));
		paint.setTextAlign(Align.LEFT);
		
		
		if(player){
			msg += msge;
			newMsg = true;
		}else{
			if(newMsg){
				ticTacToe.zamenjajText(player2, msg);
				msg = "";
				newMsg = false;
				unreadMsg = true;
			}
			if(unreadMsg){
				if(chat < 20){
					canvas.drawText(res.getString(R.string.new_message), width/4, height-res.getDimension(R.dimen.chat), paint);
					chat++;
				}else{
					canvas.drawText(res.getString(R.string.new_message_dva), width/4, height-res.getDimension(R.dimen.chat), paint);
					chat++;
					if(chat > 30) chat = 0;
				}
			}else{
				canvas.drawText(res.getString(R.string.chat), width/4, height-res.getDimension(R.dimen.chat), paint);
			}
		}
	}
	
	public void endAnimacija(){
		hitrostAnimacije ++;
		if(endAnimacija && hitrostAnimacije > 8){
				hitrostAnimacije = 0;
//				boolean tempBol = true;
//				for(int i=0;i<3;i++){
//					if(WON != 0 && ((vrt == zmagaNiz[i][1] && stl == zmagaNiz[i][0]) || igralnaPlosca[stl][vrt] == 0)){
//						tempBol = false;
//						hitrostAnimacije = 21;
//					}
//				}
//				
//				if(tempBol){
//					igralnaPlosca[stl][vrt] = 0;
//				}
				
				if(igralnaPlosca[stl][vrt] == 0) hitrostAnimacije = 9;
				if(zmagaNiz[stl][vrt] == 0) igralnaPlosca[stl][vrt] = 0;
				
				vrt++;
				if(vrt >= 3){
					vrt = 0;
					stl++;
				}
				if(stl >= 3){
					endAnimacija = false;
					vrt = 0;
					stl = 0;
				}
			
		}
	}
	
	public void drawRdeca(){
		paint2.setStyle(Style.FILL);
		paint2.setColor(Color.RED);
		
		paint2.setStrokeCap(Cap.ROUND);
		paint2.setStrokeWidth(res.getDimension(R.dimen.tictaclaser));
	    paint2.setMaskFilter(new BlurMaskFilter(res.getDimension(R.dimen.tictaclaser), BlurMaskFilter.Blur.NORMAL)); 
		
		for(int i=0;i<rdeca.size();i++){
			paint2.setAlpha(alfa);
//			canvas.drawCircle(rdeca.get(i)[0], rdeca.get(i)[1], rdeca.get(i)[2], paint);
			canvas.drawLine(rdeca.get(i)[0],rdeca.get(i)[1], rdeca.get(i)[2], rdeca.get(i)[3], paint2);
			alfa -= 10;
		}
		alfa = 255;
		paint2.setAlpha(255);
		
		if(rdeca.size() > 0){
			canvas.drawCircle(rdeca.get(0)[2], rdeca.get(0)[3], rdeca.get(0)[4], paint2);
		}
		
		float[] clen = new float[5]; // {testX,testY,10};
		clen[0] = rdecaX;
		clen[1] = rdecaY;
		
		switch (rdecaSmer){
		case 0://desno
			rdecaX += hitrost2;
			if(rdecaX >= width){
				rdecaSmer = 1;
				rdecaX = width;
			}
			break;
		case 1://dol
			rdecaY += hitrost2;
			if(rdecaY >= (tretjaVodoravna)){
				rdecaSmer = 2;
				rdecaY = tretjaVodoravna;
			}
			break;
		case 2://levo
			rdecaX -= hitrost2;
			if(rdecaX <= 0){
				rdecaSmer = 3;
				rdecaX = 0;
			}
			break;
		default ://gor
			rdecaY -= hitrost2;
			if(rdecaY <= odmikZg){
				rdecaSmer = 0;
				rdecaY = odmikZg;
			}
			break;
		}
		
		clen[2] = rdecaX;
		clen[3] = rdecaY;
		clen[4] = 10;
		
		rdeca.add(0,clen);
		
		if(rdeca.size() >= 20){
			rdeca.remove(19);
		}
	}
		
	public void drawModra(){
		paint2.setStyle(Style.FILL);
		paint2.setColor(Color.BLUE);
		
		paint2.setStrokeCap(Cap.ROUND);
		paint2.setStrokeWidth(res.getDimension(R.dimen.tictaclaser));
	    paint2.setMaskFilter(new BlurMaskFilter(res.getDimension(R.dimen.tictaclaser), BlurMaskFilter.Blur.NORMAL)); 
		
		for(int i=0;i<modra.size();i++){
			paint2.setAlpha(alfa);
//			canvas.drawCircle(rdeca.get(i)[0], rdeca.get(i)[1], rdeca.get(i)[2], paint);
			canvas.drawLine(modra.get(i)[0],modra.get(i)[1], modra.get(i)[2], modra.get(i)[3], paint2);
			alfa -= 10;
		}
		alfa = 255;
		paint2.setAlpha(255);
		
		if(modra.size() > 0){
			canvas.drawCircle(modra.get(0)[2], modra.get(0)[3], modra.get(0)[4], paint2);
		}
		
		float[] clen = new float[5]; // {testX,testY,10};
		clen[0] = modraX;
		clen[1] = modraY;
		
		switch (modraSmer){
		case 0://desno
			modraX += hitrost2;
			if(modraX >= width){
				modraSmer = 1;
				modraX = width;
			}
			break;
		case 1://dol
			modraY += hitrost2;
			if(modraY >= (tretjaVodoravna)){
				modraSmer = 2;
				modraY = tretjaVodoravna;
			}
			break;
		case 2://levo
			modraX -= hitrost2;
			if(modraX <= 0){
				modraSmer = 3;
				modraX = 0;
			}
			break;
		default ://gor
			modraY -= hitrost2;
			if(modraY <= odmikZg){
				modraSmer = 0;
				modraY = odmikZg;
			}
			break;
		}
		
		clen[2] = modraX;
		clen[3] = modraY;
		clen[4] = 10;
		
		modra.add(0,clen);
		
		if(modra.size() >= 20){
			modra.remove(19);
		}
	}
	
	public void drawZelena(){
		paint2.setStyle(Style.FILL);
		paint2.setColor(Color.GREEN);
		
		paint2.setStrokeCap(Cap.ROUND);
		paint2.setStrokeWidth(res.getDimension(R.dimen.tictaclaser));
	    paint2.setMaskFilter(new BlurMaskFilter(res.getDimension(R.dimen.tictaclaser), BlurMaskFilter.Blur.NORMAL)); 
		
		for(int i=0;i<zelena.size();i++){
			paint2.setAlpha(alfa);
//			canvas.drawCircle(zelena.get(i)[0], rdeca.get(i)[1], rdeca.get(i)[2], paint);
			canvas.drawLine(zelena.get(i)[0],zelena.get(i)[1], zelena.get(i)[2], zelena.get(i)[3], paint2);
			alfa -= 10;
		}
		alfa = 255;
		paint2.setAlpha(255);
		
		if(zelena.size() > 0){
			canvas.drawCircle(zelena.get(0)[2], zelena.get(0)[3], zelena.get(0)[4], paint2);
		}
		
		float[] clen = new float[5]; // {testX,testY,10};
		clen[0] = zelenaX;
		clen[1] = zelenaY;
		
		switch (zelenaSmer){
		case 0://desno
			zelenaX += hitrost;
			if(zelenaX >= drugaNavpicna){
				zelenaX = drugaNavpicna;
				zelenaSmer = 1;
			}
			break;
		case 1://dol
			zelenaY += hitrost;
			if(zelenaY >= (tretjaVodoravna)){
				zelenaSmer = 2;
				zelenaY = tretjaVodoravna;
			}
			break;
		case 2://levo
			zelenaX -= hitrost;
			if(zelenaX <= prvaNavpicna){
				zelenaX = prvaNavpicna;
				zelenaSmer = 3;
			}
			break;
		default ://gor
			zelenaY -= hitrost;
			if(zelenaY <= odmikZg){
				zelenaSmer = 0;
				zelenaY = odmikZg;
			}
			break;
		}
		
		clen[2] = zelenaX;
		clen[3] = zelenaY;
		clen[4] = 10;
		
		zelena.add(0,clen);
		
		if(zelena.size() >= 20){
			zelena.remove(19);
		}
	}
		
	public void drawSiva(){//svetlo modra
		paint2.setStyle(Style.FILL);
		paint2.setColor(Color.rgb(84, 205, 231));
		
		paint2.setStrokeCap(Cap.ROUND);
		paint2.setStrokeWidth(res.getDimension(R.dimen.tictaclaser));
	    paint2.setMaskFilter(new BlurMaskFilter(res.getDimension(R.dimen.tictaclaser), BlurMaskFilter.Blur.NORMAL)); 
		
		for(int i=0;i<siva.size();i++){
			paint2.setAlpha(alfa);
//			canvas.drawCircle(siva.get(i)[0], rdeca.get(i)[1], rdeca.get(i)[2], paint);
			canvas.drawLine(siva.get(i)[0],siva.get(i)[1], siva.get(i)[2], siva.get(i)[3], paint2);
			alfa -= 10;
		}
		alfa = 255;
		paint2.setAlpha(255);
		
		if(siva.size() > 0){
			canvas.drawCircle(siva.get(0)[2], siva.get(0)[3], siva.get(0)[4], paint2);
		}
		
		float[] clen = new float[5]; // {testX,testY,10};
		clen[0] = sivaX;
		clen[1] = sivaY;
		
		switch (sivaSmer){
		case 0://desno
			sivaX += hitrost;
			if(sivaX >= drugaNavpicna){
				sivaSmer = 1;
				sivaX = drugaNavpicna;
			}
			break;
		case 1://dol
			sivaY += hitrost;
			if(sivaY >= (tretjaVodoravna)){
				sivaSmer = 2;
				sivaY = tretjaVodoravna;
			}
			break;
		case 2://levo
			sivaX -= hitrost;
			if(sivaX <= prvaNavpicna){
				sivaSmer = 3;
				sivaX = prvaNavpicna;
			}
			break;
		default ://gor
			sivaY -= hitrost;
			if(sivaY <= odmikZg){
				sivaSmer = 0;
				sivaY = odmikZg;
			}
			break;
		}
		
		clen[2] = sivaX;
		clen[3] = sivaY;
		clen[4] = 10;
		
		siva.add(0,clen);
		
		if(siva.size() >= 20){
			siva.remove(19);
		}
	}
		
	public void drawRumena(){
		paint2.setStyle(Style.FILL);
		paint2.setColor(Color.YELLOW);
		
		paint2.setStrokeCap(Cap.ROUND);
		paint2.setStrokeWidth(res.getDimension(R.dimen.tictaclaser));
	    paint2.setMaskFilter(new BlurMaskFilter(res.getDimension(R.dimen.tictaclaser), BlurMaskFilter.Blur.NORMAL)); 
		
		for(int i=0;i<rumena.size();i++){
			paint2.setAlpha(alfa);
//			canvas.drawCircle(rumena.get(i)[0], rdeca.get(i)[1], rdeca.get(i)[2], paint);
			canvas.drawLine(rumena.get(i)[0],rumena.get(i)[1], rumena.get(i)[2], rumena.get(i)[3], paint2);
			alfa -= 10;
		}
		alfa = 255;
		paint2.setAlpha(255);
		
		if(rumena.size() > 0){
			canvas.drawCircle(rumena.get(0)[2], rumena.get(0)[3], rumena.get(0)[4], paint2);
		}
		
		float[] clen = new float[5]; // {testX,testY,10};
		clen[0] = rumenaX;
		clen[1] = rumenaY;
		
		switch (rumenaSmer){
		case 0://desno
			rumenaX += hitrost;
			if(rumenaX >= width){
				rumenaSmer = 1;
				rumenaX = width;
			}
			break;
		case 1://dol
			rumenaY += hitrost;
			if(rumenaY >= drugaVodoravna){
				rumenaSmer = 2;
				rumenaY = drugaVodoravna;
			}
			break;
		case 2://levo
			rumenaX -= hitrost;
			if(rumenaX <= 0){
				rumenaSmer = 3;
				rumenaX = 0;
			}
			break;
		default ://gor
			rumenaY -= hitrost;
			if(rumenaY <= prvaVodoravna){
				rumenaSmer = 0;
				rumenaY = prvaVodoravna;
			}
			break;
		}
		
		clen[2] = rumenaX;
		clen[3] = rumenaY;
		clen[4] = 10;
		
		rumena.add(0,clen);
		
		if(rumena.size() >= 20){
			rumena.remove(19);
		}
	}
	
	public void drawMagenta(){
		paint2.setStyle(Style.FILL);
		paint2.setColor(Color.MAGENTA);
		
		paint2.setStrokeCap(Cap.ROUND);
		paint2.setStrokeWidth(res.getDimension(R.dimen.tictaclaser));
	    paint2.setMaskFilter(new BlurMaskFilter(res.getDimension(R.dimen.tictaclaser), BlurMaskFilter.Blur.NORMAL)); 
		
		for(int i=0;i<magenta.size();i++){
			paint2.setAlpha(alfa);
//			canvas.drawCircle(magenta.get(i)[0], rdeca.get(i)[1], rdeca.get(i)[2], paint);
			canvas.drawLine(magenta.get(i)[0],magenta.get(i)[1], magenta.get(i)[2], magenta.get(i)[3], paint2);
			alfa -= 10;
		}
		alfa = 255;
		paint2.setAlpha(255);
		
		if(magenta.size() > 0){
			canvas.drawCircle(magenta.get(0)[2], magenta.get(0)[3], magenta.get(0)[4], paint2);
		}
		
		float[] clen = new float[5]; // {testX,testY,10};
		clen[0] = magentaX;
		clen[1] = magentaY;
		
		switch (magentaSmer){
		case 0://desno
			magentaX += hitrost;
			if(magentaX >= width){
				magentaSmer = 1;
				magentaX = width;
			}
			break;
		case 1://dol
			magentaY += hitrost;
			if(magentaY >= drugaVodoravna){
				magentaSmer = 2;
				magentaY = drugaVodoravna;
			}
			break;
		case 2://levo
			magentaX -= hitrost;
			if(magentaX <= 0){
				magentaSmer = 3;
				magentaX = 0;
			}
			break;
		default ://gor
			magentaY -= hitrost;
			if(magentaY <= prvaVodoravna){
				magentaSmer = 0;
				magentaY = prvaVodoravna;
			}
			break;
		}
		
		clen[2] = magentaX;
		clen[3] = magentaY;
		clen[4] = 10;
		
		magenta.add(0,clen);
		
		if(magenta.size() >= 20){
			magenta.remove(19);
		}
	}
		
	/**
	 * Nariše figure , X ,Y 
	 * To bo treba spremenit, da bo narisalo slike.
	 */
	public void narisiFigure(){	
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				switch(igralnaPlosca[i][j]){
				case 1:
					//narisiX(j*prvaNavpicna,i*(prvaVodoravna-odmikZg)+odmikZg);
					narisiX((i*3+j+1));
					break;
				case 2:
//				 	narisiO(j*prvaNavpicna,i*(prvaVodoravna-odmikZg)+odmikZg);
					narisiO((i*3+j+1));
					break;
				}
			}
		}
	}
	
	/**
	 * Nariše krogec.
	 * @param x
	 * @param y
	 */
	public void narisiO(int celica){
		switch (celica){
		case 1:
			canvas.drawBitmap(kroz, null,ena, null);
			break;
		case 2:
			canvas.drawBitmap(kroz, null,dva, null);
			break;
		case 3:
			canvas.drawBitmap(kroz, null,tri, null);
			break;
		case 4:
			canvas.drawBitmap(kroz, null,stiri, null);
			break;
		case 5:
			canvas.drawBitmap(kroz, null,pet, null);
			break;
		case 6:
			canvas.drawBitmap(kroz, null,sest, null);
			break;
		case 7:
			canvas.drawBitmap(kroz, null,sedem, null);
			break;
		case 8:
			canvas.drawBitmap(kroz, null,osem, null);
			break;
		default :
			canvas.drawBitmap(kroz, null,devet, null);
			break;
		}
	}
	
	/**
	 * Nariše križec
	 * @param x
	 * @param y
	 */
	public void narisiX(int  celica){
		switch (celica){
		case 1:
			canvas.drawBitmap(kriz, null,ena, null);
			break;
		case 2:
			canvas.drawBitmap(kriz, null,dva, null);
			break;
		case 3:
			canvas.drawBitmap(kriz, null,tri, null);
			break;
		case 4:
			canvas.drawBitmap(kriz, null,stiri, null);
			break;
		case 5:
			canvas.drawBitmap(kriz, null,pet, null);
			break;
		case 6:
			canvas.drawBitmap(kriz, null,sest, null);
			break;
		case 7:
			canvas.drawBitmap(kriz, null,sedem, null);
			break;
		case 8:
			canvas.drawBitmap(kriz, null,osem, null);
			break;
		default :
			canvas.drawBitmap(kriz, null,devet, null);
			break;
		}
	}
	
	/**
	 * Preveri potezo igrala, ali je polje že zasedeno ipd. Skratka ali je poteza veljavna. Èe je potem 
	 * vstavi vrednost v igralnoPlosco, ter preveri ali je morda 3 v vrsto.
	 * @param X
	 * @param Y
	 * @param figura
	 * @return
	 */
	public boolean poteza(int X,int Y,int figura){
		if(igralnaPlosca[X][Y] == 0){
			igralnaPlosca[X][Y] = figura;
			checkEndgame();
			return true;
		}else{
			return false;
		}
	}

	
	/**
	 * Touch listener
	 */
	private OnTouchListener touchlistener = new OnTouchListener() {
	    public boolean onTouch(View v, MotionEvent m) {
	    	
	    	if(m.getAction() == MotionEvent.ACTION_UP){
	    		float X = m.getX();
	    		float Y = m.getY();
	    		
	    		if(Y > odmikZg && Y < tretjaVodoravna){
	    			
		    		int x = (int) Math.floor(X / getX);
		    		int y = (int) Math.floor((Y-odmikZg)/(getY));
	    			
		    		switch (gameplay){
		    		case 3:
			    		if(krizec){//Èe je igralec na vrsti se pogleda ali je kliknil kam na ekran
			    			if(poteza(y,x,1)){
			    				krizec = false;
			    				if(!END){
			    					player.poslji(y, x);
			    				}else{
			    					player.won(y, x);
			    				}
			    			}
			    		}break;
		    		case 2:
			    		if(!END){
			    			if(krizec){//Èe je igralec na vrsti se pogleda ali je kliknil kam na ekran
				    			if(poteza(y,x,1)){
				    				krizec = false;
//				    				int[] tempAi = AI.makeMove(igralnaPlosca);
//				    				zamenjajPlosco(tempAi[0],tempAi[1],2);
				    			}
				    		}
			    		}
		    			break;
		    		default :
			    		if(!END){
			    			if(krizec){//Èe je igralec na vrsti se pogleda ali je kliknil kam na ekran
				    			if(poteza(y,x,1)){
				    				krizec = false;
				    			}
				    		}else{
				    			if(poteza(y,x,2)){
				    				krizec = true;
				    			}
				    		}
			    		}
		    			
		    		}
		    		
		    	}else if(Y < height-res.getDimension(R.dimen.chat) && !waiting && Y >= tretjaVodoravna+res.getDimension(R.dimen.trideset)+res.getDimension(R.dimen.deset)+res.getDimension(R.dimen.endmessage)+res.getDimension(R.dimen.playagain) && END){
			    	if(X < width/2){
			    		if(gameplay == 3){
			    			player.new_game();
			    		}else{
			    			newGame();
			    		}
			    	}else{
			    		if(gameplay == 3){
			    			player.end();
			    		}else{
			    		endGame();
			    		}
			    	}
		    	}else if(Y > height-res.getDimension(R.dimen.chat)){
		    		if(gameplay == 3){
		    			ticTacToe.narediDialog();
		    			unreadMsg = false;
		    		}
		    	}
	    	}
	    	
	    	return true;
	    }
	};
	
	
	/**
	 * Metoda update, ki refresha sliko menuja vsake 40ms = 25fps
	 * Metoda klièe narisiMenu
	 */
	public void update(){
		canvas.drawBitmap(ozadje, null,ekran,null);
		narisiIgro();		
		redrawHandler.sleep(50); // 40 = 25fps, 65 = 15fps, 50 = 20fps
	}
	
	
	protected void onDraw(Canvas canvas){
		canvas.drawBitmap(bitmap,0,0,paint);
	}
	
	public void finish(){
		switch(gameplay){
		case 1:
			
			break;
		case 2:
			AI.endAI();
			AI = null;
			break;
		case 3:
			
			break;
		}

	}
	
	/**
	 * Poskrbi za updejtanje risalne plosce
	 *
	 */
	class RefreshHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg){
			TicTacToeView.this.update();
			TicTacToeView.this.invalidate();
		}
		
		public void sleep(long delayMillis){
			this.removeMessages(0);
			
			if(!stopUpdate){
				sendMessageDelayed(obtainMessage(0),delayMillis);
			}
		}
	};
}

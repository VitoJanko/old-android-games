package com.fifteen_puzzle_game;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class PrvaStran extends Activity implements Runnable{
	public static final String nastavitve = "Options";
	public static String IDstevilka = "";
	Zvok zvok;
	MenuView menu;
    private static final int MENU_ABOUT = 0;
	private static final int MENU_OTHERGAMES = 1;
	private RelativeLayout ln;
	private AdView adView;
		
	public void onCreate(Bundle savedInstanceState) {
		zvok = new Zvok(this);
		menu = new MenuView(this,this,zvok);
        super.onCreate(savedInstanceState);
//        setContentView(menu);
        
        
        setContentView(R.layout.menu);
//        LinearLayout ln = (LinearLayout)findViewById(R.id.linearLayout);
         ln = (RelativeLayout)findViewById(R.id.relativeLayout);
//        ln.addView(menu);
//        setContentView(ln);  
        
      
         //menu view
         ln.addView(menu);
        
        //SHRANI PODATKE
        // Restore preferences
        try{
        SharedPreferences settings = getSharedPreferences(nastavitve, 0);
        MenuView.sound = settings.getBoolean("zvok", false);
        MenuView.background = settings.getInt("background", TopSecret.PICTURE);
        MenuView.difficulty = settings.getInt("difficulty", 1);
        MenuView.highScore = settings.getInt("highScore", 1);
        long ids=(long) (Math.random()*1000000000);
        String novid = String.valueOf(ids);
        IDstevilka = settings.getString("idstevilka", novid);
        //setSilent(silent);
        }catch(Exception e){
        	MenuView.sound = false;
        	MenuView.background = TopSecret.PICTURE;
        	MenuView.difficulty = 1;
        	MenuView.highScore = 1;
        }
        
        //ADI
//        if(!VOTE){
        	adView = new AdView(this, AdSize.BANNER,"a14dc1ae5b23890");		

        	//Ce glasuje izklopi adde 	
			ln.addView(adView);
	//	    setContentView(ln);
		    
			AdRequest request = new AdRequest();
	
			adView.loadAd(request);
//        }
//        int ver = (int)(Math.random()*10);
//        if(ver > 4) AA=false;
        
        MenuView.stopUpdate=true;
    }	
	
	protected void onPause(){
		super.onPause();
		zvok.razpustiZvok();
		MenuView.stopUpdate=false;
		
	}
	
	protected void onResume(){
		super.onResume();
		zvok.pripraviZvok();
		menu.zacetneNastavitve();
		MenuView.stopUpdate=true;
		
//        int rb = (int)(Math.random()*100);
//        if(!VOTE && rb < 35){
//        	Toast.makeText(getApplicationContext(), "Rank game on market, to disable ads.", Toast.LENGTH_SHORT).show();
//        }
	}
	
	protected void onStart(){
		super.onResume();
		zvok.pripraviZvok();
		menu.zacetneNastavitve();
		MenuView.stopUpdate=true;
	}
	
    protected void onStop(){
        super.onStop();

       // We need an Editor object to make preference changes.
       // All objects are from android.context.Context
       SharedPreferences settings = getSharedPreferences(nastavitve, 0);
       SharedPreferences.Editor editor = settings.edit();
       editor.putBoolean("zvok", MenuView.sound);
       editor.putInt("background", MenuView.background);
       editor.putInt("difficulty", MenuView.difficulty);
       editor.putInt("highScore", MenuView.highScore);
       editor.putString("idstevilka", IDstevilka);
       // Commit the edits!
       editor.commit();
     }
	
	public void zamenjajActivity(){
		try{
        TopSecret.tezavnost = MenuView.difficulty;
        TopSecret.sound = MenuView.sound;
        TopSecret.picture = MenuView.background;
        if(TopSecret.picture==TopSecret.TILE)TopSecret.strech = false;
        if(TopSecret.picture==TopSecret.PICTURE)TopSecret.strech = false;
        if(TopSecret.picture==TopSecret.GALLERY)TopSecret.strech = true;
        Intent myIntent = new Intent(this, Main.class);
        startActivity(myIntent);
        TopSecret.stopRefresh=true;
		}catch(Exception e){
			Intent myIntent = new Intent(this,PrvaStran.class);
			startActivity(myIntent);
		}
	}

	public void popup(){
////		Context mContext = getApplicationContext();
//		Dialog dialog = new Dialog(this);
//
//		dialog.setContentView(R.layout.custom_dialog);
//		dialog.setTitle("About 15 puzzle game");
//		
//		
//		
//		TextView text = (TextView) dialog.findViewById(R.id.text);
//		text.setTextSize(16);
//		//text.setText("Authors: \n Vito Janko \n and \n Jani Bizjak \n \n Verison: 1.0 \n More info on: http://www.un-isrm.info/alpha_omega.php") ;
//		text.setText(this.getString(R.string.besedilo));
//		
//		ImageView image = (ImageView) dialog.findViewById(R.id.image);
//		image.setImageResource(R.drawable.icons);
//		
//		dialog.show();
//		if(!VOTE){
//			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.fifteen_puzzle_game") ) );
//			ln.removeView(adView);
//			VOTE = true;
//		}else if(AA){
//			//SHARI APLIKACIJO
//			Intent i=new Intent(android.content.Intent.ACTION_SEND);
//			i.setType("text/plain");
//			//i.putExtra(Intent.EXTRA_SUBJECT, "Test1");
//			if(MenuView.highScore < 10){
//				i.putExtra(Intent.EXTRA_TEXT, "Hey! \n I've been playing this great 15 puzzle game. You should try it: \n http://market.android.com/details?id=com.fifteen_puzzle_game");
//			}else{
//				String smstext = "Hey! \n I've scored "+ String.valueOf(MenuView.highScore)+" points. \n Try to beat me if you can: \n http://market.android.com/details?id=com.fifteen_puzzle_game";
//				i.putExtra(Intent.EXTRA_TEXT, smstext);
//			}
//			startActivity(Intent.createChooser(i, "Share 15 puzzle game!"));
//		}else{
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.above_average") ) );
//		}
	}
	
	
	 public boolean onCreateOptionsMenu(Menu menu) {
	        super.onCreateOptionsMenu(menu);

	        menu.add(0, MENU_ABOUT, 0, R.string.menu_about);
	        menu.add(0, MENU_OTHERGAMES, 0, R.string.menu_otherGames);

	        return true;
	    }

	    
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        	case MENU_ABOUT:
	        		//TODO

	        		Dialog dialog = new Dialog(this);
	        
	        		dialog.setContentView(R.layout.custom_dialog);
	        		dialog.setTitle("About 15 puzzle game");
	        		
	        		
	        		
	        		TextView text = (TextView) dialog.findViewById(R.id.text);
	        		text.setTextSize(16);
	        		//text.setText("Authors: \n Vito Janko \n and \n Jani Bizjak \n \n Verison: 1.0 \n More info on: http://www.un-isrm.info/alpha_omega.php") ;
	        		text.setText(this.getString(R.string.besedilo));
	        		
	        		ImageView image = (ImageView) dialog.findViewById(R.id.image);
	        		image.setImageResource(R.drawable.icons);
	        		
	        		dialog.show();	
	        		return true;
	        		
	        	case MENU_OTHERGAMES:
	        		//TODO
	        		
	        		String naslov = "market://search?q=pub:\"Alpha %26 Omega\"";
	        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(naslov) ) );
	                return true;
	        }

	        return false;
	    }
	
	
	
	   public boolean isOnline() {
		    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		        return true;
		    }
		    return false;
		} 
	
	public void highscoreView(){
		//progresDialog("Loading online leaderboard","Please wait","Cancel");
		//TODO
		
		AlertDialog alert = new AlertDialog.Builder(this).create();
		
		alert.setTitle("High score");
		alert.setMessage("Sorry, this feautre is disabled at the moment.");
		
		alert.setCanceledOnTouchOutside(true);
		
		alert.show();
	}
	
	
	
	ProgressDialog progres;
	private void progresDialog(String title, String message, String buttonText)
	{
	    progres = new ProgressDialog(this);
	    progres.setTitle(title);
	    progres.setMessage(message);
	    progres.setCancelable(true);
	    
        final Thread hscr = new Thread(this);
        hscr.start();
	    progres.setButton(buttonText, new DialogInterface.OnClickListener() 
	    
	    {
	    	public void onClick(DialogInterface dialog, int which) 
	        {
	            // Use either finish() or return() to either close the activity or just the dialog
//	            return;
	    		
	    		hscr.interrupt();
	    		try {
					hscr.join(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				noninterupted=false;
				HighscoreManager.noninterupted=false;
	        	return;
	        }
	    });
	    progres.show();
	}
	
	public static boolean noninterupted=true;
	
	Dialog dialog;
	
	public void narediDialog(){
		//set up dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.maindialog);
        dialog.setTitle("Online leaderboard:");
        dialog.setCancelable(true);
        //there are a lot of settings, for dialog, check them all out!

        //set up text
        TextView text = (TextView) dialog.findViewById(R.id.TextView01);
        text.setText(scoreboard);
        
        TextView myscore = (TextView) dialog.findViewById(R.id.text);
        myscore.setText(mysc);
        //set up image view
//        ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
//        img.setImageResource(R.drawable.nista_logo);

        //set up button
        Button button = (Button) dialog.findViewById(R.id.Button01);
        button.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
                dialog.cancel();
            }
        });
        Button button2 = (Button) dialog.findViewById(R.id.Button02);
        button2.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
                MenuView.highScore=0;
            }
        });
        dialog.show();
//        MenuView.highScore=mojscr;
	}
	private String scoreboard;
	private String mysc;
	//NITI
	public void run() {
		
		String[] scoreBoard = new String[1];
		String url ="http://www.un-isrm.info/androidtest.php?id="+IDstevilka;
		if(isOnline()){
			scoreBoard=HighscoreManager.getServerData(url,IDstevilka);
		}else{
			scoreBoard[0]="Connection timeout.";
		}
		scoreboard = "";
		for(int i=0;i<scoreBoard.length-1;i++){
			scoreboard +=" "+(1+i)+".) "+scoreBoard[i]+" \n";
		}
		mysc = "You rank at: "+scoreBoard[scoreBoard.length-1];
		
//		try{
//		int mojscr = Integer.valueOf(scoreBoard[scoreBoard.length-1]);
//		}catch(Exception e){
			//MenuView.highScore=0;
//		}
	        handler.sendEmptyMessage(0);
	}
//	private int mojscr;
	
	private Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	        	if(noninterupted){	
	        		narediDialog();
	                progres.dismiss();
	        	}else{
	        		noninterupted=true;
	        		HighscoreManager.noninterupted=true;
	        	}
	
	        }
	};
}

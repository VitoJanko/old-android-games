package com.fifteen_puzzle_game;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class Main extends  Activity implements Runnable {
    private static final int MENU_START = 0;
	private static final int MENU_STOP = 2;
//	private static final int MENU_RESUME = 3;
	private static final int MENU_MEDIUM = 4;
//	private static final int MENU_PAUSE = 5;
	private static final int MENU_EASY = 6;
	private static final int MENU_HARD = 7;
	
	TopSecret mainView;
	Zvok zvok;
	Vibrator vibrator;
	public int PICK_IMAGE = 1;
	
	protected void gallery(){
		TopSecret.inProgress = true;
		//Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		//startActivityForResult(i, PICK_IMAGE);
		
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.setType("image/*");
		startActivityForResult(i,1);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	    super.onActivityResult(requestCode, resultCode, data); 
	    TopSecret.inProgress = false;
	    switch(requestCode) { 
	    case 1:
	        if(resultCode == RESULT_OK){  
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();
	            try{
		            Matrix matrix = new Matrix();
		            //Bitmap celota = BitmapFactory.decodeFile(filePath);
		            Bitmap celota = Media.getBitmap(this.getContentResolver(),selectedImage);
		            //int a = celota.getWidth();
		            //int b = celota.getHeight();
		            float razmerje1 = (float)mainView._height/(float)mainView._width;
		            float razmerje2 = (float)Math.max(celota.getWidth(),celota.getHeight())/
		            					(float)Math.min(celota.getWidth(),celota.getHeight());
		            if(celota.getHeight()<celota.getWidth()){
		    			matrix.postRotate(90);
		    			if(razmerje1>razmerje2)
		            		celota = Bitmap.createScaledBitmap
		            			(celota,mainView._height-2, (int)((mainView._height-2)/razmerje2),true);
		            	else celota = Bitmap.createScaledBitmap
	        					(celota,(int)((mainView._width-2)*razmerje2), mainView._width-2,true);
		    		}
		            else {
		            	if(razmerje1>razmerje2)
		            		celota = Bitmap.createScaledBitmap
		            			(celota,(int)((mainView._height-2)/razmerje2), mainView._height-2,true);
		            	else celota = Bitmap.createScaledBitmap
	        					(celota,mainView._width-2, (int)((mainView._width-2)*razmerje2),true);
		            }
		            //a = celota.getWidth();
		            //b = celota.getHeight();
		            TopSecret.custom = Bitmap.createBitmap(celota, 0,0,celota.getWidth(),celota.getHeight(),matrix,true);
		            TopSecret.picture = TopSecret.GALLERY;
	            }
	            catch (Exception e){
	            	TopSecret.picture = TopSecret.TILE;
	            }
	            TopSecret.stopRefresh = true;
	            mainView.setUpBoard();
	        }
	    }
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zvok = new Zvok(this);
        TopSecret.custom = null;
        mainView = new TopSecret(this,zvok);
        mainView.restore = savedInstanceState;
        mainView.host = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(mainView);
        setContentView(R.layout.main);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        
        LinearLayout ln = (LinearLayout)findViewById(R.id.linearLayout);
        
        //ADI
        	AdView adView = new AdView(this, AdSize.BANNER,"a14dc1ae5b23890");
        	//		adView.setMinimumHeight(90);
        	//		adView.setMinimumWidth(340); 
		
            //Izklopi adde ce ranka na marketu
            if(true){
            	ln.addView(adView);
            }
			ln.addView(mainView);
			
	        //Izklopi adde ce ranka na marketu
//	        if(!PrvaStran.VOTE){
//			AdRequest request = new AdRequest();
		
//			adView.loadAd(request);
//        }
    }
  
	
	//ko stisneš tipko back  
	@Override
	public void onBackPressed() {
		MenuView.stopUpdate=true;
		this.finish();
	return;
	}
	
	protected void onPause(){
		super.onPause();
		zvok.razpustiZvok();
		TopSecret.stopRefresh=false;
		TopSecret.stikalo=true;
		vibrator.cancel();
	}
	
	protected void onResume(){
		super.onResume();
		zvok.pripraviZvok();
		TopSecret.stopRefresh=true;
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
	}
	
	protected void onStart(){
		super.onStart();
		TopSecret.stopRefresh=true;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		mainView.saveState(outState);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle outState){
		super.onRestoreInstanceState(outState);
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_START, 0, R.string.menu_menu);
        //menu.add(0, MENU_STOP, 0, R.string.menu_stop);
        //menu.add(0, MENU_PAUSE, 0, R.string.menu_pause);
        //menu.add(0, MENU_RESUME, 0, R.string.menu_resume);
        menu.add(0, MENU_EASY, 0, R.string.menu_easy);
        if(TopSecret.picture==TopSecret.TILE)
        	menu.add(0, MENU_MEDIUM, 0, R.string.menu_medium);
        else
        	menu.add(0, MENU_STOP, 0, R.string.menu_stop);
        menu.add(0, MENU_HARD, 0, R.string.menu_hard);

        return true;
    }

    public void vibrator(){
        vibrator.vibrate(500);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case MENU_STOP:
        		TopSecret.tips = true;
        		mainView.timer1 = 45;
        		return true;
        		
        	case MENU_START:
            	TopSecret.keepLast = true;
            	mainView.setUpBoard();                               
                return true;
                
            case MENU_EASY:
            	TopSecret.keepLast = false;
            	if(TopSecret.picture==TopSecret.GALLERY)
            		gallery();
            	else mainView.setUpBoard();
                return true;
                
            case MENU_MEDIUM:
            	TopSecret.strech = !TopSecret.strech;
            	if(TopSecret.strech){
            		if(TopSecret.picture==TopSecret.GALLERY)
            			mainView.makeGalleryStreched();
            	}
            	else{
            		if(TopSecret.picture==TopSecret.GALLERY)
            			mainView.makeGallery();
            	}
                return true;
            
            case MENU_HARD:
            	MenuView.stopUpdate=true;
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }

        return false;
    }
    
    public void highScore(final String rezultat){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		rezultat2=rezultat;
		alert.setTitle("Submit Highscore Online");
		alert.setMessage("Enter your nickname:");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  Editable value = input.getText();
		  ime = value.toString();
		  String[] dvodelnoIme=ime.split(" ");
		  ime="";
		  for(int i=0;i<dvodelnoIme.length;i++){
			  if(i<dvodelnoIme.length-1){
				  ime+=dvodelnoIme[i]+"_";
			  }else{
				  ime+=dvodelnoIme[i];
			  }
		  }
		  //POSLJE HIGHSCORE
//		  HighscoreManager.sendServerData("http://www.un-isrm.info/androidtestp.php", PrvaStran.IDstevilka, ime, rezultat, "true");
		  progresDialog("Submiting score online","Please wait","Cancel");
		}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});
		
		alert.show();
    }
    String ime;
    String rezultat2;
public void run() {
	HighscoreManager.sendServerData("http://www.un-isrm.info/androidtestp.php", PrvaStran.IDstevilka, ime, rezultat2, "true");
	handler.sendEmptyMessage(0);
}
	
	private Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	        	if(noninterupted){	
	                progres.dismiss();
	        	}else{
	        		noninterupted=true;
	        		HighscoreManager.noninterupted=true;
	        	}
	
	        }
	};
    
    
    boolean noninterupted=true;
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
	        	progres.cancel();
	        }
	    });
	    progres.show();
	}
}
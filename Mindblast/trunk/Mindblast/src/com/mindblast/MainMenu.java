package com.mindblast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends Activity {
	private MenuView menuview;
	private Resources res;
	
	public static String ID=String.valueOf((int)(Math.random()*5000)); //èe slucajno kaj crkne
	public static String IME= "Unknown Player";
	public boolean changedName = true;
	public boolean showFeedback = true;
	
	Dialog dialog;
	
	private boolean resumeUpdate = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        ID = telephonyManager.getDeviceId(); //ID je enak IMEU od telefona, je unique za usak telefon posebi
        res = getResources();
        
        SharedPreferences settings = getSharedPreferences("options", 0);
        IME = settings.getString("ime", res.getString(R.string.unknown_player));
        showFeedback = settings.getBoolean("feedback", true);
        if(IME.equalsIgnoreCase(res.getString(R.string.unknown_player))) changedName = false;
                
        menuview = new MenuView(getApplicationContext(),this);
        setContentView(menuview);
    }  
       
    
    public void feedback(){
    	dialog = new Dialog(this);
		dialog.setContentView(R.layout.feedback);
        dialog.setTitle("Feedback");
        dialog.setCancelable(true);
        
        
         
        
        TextView about = (TextView) dialog.findViewById(R.id.intro);
        about.setText("Please give us feedback what you think about the game. How do you like graphics, animation. Is game fas enough? What do you like? What would you change?");
        
        TextView pros = (TextView) dialog.findViewById(R.id.pros);
        pros.setText("Write what you like about this game.");
        
//        TextView prosInput = (TextView) dialog.findViewById(R.id.inputText);
        final EditText prosInput = (EditText) dialog.findViewById(R.id.inputText);
        prosInput.setHint("Write what you like about game. And what would you add or change to make it even better.");
        
        TextView cons = (TextView) dialog.findViewById(R.id.cons);
        cons.setText("Write what you don't like about this game.");        
        
        //TextView consInput = (TextView) dialog.findViewById(R.id.inputText2);
        final EditText consInput = (EditText) dialog.findViewById(R.id.inputText2);
        consInput.setHint("Write what you don't like about game. Repor bugs etc.");
        
//      set up button
      Button button = (Button) dialog.findViewById(R.id.Button02);
      button.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
              dialog.cancel();
              finish();
          }
      });
      Button button2 = (Button) dialog.findViewById(R.id.Button01);
      button2.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
    	  //TODO
    	  Editable value = prosInput.getText();
		  String pros = value.toString();
		  Editable value2 = consInput.getText();
		  String cons = value2.toString();
    	  
    	  Feedback fb  = new Feedback(pros,cons);
    	  fb.start();
    	  dialog.cancel();
    	  
    	  finish();
          }
      });
      
      final CheckBox checkbox = (CheckBox) dialog.findViewById(R.id.checkBox1);
      checkbox.setText("Don't show this again");
      checkbox.setOnClickListener(new OnClickListener() {
    	    public void onClick(View v) {
    	        // Perform action on clicks
    	        if (checkbox.isChecked()) {
    	            showFeedback = false;
    	        } else {
    	           showFeedback = true;
    	        }
    	    }
    	});
      
      

      dialog.show();
    }
    

    
    public void enterYourName(){
    	AlertDialog.Builder nickname = new AlertDialog.Builder(this);
    	nickname.setTitle(res.getString(R.string.enter_your_nickname));
    	nickname.setMessage(res.getString(R.string.your_nickname_will_be_seen_by_other_players_when_playing_online));

		// Set an EditText view to get user input 
		final EditText input = new EditText(getApplicationContext());
		input.setHint(IME);
		nickname.setView(input);

		nickname.setPositiveButton(res.getString(R.string.submit), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			  Editable value = input.getText();
			  String temp = value.toString();
			  if(temp.length() > 2){
				  IME = temp;
				  changedName = true;
			  }else{
				  Toast.makeText(getApplicationContext(),res.getString(R.string.nickname_must_be_at_least_3_simbols_long), Toast.LENGTH_SHORT).show();
			  }
			  
			}
		});

		nickname.setNegativeButton(res.getString(R.string.back), new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});
		
		nickname.show();
    }
    
    

    public void onBackPressed() {
    	if(menuview.back()){
    		if(showFeedback) feedback();
    		else finish();
    	}

    }

    
    protected void onResume(){
    	super.onResume();
    	menuview.stopUpdate = false;
    	menuview.resetAnimation();
    	if(resumeUpdate)	menuview.update();
    }
    
    protected void onPause(){
    	super.onPause();
    	menuview.stopUpdate = true;
    	resumeUpdate = true;
    }
    
    protected void onStop(){
    	super.onStop();
    	
    	//Shrani nastavitve
    	SharedPreferences settings = getSharedPreferences("options", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ime", IME);
        editor.putBoolean("feedback", showFeedback);
        // Commit the edits!
        editor.commit();
    }
    
    
    
    public void newGame(boolean type){
    	try{
    	if(type){
	    	Intent novaIgra = new Intent(this,TicTacToe.class);
	    	startActivity(novaIgra);
    	}else{
    		if(!changedName){
    			enterYourName();
    		}else{
    			Intent novaIgra = new Intent(this,Lobby.class);
	    		startActivity(novaIgra);
    		}
    	}
    	
	}catch(Exception e){
		Intent fail = new Intent(this,MainMenu.class);
		startActivity(fail);
	}
    }
}
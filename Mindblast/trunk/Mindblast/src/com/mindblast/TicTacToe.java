package com.mindblast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TicTacToe extends Activity {
	public TicTacToeView ticTacToeView;
	
	//Chat
    Dialog dialog;
    ListView listView;
    private static SimpleAdapter adapter;
    private static ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    
    private static String myMsg = "";
    
    Vibrator vibrator;
    Resources res;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int gametype = MenuView.gameplay;  
        
        res = getResources();
        dialog = new Dialog(this);
        
        ticTacToeView = new TicTacToeView(getApplicationContext(),this,gametype);
        setContentView(ticTacToeView);
        
        adapter = new SimpleAdapter(
      		  getApplicationContext(),
      		  list,
      		  R.layout.custom_row_view,
      		  new String[] {"ime","cas","sporocilo"},
      		  new int[] {R.id.text1,R.id.text2, R.id.text3}

      		  );
    }  
    
    /**
     * 
     * @param ime = nick igralca ki poslje sporocilo
     * @param sporocilo = sporocilo v chatu
     */
    public void zamenjajText(String ime,String sporocilo){	
    	HashMap<String,String> temp = new HashMap<String,String>();
    	temp.put("ime",ime);
    	temp.put("cas", getTime());
    	temp.put("sporocilo", sporocilo+" ");
    	list.add(temp);
    	
    	if(list.size() >= 100){
    		list.remove(99);
    	}
    	
    	adapter.notifyDataSetChanged();
    }
    
    /**
     * Vrne trenutni cas
     * @return
     */
    public static String getTime(){
    	   Date date = new Date();
           int hours = date.getHours();
           int minutes = date.getMinutes();
           String curTime ="("+hours + ":"+minutes + ")";
           
           return curTime;
    }
    
    /**
     * Naredi dialog za chat.        
     */
    @SuppressLint("NewApi")
	public void narediDialog(){	    	
     dialog.setContentView(R.layout.chat2);
     dialog.setTitle(res.getString(R.string.chat));
     dialog.setCancelable(true);

      
     
     
      listView = (ListView)dialog.findViewById(R.id.listview);
      listView.setAdapter(adapter);
      listView.smoothScrollToPosition(list.size());
      
      final TextView myscore = (TextView) dialog.findViewById(R.id.inputText);
      myscore.setHint(res.getString(R.string.write_message));
      //TODO

      //set up button
      Button button = (Button) dialog.findViewById(R.id.Button01);
      button.setText(res.getString(R.string.back));
      button.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
    	  	  ticTacToeView.unreadMsg = false;
              dialog.cancel();
          }
      });
      Button button2 = (Button) dialog.findViewById(R.id.Button02);
      button2.setText(res.getString(R.string.send));
      button2.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
      		String msg = String.valueOf(myscore.getText());
    	  	
      		if(msg.length() > 0){
    	  		zamenjajText(res.getString(R.string.me),msg+"\n");
    	  		myMsg(true,msg);
    	  	}
      		
      		myscore.setText("");
          }
      });
      dialog.show();   
      
    }
    
    public void cancelDialog(){
    	if(dialog.isShowing()){
    		dialog.cancel();
    		ticTacToeView.unreadMsg = false;
    	}
    	
    }
    
    public void vibrate(int length){
    	vibrator.vibrate(length);
    }
    
    public synchronized static String myMsg(boolean operacija,String msg){
    	if(operacija){
    		myMsg += msg;
    		return "";
    	}else{
    		String temp = myMsg;
    		myMsg = "";
    		return temp;
    	}
    }
        
    public void endGame(){
    	Player.active = false;
    	ticTacToeView.stopUpdate = true;
//    	ticTacToeView = null;
    	finish();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(true);
        	if(dialog.isShowing()){
  	  	    	ticTacToeView.unreadMsg = false;
  	  	    	dialog.cancel();
        	}else{
        		ticTacToeView.backEnd();
        	}
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    protected void onResume(){
    	super.onResume();
    	ticTacToeView.stopUpdate = false;
    	Player.active = true;
    	vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
    }
    
    protected void onPause(){
    	super.onPause();
    	ticTacToeView.stopUpdate = true;
    	vibrator.cancel();
    }
    
    protected void onStop(){
    	super.onStop();
    	Player.active = false;
    	ticTacToeView.stopUpdate = true;
    	ticTacToeView.backEnd();
    	finish();
    }
    
    protected void onDestroy(){
    	super.onDestroy();
    	Player.active = false;
    	ticTacToeView.stopUpdate = true;
    	ticTacToeView.finish();
    	ticTacToeView = null;
    	list.clear();
    }
		

}

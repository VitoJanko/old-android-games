package com.mindblast;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Lobby extends Activity{		
		LoadLobby loadLobby;
		public static int PORT;
		Resources res;
		
		Handler pHandler;
		
		private TextView textView;
		
		private ListView listView1;
		ArrayList<Opponent> nasprotniki = new ArrayList<Opponent>();
		OpponentAdapter adapter;
		
		Dialog playRequest;
		public static String player2 = "";
		
		
		//////////////////////////////////////////
		//////////////////////////////////////////
		//////////////////////////////////////////
		 /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        res = getResources();
	        
//	        setContentView(R.layout.main);  
	        setContentView(R.layout.lobby);
	        adapter = new OpponentAdapter(this,R.layout.listview_item_row,nasprotniki);
	        
	        
	        /**
	         * Se uporablja za komunikacijo med LoadLobby in tem activityem.
	         * Sprozi se kadar se spremenijo podatki v tabeli.
	         */
	        pHandler = new Handler()
	        {
	            @Override
	            public void handleMessage(Message msg)
	            {
                    switch (msg.what) {
                    	case 0:
                    		adapter.notifyDataSetChanged();
                    		return;
                    	case 1:
                    		playRequest();
                    		return;
                    	case 2:
                    		error();
                    		return;
                    }
                    super.handleMessage(msg);
	            	
	            }
	        };
	        	   
	        textView = (TextView)findViewById(R.id.lobbytext);
	        
	        textView.setText("Lobby");
			listView1 = (ListView)findViewById(R.id.listView1);
	        listView1.setAdapter(adapter);
	        listView1.setOnItemClickListener(onItemListener);
	        
	    }
	    
	    /**
	     * Poslusa ali kliknemo element iz lista.
	     * Ce nanj kliknemo zaenkrat zahteva povezavo z njim.
	     * Mozna razsiritev, da dodamo igralca pod friends ali kaj podobnega.
	     */
	    OnItemClickListener onItemListener = new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {
	        	
	        	@SuppressWarnings("unchecked")
				ArrayAdapter<Opponent> adapter2 = (ArrayAdapter<Opponent>) parent.getAdapter();
	        	Opponent o = adapter2.getItem(position);
	        	
//	        	Toast.makeText(getApplicationContext(), o.id, Toast.LENGTH_LONG).show();
	        	player2 = o.name;
	        	loadLobby.connect(o.id);
	        }
	      };
	    
	      
	      /**
	       * Zazene nov activity: TicTacToe (zaenkrat),
	       * lahko se razsiri, da je en lobby za vse igre.
	       */
	      public void newGame(){
	      	try{
	      	//this.PORT = PORT;
	      	Intent novaIgra = new Intent(this,TicTacToe.class);
	      	startActivity(novaIgra);
	      	}catch(Exception e){
	      		Intent fail = new Intent(this,Lobby.class);
	      		startActivity(fail);
	      	}
	      }
	      
	      /**
	       * Ce je GameServer poln, oz pride do kake druge napake, se nam tukaj izpiše Toast z sporoèilom errorja.
	       * 
	       */
	      public void error(){
	    	  Toast.makeText(getApplicationContext(),loadLobby.errorMsg, Toast.LENGTH_SHORT).show();
	      }
	      
	      /**
	       * Èe nek igralec želi igrati z nami se sproži ta metoda.
	       * Zaenkrat vrže vn dialog, ampak bo treba spremenit na kaj bol estetskega.
	       */
	      public void playRequest(){  	  
	    	 //TODO
	    	  AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	  builder.setMessage(player2+" "+res.getString(R.string.wants_to_play_with_you))
	    	         .setCancelable(false)
	    	         .setPositiveButton(res.getString(R.string.accept), new DialogInterface.OnClickListener() {
	    	             public void onClick(DialogInterface dialog, int id) {
	    	                  newGame();
	    	             }
	    	         })
	    	         .setNegativeButton(res.getString(R.string.decline), new DialogInterface.OnClickListener() {
	    	             public void onClick(DialogInterface dialog, int id) {
	    	                  dialog.cancel();
	    	             }
	    	         });
	    	  AlertDialog alert = builder.create();  
	    	  alert.show();
	      }
	      
	      
	      /**
	       * onResume vedno znova zažene Thread LoadLobby, ki ga unièi on Stop:
	       */
	    protected void onResume(){
	    	super.onResume();
	    	 
	    	//TODO ali ne to povzroèi memory leaka?
			loadLobby = new LoadLobby("188.230.131.244",17525,pHandler,this);
			loadLobby.start();	    	
	    }
	    
	    protected void onPause(){
	    	super.onPause();
	    	loadLobby.active = false;
	    }
	    
	    /**
	     * Ko metoda konèa, unièi nit LoadLobby.
	     */
	    protected void onStop(){
	    	super.onStop();
	    	
	    	loadLobby.active = false;
	    	loadLobby.stop();
	    	
	    	nasprotniki.clear();
	    	adapter.notifyDataSetChanged();
	    }
}

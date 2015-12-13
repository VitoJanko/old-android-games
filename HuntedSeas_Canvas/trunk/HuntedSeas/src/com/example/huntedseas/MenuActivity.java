package com.example.huntedseas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * TODO Put here a description of what this class does.
 *
 * @author Jani.
 *         Created 27. okt. 2012.
 */
public class MenuActivity extends Activity {
	
	MenuView menuView;
	public static int selectedLvL = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		menuView = new MenuView(this,this);
		setContentView(menuView);
	}
	
	public void zamenjajActivity(int lvl){
		selectedLvL = lvl;
		
		Intent myIntent = new Intent(this,MainActivity.class);
		startActivity(myIntent);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		
		menuView.destroy();
		menuView = null;
	}
}

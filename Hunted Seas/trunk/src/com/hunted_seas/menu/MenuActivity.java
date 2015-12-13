package com.hunted_seas.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hunted_seas.game.GameActivity;
import com.hunted_seas.game.R;
import com.hunted_seas.game.world.acommon.GameSettings;

public class MenuActivity extends Activity {
	public final static String EXTRA_PREFIX = "com.hunted_seas.game.";
	public final static String WORLD = EXTRA_PREFIX + "START.WORLD";
	public final static String LEVEL = EXTRA_PREFIX + "START.LEVEL";

	
	private ToggleButton changeSkin;
	private ToggleButton compressTexture;
    private ToggleButton invertX;
    private ToggleButton invertY;
    private ToggleButton switchXY;
    
    private TextView tiltSeekBarValue;
    private SeekBar tiltSeekBar;
    

    SharedPreferences sharedPref;
    private String tempSettingsFile = "tempGameSettingsFile";
    private String tempSkin = "change_skin_Settings";
    private String tempCompressT = "compressText_Settings";
    private String tempInvertX = "invertX_Settings";
    private String tempInvertY = "invertY_Settings";
    private String tempSwitchXY = "switchXY_Settings";
    private String tempTiltSeekBarValue = "tilt_seek_bar_value";


	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.menu_temp_layout);

		changeSkin = (ToggleButton) findViewById(R.id.skin_textures_toggle);
		compressTexture = (ToggleButton) findViewById(R.id.compress_textures_toggle);
        invertX = (ToggleButton) findViewById(R.id.invert_x_toggle);
        invertY = (ToggleButton) findViewById(R.id.invert_y_toggle);
        switchXY = (ToggleButton) findViewById(R.id.change_axis_toggle);
        
        tiltSeekBarValue = (TextView) findViewById(R.id.seek_bar_value);
        tiltSeekBar = (SeekBar) findViewById(R.id.tilt_control_seekbar);
        
        
        tiltSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tiltSeekBarValue.setText(String.format("%.1f",(progress/10f-9)));
			}
		});
        
        sharedPref = getSharedPreferences(tempSettingsFile, Context.MODE_PRIVATE);
        changeSkin.setChecked(sharedPref.getBoolean(tempSkin, false));
        compressTexture.setChecked(sharedPref.getBoolean(tempCompressT,false));
        invertX.setChecked(sharedPref.getBoolean(tempInvertX,false));
        invertY.setChecked(sharedPref.getBoolean(tempInvertY,false));
        switchXY.setChecked(sharedPref.getBoolean(tempSwitchXY, false));
        tiltSeekBar.setProgress(sharedPref.getInt(tempTiltSeekBarValue, 90));
	}

    public void onPause(){
        super.onPause();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(tempSkin, changeSkin.isChecked());
        editor.putBoolean(tempCompressT, compressTexture.isChecked());
        editor.putBoolean(tempInvertX,invertX.isChecked());
        editor.putBoolean(tempInvertY,invertY.isChecked());
        editor.putBoolean(tempSwitchXY,switchXY.isChecked());
        editor.putInt(tempTiltSeekBarValue, tiltSeekBar.getProgress());
        editor.commit();

        GameSettings.compressTexture = compressTexture.isChecked();
        GameSettings.invertX = invertX.isChecked();
        GameSettings.invertY = invertY.isChecked();
        GameSettings.switchXY = switchXY.isChecked();
        GameSettings.offsetY = tiltSeekBar.getProgress()/10.f - 9;
        GameSettings.player_skin_id = changeSkin.isChecked() ? 1 : 0;
    }
	
	public void startLvL1(View v){
		Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
		startGame.putExtra(WORLD, 1);
		startGame.putExtra(LEVEL, 1);
		startActivity(startGame);
		
	}
	
	public void startLvL2(View v){
		Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
		startGame.putExtra(WORLD, 1);
		startGame.putExtra(LEVEL, 2);
		startActivity(startGame);
	}
	
	public void startLvL3(View v){
		Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
		startGame.putExtra(WORLD, 1);
		startGame.putExtra(LEVEL, 3);
		startActivity(startGame);
	}
	public void startLvL4(View v){
		Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
		startGame.putExtra(WORLD, 1);
		startGame.putExtra(LEVEL, 4);
		startActivity(startGame);
	}
	public void startLvL5(View v){
		Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
		startGame.putExtra(WORLD, 1);
		startGame.putExtra(LEVEL, 5);
		startActivity(startGame);
	}
	public void startLvL6(View v){
		Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
		startGame.putExtra(WORLD, 1);
		startGame.putExtra(LEVEL, 6);
		startActivity(startGame);
	}
	public void startLvL7(View v){
		Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
		startGame.putExtra(WORLD, 1);
		startGame.putExtra(LEVEL, 7);
		startActivity(startGame);
	}
	public void startLvL8(View v){
		Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
		startGame.putExtra(WORLD, 1);
		startGame.putExtra(LEVEL, 8);
		startActivity(startGame);
	}
	public void startLvL9(View v){
		Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
		startGame.putExtra(WORLD, 1);
		startGame.putExtra(LEVEL, 9);
		startActivity(startGame);
	}
	public void startLvL10(View v){
		Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
		startGame.putExtra(WORLD, 1);
		startGame.putExtra(LEVEL, 10);
		startActivity(startGame);
	}
	
	public void startLvLSandbox(View v){
		Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
		startGame.putExtra(WORLD, 1);
		startGame.putExtra(LEVEL, 15);
		startActivity(startGame);
		
	}
	
}

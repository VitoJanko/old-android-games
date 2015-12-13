package org.bizjak.android.HUD;

import org.bizjak.android.GPU.R;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */

public class HUD {
	LinearLayout hud;
	static TextView fps;
	static Activity a;
	
	static long timeout = 0;
	
	public HUD(Activity a,LinearLayout hud){
		this.hud = hud;
		HUD.a = a;
		
		fps = (TextView) hud.findViewById(R.id.fps);
		
		setFPS(-1);
	}
	
	
	
	//TODO optimize, don't use static
	public static void setFPS(final int frameRate){
		if(isTimeToRefresh() && fps != null){
			a.runOnUiThread(new Runnable() {
		        public void run() {
		        	fps.setText("FPS: "+frameRate);
		        }
		});
			
		}
	}
	
	public static boolean isTimeToRefresh(){
		if((System.currentTimeMillis() - timeout) > 300){
			timeout = System.currentTimeMillis();
			return true;
		}else{
			return false;
		}
	}
	
	public void onDestroy(){
		hud = null;
		fps = null;
		a = null;
	}
}

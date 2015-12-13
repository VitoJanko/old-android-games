package com.hunted_seas.game.graphics;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hunted_seas.game.GameActivity;
import com.hunted_seas.game.R;

public class HUD {
	private GameActivity gameActivity;
	
	/**
	 * HUD layout.
	 * 
	 * Found at activity_game.xml
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	private RelativeLayout gameHud;
	
	/**
	 * View where FPS is suppose to be viewed.
	 */
	private TextView renderer_fpsView;
	private TextView game_fpsView;
	private TextView playerHealth;
	private TextView foodEaten;
	private TextView rendererType;
	
	private ImageView hp1;
	private ImageView hp2;
	private ImageView hp3;
	private ImageView hp4;
	private ImageView hp5;
	
	private long fpsDelayClock = 0;
	/**
	 * INT time in miliseconds. Used for blocking fps view update.
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	private final int FPS_DELAY = 300;
	
	public HUD(GameActivity a, RelativeLayout gameHud){
		this.gameActivity = a;
		this.gameHud = gameHud;
		this.renderer_fpsView = (TextView) this.gameHud.findViewById(R.id.renderer_fps_view);
		this.game_fpsView = (TextView) this.gameHud.findViewById(R.id.game_fps_view);
		this.playerHealth = (TextView) this.gameHud.findViewById(R.id.player_health);
		this.foodEaten = (TextView) this.gameHud.findViewById(R.id.player_food_count);
		this.rendererType = (TextView) this.gameHud.findViewById(R.id.renderer_type);
		
		this.hp1 = (ImageView) this.gameHud.findViewById(R.id.hp1);
		this.hp2 = (ImageView) this.gameHud.findViewById(R.id.hp2);
		this.hp3 = (ImageView) this.gameHud.findViewById(R.id.hp3);
		this.hp4 = (ImageView) this.gameHud.findViewById(R.id.hp4);
		this.hp5 = (ImageView) this.gameHud.findViewById(R.id.hp5);
		
	}
	
	public void setRendererType(final String type){
		gameActivity.runOnUiThread(new Runnable(){
			public void run(){
				rendererType.setText("Renderer type: "+type);
			}
		});
	}
	
	public void updatePlayerHealth(final int hp){
		gameActivity.runOnUiThread(new Runnable(){
			public void run(){
				playerHealth.setText("HP: " + hp);
				
				if(hp >= 100){
					hp1.setImageResource(R.drawable.heart_full);
					hp2.setImageResource(R.drawable.heart_full);
					hp3.setImageResource(R.drawable.heart_full);
					hp4.setImageResource(R.drawable.heart_full);
					hp5.setImageResource(R.drawable.heart_full);
					
					hp1.setVisibility(View.VISIBLE);
					hp2.setVisibility(View.VISIBLE);
					hp3.setVisibility(View.VISIBLE);
					hp4.setVisibility(View.VISIBLE);
					hp5.setVisibility(View.VISIBLE);
				}else if(hp > 90){
					hp1.setImageResource(R.drawable.heart_full);
					hp2.setImageResource(R.drawable.heart_full);
					hp3.setImageResource(R.drawable.heart_full);
					hp4.setImageResource(R.drawable.heart_full);
					hp5.setImageResource(R.drawable.heart_half);
					
					hp1.setVisibility(View.VISIBLE);
					hp2.setVisibility(View.VISIBLE);
					hp3.setVisibility(View.VISIBLE);
					hp4.setVisibility(View.VISIBLE);
					hp5.setVisibility(View.VISIBLE);
				}else if(hp > 80){
					hp1.setImageResource(R.drawable.heart_full);
					hp2.setImageResource(R.drawable.heart_full);
					hp3.setImageResource(R.drawable.heart_full);
					hp4.setImageResource(R.drawable.heart_full);
					hp5.setImageResource(R.drawable.heart_half);
					
					hp1.setVisibility(View.VISIBLE);
					hp2.setVisibility(View.VISIBLE);
					hp3.setVisibility(View.VISIBLE);
					hp4.setVisibility(View.VISIBLE);
					hp5.setVisibility(View.INVISIBLE);
				}else if(hp > 70){
					hp1.setImageResource(R.drawable.heart_full);
					hp2.setImageResource(R.drawable.heart_full);
					hp3.setImageResource(R.drawable.heart_full);
					hp4.setImageResource(R.drawable.heart_half);
					hp5.setImageResource(R.drawable.heart_half);
					
					hp1.setVisibility(View.VISIBLE);
					hp2.setVisibility(View.VISIBLE);
					hp3.setVisibility(View.VISIBLE);
					hp4.setVisibility(View.VISIBLE);
					hp5.setVisibility(View.INVISIBLE);
				}else if(hp > 60){
					hp1.setImageResource(R.drawable.heart_full);
					hp2.setImageResource(R.drawable.heart_full);
					hp3.setImageResource(R.drawable.heart_full);
					hp4.setImageResource(R.drawable.heart_half);
					hp5.setImageResource(R.drawable.heart_half);
					
					hp1.setVisibility(View.VISIBLE);
					hp2.setVisibility(View.VISIBLE);
					hp3.setVisibility(View.VISIBLE);
					hp4.setVisibility(View.INVISIBLE);
					hp5.setVisibility(View.INVISIBLE);
				}else if(hp > 50){
					hp1.setImageResource(R.drawable.heart_full);
					hp2.setImageResource(R.drawable.heart_full);
					hp3.setImageResource(R.drawable.heart_half);
					hp4.setImageResource(R.drawable.heart_half);
					hp5.setImageResource(R.drawable.heart_half);
					
					hp1.setVisibility(View.VISIBLE);
					hp2.setVisibility(View.VISIBLE);
					hp3.setVisibility(View.VISIBLE);
					hp4.setVisibility(View.INVISIBLE);
					hp5.setVisibility(View.INVISIBLE);
				}else if(hp > 40){
					hp1.setImageResource(R.drawable.heart_full);
					hp2.setImageResource(R.drawable.heart_full);
					hp3.setImageResource(R.drawable.heart_half);
					hp4.setImageResource(R.drawable.heart_half);
					hp5.setImageResource(R.drawable.heart_half);
					
					hp1.setVisibility(View.VISIBLE);
					hp2.setVisibility(View.VISIBLE);
					hp3.setVisibility(View.INVISIBLE);
					hp4.setVisibility(View.INVISIBLE);
					hp5.setVisibility(View.INVISIBLE);
				}else if(hp > 30){
					hp1.setImageResource(R.drawable.heart_full);
					hp2.setImageResource(R.drawable.heart_half);
					hp3.setImageResource(R.drawable.heart_half);
					hp4.setImageResource(R.drawable.heart_half);
					hp5.setImageResource(R.drawable.heart_half);
					
					hp1.setVisibility(View.VISIBLE);
					hp2.setVisibility(View.VISIBLE);
					hp3.setVisibility(View.INVISIBLE);
					hp4.setVisibility(View.INVISIBLE);
					hp5.setVisibility(View.INVISIBLE);
				}else if(hp > 20){
					hp1.setImageResource(R.drawable.heart_full);
					hp2.setImageResource(R.drawable.heart_half);
					hp3.setImageResource(R.drawable.heart_half);
					hp4.setImageResource(R.drawable.heart_half);
					hp5.setImageResource(R.drawable.heart_half);
					
					hp1.setVisibility(View.VISIBLE);
					hp2.setVisibility(View.INVISIBLE);
					hp3.setVisibility(View.INVISIBLE);
					hp4.setVisibility(View.INVISIBLE);
					hp5.setVisibility(View.INVISIBLE);
				}else if(hp > 10){
					hp1.setImageResource(R.drawable.heart_half);
					hp2.setImageResource(R.drawable.heart_half);
					hp3.setImageResource(R.drawable.heart_half);
					hp4.setImageResource(R.drawable.heart_half);
					hp5.setImageResource(R.drawable.heart_half);
					
					hp1.setVisibility(View.VISIBLE);
					hp2.setVisibility(View.INVISIBLE);
					hp3.setVisibility(View.INVISIBLE);
					hp4.setVisibility(View.INVISIBLE);
					hp5.setVisibility(View.INVISIBLE);
				}else{
					hp1.setImageResource(R.drawable.heart_half);
					hp2.setImageResource(R.drawable.heart_half);
					hp3.setImageResource(R.drawable.heart_half);
					hp4.setImageResource(R.drawable.heart_half);
					hp5.setImageResource(R.drawable.heart_half);
					
					hp1.setVisibility(View.INVISIBLE);
					hp2.setVisibility(View.INVISIBLE);
					hp3.setVisibility(View.INVISIBLE);
					hp4.setVisibility(View.INVISIBLE);
					hp5.setVisibility(View.INVISIBLE);
				}
				
			}
		});
	}
	
	public void updateFoodEaten(final int currentFood){
		gameActivity.runOnUiThread(new Runnable(){
			public void run(){
				foodEaten.setText("Food: "+currentFood);
			}
		});
	}
	
	/**
	 * Accepts new fps value which is then updated on HUD GUI. <br />
	 * 
	 * GUI must not be null, otherwise nothing is updated.
	 * 
	 * @param newFps
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public void updateRendererFps(final int newRendererFPS, final int newGameFPS){
		if(renderer_fpsView != null && game_fpsView != null && fpsDelayTimer()){
			gameActivity.runOnUiThread(new Runnable(){
				public void run(){
					renderer_fpsView.setText("Renderer FPS: "+newRendererFPS);//TODO optimize with 2 fields (views), so you don't have to append FPS in front.
					game_fpsView.setText("Game FPS: "+newGameFPS);//TODO optimize with 2 fields (views), so you don't have to append FPS in front.
				}
			});
		}
	}
	
	
	/**
	 * Prevents fps GUI to change to often, so it blocks change for time delay. <br />
	 * 
	 * @see FPS_DELAY
	 * 
	 * @return
	 */
	private boolean fpsDelayTimer(){
		if((System.currentTimeMillis() - fpsDelayClock) > FPS_DELAY){
			fpsDelayClock = System.currentTimeMillis();
			return true;
		}else{
			return false;
		}
	}
}

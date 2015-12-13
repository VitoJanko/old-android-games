package com.hunted_seas.game.graphics.loading;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hunted_seas.game.GameActivity;
import com.hunted_seas.game.R;

public class LoadingScreenAnimation {
	
	private GameActivity gameActivity;
	
	
	private RelativeLayout animationLayout;
	private ImageView loadingBackground;
	private ImageView loadingImage;
	
	private Animation rotatingAnimation;
	
	public LoadingScreenAnimation(GameActivity gameActivity, RelativeLayout loadingScreenLayout){
		this.gameActivity = gameActivity;
		this.animationLayout = loadingScreenLayout;
	}
	
	public void startAnimation(){
		gameActivity.runOnUiThread(new Runnable(){
			public void run(){
				animationLayout.setVisibility(View.VISIBLE);
				loadingBackground = (ImageView) animationLayout.findViewById(R.id.loading_background);
				loadingImage = (ImageView) animationLayout.findViewById(R.id.loading_image);
				
				rotatingAnimation = AnimationUtils.loadAnimation(gameActivity, R.animator.rotate_animation);
				loadingImage.startAnimation(rotatingAnimation);
			}
		});
	}
	
	public void stopAnimation(){
		gameActivity.runOnUiThread(new Runnable(){
			public void run(){
				animationLayout.setVisibility(View.GONE);
				rotatingAnimation.cancel();
			}
		});
	}
}

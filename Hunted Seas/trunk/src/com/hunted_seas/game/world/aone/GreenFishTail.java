//package com.hunted_seas.game.world.aone;
//
//import java.util.Random;
//
//import android.util.Log;
//
//import com.hunted_seas.game.R;
//import com.hunted_seas.game.world.acommon.Player;
//import com.hunted_seas.game.world.acommon.Sprite;
///**
// * Green fish sprite
// * @author Jani Bizjak <janibizjak@gmail.com>
// *
// */
//public class GreenFishTail extends Sprite {
//	
//	public static int[] texture = {R.drawable.green_fish_tail_a,R.drawable.green_fish_tail_b,
//		R.drawable.green_fish_tail_c, R.drawable.green_fish_tail_d, R.drawable.green_fish_tail_e};
//	public static int[] model = {R.raw.green_fish_tail_ta,R.raw.green_fish_tail_tb,R.raw.greeen_fish_tail_tc,
//		R.raw.green_fish_tail_td,R.raw.green_fish_tail_te};
//	
//	private Random random = new Random();
//	
//	private float animationSpeed = 0;
//	private int animationCurrent = 0;
//	public int[] states = {0,0,0,0,1,2,2,1,0,0,3,4,4,3}; 
//	
//	public GreenFishTail(){
//		super(0,0,0);
//		spawnFish();
//	}
//	
//	public GreenFishTail(float x, float y, float speed, float direction){
//		super(x,y,0,speed,direction);
//		flipped = (int)-direction;
//	}
//	
//	public GreenFishTail(float x, float y, float z, float speed, float direction){
//		super(x,y,z,speed,direction);
//		flipped = (int)-direction;
//	}
//	
//	@Override
//	public boolean step(float stepScale) {
//		animationSpeed+=stepScale;
//		if(animationSpeed >= 2){
//			animationSpeed = 0;
//			animationCurrent++;
//			if(animationCurrent>=states.length)
//				animationCurrent = 0;
//			stateType = states[animationCurrent];
//			//Log.d("Neki","Neki: "+stateType);
//		}
//		return true;
//	}
//	
//	private void spawnFish(){
//		x = master.level.getRightBound()+2*radius;
//		y = random.nextInt(1400) - 700;
//		speed = random.nextInt(6)+1;
//	}
//
//	
//	@Override
//	public boolean fineCollisionDetection(Sprite sprite) {
//		float distance = getDistanceToObject(sprite.getPosition());
//		if(distance < (radius+sprite.getCoarseCollisionRadius())*0.8){
//			Player player = (Player)sprite; 
//			player.changeHealth(-5);
//			float alpha = getDirectionToPoint(sprite.getPosition(),getPosition());
//			player.graduallyMovePlayer(alpha, 25, 2f);
//			return true;
//		}
//		return false;
//	}
//
//}

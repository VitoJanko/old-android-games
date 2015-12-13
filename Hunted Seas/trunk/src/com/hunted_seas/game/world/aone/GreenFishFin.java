//package com.hunted_seas.game.world.aone;
//
//import java.util.Random;
//
//import com.hunted_seas.game.R;
//import com.hunted_seas.game.world.acommon.Player;
//import com.hunted_seas.game.world.acommon.Sprite;
//
///**
// * Green fish sprite
// * @author Jani Bizjak <janibizjak@gmail.com>
// *
// */
//public class GreenFishFin extends Sprite {
//	
//	public static int[] texture = {R.drawable.green_fish_fin_lower};
//	public static int[] model = {R.raw.green_fin};
//	
//	private Random random = new Random();
//	
//	public GreenFishFin(){
//		super(0,0,0);
//		spawnFish();
//	}
//	
//	public GreenFishFin(float x, float y, float speed, float direction){
//		super(x,y+5,0,speed,direction);
//		flipped = (int)-direction;
//	}
//	
//	public GreenFishFin(float x, float y, float z, float speed, float direction){
//		super(x,y+5,z,speed,direction);
//		flipped = (int)-direction;
//	}
//	
//	@Override
//	public boolean step(float stepScale) {
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

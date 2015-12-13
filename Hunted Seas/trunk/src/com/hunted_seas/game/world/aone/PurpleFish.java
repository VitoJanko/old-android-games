package com.hunted_seas.game.world.aone;

import java.util.ArrayList;
import java.util.Random;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

/**
 * Green fish sprite
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class PurpleFish extends Sprite {
	public static final String FOLDER = "simple_fish/";
	
//	public static String[] texture = {FOLDER + "fish_ra", FOLDER + "fish_rb", FOLDER + "fish_rc", FOLDER + "fish_rd",
//		FOLDER + "fish_re", FOLDER + "fish_rf", FOLDER + "fish_rg"};
	
	public static String[] texture = {"temp/fish_pa", "temp/fish_pb", "temp/fish_pc", "temp/fish_pd",
		"temp/fish_pe","temp/fish_pf","temp/fish_pg"};
	
	public static int[] model = {R.raw.simple_fish_a,R.raw.simple_fish_b,R.raw.simple_fish_c,R.raw.simple_fish_d,
		R.raw.simple_fish_e,R.raw.simple_fish_f,R.raw.simple_fish_g};
	
	private Random random = new Random();
	
	private GreenFishEye eye;
	private GreenFishPupil pupil;
	private GreenFishMaster connector;
	
	private float animationSpeed = 0;
	private int animationCurrent = 0;
	public int[] states = {0,1,2,3,3,2,1,0,4,5,6,6,5,4}; 
	
	int directionY;
	PurpleFish follow;
	
	int state = 0;
	LevelManager level;
	boolean sorted;
	
	public PurpleFish(){
		super(0,0,0);
		spawnFish();
	}
	
	public PurpleFish(float x, float y, float speed, float direction, GreenFishMaster connector, LevelManager level,
			PurpleFish follow){
		super(x,y,-(float)(((int)(Math.random()*20))/10f),speed,direction);
		flipped = (int)-direction;
		eye = new GreenFishEye(x,y,z,speed,direction);
		pupil = new GreenFishPupil(x,y,z,speed,direction);
		this.connector = connector;
		setScale(1.3f);
		eye.setScale(scale);
		pupil.setScale(scale);
		pupil.eyeState = eye.getAnimationState();
		connector.addEye(eye);
		connector.addPupil(pupil);
		directionY=1;
		this.level = level;
		this.follow = follow;
		sorted = true;
	}

	
	@Override
	public boolean step(float stepScale) {
		if(follow==null){
			float[] player = level.getPlayer().getPosition();
			if(state==0){
				float distance = getDistanceToObject(player);
				if(distance<1600 && x*direction<player[0]*direction)
					state=1;
				if(player[1]>y)
					directionY = 1;
				else
					directionY = -1;
			}
			if(state==1){
				if(direction>0 && x>player[0])
					state = 0;
				if(direction<0 && x<player[0])
					state = 0;
				if(directionY>0 &&  y>player[1])
					state = 0;
				if(directionY<0 &&  y<player[1])
					state = 0;
			}
			if(state==0 || state==3)
				x += direction * stepScale * speed;
			if(state==1){
				x += direction * stepScale * speed;
				y += directionY * stepScale * speed;
			}
			
		}
		else{
			if(sorted && Math.abs(y-follow.y)>100){
				if(y<follow.y)
					directionY = 1;
				else
					directionY = -1;
				sorted = false;
			}
			else if(!sorted && Math.abs(y-follow.y)<=speed*stepScale){
				sorted = true;
				directionY = 0;
			}
			x += direction * stepScale * speed;
			y += directionY * stepScale * speed;
		}
		
		
		
		
		if (x < master.getLvL().getLeftBound()-5*radius)
			setDead(true);
		if (x > master.getLvL().getRightBound()+5*radius)
			setDead(true);
		makeAnimation(stepScale);
		eye.setLocation(x, y);
		pupil.setLocation(x, y);
		return true;
	}
	
	public void makeAnimation(float stepScale){
		animationSpeed+=stepScale;
		if(animationSpeed >= 2){
			animationSpeed = 0;
			animationCurrent++;
			if(animationCurrent>=states.length)
				animationCurrent = 0;
			animationState = (states[animationCurrent]);
		}
	}
	
	private void spawnFish(){
		x = master.getLvL().getRightBound()+2*radius;
		y = random.nextInt(1400) - 700;
		speed = random.nextInt(6)+1;
	}

	
	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {
		Player player = (Player)sprite; 
		player.changeHealth(-10);
		float alpha = getDirectionToPoint(sprite.getPosition(),getPosition());
		player.graduallyMovePlayer(alpha, 35, 2f);
	}

}

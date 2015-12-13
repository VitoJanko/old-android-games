package com.hunted_seas.game.world.aone;

import java.util.Random;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

/**
 * Green fish sprite
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class RedFish extends Sprite {
	public static final String FOLDER = "simple_fish/";
	
//	public static String[] texture = {FOLDER + "fish_ra", FOLDER + "fish_rb", FOLDER + "fish_rc", FOLDER + "fish_rd",
//		FOLDER + "fish_re", FOLDER + "fish_rf", FOLDER + "fish_rg"};
	
	public static String[] texture = {"temp/fish_ra", "temp/fish_rb", "temp/fish_rc", "temp/fish_rd",
		"temp/fish_re","temp/fish_rf","temp/fish_rg"};
	
	public static int[] model = {R.raw.simple_fish_a,R.raw.simple_fish_b,R.raw.simple_fish_c,R.raw.simple_fish_d,
		R.raw.simple_fish_e,R.raw.simple_fish_f,R.raw.simple_fish_g};
	
	private Random random = new Random();
	
	private GreenFishEye eye;
	private GreenFishPupil pupil;
	private GreenFishMaster connector;
	
	private float animationSpeed = 0;
	private int animationCurrent = 0;
	public int[] states = {0,1,2,3,3,2,1,0,4,5,6,6,5,4}; 
	
	float ySpeed;
	float progression;
	
	public RedFish(){
		super(0,0,0);
		spawnFish();
	}
	
	public RedFish(float x, float y, float speed, float direction, GreenFishMaster connector, float delay){
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
		ySpeed = speed;
		progression = delay;
	}

	
	@Override
	public boolean step(float stepScale) {
		x += direction * stepScale * speed;
		y+=ySpeed*Math.sin(progression) * stepScale;
		progression+= stepScale *(2*Math.PI)/200 ;
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
		player.graduallyMovePlayer(alpha, 25, 2f);
	}

}

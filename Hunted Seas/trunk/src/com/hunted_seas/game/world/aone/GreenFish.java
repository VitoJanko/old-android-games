package com.hunted_seas.game.world.aone;

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
public class GreenFish extends Sprite {
	public static final String FOLDER = "simple_fish/";
	
	public static String[] texture = {FOLDER+"fish_a", FOLDER+"fish_b", FOLDER+"fish_c", FOLDER+"fish_d",
		FOLDER+"fish_e",FOLDER+"fish_f",FOLDER+"fish_g"};

	public static String[] textureAlt = {"temp/fish_goa", "temp/fish_gob", "temp/fish_goc", "temp/fish_god",
		"temp/fish_goe","temp/fish_gof","temp/fish_gog"};
	
	public static String[] textureYellow = {"temp/fish_yoa", "temp/fish_yob", "temp/fish_yoc", "temp/fish_yod",
		"temp/fish_yoe","temp/fish_yof","temp/fish_yog"};
	
	public static int[] model = {R.raw.simple_fish_a,R.raw.simple_fish_b,R.raw.simple_fish_c,R.raw.simple_fish_d,
		R.raw.simple_fish_e,R.raw.simple_fish_f,R.raw.simple_fish_g};

	
	private Random random = new Random();
	
	private GreenFishEye eye;
	private GreenFishPupil pupil;
	private GreenFishMaster connector;
	
	private float animationSpeed = 0;
	private int animationCurrent = 0;
	public int[] states = {0,1,2,3,3,2,1,0,4,5,6,6,5,4}; 
	
	boolean respawn = false;
	boolean wait = false;
	boolean waiting = true;
	LevelManager level;
	
	public GreenFish(){
		super(0,0,0);
		spawnFish();
	}
	
	public GreenFish(float x, float y, float speed, float direction, GreenFishMaster connector){
		super(x,y,-(float)(((int)(Math.random()*20))/10f),speed,direction);
		flipped = (int)-direction;
		eye = new GreenFishEye(x,y,z,speed,direction);
		pupil = new GreenFishPupil(x,y,z,speed,direction);
		this.connector = connector;
		setScale(1.3f);
		eye.setScale(scale);
		pupil.setScale(scale);
		connector.addEye(eye);
		connector.addPupil(pupil);
	}
	
	public GreenFish(float x, float y, float z, float speed, float direction, GreenFishMaster connector){
		super(x,y,z,speed,direction);
		flipped = (int)-direction;
		this.connector = connector;
	}
	
	@Override
	public boolean step(float stepScale) {
		if(wait){
			if(Math.abs((int)x/1100-(int)level.getPlayer().getPosition()[0]/1100)<2){
				waiting = false;
			}
		}
		if(!wait || !waiting)
			x += direction * stepScale * speed;
		if (x < master.getLvL().getLeftBound()-5*radius)
			if(respawn)
				x = master.getLvL().getRightBound()+3*radius;
			else
				die();
		if (x > master.getLvL().getRightBound()+5*radius)
			die();
		makeAnimation(stepScale);
		pupil.eyeState = eye.getAnimationState();
		eye.setLocation(x, y);
		pupil.setLocation(x, y);
		return true;
	}
	
	public void die(){
		setDead(true);
		eye.setDead(true);
		pupil.setDead(true);
	}
	
	public void makeAnimation(float stepScale){
		animationSpeed+=stepScale;
		if(animationSpeed >= 2){
			animationSpeed = 0;
			animationCurrent++;
			if(animationCurrent>=states.length)
				animationCurrent = 0;
			animationState = (states[animationCurrent]);
			//Log.d("Neki","Neki: "+stateType);
		}
	}
	
	private void spawnFish(){
		x = master.getLvL().getRightBound()+2*radius;
		y = random.nextInt(1400) - 700;
		speed = random.nextInt(6)+1;
	}

	
	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {
		Player player = (Player)sprite; //TODO tole ni uredu, ker ce sprite ni player crasha
		
		player.changeHealth(-10);
		float alpha = getDirectionToPoint(sprite.getPosition(),getPosition());
		player.graduallyMovePlayer(alpha, 25, 2f);
	}

}

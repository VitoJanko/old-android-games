package com.hunted_seas.game.world.player;

import java.util.LinkedList;
import java.util.logging.Level;

import android.content.Context;
import android.util.Log;

import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.data.LineSegment;
import com.hunted_seas.game.immersion.SoundPoolHelper;
import com.hunted_seas.game.objects.masters.SpriteMaster;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Lights;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;

/**
 * Sprite that presents player.
 * 
 * Only 1 kind of this sprite (player) can be present at one time in lvl.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class Player extends Sprite implements SpriteManagerInterface{
	public static final String FOLDER = "jelly/";
		
	private PlayerBrain brainSprite;
	private PlayerEyebrows eyebrowsSprite;
	private PlayerEyes eyesSprite;
	private PlayerHead headSprite;
	private PlayerPupils pupilsSprite;
	private PlayerTentacles tentaclesSprite;
	private PlayerMouth mouthSprite;
	
	private SpriteMaster brain;
	private SpriteMaster eyebrows;
	private SpriteMaster eyes;
	private SpriteMaster head;
	private SpriteMaster pupils;
	private SpriteMaster tentacles;
	private SpriteMaster mouth;
	
	private LevelManager lvl;
	
	
	
	
	private int player_health = 100;
	private int food_eaten = 0;
	
	private boolean healthChanged = false;
	private boolean foodChanged = false;
	
	private float forcefullDirection = 0;
	private float forcefullMagnitude = 0;
	private float forcefullPersistance = 0;
	
	private long lastTimeHit = 0;
	
	private float alpha = 1.0f;
	
	public Player(LevelManager lvl){
		super(0, 0, 0);
		
		this.lvl = lvl;
		
		brainSprite = new PlayerBrain();
		eyebrowsSprite = new PlayerEyebrows();
		eyesSprite = new PlayerEyes();
		headSprite = new PlayerHead();
		pupilsSprite = new PlayerPupils();
		tentaclesSprite = new PlayerTentacles();
		mouthSprite = new PlayerMouth();
		
		brain = new SpriteMaster(lvl,false);
		eyebrows = new SpriteMaster(lvl,false);
		eyes = new SpriteMaster(lvl,false);
		head = new SpriteMaster(lvl,false);
		pupils= new SpriteMaster(lvl,false);
		tentacles = new SpriteMaster(lvl,false);
		mouth = new SpriteMaster(lvl,false);		
	}

	@Override
	public void loadSprite(Context context, ShaderProgramInterface shaderProgram) {		
		brain.loadSprite(context, shaderProgram, PlayerBrain.texture, PlayerBrain.model,false);
		eyebrows.loadSprite(context, shaderProgram, PlayerEyebrows.texture, PlayerEyebrows.model, false);
		eyes.loadSprite(context, shaderProgram, PlayerEyes.texture, PlayerEyes.model, false);
		head.loadSprite(context, shaderProgram, PlayerHead.texture, PlayerHead.model, false);
		pupils.loadSprite(context, shaderProgram, PlayerPupils.texture, PlayerPupils.model, false);
		tentacles.loadSprite(context, shaderProgram, PlayerTentacles.texture, PlayerTentacles.model, false);
		mouth.loadSprite(context, shaderProgram, PlayerMouth.texture, PlayerMouth.model, false);
		
		brain.addElement(brainSprite);
		eyebrows.addElement(eyebrowsSprite);
		eyes.addElement(eyesSprite);
		head.addElement(headSprite);
		pupils.addElement(pupilsSprite);
		tentacles.addElement(tentaclesSprite);
		mouth.addElement(mouthSprite);
		
		calculateRadius();
	}
	

	@Override
	public boolean step(float stepScale) { //TODO uporabi stepScale
		if(forcefullMagnitude>0){
			x+=Math.cos(forcefullDirection)*forcefullMagnitude*stepScale;
			y+=Math.sin(forcefullDirection)*forcefullMagnitude*stepScale;
			forcefullMagnitude-=forcefullPersistance*stepScale;
			
			//TODO spremeni to
			headSprite.setAlpha(alpha);
			tentaclesSprite.setAlpha(alpha);
			
			
			mouthSprite.hurt = true;
		}
		else{
			mouthSprite.hurt = false;
			
			headSprite.setAlpha(1);
			tentaclesSprite.setAlpha(1);
		}
		

		
		brain.step(stepScale);
		eyebrows.step(stepScale);
		eyes.step(stepScale);
		head.step(stepScale);
		pupils.step(stepScale);
		tentacles.step(stepScale);
		mouth.step(stepScale);
		
		return true;
	}

	@Override
	public void draw(float[] viewMatrix, Lights lights) {
//		lvl.disableDepthTesting();
		
		tentacles.draw(viewMatrix, lights);
		brain.draw(viewMatrix, lights);
		head.draw(viewMatrix, lights);
		mouth.draw(viewMatrix, lights);
		eyebrows.draw(viewMatrix, lights);
		eyes.draw(viewMatrix, lights);
		pupils.draw(viewMatrix, lights);

//		lvl.enableDepthTesting();
	}

	private void calculateRadius(){
		float left = Math.min(headSprite.getBoundingBox().left, tentaclesSprite.getBoundingBox().left);
		float top = Math.max(headSprite.getBoundingBox().top, tentaclesSprite.getBoundingBox().top);
		float right = Math.max(headSprite.getBoundingBox().right, tentaclesSprite.getBoundingBox().right);
		float bottom = Math.min(headSprite.getBoundingBox().bottom, tentaclesSprite.getBoundingBox().bottom);
		
		radius = (float) Math.sqrt( Math.pow(right-left, 2) + Math.pow(top-bottom,2)) / 2f;	
	}
	
	public boolean didHealthChanged(){
		return healthChanged;
	}
	
	public boolean didFoodChanged(){
		return foodChanged;
	}
	
	public int getPlayerHealth(){
		return player_health;
	}
	
	public int getFoodEaten(){
		return getFood_eaten();
	}
	
	public void changeHealth(int dHP){
		if(dHP < 0){
			if((System.currentTimeMillis() - lastTimeHit) < 1000){
				return;
			}else{
				lastTimeHit = System.currentTimeMillis();
				lvl.playSound(SoundPoolHelper.auu);
			}
		}
				
		player_health += dHP;
		if(player_health > 100){
			player_health = 100;
		}else if(player_health < 0){ //TODO game over
			player_health = 0;
		}
		
		healthChanged = true;
	}
	
	public void eatFood(int newFoodEaten){
		setFood_eaten(getFood_eaten() + newFoodEaten);
		foodChanged = true;
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void movePlayer(float dx,float dy){
		movePlayer(dx, dy, false);
	}
	
	public void movePlayer(float dx,float dy,boolean janiForce){
		if(lockMovement && !janiForce)
			return;
		
		if(forcefullMagnitude<=0){
			x += dx;
			y += dy;
		}
		
		brainSprite.move(x,y, z);
		eyebrowsSprite.move(x, y, z);
		eyesSprite.move(x, y, z);
		headSprite.move(x, y, z);
		pupilsSprite.move(x, y, z);
		tentaclesSprite.move(x, y, z);
		mouthSprite.move(x, y, z);

		for(LineSegment seg : getCurrentLineSegment()){
			seg.moveColidableObject(getModelMatrix());
		}
	}
	
	boolean lockMovement = false;
	
	public void lockMovement(boolean lock){
		this.lockMovement = lock;
	}
	
	public void graduallyMovePlayer(float direction, float magnitude, float persistance){
		graduallyMovePlayer(0.5f, direction, magnitude, persistance);
	}
	
	public void graduallyMovePlayer(float alpha,float direction, float magnitude, float persistance){
		this.alpha = alpha;
		forcefullDirection = direction;
		forcefullMagnitude = magnitude;
		forcefullPersistance = persistance;
	}
	
	/**
	 * Deprecated. Use {@link Sprite#getPosition()}
	 * 
	 * @return x,y
	 */
	@Deprecated
	public float[] getPlayerPosition(){
		return new float[]{x,y};
	}

	
	@Override
	public float getCoarseCollisionRadius() {
		return radius;
	}

	@Override
	public BoundingBox getCoarseCollisionSquare() {
		return headSprite.getBoundingBox();
	}

	@Override
	public float[] getModelMatrix() {
		return headSprite.getModelMatrix(); //TODO to ni prav je temp!!
	}

	@Override
	public void reSpawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUp_CollisionDetector(CollisionDetector_Manager collisionDetector) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns position.
	 * [x,y,z]
	 * 
	 * @author Jani
	 */
	@Override
	public float[] getPosition() {
		return new float[]{x,y,z};
	}
	
	@Override
	public void setRadius(float radius) {
		// TODO Auto-generated method stub
		
	}

	public int getFood_eaten() {
		return food_eaten;
	}

	public void setFood_eaten(int food_eaten) {
		this.food_eaten = food_eaten;
	}
	
	@Override
	public LinkedList<LineSegment> getCurrentLineSegment(){
		return headSprite.getCurrentLineSegment(); //TODO TEMP!
	}
	
	
	
	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {}

	@Override
	public LevelManager getLvL() {
		// TODO Auto-generated method stub
		return null;
	}

}

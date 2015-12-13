package com.huntedseas;

public class LevelGenerator {
	private Level1 level1;
	private Protagonist protagonist;
	private Bubbles bubbles;
	private GreenFish fish;
	
	private Model model;
	
	public LevelGenerator(){
		//level1 = new Level1();
		//protagonist = new Protagonist();
		//bubbles = new Bubbles();
		//fish = new GreenFish();
		model = new Model(R.drawable.floor_sand_1, R.raw.floor_sand_1);
	}
	
	protected void draw(float[] mMVPMatrix){	
		//level1.draw(mMVPMatrix);
		//bubbles.drawNegative(mMVPMatrix);
		//protagonist.draw(mMVPMatrix);
		//fish.draw(mMVPMatrix);
		//bubbles.drawPositive(mMVPMatrix);
		model.draw(mMVPMatrix);
	}
	
	protected void destroy(){
		//TODO
		//Iz neznanega razloga crasha ce naredim to tuki, zna bit, da že sam OpenGL destroya objekte/texture ko zapreš
//		bubbles.destroy();
//		fish.destroy();
//		level1 = null;
//		protagonist = null;
//		bubbles = null;
//		fish = null;
	}
}

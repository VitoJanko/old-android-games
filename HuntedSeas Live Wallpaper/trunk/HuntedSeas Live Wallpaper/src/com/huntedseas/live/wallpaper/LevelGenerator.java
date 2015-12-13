package com.huntedseas.live.wallpaper;

public class LevelGenerator {	
	//private Model model;
	private SquareGL ozadje;
	
	protected boolean ozadjeReady = false;
	
	public LevelGenerator(){
		//level1 = new Level1();
		//protagonist = new Protagonist();
		//bubbles = new Bubbles();
		//fish = new GreenFish();
		//model = new Model(R.drawable.floor_sand_1, R.raw.floor_sand_1);
		
		
		

	}
	
	protected void draw(float[] mMVPMatrix){	
		//level1.draw(mMVPMatrix);
		//bubbles.drawNegative(mMVPMatrix);
		//protagonist.draw(mMVPMatrix);
		//fish.draw(mMVPMatrix);
		//bubbles.drawPositive(mMVPMatrix);
		//model.draw(mMVPMatrix);
		
		if(ozadjeReady){
			ozadje.draw(mMVPMatrix);
		}
	}
	
	protected void makeOzadje(){
		float coordinates[] = {
				-GameRenderer.offsetX,  GameRenderer.offsetY, 0.0f, //top left
				-GameRenderer.offsetX, -GameRenderer.offsetY, 0.0f, //bottom left
				GameRenderer.offsetX, -GameRenderer.offsetY, 0.0f, //bottom right
				GameRenderer.offsetX,  GameRenderer.offsetY, 0.0f  //top right
			};
			ozadje = new SquareGL(R.drawable.conceptart, coordinates);
			
			ozadjeReady = true;
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

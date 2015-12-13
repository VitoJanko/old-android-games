package com.huntedseas;


public class Level1{
	private float coordinates1[] = {
		-400.0f,  220.8f, 100.0f, //top left
		-400.0f, -220.8f, 100.0f, //bottom left
		 400.0f, -220.8f, 100.0f, //bottom right
		 400.0f,  220.8f, 100.0f  //top right
	};
	
	private float coordinates2[] = {
		-40.0f,  22.4f, 0.0f, //top left
		-40.0f, -22.4f, 0.0f, //bottom left
		 40.0f, -22.4f, 0.0f, //bottom right
		 40.0f,  22.4f, 0.0f  //top right
	};
	
	private float coordinates3[] = {
		-40.0f,  22.4f, -0.1f, //top left
		-40.0f, -22.4f, -0.1f, //bottom left
		 40.0f, -22.4f, -0.1f, //bottom right
		 40.0f,  22.4f, -0.1f  //top right
	};
	
	private float coordinates4[] = {
		-40.0f,  22.4f, -0.1f, //top left
		-40.0f, -22.4f, -0.1f, //bottom left
		 40.0f, -22.4f, -0.1f, //bottom right
		 40.0f,  22.4f, -0.1f  //top right
	};

	private static SquareGL background1;
	private SquareGL background2;
	private SquareGL background3;
	private SquareGL background4;
	
	protected static float viewYP = 40;
	protected static float viewYM = -40;
	protected static float viewXP = 22.4f;
	protected static float viewXM = -22.4f;
	
	public Level1() {
		background1 = new SquareGL(R.drawable.water, coordinates1);
		background2 = new SquareGL(R.drawable.floor1, coordinates2);
		background3 = new SquareGL(R.drawable.sand, coordinates3);
		background4 = new SquareGL(R.drawable.foreground,coordinates4);
	}
	
	public void draw(float[] mVPMatrix){
		background1.draw(mVPMatrix);
		background2.draw(mVPMatrix);
		background3.draw(mVPMatrix);
		background4.draw(mVPMatrix);
	}
	
}
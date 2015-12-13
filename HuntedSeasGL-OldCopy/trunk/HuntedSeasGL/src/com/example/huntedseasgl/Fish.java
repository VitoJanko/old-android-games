package com.example.huntedseasgl;

public class Fish extends Instance{

	static LoadState loader;
	
	
	public static void load(Sea host){
		loader = new LoadState(host,R.drawable.fish,R.raw.green_fish_2);
	}

	
	public Fish(Sea host, float x, float y) {
		super(host, x, y,loader);
		System.out.println(loader.texture[0]);
		textureList[0]=loader.texture[0];
		speed = 0.5f;
	}
	
	void step(){
		x+=host.timePassed*speed;
		super.step();
	}

}

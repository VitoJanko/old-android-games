package org.bizjak.android.GPU;



public class Fish extends Instance{

	static LoadState loader;
	int animation = 0;
	
	public static void load(Sea host){
		int[] images = {R.drawable.fish1,R.drawable.fish2,R.drawable.fish4,R.drawable.fish3};
		loader = new LoadState(host,images,R.raw.green_fish_2);
	}

	
	public Fish(Sea host, float x, float y) {
		super(host, x, y,0.8f,loader);
		//textureList[0]=loader.texture[0];
		speed = 0.5f;
	}
	
	void step(){
		animation+=host.timePassed;
		if (animation>300){
			nextFrame();
			animation= 0;
		}
		x+=host.timePassed*speed;
		if (collide(getBox(),host.hero.getBox()) )
			demage(1f,0.01f,2);
		if(x>host.xEnd+50)
			dead=true;
		super.step();
	}

}

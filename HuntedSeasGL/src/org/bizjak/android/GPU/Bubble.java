package org.bizjak.android.GPU;



public class Bubble extends Instance{

	static LoadState loader;
	
	
	public static void load(Sea host){
		loader = new LoadState(host,R.drawable.bubble2,R.raw.bubble);
	}

	
	public Bubble(Sea host, float x, float y) {
		super(host, x, y,(float)(0.5f+Math.random()*1.5),loader);
		speed = (float)(0.1f+Math.random()*scaleFactor/2);
	}
	
	void step(){
		nextFrame();
		y-=host.timePassed*speed;
		if(y<host.yStart-50)
			dead=true;
		super.step();
	}

}

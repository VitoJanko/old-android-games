package com.example.huntedseasgl;

public class Jelly extends Instance{

	static LoadState loader;
	
	public static void load(Sea host){
		loader = new LoadState(host,R.drawable.blowfish_full,R.raw.blowfish_full);
	}

	
	public Jelly(Sea host, float x, float y) {
		super(host, x, y,loader);
		System.out.println(loader.texture[0]);
		textureList[0]=loader.texture[0];
		speed = 0.4f;
	}
	
	void step(){
		float planedX = host.timePassed*host.angleY*speed;
		float planedY = host.timePassed*(host.angleX-host.nagib)*speed;
		//planedX=0;
		//x+=planedX;
		//y+=planedY;
		//host.premikX+=planedX;
		//host.premikY+=planedY;
		
		System.out.println("premikX: "+host.premikY);
		System.out.println("marginX: "+host.marginY);
		System.out.println("x: "+y);
		
		if(x+planedX>host.xStart+host.wallX && x+planedX<host.xEnd-host.wallX){
			x+=planedX;

			if(x-host.premikX<-host.marginX)
				host.premikX=(int)(x+host.marginX);
			if(x-host.premikX>host.marginX)
				host.premikX=(int)(x-host.marginX);
			if(host.premikX>host.xEnd-host.width/2) host.premikX=host.xEnd-host.width/2;
			if(host.premikX<host.xStart+host.width/2) host.premikX=host.xStart+host.width/2;
		}
		
		if(y+planedY>host.yStart+host.wallUp && y+planedY<host.yEnd-host.wallDown){
			y+=planedY;

			if(y-host.premikY<-host.marginY)
				host.premikY=(int)(y+host.marginY);
			if(y-host.premikY>host.marginY)
				host.premikY=(int)(y-host.marginY);
			if(host.premikY>host.yEnd-host.height/2) host.premikY=host.yEnd-host.height/2;
			if(host.premikY<host.yStart+host.height/2) host.premikY=host.yStart+host.height/2;
		}
		//scrolling vertical
		//if(y+planedY>host.wallUp && y+planedY<host.realHeight-host.wallDown-host.hero.h/2){
		//	y+=planedY;
		//	if(y+host.premikY<host.marginY)
		//		host.premikY+=host.marginY-(y+host.premikY);
		//	if(y+host.premikY>host.height-host.marginY)
		//		host.premikY-=y+host.premikY-(host.height-host.marginY);
		//	if(host.premikY>0) host.premikY=0;
		//	if(host.premikY<-(host.realHeight-host.height)) host.premikY=-(host.realHeight-host.height);
		//}
		
		
		
		super.step();
	}

}

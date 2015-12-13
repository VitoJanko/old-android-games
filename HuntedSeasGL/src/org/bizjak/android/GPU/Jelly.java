package org.bizjak.android.GPU;




public class Jelly extends Instance{

	static LoadState loader;
	float recoil;
	float diminish;
	float dirX;
	float dirY;
	
	public static void load(Sea host){
		loader = new LoadState(host,R.drawable.blowfish_full,R.raw.blowfish_full);
	}

	
	public Jelly(Sea host, float x, float y) {
		super(host, x, y,1,loader);
		//System.out.println(loader.texture[0]);
		textureList[0]=loader.texture[0];
		speed = 0.4f;
		recoil = 0;
	}
	
	void step(){
		float planedX = host.timePassed*host.angleY*speed;
		float planedY = host.timePassed*(host.angleX-host.nagib)*speed;
		
		if(recoil!=0){
			float size = (float) Math.sqrt(dirX*dirX+dirY*dirY);
			planedX=(float)(dirX/size)*host.timePassed*recoil;
			planedY=(float)(dirY/size)*host.timePassed*recoil;
			recoil-=host.timePassed*diminish;
			if(recoil<0)recoil=0;
		}
		
		int delay = 25;
		int xStart = host.xStart+delay;
		int yStart = host.yStart+delay;
		int xEnd = host.xEnd-delay;
		int yEnd = host.yEnd-delay;
		
		if(x+planedX>xStart+host.wallX && x+planedX<xEnd-host.wallX){
			x+=planedX;

			if(x-host.premikX<-host.marginX)
				host.premikX=(int)(x+host.marginX);
			if(x-host.premikX>host.marginX)
				host.premikX=(int)(x-host.marginX);
			if(host.premikX>xEnd-host.width/2) host.premikX=xEnd-host.width/2;
			if(host.premikX<xStart+host.width/2) host.premikX=xStart+host.width/2;
		}
		
		if(y+planedY>yStart+host.wallUp && y+planedY<yEnd-host.wallDown){
			y+=planedY;

			if(y-host.premikY<-host.marginY)
				host.premikY=(int)(y+host.marginY);
			if(y-host.premikY>host.marginY)
				host.premikY=(int)(y-host.marginY);
			if(host.premikY>yEnd-host.height/2) host.premikY=yEnd-host.height/2;
			if(host.premikY<yStart+host.height/2) host.premikY=yStart+host.height/2;
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

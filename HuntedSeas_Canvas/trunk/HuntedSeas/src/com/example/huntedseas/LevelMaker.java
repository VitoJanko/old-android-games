package com.example.huntedseas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Debug;
import android.util.Log;

public class LevelMaker{
	
	Sea host;
	float timer;
	
	boolean start = true;
	boolean ate = false;
	boolean firstFive = true;
	boolean spawnBlow = true;
	boolean startLvL = false;
	boolean spawnFood = true;
	
	LevelMaker(Sea host){
		this.host=host;
	}
	
	void inicialize(){
		if (host.level==1){
			for (int i=0; i<8; i++){
				//spawnFood(1);
			}		
		}
		if(host.level==2){
			loadFish();
			loadBubble();
			loadUrchin();
			loadFood();
			loadFishRed();
			makeFloorNormal();
			for (int i=0; i<5; i++)
				spawnUrchin((int)(Math.random()*host.realWidth),host.realHeight-65);
		}
		if (host.level==3){
			loadFishRed();
			loadUrchin();
			loadBubble();
			makeFloorCorridor();
			generateLevel();
		}
		if(host.level==4){
			makeFloorNormal();
			loadFish();
			loadBubble();
			loadUrchin();
			loadFishRed();
			timer=120;
			host.hero.food=25;
			//for(int i=0;i<5;i++){
			//	spawnBlowFishFollower();
			//}
		}
	}
	
	void step(){
		Protagonist hero = host.hero;
		spawnBubbles(0.055);
		if(host.gameMode==host.NORMAL){
			//spawnFood(0.015);
			if(host.level==1)
				tutorialLvL();
			if(host.level==2){
				spawnFood(0.025);
				if (hero.food < 10)
					spawnFish(0.025);
				if(hero.food>5){
					spawnFlock(0.015);
				}
				if(hero.food>10){
					spawnFishWall(0.0035);
				}
				if(hero.food>15){
					spawnFollower(0.003);
				}
			}else if(host.level==4){
				timer-=(float)(1.0/host.framerate);
				if(timer!=0){
					spawnFish(0.025);
					if(timer<100)
						spawnFallingUrchin(0.015);
					//if(timer<40)
					//	spawnAdvanceFlock(0.01);
					if(timer<120)
						spawnHeadbut(0.02);
				}
			}
		}
	}
	

	void tutorialLvL(){
		if(start){
			spawnFood((int)host.hero.x+100,(int)host.hero.y);
			start = false;
			Log.d("Tutorial","Tutorial: start = false");
		}else if(!ate){
			if(host.hero.food >= 1){
				ate = true;
				Log.d("Tutorial","Tutorial: ate = true");
			}
		}else{
			if(firstFive){
				for(int i=0;i<20;i++){
					spawnFood(2);
				}
				firstFive = false;
				Log.d("Tutorial","Tutorial: firstFive = false");
			}else if(host.hero.food >= 5 && spawnBlow){
				spawnBlowFish((int)host.hero.x+150,(int)host.hero.y);
				spawnBlowFish((int)host.hero.x-150,(int)host.hero.y);
				spawnBlowFish((int)host.hero.x,(int)host.hero.y+150);
				spawnBlowFish((int)host.hero.x,(int)host.hero.y+150);
				spawnBlow = false;
				Log.d("Tutorial","Tutorial: soawbBlow = false , startLvL = true");
			}else if(host.hero.food == 0){
				startLvL = true;
			}else if(startLvL){
				if(spawnFood){
					for(int i=0; i < 10; i++){
						spawnFood(1);
					}
					spawnFood=false;
				}
				spawnFish(0.010);
				spawnFood(0.025);
				if(host.hero.food > 10){
					spawnFlock(0.005);
				}
				if(host.hero.food > 15){
					spawnFollower(0.002);
				}
			}
		}
	}
	
	void makeFloorNormal(){
		Bitmap floorFront = BitmapFactory.decodeResource(host.getResources(), R.drawable.floor1);
		floorFront = Bitmap.createScaledBitmap(floorFront,host.realWidth,host.realHeight, true);
		floorFront = Bitmap.createBitmap(floorFront, 0, host.realHeight/2, host.realWidth, host.realHeight/2);
		host.addInstance(new Background(0,host.realHeight/2,host,floorFront,1));
		
		Bitmap floorBack = BitmapFactory.decodeResource(host.getResources(), R.drawable.sand);
		floorBack = Bitmap.createScaledBitmap(floorBack,host.realWidth,host.realHeight, true);
		floorBack = Bitmap.createBitmap(floorBack, 0, host.realHeight/2, host.realWidth, host.realHeight/2);
		host.addInstance(new Background(0,host.realHeight/2,host,floorBack,4));
		
		Bitmap floorRock = BitmapFactory.decodeResource(host.getResources(), R.drawable.foreground);
		floorRock = Bitmap.createScaledBitmap(floorRock,host.realWidth,host.realHeight, true);
		floorRock = Bitmap.createBitmap(floorRock, 0, host.realHeight/2, host.realWidth, host.realHeight/2);
		host.addInstance(new Background(0,host.realHeight/2,host,floorRock,0));
		
		Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
		Debug.getMemoryInfo(memoryInfo);

		String memMessage = String.format(
		    "Memory: Pss=%.2f MB, Private=%.2f MB, Shared=%.2f MB",
		    memoryInfo.getTotalPss() / 1024.0,
		    memoryInfo.getTotalPrivateDirty() / 1024.0,
		    memoryInfo.getTotalSharedDirty() / 1024.0);
		
		Log.d("memory",memMessage);
	}
	
	void makeFloorCorridor(){
		//Popravi trojko
		Bitmap floor1 = BitmapFactory.decodeResource(host.getResources(), R.drawable.floor1v2);
		Bitmap floor2 = BitmapFactory.decodeResource(host.getResources(), R.drawable.floor2v2);
		Bitmap floor3 = BitmapFactory.decodeResource(host.getResources(), R.drawable.floor2v2);
		Bitmap floor1s = BitmapFactory.decodeResource(host.getResources(), R.drawable.floor1solid);
		Bitmap floor2s = BitmapFactory.decodeResource(host.getResources(), R.drawable.floor2solid);
		Bitmap floor3s = BitmapFactory.decodeResource(host.getResources(), R.drawable.floor2solid);
		/*
		Bitmap floor1 = BitmapFactory.decodeResource(host.getResources(), R.drawable.floor1_v2);
		Bitmap floor2 = BitmapFactory.decodeResource(host.getResources(), R.drawable.floor2_v2);
		Bitmap floor3 = BitmapFactory.decodeResource(host.getResources(), R.drawable.floor3_v2);
		*/
		for (int i=0; i<6; i++){
			double r= Math.random();
			if(r<0.33){
				//Bitmap floor = host.scale(floor1,host.width,2*(host.width*floor1.getHeight())/floor1.getWidth());
				Bitmap floor = host.scale(floor1,host.width);
				host.addInstance(new Background(i*host.width,host.realHeight-floor.getHeight(),host,floor,4));
				Bitmap floorS = host.scale(floor1s,host.width);
				host.addInstance(new Background(i*host.width,host.realHeight-floorS.getHeight(),host,floorS,0));
				host.wallDown=(int)(floorS.getHeight()*0.8);
			}
			else if(r<0.66){
				Bitmap floor = host.scale(floor2,host.width);
				host.addInstance(new Background(i*host.width,host.realHeight-floor.getHeight(),host,floor,4));
				Bitmap floorS = host.scale(floor2s,host.width);
				host.addInstance(new Background(i*host.width,host.realHeight-floorS.getHeight(),host,floorS,0));
				host.wallDown=(int)(floorS.getHeight()*0.8);
			}
			else{
				Bitmap floor = host.scale(floor3,host.width);
				host.addInstance(new Background(i*host.width,host.realHeight-floor.getHeight(),host,floor,4));
				Bitmap floorS = host.scale(floor3s,host.width);
				host.addInstance(new Background(i*host.width,host.realHeight-floorS.getHeight(),host,floorS,0));
				host.wallDown=(int)(floorS.getHeight()*0.8);
			}
		}
		Bitmap grass1 = BitmapFactory.decodeResource(host.getResources(), R.drawable.seaweed1);
		Bitmap grass2 = BitmapFactory.decodeResource(host.getResources(), R.drawable.seaweed2);
		Bitmap grass3 = BitmapFactory.decodeResource(host.getResources(), R.drawable.seaweed3);
		Bitmap grass4 = BitmapFactory.decodeResource(host.getResources(), R.drawable.seaweed4);
		Bitmap grass5 = BitmapFactory.decodeResource(host.getResources(), R.drawable.seaweed5);
		Bitmap grass6 = BitmapFactory.decodeResource(host.getResources(), R.drawable.seaweed6);
		Bitmap grass7 = BitmapFactory.decodeResource(host.getResources(), R.drawable.seaweed7);
		Bitmap grass8 = BitmapFactory.decodeResource(host.getResources(), R.drawable.seaweed8);
		for (int i=0; i<40; i++){
			double r= Math.random();
			Bitmap grass;
			if (r<0.125) grass=host.scale(grass1,30);
			else if (r<0.250) grass=host.scale(grass2,30);
			else if (r<0.375) grass=host.scale(grass3,30);
			else if (r<0.500) grass=host.scale(grass4,30);
			else if (r<0.625) grass=host.scale(grass5,30);
			else if (r<0.750) grass=host.scale(grass6,30);
			else if (r<0.875) grass=host.scale(grass7,30);
			else grass=host.scale(grass8,30);
			int x=(int)(host.realWidth*Math.random());
			int y=(int)(host.wallDown*1.5*Math.random());
			int depth=0;
			if(y>host.wallDown) depth=3;
			host.addInstance(new Background(x,host.realHeight-y-grass.getHeight(),host,grass,depth));
		}
	}
	
	void generateLevel(){
		BufferedReader br = null;
		try {
			String currentLine;
			InputStream is = host.getResources().openRawResource(R.raw.level3);
			InputStreamReader inputStreamReader = new InputStreamReader(is);
			br = new BufferedReader(inputStreamReader);
			int y=0;
			while ((currentLine = br.readLine()) != null) {
				y+=host.realHeight/10;
				int x=0;
				for (int i=0; i<currentLine.length(); i++){
					x+=host.realWidth/100;
					if(currentLine.charAt(i)=='x')
						spawnBlowFish(x,y);
					if(currentLine.charAt(i)=='U')	
						spawnUrchin(x,y);
					if(currentLine.charAt(i)=='1')
						makeRock(1,x);
					if(currentLine.charAt(i)=='2')
						makeRock(2,x);
					if(currentLine.charAt(i)=='3')
						makeRock(3,x);
					if(currentLine.charAt(i)=='S'){
						float speed = host.calibrate(3);
						float angular = (float)(Math.PI/140);
						float radij = speed/angular;
						makeSpinner(x,(int)(y+radij),speed,0,angular);
						makeSpinner(x,(int)(y-radij),speed,(float)Math.PI,angular);
						
						makeSpinner((int)(x-radij),y,speed,(float)Math.PI/2,angular);
						makeSpinner((int)(x+radij),y,speed,(float)(Math.PI*1.5),angular);
					}
						
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	void makeSpinner(int x, int y, float speed, float direction, float angular){
		SpinFish f = new SpinFish(x,y,host,2,speed,direction,angular);
		host.addInstance(f);
	}
	
	void makeRock(int index, int x){ 
		Bitmap[] rocks=new Bitmap[3];
		rocks[0] = host.scale(BitmapFactory.decodeResource(host.getResources(), R.drawable.rock1),250);
		rocks[1] = host.scale(BitmapFactory.decodeResource(host.getResources(), R.drawable.rock_bv2),250);
		rocks[2] = host.scale(BitmapFactory.decodeResource(host.getResources(), R.drawable.rock_v2),180);
		int depth=0;
		Background bg = new Background(x,host.realHeight-host.wallDown-rocks[index-1].getHeight(),
				host,rocks[index-1],depth);
		host.addInstance(bg);
		host.obstacles.add(new Rect((int)bg.x,(int)bg.y,(int)bg.x+bg.w,(int)bg.y+bg.h));
	}
	
	void spawnFishWall(double probability){
		if (Math.random()<host.calibrateProb(probability)){
			int y= (int)(Math.random()*host.realHeight*0.5)+host.realHeight/6;
			float speed = -(2+(int)(Math.random()*2));
			for (int i=0; i<5; i++){
				makeFish(false,host.realWidth+50+i*30,y+i*60,speed);
			}
		}	
	}
	
	void spawnFallingUrchin(double probability){
		if (Math.random()<host.calibrateProb(probability)){
			FallingUrchin b = new FallingUrchin((int)(Math.random()*host.realWidth),-50,host,2);
			host.addInstance(b);
		}			
	}
	
	void spawnUrchin(int x, int y){
		host.addInstance(new Urchin(x,y,host,2));
	}
	
	void loadUrchin(){
		Bitmap urchin = BitmapFactory.decodeResource(host.getResources(), R.drawable.urchin1);
		host.loader.urchin = Bitmap.createScaledBitmap(urchin,70,70, true);
	}
	
	void spawnHeadbut(double probability){
		if (Math.random()<host.calibrateProb(probability)){
			int x =host.realWidth+50;
			boolean flipped=false;
			if (Math.random()<0.5){
					x=-50;
					flipped=true;
			}
			int y= (int)(Math.random()*host.realHeight*0.7)+host.realHeight/6;
			host.addInstance(new HeadbutFish(x,y,host,2,flipped));
		}
	}
	
	void spawnBlowFishFollower(double probability){
		//int x = (int)(Math.random()*host.realWidth);
		if (Math.random()<host.calibrateProb(probability)){
			int x = (int)(Math.random()*host.realWidth);
			int y = (int)(Math.random()*host.realHeight);
			while((x>-host.premikX && x<-host.premikX+host.width)||
					(y>-host.premikY && y<-host.premikY+host.height)){
				x = (int)(Math.random()*host.realWidth);
				y = (int)(Math.random()*host.realHeight);
			}
			makeBlowFishFollower(x,y);
		}
	}
	
	void makeBlowFishFollower(int x, int y){
		Bitmap fish1 = BitmapFactory.decodeResource(host.getResources(), R.drawable.blowfish);
		Bitmap fish2 = BitmapFactory.decodeResource(host.getResources(), R.drawable.blowfish2);
		Bitmap fish3 = BitmapFactory.decodeResource(host.getResources(), R.drawable.blowfish3);	
		float speed = 1;
		Bitmap[] animation = new Bitmap[3];
		animation[0] = host.scale(fish3,100);
		animation[1] = host.scale(fish2,100);
		animation[2] = host.scale(fish1,100);
		BlowfishFollower f = new BlowfishFollower(x,y,host, animation[0],2,host.calibrate(speed),animation);	
		host.addInstance(f);
	}
	
	void spawnBlowFish(){
		int x = (int)(Math.random()*host.realWidth);
		int y = (int)(Math.random()*host.realHeight*0.75)+(int)(host.realHeight*0.1);
		spawnBlowFish(x,y);
	}
	
	void spawnBlowFish(int x, int y){
		Bitmap fish1 = BitmapFactory.decodeResource(host.getResources(), R.drawable.blowfish);
		Bitmap fish2 = BitmapFactory.decodeResource(host.getResources(), R.drawable.blowfish2);
		Bitmap fish3 = BitmapFactory.decodeResource(host.getResources(), R.drawable.blowfish3);	
		float speed = 1;
		Bitmap[] animation = new Bitmap[3];
		animation[0] = host.scale(fish3,100);
		animation[1] = host.scale(fish2,100);
		animation[2] = host.scale(fish1,100);
		Blowfish f = new Blowfish(x,y,host, animation[0],2,host.calibrate(speed),animation);	
		host.addInstance(f);
	}
	
	void spawnFlock(double probability){
		if (Math.random()<host.calibrateProb(probability)){
			int y = (int)(Math.random()*host.realHeight*0.75);
			int speed =(int)(Math.random()*2);
			for (int i=0; i<3; i++){
				makeFishRed(true,-50-i*50,y-i*10,-(3+speed),i*(float)(-(Math.PI)/10));
			}
		}
	}
	
	void makeFish(boolean fliped, int x, int y, float speed){
		if (fliped)
			speed=-speed;
		Fish f = new Fish(x,y,host,2,host.calibrate(speed),fliped);
		host.addInstance(f);
	}
	
	void makeFish(boolean fliped){
		int x =host.realWidth+50;
		if (fliped){
			x=-50;
		}
		int y= (int)(Math.random()*host.realHeight*0.7)+host.realHeight/6;
		float speed = -1*(3+(int)(Math.random()*3));
		makeFish(fliped,x,y,speed);
	}
	
	void loadFish(){
		Bitmap fish1 = BitmapFactory.decodeResource(host.getResources(), R.drawable.fish_animation1);
		Bitmap fish2 = BitmapFactory.decodeResource(host.getResources(), R.drawable.fish_animation2);
		Bitmap fish3 = BitmapFactory.decodeResource(host.getResources(), R.drawable.fish_animation3);
		host.loader.fish =new Bitmap[4];
		host.loader.fish[0] = host.scale(fish1,50);
		host.loader.fish[1] = host.scale(fish2,50);
		host.loader.fish[2] = host.scale(fish3,50);
		host.loader.fish[3] = host.scale(fish2,50);
		fish1=flip(fish1);
		fish2=flip(fish2);
		fish3=flip(fish3);
		host.loader.fishF =new Bitmap[4];
		host.loader.fishF[0] = host.scale(fish1,50);
		host.loader.fishF[1] = host.scale(fish2,50);
		host.loader.fishF[2] = host.scale(fish3,50);
		host.loader.fishF[3] = host.scale(fish2,50);
	}
	
	void loadFishRed(){
		Bitmap fish1 = BitmapFactory.decodeResource(host.getResources(), R.drawable.fish_animation1);
		Bitmap fish2 = BitmapFactory.decodeResource(host.getResources(), R.drawable.fish_animation2);
		Bitmap fish3 = BitmapFactory.decodeResource(host.getResources(), R.drawable.fish_animation3);
		host.loader.fishSmall =new Bitmap[4];
		host.loader.fishSmall[0] = host.scale(fish1,35);
		host.loader.fishSmall[1] = host.scale(fish2,35);
		host.loader.fishSmall[2] = host.scale(fish3,35);
		host.loader.fishSmall[3] = host.scale(fish2,35);
		fish1=flip(fish1);
		fish2=flip(fish2);
		fish3=flip(fish3);
		host.loader.fishSmallF =new Bitmap[4];
		host.loader.fishSmallF[0] = host.scale(fish1,35);
		host.loader.fishSmallF[1] = host.scale(fish2,35);
		host.loader.fishSmallF[2] = host.scale(fish3,35);
		host.loader.fishSmallF[3] = host.scale(fish2,35);
	}
	
	void makeFishRed(boolean fliped, int x, int y, float speed, float delay){
		if(fliped)
			speed=-speed;
		SinusFish f = new SinusFish(x,y,host,2,host.calibrate(speed),delay,fliped);
		host.addInstance(f);
	}
	
	void makeFishRed(boolean fliped){
		int x =host.realWidth+50;
		if (fliped){
			x=-50;
		}
		int y= (int)(Math.random()*host.realHeight*0.5)+host.realHeight/6;
		float speed = -1*(4+(int)(Math.random()*4));
		makeFishRed(fliped,x,y,speed,0);
	}
	
	
	void spawnFood(double probability){
		if (Math.random()<host.calibrateProb(probability)){
			int x = (int)(Math.random()*host.realWidth);
			int y = (int)(Math.random()*host.realHeight);
			while((x>-host.premikX && x<-host.premikX+host.width)||
					(y>-host.premikY && y<-host.premikY+host.height)){
				x = (int)(Math.random()*host.realWidth);
				y = (int)(Math.random()*host.realHeight);
			}
			spawnFood(x,y);
		}
	}
	
	void spawnFood(int x,int y){
		
		Squid f = new Squid(x,y,host,2 , host.calibrate(1), (float) (Math.random()*2*Math.PI));
		host.addInstance(f);
	}
	
	void loadFood(){
		Bitmap blob = BitmapFactory.decodeResource(host.getResources(), R.drawable.food2);
		host.loader.food =  Bitmap.createScaledBitmap(blob,25,40, true);
	}
	
	/*void spawnLittle(double probability){
		if (Math.random()<host.calibrateProb(probability)){
			Bitmap fish = BitmapFactory.decodeResource(host.getResources(), R.drawable.fish);
			fish=host.scale(fish,35);
			if(Math.random()<0.50){
				RedFish f = new RedFish(-50,(int)(Math.random()*host.realHeight*0.75),host,
						flip(fish),2 , host.calibrate(4+(int)(Math.random()*4)));
				host.addInstance(f);
			}
			else{
				RedFish f = new RedFish(host.realWidth+50,(int)(Math.random()*host.realHeight*0.75),host,
						fish,2,host.calibrate(-1*(4+(int)(Math.random()*4))));
				host.addInstance(f);
			}
		}
	}
	*/
	void spawnFish(double probability){
		if (Math.random()<host.calibrateProb(probability)){
			if(Math.random()<0.50){
				makeFish(false);
			}
			else{
				makeFish(true);
			}
		}
	}
	
	void spawnFollower(double probability){
		if (Math.random()<host.calibrateProb(probability)){
			Bitmap fish = BitmapFactory.decodeResource(host.getResources(), R.drawable.fish);
			fish=host.scale(fish,65);
			if(Math.random()<0.50){
				SuperFish f = new SuperFish(-50,(int)(Math.random()*host.realHeight*0.75),host,
						flip(fish),2 , host.calibrate(2+(int)(Math.random()*3)));
				host.addInstance(f);
			}
			else{
				SuperFish f = new SuperFish(host.realWidth+50,(int)(Math.random()*host.realHeight*0.75),host,
						fish,2,host.calibrate(-1*(2+(int)(Math.random()*3))));

				host.addInstance(f);
			}
		}
	}
	
	void spawnBubbles(double probability){
		if (Math.random()<host.calibrateProb(probability)){
			Bubble b = new Bubble((int)(Math.random()*host.realWidth),host.realHeight+50,host,1 ,
					host.calibrate(-1*(9+(int)(Math.random()*4))),1);
			host.addInstance(b);
		}
		if (Math.random()<host.calibrateProb(probability)){	
			Bubble b = new Bubble((int)(Math.random()*host.realWidth),host.realHeight+50,host,3 ,
					host.calibrate(-1*(5+(int)(Math.random()*4))),2);
			host.addInstance(b);
		}
		if (Math.random()<host.calibrateProb(probability)){		
			Bubble b = new Bubble((int)(Math.random()*host.realWidth),host.realHeight+50,host,4 ,
					host.calibrate(-1*(1+(int)(Math.random()*4))),3);
			host.addInstance(b);
		}
	}
	
	void loadBubble(){
		Bitmap bubble1 = BitmapFactory.decodeResource(host.getResources(), R.drawable.bubble_l);
		host.loader.bubble1 = host.scale(bubble1,30);
		Bitmap bubble2 = BitmapFactory.decodeResource(host.getResources(), R.drawable.bubble_m);
		host.loader.bubble2 = host.scale(bubble2,20);
		Bitmap bubble3 = BitmapFactory.decodeResource(host.getResources(), R.drawable.bubble_s);
		host.loader.bubble3 = host.scale(bubble3,10);
	}
	
	protected Bitmap flip(Bitmap picture){
		Matrix m = new Matrix();
		int w=picture.getWidth();
		int h=picture.getHeight();
		m.setScale(-1,1);
		m.postTranslate(w,0);

		return(Bitmap.createBitmap(picture, 0, 0, w,h, m, true));
	}
	
}

package com.above_average;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

public class LevelMaker extends Instance{

	int time;
	 //za infinite level
	float points;
	ArrayList<Wave> horde;
	float increase;
	int nextWave;
	//dark level
	ArrayList<Firefly> light;
	int ffCount;
	boolean ffDone;
	//apocalypse
	WarStation war;
	Liner liner;
	ArrayList<Shield> shields;
	int plague;
	int battlefield;
	
	LevelMaker(Darkness host) {
		super(0,0,0,host);
		time=0;
		battlefield=0;
		enemy=false;
		horde = new ArrayList<Wave>();
		light = new ArrayList<Firefly>();
		shields=new ArrayList<Shield>();
		ffCount=0;
		ffDone=false;
		plague=0;
		host.spawnDot=0;
		host.spawnStalker=0;
		host.spawnWorm=0;
		host.spawnBomb=0;
		host.spawnChain=0;
		host.spawnLeaf=0;
		host.spawnPrison=0;
		host.spawnNet=0;
		host.spawnHealth=0;
		host.spawnHedgehog=0;
		host.spawnSniper=0;
		host.spawnSpikeChain=0;
		host.spawnFirefly=0;
		host.spawnCannon=0;
		Prison.prisonNumber=0;
		Prison.ringNumber=1;
		Net.netNumber=0;
		Net.maximum=10;
		Cannon.type=0;
		Slime.number=0;
		Slime.holdBack=0;
	}
	
	void step(){
		if (host.stage==1) stage0();
		if (host.stage==2) stage1();
		if (host.stage==3) stage2();
		if (host.stage==4) stage3();
		if (host.stage==5) stage5();
		if (host.stage==6) stage4();
		if (host.stage==7) stage6();
		if (host.stage==8) stage7();
		if (host.stage==9) stage8();
		if (host.stage==10) stage9();
		if (host.stage==11) stage11();
		if (host.stage==12) stage12();
		if (host.stage==15) stage10();
		if (host.stage==13) stage13();
		if (host.stage==14) stage14();
	}
	
	void end(){
		MainMenu.number=host.stage;
		MainMenu.demage=(float)(150-host.hero.demage)/150f;
		host.won=true;
	}
	
	void stage14(){
		if(time==0){
			host.makeGridOrange(false);
			host.texter.addQue("Good luck...", 15, 70);
		}
		if(time==20){
			host.instances.add(new HandGunPickUp(host,(int)(host.hero.radij*0.75),(int)(15*host.ratio),2));
			host.instances.add(new Boss2((int)(110*host.ratio),host));
		}
		
		time++;
	}
	
	void stage13(){
		if(time==0){
			//host.makeGridGray();
			host.instances.add(new Maze(host));
		}
		if(time==20){
			host.texter.addNow("Firefly is the key", 140);
		}
		for(int i=0; i<light.size(); i++){
			Firefly f = light.get(i);
			if(f.dead){
				light.remove(i);
				i--;
			}
		}
		time++;
	}
	
	void stage12(){
		if(time==0){
			host.makeGridGray();
			host.texter.addQue("This is not where you parked your car...",15,95);
		}
		if(time==20){
			host.texter.addNow("12 Fireflies = Weapon", 140);
			host.spawnFirefly=0.01f;
			host.spawnGhostBomb=0.008f;
		}
		if(ffCount==1 && !ffDone){
			host.instances.add(new Spider((int)(26*host.ratio),host,false));
			ffDone=true;
		}
		if(ffCount==3 && !ffDone){
			host.instances.add(new Jack((int)(26*host.ratio),(int)(150*host.ratio),host));
			ffDone=true;
		}
		if(ffCount==5 && !ffDone){
			host.instances.add(new Spider((int)(15*host.ratio),host,true));
			host.instances.add(new Spider((int)(15*host.ratio),host,true));
			ffDone=true;
		}
		if(ffCount==7 && !ffDone){
			host.instances.add(new Jack((int)(26*host.ratio),(int)(150*host.ratio),host));
			ffDone=true;
		}
		if(ffCount==8 && !ffDone){
			host.instances.add(new Spider((int)(26*host.ratio),host,false));
			ffDone=true;
		}
		if(ffCount==10 && !ffDone){
			host.instances.add(new Jack((int)(26*host.ratio),(int)(150*host.ratio),host));
			//host.instances.add(new Spider((int)(15*host.ratio),host,true));
			ffDone=true;
		}
		if(ffCount==12 && !ffDone){
			if(host.hero.weapon!=null) host.hero.weapon.dead=true;
			HandGun gun = new HandGun(host,(int)(15*host.ratio),1,0);
			host.hero.weapon=gun;
			host.instances.add(gun);
			ffDone=true;
		}
		if(ffCount==14 && !ffDone){
			host.texter.addNow("21 Fireflies = Salvation", 140);
			host.spawnBat=0.01f;
			ffDone=true;
		}
		if(ffCount==16 && !ffDone){
			host.spawnBat=0.027f;
			host.spawnSpider=0.009f;
		}
		if(ffCount==21 && !ffDone){
			ffDone=true;
			host.spawnFirefly=0;
			host.spawnBat=0;
			host.spawnGhostBomb=0;
			end();
			for(int i=0; i<5; i++){				
				Firefly f=new Firefly(host,(int)(12*host.ratio));
				f.x=host.hero.x;
				f.y=host.hero.y;
				f.pacman=true;
				f.direction=(int)(Math.random()*360);
				host.instances.add(f);
				light.add(f);
			}
		}
		for(int i=0; i<light.size(); i++){
			Firefly f = light.get(i);
			if(f.dead){
				light.remove(i);
				i--;
			}
		}
		time++;
	}
	
	void stage0(){
		if(time==0){
			host.makeGrid();
		}
		if(time==40){
			host.texter.addNow("Try moving around by tilting your phone", 150);
		}
		if(time==200){
			host.texter.addNow("You can pass thrue the edge of the screen", 150);
		}
		if(time==350){
			host.texter.addNow("The goal is to avoid everything that is colored white", 100);
			for(int i=0; i<4; i++) host.instances.add(new Dot((int)(7*host.ratio),host));
		}
		if(time==600){
			host.instances.add(new Dot((int)(7*host.ratio),host));
			host.instances.add(new Dot((int)(7*host.ratio),host));
			host.texter.addNow("On contact your colors will become paler", 120);
		}
		if(time==720){
			host.instances.add(new Dot((int)(7*host.ratio),host));
			host.instances.add(new Dot((int)(7*host.ratio),host));
			host.texter.addNow("When you become white, you lose!", 120);
		}
		if(time==840){
			host.texter.addNow("Try to avoid this one", 100);
			host.instances.add(new Stalker(host,host.width,host.height,(int)(15*host.ratio)));
		}
		if(time==1180){
			host.texter.addNow("Survive a level to the end to unlock new one", 100);
		}
		if(time==1320){
			end();
		}
		time++;
	}
	
	void stage11(){
		if(time==0){
			host.makeGridOrange(true);
			host.texter.addQue("They tought you hate...",15,95);
			host.texter.addQue("Teach them fear!", 65,90);
		}
		if(time==40){
			host.instances.add(new HandGunPickUp(host,(int)(host.hero.radij*0.75),(int)(15*host.ratio),0));
		}
		if(time==210){
			host.spawnStalker=0.01f;
		}
		if(time==380){
			host.spawnStalker=0.015f;
		}
		if(time==610){
			host.spawnStalker=0.025f;
			host.spawnHedgehog=0.025f;
		}
		if(time==1500){
			host.instances.add(new HandGunPickUp(host,(int)(host.hero.radij*0.75),(int)(15*host.ratio),1));
			host.spawnHealth=0.002f;
			host.spawnHedgehog=0;
			host.spawnStalker=0.02f;
			host.spawnDot=0.05f;
		}
		if(time==2500){
			host.instances.add(new HandGunPickUp(host,(int)(host.hero.radij*0.75),(int)(15*host.ratio),2));
		}
		if(time==2650){
			host.spawnHedgehog=0.02f;
			host.spawnStalker=0.02f;
		}
		
		if(time==3000){
			host.spawnHedgehog=0f;
			host.spawnStalker=0f;
			host.spawnSniper=0.01f;
		}
		if(time==3700){
			host.spawnSniper=0.006f;
			host.spawnDot=0.02f;
			host.spawnCannon=0.01f;
		}
		if(time==4950){
			host.spawnSniper=0;
			host.spawnDot=0;
			host.spawnCannon=0;
			host.spawnHealth=0;
		}
		if(time==5090){
			end();
			host.texter.addQue("You are the predator...",15,95);
			host.texter.addQue("and they are the pray", 65,90);
		}
		time++;
	}
	
	void stage9(){
		if(time==0){
			host.makeGridRed();
		}
		if(time==20){
			host.texter.addNow("First rider: PLAGUE", 80);
		}
		if(time==60){
			host.instances.add(new Slime((int)(20*host.ratio),host));
			Slime.holdBack-=2;
		}
		if(time==90){
			Slime.holdBack+=2;
		}
		if(time==160 && plague==0){
			Shield s=new Shield(host,(int)(190*host.ratio));
			host.instances.add(s);
			shields.add(s);
		}
		if(time==300 && plague==0){
			Shield s=new Shield(host,(int)(190*host.ratio));
			host.instances.add(s);
			shields.add(s);
		}
		if(time==440 && plague==0){
			Shield s=new Shield(host,(int)(190*host.ratio));
			host.instances.add(s);
			shields.add(s);
		}
		if(time==640 && plague==0){
			Shield s=new Shield(host,(int)(190*host.ratio));
			host.instances.add(s);
			shields.add(s);
		}
		if(time==840 && plague==0){
			Shield s=new Shield(host,(int)(190*host.ratio));
			host.instances.add(s);
			shields.add(s);
		}
		if(time==1040 && plague==0){
			Shield s=new Shield(host,(int)(190*host.ratio));
			host.instances.add(s);
			shields.add(s);
		}
		if(time==1240 && plague==0){
			Shield s=new Shield(host,(int)(190*host.ratio));
			host.instances.add(s);
			shields.add(s);
		}
		if(time==1540 && plague==0){
			Shield s=new Shield(host,(int)(190*host.ratio));
			host.instances.add(s);
			shields.add(s);
		}
		if(time==2000 && plague==0){
			Slime.holdBack+=15;
			for(int i=0; i<5; i++){
				Shield s=new Shield(host,(int)(190*host.ratio));
				host.instances.add(s);
				shields.add(s);
			}
		}
		if(Slime.number<=0 && time>300 &&  plague==0){
			plague=time;
		}
		if(plague!=0 && battlefield==0){
			if(time==plague+10){
				for(int i=0; i<shields.size(); i++){
					shields.get(i).timeToDie=true;
				}
			}
			if(time==plague+60){
				host.texter.addNow("Second rider: WAR", 80);
				host.instances.add(new Health((int)(25*host.ratio),host));
				host.instances.add(new Health((int)(25*host.ratio),host));
			}
			if(time==plague+150){
				war=new WarStation(host); 
			}
			if(war!=null && (war.teamCircle==0 || war.teamSquare==0)){
				battlefield=time;
				for(int i=0; i<war.stations.size(); i++){
					war.stations.get(i).dead=true;
				}
			}
		}
		if(battlefield!=0 && host.won==false){
			if(time==battlefield+40){
				host.texter.addNow("Third rider: FAMINE", 80);
				host.instances.add(new Health((int)(25*host.ratio),host));
				host.instances.add(new Health((int)(25*host.ratio),host));
			}
			if(time==battlefield+60){
				liner = new Liner(host);
				host.instances.add(liner);
				host.spawnHealth=0.005f;
			}
			if(time>battlefield+80 && time%85==0 && host.hero.demage<142){
				host.texter.addNow("hungry...", 30);
				host.hero.demage+=6;
				host.instances.add(new AntiHealth(host));
				host.instances.add(new AntiHealth(host));
				host.instances.add(new AntiHealth(host));
				host.instances.add(new AntiHealth(host));
				host.instances.add(new AntiHealth(host));
			}
			if(liner!=null && liner.dead){
				end();
				host.spawnHealth=0f;
			}
		}
		time++;
	}
	
	void stage3(){
		if(time==0){
			host.makeGrid();
			host.texter.addQue("Remember: ", 25, 80);
			host.texter.addQue("Pain is temporary...", 70, 80);
			host.texter.addQue("Glory is eternal!", 70, 90);
		}
		if(time==20)
			host.spawnNet=0.007f;
		if(time==500)
			host.spawnStalker=0.015f;
		if(time==1450){
			host.spawnNet=0;
			host.spawnStalker=0;
		}
		if(time==1600){
			host.spawnPrison=1f;
			host.spawnDot=0.02f;
		}
		if(time==1800){
			host.spawnDot=0.03f;
		}
		if(time==2300){
			host.spawnDot=0.06f;
		}
		if(time==2700){
			host.spawnPrison=0;
		}
		if(time==3100){
			host.spawnDot=0;
		}
		if(time==3200){
			host.texter.addQue("Reach the gate of Heaven...", 50,70);
			host.texter.addQue("kick it open!", 60,70);
			end();
		}
		
		time++;
	}
	
	void stage7(){
		if(time==0){
			host.makeGridGreen();
			host.texter.addQue("The more you sweat in practice,", 25, 80);
			host.texter.addQue("the less you bleed in battle", 70, 80);
		}
		if(time==20){
			host.texter.addNow("Use blocks to your best advantage!", 140);
		}
		if(time==100)
			host.spawnHedgehog=0.01f;
		if(time==210){
			host.spawnSniper=0.01f;
		}
		if(time==700){
			host.spawnStalker=0.017f;
			host.spawnHealth=0.001f;
		}
		if(time==800){
			host.spawnHedgehog=0;
		}
		if(time==1200){
			host.spawnSniper=0;
		}
		if(time==1300){
			host.instances.add(new Health((int)(25*host.ratio),host));
			host.spawnStalker=0.01f;
			host.spawnCannon=0.007f;
			Cannon.type=0;
		}
		if(time==1600){
			host.spawnSniper=0.01f;
		}
		if(time==2100){
			host.spawnSniper=0;
		}
		if(time==2700){
			host.spawnCannon=0.009f;
			host.instances.add(new Health((int)(25*host.ratio),host));
			Cannon.type=1;
		}
		if(time==3200){
			host.spawnStalker=0.01f;
		}
		if(time==3500){
			host.spawnSniper=0.006f;
		}
		if(time>3800 && time<4700){
			if(Cannon.type==0)Cannon.type=1;
			else Cannon.type=0;
		}
		if(time==4200){
			host.spawnStalker=0;
			host.instances.add(new Health((int)(25*host.ratio),host));
		}
		if(time==5000){
			host.spawnSniper=0;
			host.spawnHedgehog=0;
			host.spawnCannon=0;
		}
		if(time==5080){
			end();
			host.texter.addQue("What doesn't kill you...", 15,90);
			host.texter.addQue("Dies horribly!", 45,90);
		}
		time++;
	}
	
	void stage5(){
		if(time==0){
			host.makeGrid();
			host.texter.addQue("Feel free to experiment with non-white things", 15, 90);
			host.texter.addQue("they won't hurt you", 90, 70);
		}
		if(time==20){
			host.instances.add(new Boss((int)(90*host.ratio),host));
		}
		
		time++;
	}
	
	void stage8(){
		if(time==0){
			host.makeGridGray();
			host.texter.addQue("Fear is a reflex...", 15, 85);
			host.texter.addQue("Confidence is a choice!", 55, 100);
		}
		if(time==20){
			host.texter.addNow("GOAL: Collect 25 fireflies", 140);
			for(int i=0; i<3; i++) host.instances.add(new Bat(20,host,false));
			//for(int i=0; i<5; i++) host.instances.add(new Spider(15,host,true));
			host.spawnFirefly=0.01f;
		}
		if(ffCount==1 && !ffDone){
			host.instances.add(new Spider((int)(15*host.ratio),host,true));
			ffDone=true;
		}
		if(ffCount==3 && !ffDone){
			host.instances.add(new Bat((int)(20*host.ratio),host,false));
			ffDone=true;
		}
		if(ffCount==5 && !ffDone){
			host.instances.add(new Bat((int)(20*host.ratio),host,false));
			ffDone=true;
		}
		if(ffCount==7 && !ffDone){
			host.instances.add(new Spider((int)(26*host.ratio),host,false));
			ffDone=true;
		}
		if(ffCount==9 && !ffDone){
			host.instances.add(new Spider((int)(26*host.ratio),host,false));
			ffDone=true;
		}
		if(ffCount==13 && !ffDone){
			host.instances.add(new Spider((int)(15*host.ratio),host,true));
			ffDone=true;
		}
		if(ffCount==17 && !ffDone){
			host.instances.add(new Spider((int)(26*host.ratio),host,false));
			ffDone=true;
		}
		if(ffCount==20 && !ffDone){
			host.instances.add(new Bat((int)(20*host.ratio),host,false));
			ffDone=true;
		}
		if(ffCount==23 && !ffDone){
			host.instances.add(new Spider((int)(26*host.ratio),host,false));
			ffDone=true;
		}
		if(ffCount==25 && !ffDone){
			ffDone=true;
			host.spawnFirefly=0;
			end();
			for(int i=0; i<5; i++){				
				Firefly f=new Firefly(host,(int)(12*host.ratio));
				f.x=host.hero.x;
				f.y=host.hero.y;
				f.pacman=true;
				f.direction=(int)(Math.random()*360);
				host.instances.add(f);
				light.add(f);
			}
		}
		for(int i=0; i<light.size(); i++){
			Firefly f = light.get(i);
			if(f.dead){
				light.remove(i);
				i--;
			}
		}
		time++;
	}
	
	void stage6(){
		if(time==0){
			host.makeGridGray();
			host.texter.addQue("Don't say, you will go without a light...", 15, 85);
			host.texter.addQue("if you have never seen the night...", 70, 100);
		}
		if(time==20){
			host.texter.addNow("GOAL: Collect 15 fireflies", 140);
			for(int i=0; i<2; i++) host.instances.add(new Spider(26,host,false));
			//for(int i=0; i<5; i++) host.instances.add(new Spider(15,host,true));
			host.spawnFirefly=0.01f;
		}
		if(time==50){
			for(int i=0; i<1; i++){				
				Firefly f=new Firefly(host,(int)(12*host.ratio));
				host.instances.add(f);
				light.add(f);
			}
		}
		if(ffCount==1 && !ffDone){
			host.instances.add(new Spider((int)(15*host.ratio),host,true));
			ffDone=true;
		}
		if(ffCount==3 && !ffDone){
			host.instances.add(new Spider((int)(15*host.ratio),host,true));
			ffDone=true;
		}
		if(ffCount==5 && !ffDone){
			 host.instances.add(new Spider((int)(26*host.ratio),host,false));
			ffDone=true;
		}
		if(ffCount==6 && !ffDone){
			host.instances.add(new Spider((int)(15*host.ratio),host,true));
			ffDone=true;
		}
		if(ffCount==7 && !ffDone){
			host.instances.add(new Spider((int)(15*host.ratio),host,true));
			ffDone=true;
		}
		if(ffCount==9 && !ffDone){
			host.instances.add(new Spider((int)(15*host.ratio),host,true));
			ffDone=true;
		}
		if(ffCount==12 && !ffDone){
			host.instances.add(new Spider((int)(26*host.ratio),host,false));
			ffDone=true;
		}
		if(ffCount==15 && !ffDone){
			ffDone=true;
			host.spawnFirefly=0;
			end();
			for(int i=0; i<5; i++){				
				Firefly f=new Firefly(host,(int)(12*host.ratio));
				f.x=host.hero.x;
				f.y=host.hero.y;
				f.pacman=true;
				f.direction=(int)(Math.random()*360);
				host.instances.add(f);
				light.add(f);
			}
		}
		for(int i=0; i<light.size(); i++){
			Firefly f = light.get(i);
			if(f.dead){
				light.remove(i);
				i--;
			}
		}
		time++;
	}
	
	void stage1(){
		if(time==0){
			host.makeGrid();
			host.texter.addQue("Long time ago...", 15, 70);
			host.texter.addQue("Beyond hills and far away...", 70, 70);
			host.texter.addQue("There was a dot of color", 70, 100);
			host.texter.addQue("in a world of black and white", 70, 70);
			host.texter.addQue("Hated",70,50);
			host.texter.addQue("HUNTED", 50, 50);
		}
		if(time==20){
			host.texter.addNow("GENERAL ADVICE: Stay away from White!", 140);
		}
		if(time>60 && time<640)
			host.spawnDot+=0.00015;
		if(time==700){
			host.spawnDot=(float)0.03;
			host.spawnStalker=(float)0.02;
		}
		if(time==900){
			host.spawnDot=(float)0.01;
		}
		if(time>900 && time<1100){
			host.spawnDot+=0.0001;
		}
		if(time==1100){
			host.spawnStalker=0;
		}
		if(time==1200){
			host.spawnDot=0;
			host.spawnLeaf=0.08f;
		}
		
		if(time==1700){
			host.spawnLeaf=0;
		}
		if(time==1800){
			host.texter.addQue("The tallest man...", 50,70);
			host.texter.addQue("is last man standing!", 60,70);
			end();
		}
		time++;
	}
	
	void stage2(){
		if(time==0){
			host.makeGridRed();
		}

		if(time==20){
			host.spawnDot=(float)0.03;
			host.spawnStalker=(float)0.01;
			host.spawnWorm=(float)0.01;
		}
		if(time==200){
			host.spawnWorm=0.02f;
			host.spawnStalker=0f;
		}
		if(time==400){
			host.spawnDot=(float)0.02;
			host.spawnBomb=(float)0.02;
			host.spawnWorm=0.01f;
			host.spawnStalker=(float)0;
		}
		if(time==1100){
			host.spawnWorm=0f;
			host.spawnBomb=0.01f;
			host.spawnChain=0.01f;
		}
		if(time>1400 && time<1600){
			host.spawnWorm+=0.00005;
		}
		if(time==1900){
			host.spawnDot=0;
			host.spawnStalker=0;
			host.spawnWorm=0;
			host.spawnBomb=0;
			host.spawnChain=0;
		}
		if(time==2000){
			host.texter.addQue("why sleep...", 50,70);
			host.texter.addQue("when you can dream!", 60,70);
			end();
		}
		time++;
	}
	
	void stage4(){
		if(time==0){
			host.makeGridGreen();
			host.instances.add(new Health((int)(25*host.ratio),host));
			host.instances.add(new Health((int)(25*host.ratio),host));
			host.instances.add(new Health((int)(25*host.ratio),host));
			host.hero.demage = 130;
		}
		if(time==20){
			host.texter.addNow("Sometimes help comes least expected!", 140);
		}
		if(time==190){
			host.spawnHedgehog=0.02f;
		}
		if(time>190 && time<740)
			host.spawnHedgehog+=0.0001;
			host.spawnHealth=0.001f;
		if(time==800){
			host.spawnHedgehog=0.01f;
			host.spawnSniper =0.01f;
		}
		if(time==1200){
			host.instances.add(new Health((int)(25*host.ratio),host));
			host.spawnHedgehog=0;
			host.spawnSniper =0.02f;
		}
		if(time==2000){
			host.spawnSniper=0.003f;
			host.spawnSpikeChain=0.02f;
		}
		if(time==2150){
			host.instances.add(new Health((int)(25*host.ratio),host));
			host.spawnHealth=0.003f;
			host.spawnSpikeChain=0.005f;
			host.spawnHedgehog = 0.01f;
		}
		if(time==2900){
			host.spawnHedgehog = 0.015f;
			host.spawnSpikeChain=0.002f;
		}
		if(time==3500){
			host.spawnSniper=0;
			host.spawnHedgehog=0;
			host.spawnSpikeChain=0;
		}
		if(time==3650){
			end();
			host.texter.addQue("Piss lightning", 15,70);
			host.texter.addQue("shit success!", 45,70);
		}
		time++;
	}
	
	void stage10(){
		if(time==0){
			host.makeGrid();
			host.texter.addQue("Once more unto the breach,", 15, 70);
			host.texter.addQue("dear friends, once more...", 45, 70);
			points = 1;
			increase = 0.012f;
			nextWave = 5;
		}
		if(time==20){
			host.texter.addNow("Stay sharp!", 140);
			host.spawnHealth = 0.001f;
		}
		if(time>20){
			for(int i=0; i<horde.size(); i++){
				Wave w = horde.get(i);
				if(!w.step()){
					horde.remove(i);
					i--;
				}
			}
			increase+=0.00001;
			points+=increase;
			nextWave--;
			if(time%(15*33)==0) {
				host.texter.addNow(time/33+" seconds done!",50);
			}
			if(nextWave == 0){
				nextWave=15*(int)(Math.random()*4+1);
				int duration =30*(int)(Math.random()*10+8);
				if(points>=12 && host.spawnPrison==0)  {horde.add(new Wave(Wave.PRISON,duration,host)); points-=12;}
				else if (points>=9) {horde.add(new Wave(Wave.CHAINLINK,duration,host)); points-=9;}
				else if (points>=7.5) {horde.add(new Wave(Wave.NET,duration,host)); points-=7.5;}
				else if (points>=6.5){horde.add(new Wave(Wave.CANNON,0.004f,duration,host)); points-=6.5;}
				else if (points>=5.5 && host.spawnChain<0.01){ horde.add(new Wave(Wave.CHAIN,duration,host)); points-=5.5;}
				else if (points>=5) {horde.add(new Wave(Wave.WORM,duration,host)); points-=5;}
				else if (points>=4.5) {horde.add(new Wave(Wave.SNIPER,duration,host)); points-=4.5;}
				else if (points>=4) {horde.add(new Wave(Wave.STALKER,duration,host)); points-=4;}
				else if (points>=3.5) {horde.add(new Wave(Wave.HEDGEHOG,duration,host)); points-=3.5;}
				else if (points>=3) {horde.add(new Wave(Wave.BOMB,duration,host)); points-=3;}
				else if (points>=1) {horde.add(new Wave(Wave.DOT,0.012f,duration,host)); points-=1;}
				else {horde.add(new Wave(Wave.LEAF,0.02f,duration,host));}
			}
		}
		time++;
	}
	
	void blueToRed(){
		for(int i=0; i<host.grid.length; i++){
			for(int j=0; j<host.grid[i].length; j++){
				host.instances.remove(host.grid[i][j]);
			}
		}
		host.makeGridRed();
	}

}

class Wave{
	int type;
	int remaining;
	float value;
	Darkness host;
	static final int DOT = 0;
	static final int STALKER = 1;
	static final int LEAF = 2;
	static final int WORM = 3;
	static final int BOMB = 4;
	static final int CHAIN = 5;
	static final int NET = 6;
	static final int PRISON = 7;
	static final int SNIPER = 8;
	static final int HEDGEHOG = 9;
	static final int CHAINLINK = 10;
	static final int CANNON = 11;
	
	Wave (int type, int remaining, Darkness host){
		this(type,0.008f,remaining,host);
	}
	
	Wave (int type, float value, int remaining, Darkness host){
		if(type==CHAIN && type==CHAINLINK) value/=4;
		this.type = type;
		this.remaining = remaining;
		this.value=value;
		this.host=host;
		if(type==DOT) host.spawnDot+=value;
		if(type==STALKER) host.spawnStalker+=value;
		if(type==LEAF) host.spawnLeaf+=value;
		if(type==WORM) host.spawnWorm+=value;
		if(type==BOMB) host.spawnBomb+=value;
		if(type==HEDGEHOG) host.spawnHedgehog+=value;
		if(type==PRISON) host.spawnPrison+=value;
		if(type==SNIPER) host.spawnSniper+=value;
		if(type==CHAIN) host.spawnChain+=value;
		if(type==CHAINLINK) host.spawnSpikeChain+=value;
		if(type==NET) host.spawnNet+=value;
		if(type==CANNON) host.spawnCannon+=value;
	}
	
	boolean step(){
		remaining--;
		if (remaining == 0){
			if(type==DOT) host.spawnDot-=value;
			if(type==STALKER) host.spawnStalker-=value;
			if(type==LEAF) host.spawnLeaf-=value;
			if(type==WORM) host.spawnWorm=-value;
			if(type==BOMB) host.spawnBomb=-value;
			if(type==HEDGEHOG) host.spawnHedgehog=-value;
			if(type==PRISON) host.spawnPrison=-value;
			if(type==SNIPER) host.spawnSniper=-value;
			if(type==CHAIN) host.spawnChain=value;
			if(type==CHAINLINK) host.spawnSpikeChain=-value;
			if(type==NET) host.spawnNet=-value;
			if(type==CANNON) host.spawnCannon-=value;
			return false;
		}
		return true;
	}
}

class AntiHealth extends Instance{
	
	int color;
	
	AntiHealth(Darkness host){
		super(host.hero.x,host.hero.y,host.hero.radij/3,host);
		color=host.hero.color;
		direction=(int)(Math.random()*360);
		left=38;
		speed=5;
		enemy=false;
	}
	
	protected void step(){
		left--;
		if(left==0) dead=true;
		x+=Math.cos(Math.toRadians(direction))*speed;
		y+=Math.sin(Math.toRadians(direction))*speed;
	}
	
	protected void draw(Canvas c, Paint p){
		p.setColor(color);
		p.setStyle(Style.FILL);
		RectF r = new RectF(x-radij/2,y-radij/2,x+radij/2,y+radij/2);
		c.drawOval(r,p);
	}
}

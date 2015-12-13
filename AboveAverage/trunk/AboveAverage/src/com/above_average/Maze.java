package com.above_average;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Maze extends Instance{
	
	boolean [][] navpicno;
	boolean [][] vodoravno;
	boolean won;
	float number;
	int size;
	int hopsMax;
	int prizeX;
	int prizeY;
	int delayX;
	int delayY;
	LinkedList<Integer[]> outpost;
	
	Maze(Darkness host){
		super(0,0,0,host);
		onlyAlt=true;
		//enemy=false;
		hopsMax=6;
		number=0;
		demageDealt=8;
		size=(int)(host.hero.radij*2);
		generate();
		setBounds();
		makePrize();
		won=false;
	}
	
	protected void step(){
		if(won){
			if(number<6) {
				size=(int)(host.hero.radij*(2-0.1*number));
				generate();
				setBounds();
				makePrize();
				number+=0.5;
				won=false;
			}
			else host.maker.end();
		}
	}

	boolean altCollision(int xHero, int yHero, int radij){
		boolean collide = false;
		for(int i=0; i<navpicno.length; i++){
			for(int j=0; j<navpicno[0].length; j++){
				if(navpicno[i][j]){
					float x1= i*size +delayX;
					float y1= j*size+3 +delayY;
					float y2= (j+1)*size+3 +delayY;
					if (localCollision(xHero,yHero,radij,x1,y1,x1+1,y2)) collide = true;
				}
			}
		}
		for(int i=0; i<vodoravno.length; i++){
			for(int j=0; j<vodoravno[0].length; j++){
				if(vodoravno[i][j]){
					float x1= i*size +delayX;
					float x2= (i+1)*size +delayX;
					float y1= j*size+3 +delayY;
					if (localCollision(xHero,yHero,radij,x1,y1,x2,y1)) collide = true;
				}
			}
		}
		return collide;
	}
	
	void setBounds(){
		delayX=(int)(host.hero.x % size) - size/2;
		delayY=(int)(host.hero.y % size) - size/2;
		if(delayX<0){host.hero.x-=delayX; delayX=0;}
		if(delayY<0){host.hero.y-=delayY; delayY=0;}
		if(host.hero.x>(navpicno.length-1)*size+delayX)  host.hero.x-=size;
		if(host.hero.y>(vodoravno[0].length-1)*size+delayY) host.hero.y-=size;

	}
	
	void makePrize(){
		Integer[] stats = outpost.get((int)(Math.random()*outpost.size()));
		prizeX=(int)((stats[0]+0.5)*size)+delayX;
		prizeY=(int)((stats[1]+0.5)*size)+delayY;
		Firefly f=new Firefly(host,(int)(12*host.ratio));
		host.instances.add(f);
		host.maker.light.add(f);
		f.x=(float)(prizeX+size*0.3);
		f.y=prizeY;
		f.master=this;
	}
	
	void generate(){
		int numberX=host.width/size;
		int numberY=host.height/size;
		navpicno = new boolean[numberX+1][numberY];
		vodoravno= new boolean[numberX][numberY+1];
		int startX=numberX/2;
		int startY=numberY/2;
		outpost=new LinkedList<Integer[]>();
		for(int i=0; i<navpicno.length; i++){
			for(int j=0; j<navpicno[0].length; j++){
				if(i==0 || i==navpicno.length-1) navpicno[i][j]=true;
			}
		}
		for(int i=0; i<vodoravno.length; i++){
			for(int j=0; j<vodoravno[0].length; j++){
				if(j==0 || j==vodoravno[0].length-1) vodoravno[i][j]=true;
			}
		}
		makeOutpost(startX,startY,-1,0);
		for(int i=0; i<numberX; i++){
			for(int j=0; j<numberY; j++){
				int alone=0;
				if(navpicno[i][j])alone++;
				if(vodoravno[i][j])alone++;
				if(navpicno[i+1][j])alone++;
				if(vodoravno[i][j+1])alone++;
				if(alone<2){
					makeOutpost(i,j,-1,0);
				}
			}
		}
		while(true){
			boolean connected[][] = new boolean[numberX][numberY];
			connected[0][0]=true; 
			LinkedList<Integer[]> borderline = new LinkedList<Integer[]>();
			connect(connected,0,0);
			check(connected,borderline,numberX,numberY);
			if(borderline.size()==0) break;
			else{
				Integer[] stats = borderline.get((int)(Math.random()*borderline.size()));
				if(stats[2]==0) navpicno[stats[0]][stats[1]]=false;
				if(stats[2]==1) vodoravno[stats[0]][stats[1]]=false;	
				if(stats[2]==2) navpicno[stats[0]+1][stats[1]]=false;
				if(stats[2]==3) vodoravno[stats[0]][stats[1]+1]=false;
			}
			
//			boolean pass=true;
//			for(int i=0; i<numberX && pass; i++){
//				for(int j=0; j<numberY && pass; j++){
//					if(!connected[i][j]){
//						pass=false;
//						if(j!=0){
//							vodoravno[i][j]=false;
//							}
//						else{
//							navpicno[i][j]=false;}
//					}
//				}
//			}
//			if(pass) break;
			
//			int count=0;
//			for(int i=0; i<numberX; i++){
//				for(int j=0; j<numberY; j++){
//					if(!connected[i][j]) count++;
//				}
//			}
//			pass=false;
		}
	}
	
	void check(boolean[][] connected, LinkedList<Integer[]> borderline, int numberX, int numberY){
		for(int i=0; i<numberX; i++){
			for(int j=0; j<numberY; j++){
				if(connected[i][j]){
					if(i!=0 && !connected[i-1][j]){
						Integer[] o= {i-1,j,2}; 
						borderline.add(o);
					}
					if(i+2!=navpicno.length && !connected[i+1][j]){
						Integer[] o= {i+1,j,0}; 
						borderline.add(o);
					}
					if(j!=0 && !connected[i][j-1]){
						Integer[] o= {i,j-1,3}; 
						borderline.add(o);
					}
					if(j+2!=vodoravno[0].length && !connected[i][j+1]){
						Integer[] o= {i,j+1,1}; 
						borderline.add(o);
					}
				}
			}
		}
	}
	
	void connect(boolean[][] connected,int startX, int startY){
		if(startX!=0 && !connected[startX-1][startY]){
			if(!navpicno[startX][startY]){
				connected[startX-1][startY]=true;
				connect(connected, startX-1,startY);
			}
		}
		if(startX+2!=navpicno.length && !connected[startX+1][startY]){
			if(!navpicno[startX+1][startY] ){
				connected[startX+1][startY]=true;
				connect(connected,startX+1,startY);
			}
		}
		if(startY!=0 && !connected[startX][startY-1]){
			if(!vodoravno[startX][startY]){
				connected[startX][startY-1]=true;
				connect(connected,startX,startY-1);
			}
		}
		if(startY+2!=vodoravno[0].length && !connected[startX][startY+1]){
			if(!vodoravno[startX][startY+1]){
				connected[startX][startY+1]=true;
				connect(connected,startX,startY+1);
			}
		}
	}
	
	void makeOutpost(int startX, int startY, int direction, int hops){
		hops++;
		LinkedList<Integer> ways = new LinkedList<Integer>(); 
		if(!navpicno[startX][startY] && direction!=2) 
			ways.add(0);
		if(!vodoravno[startX][startY] && direction!=3) 
			ways.add(1);
		if(!navpicno[startX+1][startY] && direction!=0) 
			ways.add(2);
		if(!vodoravno[startX][startY+1] && direction!=1) 
			ways.add(3);
		if(ways.size()==0){
			Integer[] o = {startX,startY};
			outpost.add(o);
		}
		else if(hops>=hopsMax){
			for(int i=0; i<4; i++){
				if(direction==-1 || i!=(direction+2)%4){
					if(i==0) navpicno[startX][startY]=true;
					if(i==1) vodoravno[startX][startY]=true;
					if(i==2) navpicno[startX+1][startY]=true;
					if(i==3) vodoravno[startX][startY+1]=true;
				}
			}
			Integer[] o = {startX,startY};
			outpost.add(o);
		}
		else{
			if((Math.random() <0.8|| hops==1) && ways.size()>=2){
				Integer a = ways.get((int)(Math.random()*ways.size()));
				ways.remove(a);
				int b =  ways.get((int)(Math.random()*ways.size()));
				for(int i=0; i<4; i++){
					if(i!=a && i!=b && (direction==-1 || i!=(direction+2)%4)){
						if(i==0) navpicno[startX][startY]=true;
						if(i==1) vodoravno[startX][startY]=true;
						if(i==2) navpicno[startX+1][startY]=true;
						if(i==3) vodoravno[startX][startY+1]=true;
					}
				}
				makeCorridor(startX,startY,a, hops);
				makeCorridor(startX,startY,b, hops);
			}
			else{
				Integer a = ways.get((int)(Math.random()*ways.size()));
				for(int i=0; i<4; i++){
					if(i!=a && (direction==-1 || i!=(direction+2)%4)){
						if(i==0) navpicno[startX][startY]=true;
						if(i==1) vodoravno[startX][startY]=true;
						if(i==2 && startX+1<navpicno.length) navpicno[startX+1][startY]=true;
						if(i==3 && startY+1<vodoravno[0].length) vodoravno[startX][startY+1]=true;
					}
				}
				makeCorridor(startX,startY,a, hops);
			}
		}
	}
	
	void makeCorridor(int startX, int startY, int direction, int hops){
		int predznakX=0, predznakY=0;
		if(direction==0){ predznakX=-1; predznakY=0;}
		if(direction==1){ predznakY=-1; predznakX=0;}
		if(direction==2){ predznakX=1; predznakY=0;}
		if(direction==3){ predznakY=1; predznakX=0;}
		boolean free1 = false;
		if(direction==0 && !navpicno[startX][startY]) free1=true;
		if(direction==1 && !vodoravno[startX][startY]) free1=true;
		if(direction==2 && !navpicno[startX+1][startY]) free1=true;
		if(direction==3 && !vodoravno[startX][startY+1]) free1=true;
		if(free1){
			startX+=predznakX;
			startY+=predznakY;
			while(true){
				boolean free = false;
				if(direction==0 && !navpicno[startX][startY]) free=true;
				if(direction==1 && !vodoravno[startX][startY]) free=true;
				if(direction==2 && !navpicno[startX+1][startY]) free=true;
				if(direction==3 && !vodoravno[startX][startY+1]) free=true;
				//if(startX>=vodoravno.length || startY==navpicno[0].length) break;
				//if(startX<0 || startY<0) break;
				if(Math.random()<0.7 && free){
					if(predznakX==0){
						navpicno[startX][startY]=true;
						navpicno[startX+1][startY]=true;
					}
					else{
						vodoravno[startX][startY]=true;
						vodoravno[startX][startY+1]=true;
					}
					startX+=predznakX;
					startY+=predznakY;
				}
				else{
					makeOutpost(startX, startY, direction, hops);
					break;
				}
			}
		}
	}

	void draw(Canvas c, Paint p){
		p.setColor(Color.WHITE);
		Protagonist pro = host.hero;
		for(int i=0; i<navpicno.length; i++){
			for(int j=0; j<navpicno[0].length; j++){
				if(navpicno[i][j]){
					for(int l=0; l<3; l++){
						float x1= i*size +delayX;
						float y1= (float)((j+0.33*l)*size+3) +delayY;
						float y2= (float)(j+0.33*(l+1))*size+3 +delayY;
						int razdaljaAlpha=host.razdalja(x1,(y1+y2)/2,pro.x,pro.y);
						int alpha=0;
						for(int k=0; k<host.maker.light.size(); k++){
							Firefly f = host.maker.light.get(k);
							razdaljaAlpha=Math.min(razdaljaAlpha, host.razdalja(x1,(y1+y2)/2, f.x, f.y));
						}
						if(razdaljaAlpha>pro.radij*3) 
							alpha=0;
						else 
							alpha= 255 * (pro.radij*3-razdaljaAlpha)/(pro.radij*3);
						//alpha=Math.max(60, alpha);
						alpha=(int)(alpha*0.7);
						Paint pp=new Paint();
						pp.setColor(Color.argb(alpha, 255,255,255));
						c.drawLine(x1, y1, x1, y2, pp);
					}
				}
			}
		}
		for(int i=0; i<vodoravno.length; i++){
			for(int j=0; j<vodoravno[0].length; j++){
				if(vodoravno[i][j]){
					for(int l=0; l<3; l++){
						float x1= (float)((i+l*0.33)*size) +delayX;
						float x2= (float)((i+(l+1)*0.33)*size) +delayX;
						float y1= j*size+3 +delayY;
						int razdaljaAlpha=host.razdalja((x1+x2)/2,y1,pro.x,pro.y);
						int alpha=0;
						for(int k=0; k<host.maker.light.size(); k++){
							Firefly f = host.maker.light.get(k);
							razdaljaAlpha=Math.min(razdaljaAlpha, host.razdalja((x1+x2)/2,y1, f.x, f.y));
						}
						if(razdaljaAlpha>pro.radij*3) 
							alpha=0;
						else 
							alpha= 255 * (pro.radij*3-razdaljaAlpha)/(pro.radij*3);
						//alpha=Math.max(60, alpha);
						alpha=(int)(alpha*0.7);
						Paint pp=new Paint();
						pp.setColor(Color.argb(alpha, 255,255,255));
						c.drawLine(x1, y1, x2, y1, pp);
					}
				}
			}
		}
	}
	
}

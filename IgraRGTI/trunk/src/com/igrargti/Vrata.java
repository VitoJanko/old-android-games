package com.igrargti;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * TODO Put here a description of what this class does.
 *
 * @author Jani.
 *         Created 19. jan. 2012.
 */
public class Vrata extends Cube {
	
	public boolean open = false;
	public boolean[] stikala;
	public float zacetniX;
	
	/**
	 * TODO Put here a description of what this constructor does.
	 *
	 * @param textura
	 * @param sirina
	 * @param visina
	 * @param dolzina
	 * @param x
	 * @param y
	 * @param z
	 * @param move
	 * @param mx
	 * @param my
	 * @param mz
	 */
	public Vrata(int textura, float sirina, float visina, float dolzina,
			float x, float y, float z, boolean[] move, float[] mx, float[] my,
			float[] mz,boolean[] stikala) {
		super(textura, sirina, visina, dolzina, x, y, z, move, mx, my, mz);
		// TODO Auto-generated constructor stub.
		this.stikala = stikala;
		this.zacetniX = x;
	}
	
	@Override
	public void move(){
		if(open){
			float timeNow = System.nanoTime();
			float time = (timeNow - lastTime);
			if( time > 1000000){
				lastTime = timeNow;
				
				if(left){
					moveX -= 0.002;
					if(moveX < zacetniX-sirina*2+0.01){
						MiscSound.pauseFireplace();
						left = false;
					}
				}
	
	
				
				float vertice[] = {
						//Vertices according to faces
						-sirina+moveX, -visina+moveY, dolzina+moveZ, //v0
						sirina+moveX, -visina+moveY, dolzina+moveZ, 	//v1
						-sirina+moveX, visina+moveY, dolzina+moveZ, 	//v2
						sirina+moveX, visina+moveY, dolzina+moveZ, 	//v3
			
						sirina+moveX, -visina+moveY, dolzina+moveZ, 	// ...
						sirina+moveX, -visina+moveY, -dolzina+moveZ, 
						sirina+moveX, visina+moveY, dolzina+moveZ, 
						sirina+moveX, visina+moveY, -dolzina+moveZ,
			
						sirina+moveX, -visina+moveY, -dolzina+moveZ, 
						-sirina+moveX, -visina+moveY, -dolzina+moveZ, 
						sirina+moveX, visina+moveY, -dolzina+moveZ, 
						-sirina+moveX, visina+moveY, -dolzina+moveZ,
			
						-sirina+moveX, -visina+moveY, -dolzina+moveZ, 
						-sirina+moveX, -visina+moveY, dolzina+moveZ, 
						-sirina+moveX, visina+moveY, -dolzina+moveZ, 
						-sirina+moveX, visina+moveY, dolzina+moveZ,
			
						-sirina+moveX, -visina+moveY, -dolzina+moveZ, 
						sirina+moveX, -visina+moveY, -dolzina+moveZ, 
						-sirina+moveX, -visina+moveY, dolzina+moveZ, 
						sirina+moveX, -visina+moveY, dolzina+moveZ,
			
						-sirina+moveX, visina+moveY, dolzina+moveZ, 
						sirina+moveX, visina+moveY, dolzina+moveZ, 
						-sirina+moveX, visina+moveY, -dolzina+moveZ, 
						sirina+moveX, visina+moveY, -dolzina+moveZ, 
											};
				
				
				ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertice.length * 4);
				byteBuf.order(ByteOrder.nativeOrder());
				vertexBuffer = byteBuf.asFloatBuffer();
				vertexBuffer.put(vertice);
				vertexBuffer.position(0);
			}
		}
	}
	
	public void openStikalo(int num){
		stikala[num] = true;
		
		boolean tempSt = true;
		for(int i=0;i<stikala.length;i++){
			if(stikala[i] == false){
				tempSt = false;
				break;
			}
		}
		
		if(tempSt && !open){
			open = true;
			MiscSound.playFireplace();
		}
	}
	
	public void closeAll(){
		
	}

}

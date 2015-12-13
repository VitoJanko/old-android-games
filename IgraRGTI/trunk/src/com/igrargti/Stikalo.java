package com.igrargti;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * TODO Put here a description of what this class does.
 *
 * @author Jani.
 *         Created 18. jan. 2012.
 */
public class Stikalo extends Cube {
	
	protected boolean aktivirajGumb = false;
	private int id;
	private Vrata vrata;
	
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
	public Stikalo(int textura, float sirina, float visina, float dolzina,
			float x, float y, float z, boolean[] move, float[] mx, float[] my,
			float[] mz,int id,Vrata vrata) {
		super(textura, sirina, visina, dolzina, x, y, z, move, mx, my, mz);
		this.id = id;
		this.vrata = vrata;
	}
	
	
	@Override
	public void move(){
		if(aktivirajGumb){
			float timeNow = System.nanoTime();
			float time = (timeNow - lastTime);
			if( time > 1000000){
				lastTime = timeNow;
				
				if(up){
					moveY -= 0.001;
					if(moveY < -visina+0.01) up = false;
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
	
	@Override
	public boolean playerContact(float X,float Z){
		if(X >= (moveX-sirina) && X <= (moveX+sirina)){
			if(Z >= (moveZ-sirina) && Z <= (moveZ+sirina)){
				aktivirajGumb = true;
				vrata.openStikalo(id);
				return true;
			}
		}
		return false;
	}
}

package com.igrargti;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * TODO Put here a description of what this class does.
 *
 * @author Jani.
 *         Created 19. jan. 2012.
 */
public class Boss extends Cube {

	private float hitrost;
	
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
	public Boss(int textura, float sirina, float visina, float dolzina,
			float x, float y, float z, boolean[] move, float[] mx, float[] my,
			float[] mz,float hitrost) {
		super(textura, sirina, visina, dolzina, x, y, z, move, mx, my, mz);
		
		this.hitrost = hitrost;
	}
	
	
	public boolean player(float x, float z,GL10 gl,int filter){
		draw(gl,filter);
		
		if(x > 30.5 && x < 41.5 && z > 16.5 && z < 24.5){
			movee(x,z);
			if(playerContact(x,z)) return true;
		}		
		return false;
	}
	
	@Override
	public void move(){
		//empty
	}
	
	public void movee(float x,float z){
		float timeNow = System.nanoTime();
		float time = (timeNow - lastTime);
		if( time > 10000){
			lastTime = timeNow;
			
			float vx = x-(moveX);
			float vz = z-(moveZ);
			
			vx = vx / Math.abs(vx); //normiramo
			vz = vz/ Math.abs(vz);
			
			moveX += vx * hitrost;
			moveZ += vz * hitrost;
			
			//Log.i("Boss position","X ="+String.valueOf(moveX)+" Z="+String.valueOf(moveZ));
			
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

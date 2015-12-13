package com.igrargti;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;


public class Pyramid {

	protected float moveX = 0;
	protected float moveY = 0;
	protected float moveZ = 0;
	protected float lastTime = System.nanoTime();
	
	protected boolean left = true;
	protected boolean up = true;
	protected boolean foward = true;
	
	protected float sirina = 0.1f;
	protected float visina = 0.1f;
	protected float dolzina = 0.1f;
	
	protected boolean move[];
	protected float mx[];
	protected float my[];
	protected float mz[];
	
	int textura;
	
	
	/** The buffer holding the vertices */
	protected FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	protected FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	protected ByteBuffer indexBuffer;
	/** The buffer holding the normals */
	protected FloatBuffer normalBuffer;
	/** The buffer holding the color values */
	private FloatBuffer colorBuffer;

	/** Our texture pointer */
	protected int[] textures = new int[3];

	/** The initial vertex definition */	
//	protected float vertices[] = {
//						//Vertices according to faces
//						-sirina+moveX, -visina+moveY, dolzina+moveZ, //v0
//						sirina+moveX, -visina+moveY, dolzina+moveZ, 	//v1
//						-sirina+moveX, visina+moveY, dolzina+moveZ, 	//v2
//						sirina+moveX, visina+moveY, dolzina+moveZ, 	//v3
//			
//						sirina+moveX, -visina+moveY, dolzina+moveZ, 	// ...
//						sirina+moveX, -visina+moveY, -dolzina+moveZ, 
//						sirina+moveX, visina+moveY, dolzina+moveZ, 
//						sirina+moveX, visina+moveY, -dolzina+moveZ,
//			
//						sirina+moveX, -visina+moveY, -dolzina+moveZ, 
//						-sirina+moveX, -visina+moveY, -dolzina+moveZ, 
//						sirina+moveX, visina+moveY, -dolzina+moveZ, 
//						-sirina+moveX, visina+moveY, -dolzina+moveZ,
//			
//						-sirina+moveX, -visina+moveY, -dolzina+moveZ, 
//						-sirina+moveX, -visina+moveY, dolzina+moveZ, 
//						-sirina+moveX, visina+moveY, -dolzina+moveZ, 
//						-sirina+moveX, visina+moveY, dolzina+moveZ,
//			
//						-sirina+moveX, -visina+moveY, -dolzina+moveZ, 
//						sirina+moveX, -visina+moveY, -dolzina+moveZ, 
//						-sirina+moveX, -visina+moveY, dolzina+moveZ, 
//						sirina+moveX, -visina+moveY, dolzina+moveZ,
//			
//						-sirina+moveX, visina+moveY, dolzina+moveZ, 
//						sirina+moveX, visina+moveY, dolzina+moveZ, 
//						-sirina+moveX, visina+moveY, -dolzina+moveZ, 
//						sirina+moveX, visina+moveY, -dolzina+moveZ, 
//											};


	protected float normals[] = {
						// Normals
			0.0f, 0.0f, 1.0f, 						
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f, 
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
											};

	/** The initial texture coordinates (u, v) */	
	protected float texture[] = {
						//Mapping coordinates for the vertices
						0.0f, 0.0f, 
						0.0f, 1.0f, 
						1.0f, 0.0f, 
			
						0.0f, 0.0f,
						0.0f, 1.0f, 
						1.0f, 0.0f,
			
						0.0f, 0.0f, 
						0.0f, 1.0f, 
						1.0f, 0.0f, 
			
						0.0f, 0.0f, 
						0.0f, 1.0f, 
						1.0f, 0.0f, 
									};

	/** The initial indices definition */
	protected byte indices[] = {
						// Faces definition
						0,1,2, 		// Face front
						3, 4, 5, 		// Face right
						6, 7, 8, 	// ...
						9, 10, 11, 
												};
	
	private float colors[] = {
    		1.0f, 0.0f, 0.0f, 1.0f, //Red
    		0.0f, 1.0f, 0.0f, 1.0f, //Green
    		0.0f, 0.0f, 1.0f, 1.0f, //Blue
    		
    		1.0f, 0.0f, 0.0f, 1.0f, //Red
    		0.0f, 0.0f, 1.0f, 1.0f, //Blue
    		0.0f, 1.0f, 0.0f, 1.0f, //Green
    		
    		1.0f, 0.0f, 0.0f, 1.0f, //Red
    		0.0f, 1.0f, 0.0f, 1.0f, //Green
    		0.0f, 0.0f, 1.0f, 1.0f, //Blue
    		
    		1.0f, 0.0f, 0.0f, 1.0f, //Red
    		0.0f, 0.0f, 1.0f, 1.0f, //Blue
    		0.0f, 1.0f, 0.0f, 1.0f 	//Green
			    					};

	

	

	public Pyramid(int textura,float sirina, float visina, float dolzina, float x,float y, float z,boolean[] move,float[] mx,float[] my,float[] mz) {
		this.textura = textura;
		
		this.sirina = sirina;
		this.visina = visina;
		this.dolzina = dolzina;
		
		this.move = move;
		this.mx = mx;
		this.my = my;
		this.mz = mz;
		
		moveX = x;
		moveY = y;
		moveZ = z;
		
		float vertices[] = {
				//Vertices according to faces
				-sirina+moveX, -visina+moveY,-dolzina+moveZ, //spodnji trikotnik
				sirina+moveX,-visina+moveY,-dolzina+moveZ,
				moveX,-visina+moveY,dolzina+moveZ,
				
				-sirina+moveX, -visina+moveY,-dolzina+moveZ, //spredi
				sirina+moveX,-visina+moveY,-dolzina+moveZ,
				moveX,visina+moveY,moveZ,
				
				sirina+moveX,-visina+moveY,-dolzina+moveZ, //desni
				moveX,-visina+moveY,dolzina+moveZ,
				moveX,visina+moveY,moveZ,
				
				moveX,-visina+moveY,dolzina+moveZ, //levi
				-sirina+moveX, -visina+moveY,-dolzina+moveZ,
				moveX,visina+moveY,moveZ,
									};
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		//
//		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
//		byteBuf.order(ByteOrder.nativeOrder());
//		textureBuffer = byteBuf.asFloatBuffer();
//		textureBuffer.put(texture);
//		textureBuffer.position(0);
		byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		colorBuffer = byteBuf.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);

		//
		byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuf.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);

		//
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		
	}


	public void draw(GL10 gl, int filter) {
		//Bind the texture according to the set texture filter
		gl.glBindTexture(gl.GL_TEXTURE_2D, textures[filter]);

		//Enable the vertex, texture and normal state
		gl.glEnableClientState(gl.GL_VERTEX_ARRAY);
		//gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(gl.GL_COLOR_ARRAY);
		gl.glEnableClientState(gl.GL_NORMAL_ARRAY);

		//Set the face rotation
		gl.glFrontFace(gl.GL_CCW);
		
		//Point to our buffers
		gl.glVertexPointer(3, gl.GL_FLOAT, 0, vertexBuffer);
		//gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glColorPointer(4, gl.GL_FLOAT, 0, colorBuffer);
		gl.glNormalPointer(gl.GL_FLOAT, 0, normalBuffer);
		
		//Draw the vertices as triangles, based on the Index Buffer information
		gl.glDrawElements(gl.GL_TRIANGLES, indices.length, gl.GL_UNSIGNED_BYTE, indexBuffer);
		
		//Disable the client state before leaving
		gl.glDisableClientState(gl.GL_VERTEX_ARRAY);
		//gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(gl.GL_COLOR_ARRAY);
		gl.glDisableClientState(gl.GL_NORMAL_ARRAY);
		
		move();
	}
	
	public boolean playerContact(float X,float Z){
		if(X >= (moveX-sirina) && X <= (moveX+sirina)){
			if(Z >= (moveZ-sirina) && Z <= (moveZ+sirina)){
				return true;
			}
		}
		return false;
	}
	
	public void move(){
		if((System.nanoTime() - lastTime) > 50000000){
			lastTime = System.nanoTime();
			
			float R = (float) Math.random();
			float G = (float) Math.random();
			float B = (float) Math.random();
			
			float col[] = {
		    		R, G, B, 1.0f,
		    		R, G, B, 1.0f,
		    		R, G, B, 1.0f,
		    		
		    		R, G, B, 1.0f,
		    		R, G, B, 1.0f,
		    		R, G, B, 1.0f,
		    		
		    		R, G, B, 1.0f,
		    		R, G, B, 1.0f,
		    		R, G, B, 1.0f,
		    		
		    		R, G, B, 1.0f,
		    		R, G, B, 1.0f,
		    		R, G, B, 1.0f,
					    					};
			
			
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(col.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			colorBuffer = byteBuf.asFloatBuffer();
			colorBuffer.put(col);
			colorBuffer.position(0);
		}
	}


	public void loadGLTexture(GL10 gl, Context context) {
		//Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(textura);
		Bitmap bitmap = null;
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}

		//Generate there texture pointer
		gl.glGenTextures(3, textures, 0);

		//Create Nearest Filtered Texture and bind it to texture 0
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		//Create Linear Filtered Texture and bind it to texture 1
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		//Create mipmapped textures and bind it to texture 2
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);

		/*
		 * This is a change to the original tutorial, as buildMipMap does not exist anymore
		 * in the Android SDK.
		 * 
		 * We check if the GL context is version 1.1 and generate MipMaps by flag.
		 * Otherwise we call our own buildMipMap implementation
		 */
		if(gl instanceof GL11) {
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			
		//
		} else {
			buildMipmap(gl, bitmap);
		}		
		
		//Clean up
		bitmap.recycle();
	}
	

	protected void buildMipmap(GL10 gl, Bitmap bitmap) {
		//
		int level = 0;
		//
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();

		//
		while(height >= 1 || width >= 1) {
			//First of all, generate the texture from our bitmap and set it to the according level
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
			
			//
			if(height == 1 || width == 1) {
				break;
			}

			//Increase the mipmap level
			level++;

			//
			height /= 2;
			width /= 2;
			Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
			
			//Clean up
			bitmap.recycle();
			bitmap = bitmap2;
		}
	}
}

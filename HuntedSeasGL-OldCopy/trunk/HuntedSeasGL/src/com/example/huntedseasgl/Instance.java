package com.example.huntedseasgl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Instance {

	protected int textureList[];
	
	//private int animationDirection = 1;
	protected int textureImage = 0;
	protected float offsetX = 0;
	protected float offsetY = 0;
	protected long time = 0;
	protected float angle = 0;
	
	float coordinates[];
	float cubeTextureCoordinateData[];
	short order[];
	
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    
    private FloatBuffer mCubeTextureCoordinates;
    private int mTextureUniformHandle;
    private int mTextureCoordinateHandle;
    private final int mTextureCoordinateDataSize = 2;
    
    float[] mVPMatrix;
    
    Sea host;
    boolean dead = false;
	float x;
	float y;
	float speed;
	int w;
	int h;
	
	boolean started;
        
    public Instance(Sea host, float x, float y, LoadState loader){
    	textureList = new int[1];
    	started=false;
    	this.host=host;
    	this.x=x;
    	this.y=y;
    	coordinates = loader.coordinates;
    	cubeTextureCoordinateData = loader.cubeTextureCoordinateData;
    	order = loader.order;
    	inicializeGL();
    	mVPMatrix = new float[16];
    }
    
    
    public void draw(){   	
    	GLES20.glUseProgram(host.renderer.programHandle);
    	
    	if(!started){
            mTextureUniformHandle = GLES20.glGetUniformLocation(host.renderer.programHandle, "uTexture");
            mTextureCoordinateHandle = GLES20.glGetAttribLocation(host.renderer.programHandle, "aTexCoordinate");
            GLES20.glActiveTexture(GLES20.GL_TEXTURE);
            started=true;
    	}
    	
    	 int mTimeHandler = GLES20.glGetUniformLocation(host.renderer.programHandle, "mTime");
         GLES20.glUniform1f(mTimeHandler, host.renderer.mTime);
    	
    	
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureList[textureImage]);
        GLES20.glUniform1i(mTextureUniformHandle, 0);
    	
    	mPositionHandle = GLES20.glGetAttribLocation(host.renderer.programHandle, "vPosition");
    	GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, vertexBuffer);
    	GLES20.glEnableVertexAttribArray(mPositionHandle);
    	    	
        mCubeTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mCubeTextureCoordinates);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
    	
        mMVPMatrixHandle = GLES20.glGetUniformLocation(host.renderer.programHandle, "uMVPMatrix");
    	GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mVPMatrix, 0);
    	
    	GLES20.glDrawElements(GLES20.GL_TRIANGLES, order.length, GLES20.GL_UNSIGNED_SHORT,drawListBuffer);
    	//GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6); <-- lahko tudi tako izpišeš ampak mores pazit na vrsti red potem
    	GLES20.glDisableVertexAttribArray(mPositionHandle);
    	GLES20.glDisableVertexAttribArray(mColorHandle);
    }
	
	
	void step(){
		Matrix.setIdentityM(mVPMatrix,0);
        Matrix.translateM(mVPMatrix,0,-x+host.premikX,-y+host.premikY,0);
		Matrix.multiplyMM(mVPMatrix, 0, host.renderer.mMVPMatrix, 0, mVPMatrix, 0);
		
		//for(int i=0; i<4; i++){
		//	for(int j=0; j<4; j++)
		//		System.out.print(mVPMatrix[4*j+i]+ " ");
		//	System.out.println();
		//}
	}
	
	
	void inicializeGL(){
		vertexBuffer = ByteBuffer.allocateDirect(coordinates.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    	vertexBuffer.put(coordinates).position(0);
    	
    	drawListBuffer = ByteBuffer.allocateDirect(order.length*2).order(ByteOrder.nativeOrder()).asShortBuffer();
    	drawListBuffer.put(order).position(0);
    	    	
    	mCubeTextureCoordinates = ByteBuffer.allocateDirect(cubeTextureCoordinateData.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubeTextureCoordinates.put(cubeTextureCoordinateData).position(0);
	}
}

package org.bizjak.android.GPU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

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
	int centerX;
	int centerY;
	float scaleFactor;
	
	boolean started;
        
    public Instance(Sea host, float x, float y, float scaleFactor,LoadState loader){
    	textureList = loader.texture;
    	started=false;
    	this.host=host;
    	this.x=x;
    	this.y=y;
    	this.scaleFactor=scaleFactor;
    	w = (int)((loader.xEnd-loader.xStart)*scaleFactor);
    	h = (int)((loader.yEnd-loader.yStart)*scaleFactor);
    	centerX = (int)(loader.xEnd*scaleFactor+loader.xStart*scaleFactor)/2;
    	centerY = (int)(loader.yEnd*scaleFactor+loader.yStart*scaleFactor)/2;
    	Log.d("Instance","Around 65: w: "+w+" h: "+h+" centerX: "+centerX+" ceterY: "+centerY);
    	coordinates = loader.coordinates;
    	cubeTextureCoordinateData = loader.cubeTextureCoordinateData;
    	order = loader.order;
    	inicializeGL();
    	mVPMatrix = new float[16];
    }
    
    public void nextFrame(){
    	textureImage+=1;
    	if (textureImage==textureList.length)
    		textureImage=0;
    }
    
    protected boolean collide(int[] b1, int[] b2){
		boolean vertical = false;
		boolean horizontal = false;
		if (b2[0]>=b1[0] && b2[0]<=b1[2])
			horizontal = true;
		if (b1[0]>=b2[0] && b1[0]<=b2[2])
			horizontal = true;
		if (b2[1]>=b1[1] && b2[1]<=b1[3])
			vertical = true;
		if (b1[1]>=b2[1] && b1[1]<=b2[3])
			vertical = true;
		return (horizontal && vertical);
	}
	
	protected void demage(float recoil, float diminish, int lose){
		host.hero.dirX=host.hero.x-x;
		host.hero.dirY=host.hero.y-y;
		host.hero.recoil=recoil;
		host.hero.diminish=diminish;
		//host.hero.food-=lose;
		//if(host.hero.food<0) host.hero.food=0;
	}
	
	protected int[] getBox(){
		int[] bounds = new int[4];
		bounds[0]=(int)(x-(w/2)*0.85);
		bounds[1]=(int)(y-(h/2)*0.85);
		bounds[2]=(int)(x+(w/2)*0.85);
		bounds[3]=(int)(y+(h/2)*0.85);
		return bounds;
	}
    
    
    public void draw(){       	
//    	GLES20.glUseProgram(host.renderer.programHandle);
    	
    	if(!started){
            mTextureUniformHandle = GLES20.glGetUniformLocation(host.renderer.programHandle, "uTexture");
            mTextureCoordinateHandle = GLES20.glGetAttribLocation(host.renderer.programHandle, "aTexCoordinate");
            GLES20.glActiveTexture(GLES20.GL_TEXTURE);
            started=true;
    	}
    	
    	 int mTimeHandler = GLES20.glGetUniformLocation(host.renderer.programHandle, "mTime");
         GLES20.glUniform1f(mTimeHandler, host.renderer.mTime);
    	
    	//TODO zbrisi try catch
         try{
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureList[textureImage]);
         }catch(ArrayIndexOutOfBoundsException e){
        	 Log.e("Instance","Around 130: size: "+textureList.length+"  index: "+textureImage);
        	 e.printStackTrace();
        	 System.exit(1);
         }
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
		
        Matrix.translateM(mVPMatrix,0,-x -centerX +host.premikX,-y-centerY+host.premikY,0);
        Matrix.scaleM(mVPMatrix, 0, scaleFactor, scaleFactor, 1);
        //Matrix.translateM(mVPMatrix,0,-x +host.premikX,-y+host.premikY,0);
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

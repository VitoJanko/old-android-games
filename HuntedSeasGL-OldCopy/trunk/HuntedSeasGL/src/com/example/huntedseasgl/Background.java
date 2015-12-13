package com.example.huntedseasgl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

public class Background {
	float[] mVPMatrix;
	
	final int mBytesPerFloat = 4;	
	
	final FloatBuffer bgPositionBuffer;
	final FloatBuffer bgTextureBuffer;

	final float[] bgTexture ;
	final float[] bgPosition; 

	int textureDataHandle;

	int slices = 100;
	Sea host;
	
	private static final float touch[] = {
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f, //red
		0.0f, 0.0f, 0.0f, 0.0f //red
	};

	Background(int startX, int startY, int width, int height, int depth, Sea host, int resource){
		this.host=host;
		bgPosition=makeSquare(startX,startY,width,height,depth,slices);
		bgTexture=makeSquare2D(0f,1f,1f,1f,slices);
		
		bgPositionBuffer = ByteBuffer.allocateDirect(bgPosition.length * mBytesPerFloat)
		        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		bgPositionBuffer.put(bgPosition).position(0);		
	
				
		bgTextureBuffer= ByteBuffer.allocateDirect(bgTexture.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();							
		bgTextureBuffer.put(bgTexture).position(0);
		
		textureDataHandle  = loadTexture(host.context,resource);
		mVPMatrix = new float[16];
	}
	
	float[] makeSquare(float startX, float startY, float width, float height, float depth,int web){
		float[] square = new float[web*web*6*3];
		int index = 0;
		int sqX = 0;
		int sqY= 0;
		for (int i=0; i<web; i++){
			for(int j=0; j<web; j++){
				float t_width = width/web;
				float t_height = height/web;
				float t_startX = startX- (width/web)*sqX;
				float t_startY = startY+ (height/web)*sqY;
				square[index+0] = t_startX;
				square[index+1] = t_startY;
				square[index+2] = depth;
				square[index+3] = t_startX;
				square[index+4] = t_startY+t_height;
				square[index+5] = depth;
				square[index+6] = t_startX-t_width;
				square[index+7] = t_startY;
				square[index+8] = depth;
				square[index+9] = t_startX;
				square[index+10] = t_startY+t_height;
				square[index+11] = depth;
				square[index+12] = t_startX-t_width;
				square[index+13] = t_startY+t_height;
				square[index+14] = depth;
				square[index+15] = t_startX-t_width;
				square[index+16] = t_startY;
				square[index+17] = depth;
				index+=18;
				sqX+=1;
			}
			sqY+=1;
			sqX=0;
			
		}
		return square;
	}
	
	float[] makeSquare2D(float startX, float startY, float width, float height, int web){
		float[] square = new float[web*web*6*2];
		int index = 0;
		int sqX = 0;
		int sqY= 0;
		for (int i=0; i<web; i++){
			for(int j=0; j<web; j++){
				float t_width = width/web;
				float t_height = height/web;
				float t_startX = startX+ (width/web)*sqX;
				float t_startY = startY- (height/web)*sqY;
				square[index+0] = t_startX;
				square[index+1] = t_startY;
				square[index+2] = t_startX;
				square[index+3] = t_startY-t_height;
				square[index+4] = t_startX+t_width;
				square[index+5] = t_startY;
				square[index+6] = t_startX;
				square[index+7] = t_startY-t_height;
				square[index+8] = t_startX+t_width;
				square[index+9] = t_startY-t_height;
				square[index+10] = t_startX+t_width;
				square[index+11] = t_startY;
				index+=12;
				sqX+=1;
			}
			sqY+=1;
			sqX=0;
			
		}
		return square;
	}
	
	public void draw(int programHandle){
		
		
    	GLES20.glUseProgram(programHandle);
    	           
        int mTimeHandler = GLES20.glGetUniformLocation(programHandle, "mTime");
        int mOffXHandler = GLES20.glGetUniformLocation(programHandle, "offX");
        int mOffYHandler = GLES20.glGetUniformLocation(programHandle, "offY");
        int mSizeHandler = GLES20.glGetUniformLocation(programHandle, "size");
        int mTouchHandler = GLES20.glGetUniformLocation(programHandle, "touchEvent");
    	
        float waveSize = 0.008f;
        float offX = 0.0f;
    	float offY = 0.0f;
    	
    	System.out.println(host.renderer.mTime);
    	touch[0] = offX;
		touch[1] = offY;
    	touch[2]=waveSize;
    	
        GLES20.glUniform1f(mTimeHandler, host.renderer.mTime);
        GLES20.glUniform1f(mOffXHandler, offX);
        GLES20.glUniform1f(mOffYHandler, offY);
        GLES20.glUniform1f(mSizeHandler, waveSize);
        GLES20.glUniform4fv(mTouchHandler, touch.length, touch, 0);

        int mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "uMVPMatrix");

        int positionHandle = GLES20.glGetAttribLocation(programHandle, "vPosition");
        int textureUniformHandle = GLES20.glGetUniformLocation(programHandle, "uTexture");
    	int textureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "aTexCoordinate");
		
    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle);
    	GLES20.glUniform1i(textureUniformHandle, 0);
    	
    	bgTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(textureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 
        		0, bgTextureBuffer);
        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
        
		bgPositionBuffer.position(0);		
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false,
        		0, bgPositionBuffer);        
        GLES20.glEnableVertexAttribArray(positionHandle);        
         
        Matrix.setIdentityM(mVPMatrix,0);
        Matrix.translateM(mVPMatrix,0,host.premikX,host.premikY,0);
		Matrix.multiplyMM(mVPMatrix, 0, host.renderer.mMVPMatrix, 0, mVPMatrix, 0);
        
        
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mVPMatrix, 0);
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6*slices*slices);     
	}
	

	public static int loadTexture(final Context context, final int resourceId){
	    final int[] textureHandle = new int[1];
	    GLES20.glGenTextures(1, textureHandle, 0);
	    if (textureHandle[0] != 0){
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inScaled = false;  	 
	        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);	 
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
	        bitmap.recycle();
	    }
	 
	    if (textureHandle[0] == 0)
	        throw new RuntimeException("Error loading texture.");

	    return textureHandle[0];
	}
	
}

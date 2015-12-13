package org.bizjak.android.GPU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidGL20;

public class BackgroundBackupVPO {
	float[] mVPMatrix;
	
	final int mBytesPerFloat = 4;	
	
	final FloatBuffer bgPositionBuffer;
	final FloatBuffer bgTextureBuffer;

	final float[] bgTexture ;
	final float[] bgPosition; 
	final float[] bgNormals = {};

	float x;
	float y;
	boolean moves;
	float direction;
	
	int textureDataHandle;

	int slices = 100;
	Sea host;
	
	int POSITION_DATA_SIZE = 3;
	int NORMAL_DATA_SIZE = 0;
	int TEXTURE_COORDINATE_DATA_SIZE = 2;

	
	AndroidGL20 mGlEs20;
	
	int mCubePositionsBufferIdx;
	int mCubeNormalsBufferIdx;
	int mCubeTexCoordsBufferIdx;
	
	private static final float touch[] = {
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f, 
		0.0f, 0.0f, 0.0f, 0.0f 
	};

	BackgroundBackupVPO(int startX, int startY, int width, int height, int depth, Sea host, int resource, boolean moves){
		mGlEs20 = new AndroidGL20();
		this.host=host;
		this.moves=moves;
		this.direction=0;
		bgPosition=makeSquare(startX,startY,width,height,depth,slices);
		bgTexture=makeSquare2D(0f,1f,1f,1f,slices);
		
		bgPositionBuffer = ByteBuffer.allocateDirect(bgPosition.length * mBytesPerFloat)
		        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
		bgPositionBuffer.put(bgPosition).position(0);		
	
				
		bgTextureBuffer= ByteBuffer.allocateDirect(bgTexture.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();							
		bgTextureBuffer.put(bgTexture).position(0);
		
//		cubeBuffer = getInterleavedBuffer(bgPosition,bgTexture,1);
		
		textureDataHandle  = loadTexture(host.context,resource);
		mVPMatrix = new float[16];	
		
		
//		final int buffers[] = new int[2];
//		GLES20.glGenBuffers(2, buffers, 0);						
//
//		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
//		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,bgPositionBuffer.capacity() * mBytesPerFloat, bgPositionBuffer, GLES20.GL_STATIC_DRAW);
//
//		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
//		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, bgTextureBuffer.capacity() *mBytesPerFloat, bgTextureBuffer,GLES20.GL_STATIC_DRAW);
//
//		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	      
//			mCubePositionsBufferIdx = buffers[0];
//			mCubeTexCoordsBufferIdx = buffers[1];
		
		
		FloatBuffer cubeBuffer = getInterleavedBuffer(bgPosition, bgTexture, 1);			
		
		// Second, copy these buffers into OpenGL's memory. After, we don't need to keep the client-side buffers around.					
		final int buffers[] = new int[1];
		GLES20.glGenBuffers(1, buffers, 0);						

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, cubeBuffer.capacity() * mBytesPerFloat, cubeBuffer, GLES20.GL_STATIC_DRAW);			

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		mCubePositionsBufferIdx = buffers[0];			
		
		cubeBuffer.limit(0);
		cubeBuffer = null;
	     
	     // IMPORTANT: Unbind from the buffer when we're done with it.
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
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
	
	public void step(){
		direction+= (float)(Math.random()*(Math.PI/400));
		x+=host.timePassed*Math.cos(direction)*0.09f;
		y+=host.timePassed*Math.sin(direction)*0.015f;
	}
	
	public void draw(int programHandle){
		
		final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + TEXTURE_COORDINATE_DATA_SIZE) * mBytesPerFloat;
		
		
    	GLES20.glUseProgram(programHandle);
    	           
        int mTimeHandler = GLES20.glGetUniformLocation(programHandle, "mTime");
        int mOffXHandler = GLES20.glGetUniformLocation(programHandle, "offX");
        int mOffYHandler = GLES20.glGetUniformLocation(programHandle, "offY");
        int mSizeHandler = GLES20.glGetUniformLocation(programHandle, "size");
        int mTouchHandler = GLES20.glGetUniformLocation(programHandle, "touchEvent");
    	
        float waveSize = 0.008f;
        float offX = 0.0f;
    	float offY = 0.0f;
    	
    	Log.d("Background","Around 172. Render mTime: "+host.renderer.mTime);
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
//        int textureUniformHandle = GLES20.glGetUniformLocation(programHandle, "uTexture");
    	int textureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "aTexCoordinate");
		
//    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle);
//    	GLES20.glUniform1i(textureUniformHandle, 0);
      	
    	
//		// Pass in the position information
//		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubePositionsBufferIdx);
//		GLES20.glEnableVertexAttribArray(positionHandle);
//		mGlEs20.glVertexAttribPointer(positionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);
//		
//		// Pass in the texture information
//		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeTexCoordsBufferIdx);
//		GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
//		mGlEs20.glVertexAttribPointer(textureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
//				0, 0);
    	
    	
		// Pass in the position information
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubePositionsBufferIdx);
		GLES20.glEnableVertexAttribArray(positionHandle);
		mGlEs20.glVertexAttribPointer(positionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, 0);

		
		// Pass in the texture information
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubePositionsBufferIdx);
		GLES20.glEnableVertexAttribArray(textureCoordinateHandle);;
		mGlEs20.glVertexAttribPointer(textureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
				stride, (POSITION_DATA_SIZE + NORMAL_DATA_SIZE) * mBytesPerFloat);

		// Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    	
    	
    	
    	
         
        Matrix.setIdentityM(mVPMatrix,0);
        Matrix.translateM(mVPMatrix,0,host.premikX-x,host.premikY-y,0);
		Matrix.multiplyMM(mVPMatrix, 0, host.renderer.mMVPMatrix, 0, mVPMatrix, 0);
        
        
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mVPMatrix, 0);
           	
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6*slices*slices);     
	}
	

	FloatBuffer getInterleavedBuffer(float[] cubePositions,float[] cubeTextureCoordinates, int generatedCubeFactor) {
		final int cubeDataLength = cubePositions.length  + cubeTextureCoordinates.length;	
		int cubePositionOffset = 0;
		int cubeTextureOffset = 0;
		
		final FloatBuffer cubeBuffer = ByteBuffer.allocateDirect(cubeDataLength * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();											
		
		for (int i = 0; i < generatedCubeFactor * generatedCubeFactor * generatedCubeFactor; i++) {								
			for (int v = 0; v < 6*slices*slices; v++) {
				cubeBuffer.put(cubePositions, cubePositionOffset, POSITION_DATA_SIZE);
				cubePositionOffset += POSITION_DATA_SIZE;
				cubeBuffer.put(cubeTextureCoordinates, cubeTextureOffset, TEXTURE_COORDINATE_DATA_SIZE);
				cubeTextureOffset += TEXTURE_COORDINATE_DATA_SIZE;					
			}
			
			// The  texture data is repeated for each cube.
			cubeTextureOffset = 0;	
		}
		
		cubeBuffer.position(0);
		
		return cubeBuffer;
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

package com.huntedseas;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class SquareGL {
	//private int image[];
	private int textureList[];
	
	private int animationDirection = 1;
	protected int textureImage = 0;
	protected float offsetX = 0;
	protected float offsetY = 0;
	protected long time = 0;
	protected float angle = 0;
	
	private float coordinates[];
	
	/*
	private static float coordinates[] = {
		-2.0f,  2.0f, 0.0f, //top left
		-2.0f, -2.0f, 0.0f, //bottom left
		 2.0f, -2.0f, 0.0f, //bottom right
		 2.0f,  2.0f, 0.0f  //top right
	};*/
	
	private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 };

	private static final float color[] = {
		1.0f, 0.0f, 0.0f, 1.0f, //red
		0.0f, 1.0f, 0.0f, 1.0f, //red
		0.0f, 0.0f, 1.0f, 1.0f, //red
		1.0f, 1.0f, 1.0f, 1.0f  //red
	};
	
	final float[] cubeTextureCoordinateData =
		{
		        1.0f, 0.0f,
		        1.0f, 1.0f,
		        0.0f, 1.0f,
		        0.0f, 0.0f,
		};
	
	private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "attribute vec4 aColor;" +
            "attribute vec2 aTexCoordinate;"+
            "varying vec2 vTexCoordinate;"+
            "varying vec4 vColor;" +
            "void main() {" +
            " vColor = aColor;" +
            "vTexCoordinate = aTexCoordinate;"+
            "  gl_Position = uMVPMatrix* vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform sampler2D uTexture;"+
            "varying vec2 vTexCoordinate;"+
            "varying vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = texture2D(uTexture,vTexCoordinate);" + //gl_FragColor = vColor;
            "}";
    
    
    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final FloatBuffer colorBuffer;
    
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    
    
    private final FloatBuffer mCubeTextureCoordinates;
    private int mTextureUniformHandle;
    private int mTextureCoordinateHandle;
    private final int mTextureCoordinateDataSize = 2;
        
    public SquareGL(int image,float[] coordinatesF){
    	//this.image = new int[]{image};
    	this.textureList = new int[1];
    	this.coordinates = coordinatesF;
    	
    	vertexBuffer = ByteBuffer.allocateDirect(coordinates.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    	vertexBuffer.put(coordinates).position(0);
    	
    	drawListBuffer = ByteBuffer.allocateDirect(drawOrder.length*2).order(ByteOrder.nativeOrder()).asShortBuffer();
    	drawListBuffer.put(drawOrder).position(0);
    	
    	colorBuffer = ByteBuffer.allocateDirect(color.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    	colorBuffer.put(color).position(0);
    	
    	mCubeTextureCoordinates = ByteBuffer.allocateDirect(cubeTextureCoordinateData.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubeTextureCoordinates.put(cubeTextureCoordinateData).position(0);
    	
    	final int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
    	final int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
    	
    	mProgram = GLES20.glCreateProgram();
    	GLES20.glAttachShader(mProgram, vertexShader);
    	GLES20.glAttachShader(mProgram, fragmentShader);
    	//SE BIND ATTRIBUTE LOCATION
    	GLES20.glLinkProgram(mProgram);
    	
    	textureList[0] = loadTexture(GameView.context, image); //ta image je iz metode, ne od razreda
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "uTexture");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoordinate");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE);
    }
    
    public SquareGL(int[] image,float[] coordinatesF){
    	//this.image = image;
    	this.textureList = new int[image.length];
    	this.coordinates = coordinatesF;
    	
    	vertexBuffer = ByteBuffer.allocateDirect(coordinates.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    	vertexBuffer.put(coordinates).position(0);
    	
    	drawListBuffer = ByteBuffer.allocateDirect(drawOrder.length*2).order(ByteOrder.nativeOrder()).asShortBuffer();
    	drawListBuffer.put(drawOrder).position(0);
    	
    	colorBuffer = ByteBuffer.allocateDirect(color.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    	colorBuffer.put(color).position(0);
    	
    	mCubeTextureCoordinates = ByteBuffer.allocateDirect(cubeTextureCoordinateData.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubeTextureCoordinates.put(cubeTextureCoordinateData).position(0);
    	
    	final int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
    	final int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
    	
    	mProgram = GLES20.glCreateProgram();
    	GLES20.glAttachShader(mProgram, vertexShader);
    	GLES20.glAttachShader(mProgram, fragmentShader);
    	//SE BIND ATTRIBUTE LOCATION
    	GLES20.glLinkProgram(mProgram);
    	
    	
    	//texture
    	for(int i=0;i<3;i++){
    		textureList[i] = loadTexture(GameView.context,image[i]);
    	}
    	//mTextureDataHandle = loadTexture(GameView.context, image);
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "uTexture");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoordinate");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE);
    }
    
    public void draw(float[] mVPMatrix){   	
    	GLES20.glUseProgram(mProgram);
    	           
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureList[textureImage]);
        GLES20.glUniform1i(mTextureUniformHandle, 0);
    	
    	mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
    	GLES20.glEnableVertexAttribArray(mPositionHandle);
    	GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, vertexBuffer);
    	    	
    	mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
    	GLES20.glEnableVertexAttribArray(mColorHandle);
    	GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4*4, colorBuffer);
    	  	
    	//texture
        mCubeTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mCubeTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
    	
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    	GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mVPMatrix, 0);
    	
    	GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT,drawListBuffer);
    	//GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6); <-- lahko tudi tako izpišeš ampak mores pazit na vrsti red potem
    	GLES20.glDisableVertexAttribArray(mPositionHandle);
    	GLES20.glDisableVertexAttribArray(mColorHandle);
    }
    
    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
    
    public static int loadTexture(final Context context, final int resourceId)
    {
        final int[] textureHandle = new int[1];
     
        GLES20.glGenTextures(1, textureHandle, 0);
     
        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling
     
            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
     
            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
     
            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
     
            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
     
            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }
     
        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }
     
        return textureHandle[0];
    }
    
    protected void makeAnimation(){
    	if(animationDirection > 0){
    		if(textureImage == textureList.length){
    			animationDirection *= -1;
    		}
    	}else{
    		if(textureImage == 0){
    			animationDirection *= -1;
    		}
    	}
    	textureImage += animationDirection;
    }
    
    protected void destroy(){
    	GLES20.glDeleteTextures(textureList.length,textureList,0);
    	GLES20.glDeleteProgram(mProgram);
    }
}
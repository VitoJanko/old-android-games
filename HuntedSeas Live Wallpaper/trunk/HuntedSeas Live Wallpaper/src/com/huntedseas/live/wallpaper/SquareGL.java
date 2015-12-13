package com.huntedseas.live.wallpaper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

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
            "uniform float mTime;"+
            "uniform float offX;"+//offset iz centra WARNING: koordinate od 0-1
            "uniform float offY;"+
            "uniform float size;"+
            "uniform vec4 touchEvent[3];"+
            "void main() {" +            
            "	vec2 myvec = vTexCoordinate;"+
            "	vec2 cPos = -1.0 + 2.0 * vTexCoordinate.xy;" + //normalizira
            "	for(int i=0;i<3;i++){" +
            " 		vec2 off3 = vec2(touchEvent[i].x,touchEvent[i].y);" +
            " 		vec2 ofvec3 = cPos+off3;"+
            "		float r = length(ofvec3);" +
            "       float mvm = touchEvent[i].w;"+
            "		if(mvm <= 0.25){" +
            "			if(r < mvm){" +
            "				myvec = myvec+(ofvec3/r)*sin(r*48.0-mTime*8.0)*touchEvent[i].z/(r*1.0);" + //r*2.5" +
            "			}" + 
            "		}else{"+
            " 			if(r > mvm-0.25 && r < mvm){"+
            "				myvec = myvec+(ofvec3/r)*sin(r*48.0-mTime*8.0)*touchEvent[i].z/(r*1.0);" + //r*2.5
            "			}" +
            "		}"+
            "	}"+
            "	gl_FragColor = texture2D(uTexture,myvec);" + //narise pixel
            "}";
    
    /*
    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform sampler2D uTexture;"+
            "varying vec2 vTexCoordinate;"+
            "varying vec4 vColor;" +
            "uniform float mTime;"+
            "uniform float offX;"+//offset iz centra WARNING: koordinate od 0-1
            "uniform float offY;"+
            "uniform float size;"+
            "void main() {" +
            "	vec2 off2 = vec2(offX,offY);"+
            "	vec2 cPos = -1.0 + 2.0 * vTexCoordinate.xy;" + //normalizira
            "	vec2 ofvec = cPos+off2;"+ //doda offset
            "	float r = sqrt(dot(ofvec,ofvec));"+ //sombrero funkcija
            "	float ofvecL = length(ofvec);"+ //dolzina vektorja (za normalizacijo)
            //"	vec2 uv = vTexCoordinate+(ofvec/ofvecL)*cos(r*48.0-mTime*8.0)*size;"+ //izracuna offset
            "vec2 uv = vTexCoordinate+(ofvec/ofvecL)*sin(r*48.0-mTime*8.0)*size/(r*1.8); "+
            
            " vec2 off3 = vec2(offX-0.35,offY-0.35);"+
            " vec2 ofvec2 = cPos+off3;"+
            " r = sqrt(dot(ofvec2,ofvec2));"+
            "ofvecL = length(ofvec2);"+
            "vec2 uv2 = uv+(ofvec2/ofvecL)*sin(r*48.0-mTime*8.0)*size/(r*1.8);"+
            "	gl_FragColor = texture2D(uTexture,uv2);" + //narise pixel
            "}";
     */
    

    
    
    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final FloatBuffer colorBuffer;
    
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    
    private float mTime = 0.5f;
    private int mTimeHandler;
    private int mOffXHandler;
    private int mOffYHandler;
    private static float offX = 0.0f;
    private static float offY = 0.0f;
    private int mSizeHandler;
    private static float waveSize = 0.008f;
    
    private int mTouchHandler;
    private FloatBuffer touchBuffer;
    
    
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
		
		touchBuffer = ByteBuffer.allocateDirect(touch.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer(); //to je za preverit
		touchBuffer.put(touch).position(0);
    	
    	final int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
    	final int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
    	
    	mProgram = GLES20.glCreateProgram();
    	GLES20.glAttachShader(mProgram, vertexShader);
    	GLES20.glAttachShader(mProgram, fragmentShader);
    	//SE BIND ATTRIBUTE LOCATION
    	GLES20.glLinkProgram(mProgram);
    	
    	textureList[0] = loadTexture(GLWallpaperService.context, image); //ta image je iz metode, ne od razreda
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "uTexture");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoordinate");
        mTimeHandler = GLES20.glGetUniformLocation(mProgram, "mTime");
        mOffXHandler = GLES20.glGetUniformLocation(mProgram, "offX");
        mOffYHandler = GLES20.glGetUniformLocation(mProgram, "offY");
        mSizeHandler = GLES20.glGetUniformLocation(mProgram, "size");
        mTouchHandler = GLES20.glGetUniformLocation(mProgram, "touchEvent");

        		
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
    		textureList[i] = loadTexture(GLWallpaperService.context,image[i]);
    	}
    	//mTextureDataHandle = loadTexture(GameView.context, image);
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "uTexture");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoordinate");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE);
    }
    
    public static void TouchedEvent(float x, float y){
    	waveSize = 0.008f;
    	offX = (240-x)/240;
    	offY = (400-y)/400;
    	
    	float minSize = Float.MAX_VALUE;
    	int indexMinSize = 0;
    	boolean inserted = false;
    	Log.d("touchsize","i: "+"clear");
    	for(int i=0;i<12;i+=4){
    		if(touch[i+2] <= 0.0){
	    		touch[i] = offX;
	    		touch[i+1] = offY;
	    		touch[i+2] = waveSize;
	    		touch[i+3] = 0f;
    			inserted = true;
    			break;
    		}else{
    			if(touch[i+2] < minSize){
    				minSize = touch[i+2];
    				indexMinSize = i;
    			}
    		}
    	}
    	
    	if(!inserted){//ce nismo ustavli, zamenjamo najmanjsi val z novim
    		touch[indexMinSize] = offX;
    		touch[indexMinSize+1] = offY;
    		touch[indexMinSize+2] = waveSize;
    		touch[indexMinSize+3] = 0f;
    	}
    	
    	Log.d("touch","x: "+touch[0]+touch[1]+touch[2]);
    }
    
    public void draw(float[] mVPMatrix){
    	if(System.currentTimeMillis() - time >= 30){
    		time = System.currentTimeMillis();
    		mTime += 0.1;
    		for(int i=0; i < 12;i+=4){
    			if(touch[i+2] > 0){
    				touch[i+2] -= 0.0001f;
    				touch[i+3] += 0.035;
        			if(touch[i+2] <= 0){
        				touch[i+2] = 0;
        			}
    			}
    		}
    	}
    	
    	GLES20.glUseProgram(mProgram);
    	           
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureList[textureImage]);
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        
        GLES20.glUniform1f(mTimeHandler, mTime);
        GLES20.glUniform1f(mOffXHandler, offX);
        GLES20.glUniform1f(mOffYHandler, offY);
        GLES20.glUniform1f(mSizeHandler, waveSize);
        GLES20.glUniform4fv(mTouchHandler, touch.length, touch, 0);
    	
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
    	
    	
    	Log.d("count: ","drawl: "+drawOrder.length);
    	Log.d("order","order: "+drawOrder);
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
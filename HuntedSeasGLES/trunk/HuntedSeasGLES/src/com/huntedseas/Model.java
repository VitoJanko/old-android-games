package com.huntedseas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Model {
	private int textureList[];
	private int model;
	
	private int animationDirection = 1;
	protected int textureImage = 0;
	protected float offsetX = 0;
	protected float offsetY = 0;
	protected long time = 0;
	protected float angle = 0;
	
	private Vector<Float> coord = new Vector<Float>();
	private Vector<Float> tex = new Vector<Float>();
	private Vector<Short> drawOrder = new Vector<Short>();
	private float coordinates[];
	private float cubeTextureCoordinateData[];
	private short order[];
	
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
    
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    
    
    private final FloatBuffer mCubeTextureCoordinates;
    private int mTextureUniformHandle;
    private int mTextureCoordinateHandle;
    private final int mTextureCoordinateDataSize = 2;
        
    public Model(int image,int model ){
    	textureList = new int[1];
    	this.model = model;
    	loadModel();
    	
    	coordinates = new float[coord.size()]; //to zna bit da se da se kaj zooptimizirat
    	for(int i=0;i<coord.size();i++){
    		coordinates[i] = coord.get(i);
    	}
    	
    	cubeTextureCoordinateData = new float[tex.size()];
    	for(int i=0;i<tex.size();i++){
    		cubeTextureCoordinateData[i] = tex.get(i);
    	}
    	
    	order = new short[drawOrder.size()];
    	for(int i=0;i<drawOrder.size();i++){
    		order[i] = drawOrder.get(i);
    	}
    	
    	
    	vertexBuffer = ByteBuffer.allocateDirect(coordinates.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    	vertexBuffer.put(coordinates).position(0);
    	
    	drawListBuffer = ByteBuffer.allocateDirect(order.length*2).order(ByteOrder.nativeOrder()).asShortBuffer();
    	drawListBuffer.put(order).position(0);
    	    	
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
    
    /*
    public Model(int[] image,float[] coordinatesF){
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
    */
    
    public void draw(float[] mVPMatrix){   	
    	GLES20.glUseProgram(mProgram);
        
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureList[textureImage]);
        GLES20.glUniform1i(mTextureUniformHandle, 0);
    	
    	mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
    	GLES20.glEnableVertexAttribArray(mPositionHandle);
    	GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, vertexBuffer);
    	    	
    	//mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
    	//GLES20.glEnableVertexAttribArray(mColorHandle);
    	//GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4*4, colorBuffer);
    	  	
    	//texture
        mCubeTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
        		0, mCubeTextureCoordinates);
        
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
    	
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    	GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mVPMatrix, 0);
    	
    	GLES20.glDrawElements(GLES20.GL_TRIANGLES, order.length, GLES20.GL_UNSIGNED_SHORT,drawListBuffer);
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
    
    private void loadModel()
    {
        InputStream inputStream = GameView.context.getResources().openRawResource(model);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        try {
			makeObject(in);
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    private void makeObject(BufferedReader in) throws IOException{
    	String line;
    	while ((line = in.readLine()) != null)
    	    {
    	        if (line.startsWith("f")){//faces
    	            processFLine(line);
    	        } else if (line.startsWith("vn")){//vertex normals (to bomo rabli ce bomo s svetlobo se igrali)
    	            //processVNLine(line);
    	        } else if (line.startsWith("vt")){//textures
    	            processVTLine(line);
    	        } else if (line.startsWith("v")){//vertex
    	            processVLine(line);
    	        }
    	    }
    }
    
    private void processVLine(String line){
	    String[] tokens = line.split("[ ]+");
	    int c = tokens.length;
	    for (int i = 1; i < c; i++){
	        coord.add(Float.valueOf(tokens[i]));
	    }
    }
    
    private void processVTLine(String line){
	    String[] tokens = line.split("[ ]+");
	    int c = tokens.length;
	    for (int i = 1; i < c; i++){
	    	if(i==1){
	    		tex.add(Float.valueOf(tokens[i]));
	    	}else{
	    		tex.add(1-Float.valueOf(tokens[i]));
	    	}
	    	
	    }
    }
    
    private void processFLine(String line){
	    String[] tokens = line.split("[ ]+");
	    int c = tokens.length;
	    //razlicni primeri facov, to se lahko potem na koncu se malo zbrise, ce se nam bo slo ka kb, v koncni velicini aplikacije 
	    if (tokens[1].matches("[0-9]+")){
	        //caseFEqOne(tokens, c);
	    }
	    if (tokens[1].matches("[0-9]+/[0-9]+")){
	        //caseFEqTwo(tokens, c);
	    }
	    if (tokens[1].matches("[0-9]+//[0-9]+")){
	        //caseFEqOneAndThree(tokens, c);
	    }
	    if (tokens[1].matches("[0-9]+/[0-9]+/[0-9]+")){ //ta je edini format, ki se trenutno uporablja
	        caseFEqThree(tokens, c);
	    }
    }
    
    private void caseFEqThree(String[] tokens, int c){
	    for (int i = 1; i < c; i++){
	        Short s = Short.valueOf(tokens[i].split("/")[0]);
	        s--;
	        drawOrder.add(s);
	        
	        //texture imajo itak enak vrstni red, kot vertexi tako da ni treba se enkrat jih pisat
	        //s = Short.valueOf(tokens[i].split("/")[1]);
	        //s--;
	        //_vtPointer.add(s);
	    }
	}
}
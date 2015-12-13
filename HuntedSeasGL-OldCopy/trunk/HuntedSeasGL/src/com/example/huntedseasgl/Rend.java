package com.example.huntedseasgl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;

public class Rend implements Renderer {

	Sea host;

	protected float viewX = 0;
	protected float viewY = 0;
	
	protected float offsetX = 0;
	protected float offsetY = 0;
	//private float viewZ = 0;
	
	private float cameraAway = -1000;
	
	private float[] mProjectionMatrix = new float[16]; //Projekcijska matrika
	private float[] mVMatrix = new float[16];
	float[] mMVPMatrix = new float[16];
	
	private Vector<Float> coord; 
	private Vector<Float> tex; 
	private Vector<Short> drawOrder;
	
	int programHandle;
	int programHandleRipple;
	
	long past;
	long timePassed;
	
	protected float mTime =0;
	protected long timeRipple = 0; //Jani implementacija 
	
	
	Rend(Sea host){
		this.host=host;
	}
		
	public void onDrawFrame(GL10 arg0) {
		if(!host.maker.loaded)
			host.maker.load();
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		long current=System.currentTimeMillis();
		timePassed = (current-past);
		if(System.currentTimeMillis() - timeRipple >= 30){
    		timeRipple = System.currentTimeMillis();
    		mTime += 0.1;
		}
		
		past = current;
		//System.out.println(timePassed);
		Matrix.setLookAtM(mVMatrix, 0, viewY, viewX, cameraAway, viewY, viewX, 0, 0.0f, 1.0f, 0.0f); //set view
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mVMatrix, 0); //calculate view transformation
		host.bg.draw(programHandleRipple);
		for(int i=0; i<host.instances.size(); i++){ 
			host.instances.get(i).draw();
		}
		
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
        
        float ratio = (float) width/height;
        float near = 1.0f;
        float far = 1500.0f;
        float fov = 80;
        float top = (float) (Math.tan(fov * Math.PI / 360.0f) * near);
        float bottom = -top;
        float left = ratio * bottom;
        float right = ratio * top;
 
        offsetY = (float) (Math.tan(Math.PI/180*fov/2) * Math.abs(cameraAway));
        offsetX = offsetY * ratio;
        
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

	    //GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
	    //GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	    
	    //GLES20.glDisable(GLES20.GL_CULL_FACE); //No culling of back faces  \\ To nevem al je treba da je uklopljeno al ne
	    //GLES20.glDisable(GLES20.GL_DEPTH_TEST); //No depth testing   \\  --||--
	    //GLES20.glEnable(GLES20.GL_BLEND); //Blending
	    //GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA); //Interpolated blending
	    	    
	    final int vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, 
				readTextFile(host.context,R.raw.vertex));
		final int fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, 
				readTextFile(host.context,R.raw.frament));		
		
		programHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"a_Position",  "a_Color", "a_Normal"});		
		
		final int vertexShaderHandleRipple = compileShader(GLES20.GL_VERTEX_SHADER, 
				readTextFile(host.context,R.raw.ripple_vertex2));
		final int fragmentShaderHandleRipple = compileShader(GLES20.GL_FRAGMENT_SHADER, 
				readTextFile(host.context,R.raw.ripple_fragment2));		
		
		programHandleRipple = createAndLinkProgram(vertexShaderHandleRipple, fragmentShaderHandleRipple, 
				new String[] {"a_Position",  "a_Color", "a_Normal"});		
		

	}
	
	void loadModel(int model, LoadState ins)
    {
		coord = new Vector<Float>();
		tex = new Vector<Float>();
		drawOrder = new Vector<Short>();
		
        InputStream inputStream = host.context.getResources().openRawResource(model);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        try {
			makeObject(in);
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        ins.coordinates = new float[coord.size()]; //to zna bit da se da se kaj zooptimizirat
    	for(int i=0;i<coord.size();i++){
    		ins.coordinates[i] = coord.get(i);
    	}
    	
    	ins.cubeTextureCoordinateData = new float[tex.size()];
    	for(int i=0;i<tex.size();i++){
    		ins.cubeTextureCoordinateData[i] = tex.get(i);
    	}
    	
    	ins.order = new short[drawOrder.size()];
    	for(int i=0;i<drawOrder.size();i++){
    		ins.order[i] = drawOrder.get(i);
    	}
    }
    
    void makeObject(BufferedReader in) throws IOException{
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
	    }
	}
	
	public int loadTexture(final Context context, final int resourceId)
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
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
     
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
	
	
	private int compileShader(final int shaderType, final String shaderSource) 
	{
		int shaderHandle = GLES20.glCreateShader(shaderType);

		if (shaderHandle != 0) {
			GLES20.glShaderSource(shaderHandle, shaderSource);
			GLES20.glCompileShader(shaderHandle);

			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
			if (compileStatus[0] == 0) {
				GLES20.glDeleteShader(shaderHandle);
				shaderHandle = 0;
			}
		}

		if (shaderHandle == 0){			
			throw new RuntimeException("Error creating shader.");
		}
		
		return shaderHandle;
	}	
	

	private int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes) 
	{
		int programHandle = GLES20.glCreateProgram();
		
		if (programHandle != 0) {
			GLES20.glAttachShader(programHandle, vertexShaderHandle);			
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);
			if (attributes != null){
				final int size = attributes.length;
				for (int i = 0; i < size; i++)
				{
					GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
				}						
			}
		
			GLES20.glLinkProgram(programHandle);
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
			if (linkStatus[0] == 0) {				
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}
		if (programHandle == 0){
			throw new RuntimeException("Error creating program.");
		}
		
		return programHandle;
	}
	
	public static String readTextFile(final Context context,
            final int resourceId)
    {
        final InputStream inputStream = context.getResources().openRawResource(
                resourceId);
        final InputStreamReader inputStreamReader = new InputStreamReader(
                inputStream);
        final BufferedReader bufferedReader = new BufferedReader(
                inputStreamReader);
 
        String nextLine;
        final StringBuilder body = new StringBuilder();
 
        try
        {
            while ((nextLine = bufferedReader.readLine()) != null)
            {
                body.append(nextLine);
                body.append('\n');
            }
        }
        catch (IOException e)
        {
            return null;
        }
 
        return body.toString();
    }
	
}
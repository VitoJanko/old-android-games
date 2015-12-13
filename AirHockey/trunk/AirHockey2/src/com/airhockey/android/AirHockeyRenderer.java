package com.airhockey.android;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.airhockey.androd.util.LoggerConfig;
import com.airhockey.androd.util.ShaderHelper;
import com.airhockey.androd.util.TextResourceReader;

public class AirHockeyRenderer implements Renderer{
	private static final int POSITION_COMPONENT_COUNT = 2;
	private static final int BYTES_PER_FLOAT = 4; // float=32bits :-> 4 bytes in float
	private final FloatBuffer vertexData;
	
	private final Context context;
	
	private int program;
	
	private static final String A_POSITION = "a_Position";
	private int aPositionLocation;
	
	private static final String A_COLOR = "a_Color";
	private static final int COLOR_COMPONENT_COUNT = 3;
	/**
	 * Because now we have coordinate and color in same array, we need to tell OpenGL how much he has to skip when
	 * drawing points (positions) to get to next position. This basically tells distance between each position or each color.
	 * 
	 * !!IMPORTANT it needs to be given in BYTES!!!
	 */
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
	
	private int aColorLocation;
	
	public AirHockeyRenderer(Context context){		
		this.context = context;
		
		float[] tableVertices = {
				// Order of coordinates: X, Y, R, G, B
				
				// Triangle Fan
				0f, 0f, 1f, 1f, 1f,
				-0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
				0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
				0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
				-0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
				-0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
				
				// Line 1
				-0.5f, 0f, 1f, 0f, 0f,
				0.5f, 0f, 1f, 0f, 0f,
				
				// Mallets
				0f, -0.25f, 0f, 0f, 1f,
				0f, 0.25f, 1f, 0f, 0f
		};
		
		vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT) //allocate block of native memory (not managed by GC)
				.order(ByteOrder.nativeOrder())// bigEnd/litleEnd ... use native one
				.asFloatBuffer();// work with Float instead of 4*bytes
		
		vertexData.put(tableVertices); // put data to buffer
	}
	
	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
		String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);
		
		int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
		
		program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
		
		if(LoggerConfig.ON)
			ShaderHelper.validateProgram(program);
		
		glUseProgram(program); //tell openGL to use this program when drawing anything
	
		aColorLocation = glGetAttribLocation(program, A_COLOR); // get location of uniform this is automatic and can't be assigned manually
		aPositionLocation = glGetAttribLocation(program, A_POSITION); // get location of where attribute is stored, this can be assigned manually
	
		vertexData.position(0);//set "reader" at starting point
		
		/**
		 * Tell OpenGL that it can find data for aPosition in buffer vertexData
		 * 
		 * int index: attribute location (on OpenGL memory?)
		 * int size: how many components are associated with each vertex for this attribute (x and y coordinate) max 4 components, defined in vertex shader vec4, default (missing) 0,0,0,1
		 * int type: type of data, here is list of floating points so GL_FLOAT
		 * boolean normalized: only if using integer data
		 * int stride: when we store mroe than one attribute in single array
		 * Buffer ptr: Tells OpenGL where to read the data.
		 */
		glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
		glEnableVertexAttribArray(aPositionLocation); // with this openGL knows where to find all the data it needs
		
		/**
		 * Color starts at second position so we have to offset it.
		 * 
		 * x,y,r,g,b
		 */
		vertexData.position(POSITION_COMPONENT_COUNT);
		glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
		glEnableVertexAttribArray(aColorLocation);
	}
	
	
	@Override
	public void onDrawFrame(GL10 glUnused) {
		glClear(GL_COLOR_BUFFER_BIT);
		
		/**
		 * Draws hockey-table on screen. 
		 * 
		 * First argument tells OpenGL we want to draw Triangles
		 * Second says start reading at the beginning of vertex array.
		 * Third says: read 6 vertices. 
		 * 
		 * Comment: Since there are 3 vertices per triangle this will draw first 2 triangles (and won't draw lines and dots).
		 */
		glDrawArrays(GL_TRIANGLE_FAN, 0 , 6);
		
		/**
		 * Draws lines, starts at vertice 6 and ready 2 vertices -> 1 line.
		 */
		glDrawArrays(GL_LINES, 6, 2);
		
		glDrawArrays(GL_POINTS, 8, 1);
		
		glDrawArrays(GL_POINTS, 9, 1);
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		glViewport(0, 0, width, height);	
	}

}

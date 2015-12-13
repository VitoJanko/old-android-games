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
	
	private static final String U_COLOR = "u_Color";
	private int uColorLocation;
	private static final String A_POSITION = "a_Position";
	private int aPositionLocation;
	
	public AirHockeyRenderer(Context context){		
		this.context = context;
		
		float[] tableVertices = {
				// Triangle 1
				-0.5f, -0.5f,
				0.5f, 0.5f,
				-0.5f, 0.5f,
				
				// Triangle 2
				-0.5f, -0.5f,
				0.5f, -0.5f,
				0.5f, 0.5f,
				
				//Middle line
				-0.5f, 0f,
				0.5f, 0f,
				
				// Points
				0f, -0.25f,
				0f, 0.25f
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
	
		uColorLocation = glGetUniformLocation(program, U_COLOR); // get location of uniform this is automatic and can't be assigned manually
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
		glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
		glEnableVertexAttribArray(aPositionLocation); // with this openGL knows where to find all the data it needs
	}
	
	
	@Override
	public void onDrawFrame(GL10 glUnused) {
		glClear(GL_COLOR_BUFFER_BIT);
		
		glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);//update value of u_Color
		/**
		 * Draws hockey-table on screen. 
		 * 
		 * First argument tells OpenGL we want to draw Triangles
		 * Second says start reading at the beginning of vertex array.
		 * Third says: read 6 vertices. 
		 * 
		 * Comment: Since there are 3 vertices per triangle this will draw first 2 triangles (and won't draw lines and dots).
		 */
		glDrawArrays(GL_TRIANGLES, 0 , 6);
		
		glUniform4f(uColorLocation, 1.0f, 0.0f,0.0f, 1.0f); //red color
		/**
		 * Draws lines, starts at vertice 6 and ready 2 vertices -> 1 line.
		 */
		glDrawArrays(GL_LINES, 6, 2);
		
		glUniform4f(uColorLocation,0.0f,0.0f,1.0f,1.0f);
		glDrawArrays(GL_POINTS, 8, 1);
		
		glUniform4f(uColorLocation, 1.0f,0.0f,0.0f,1.0f);
		glDrawArrays(GL_POINTS, 9, 1);
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		glViewport(0, 0, width, height);	
	}

}

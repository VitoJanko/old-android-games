package com.hunted_seas.game.util;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;
import static com.hunted_seas.debugging.Logger.logShaderHelper;
import android.util.Log;

/**
 * Compiles and links shaders.
 * 
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class ShaderHelper {

	public static int compileVertexShader(String shaderCode){
		return compileShader(GL_VERTEX_SHADER, shaderCode);
	}
	
	public static int compileFragmentShader(String shaderCode){
		return compileShader(GL_FRAGMENT_SHADER,shaderCode);
	}
	
	/**
	 * 
	 * Creates new shaderID, compiles and attaches shader program to it.
	 * 
	 * @param type of shader (vertex/fragment)
	 * @param shaderCode String code of shader
	 * 
	 * @return shaderObjectId or 0 if shader could not be loaded.
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	private static int compileShader(int type,String shaderCode){
		final int shaderObjId = glCreateShader(type);
		
		if(shaderObjId == 0){
			logShaderHelper(Log.WARN, "Could not create new shader!");
			return 0;
		}
		
		glShaderSource(shaderObjId, shaderCode);
		glCompileShader(shaderObjId);
		
		final int[] compileStatus = new int[1];
		/**
		 * Tells openGL to read status associated with this object.
		 */
		glGetShaderiv(shaderObjId, GL_COMPILE_STATUS, compileStatus, 0);
		
		logShaderHelper(Log.INFO,"Result of compiling source: \n"+shaderCode+"\n"+glGetShaderInfoLog(shaderObjId));
		
		if(compileStatus[0] == 0){
			glDeleteShader(shaderObjId);
			logShaderHelper(Log.WARN, "Compilation of shader failed.");
			return 0;
		}
		
		return shaderObjId;
	}
	
	
	/**
	 * Links vertex and fragment shader and returns ID of program.
	 * 
	 * @param vertexShaderId ID
	 * @param fragmentShaderId ID
	 * @return programID
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public static int linkShaderPrograms(int vertexShaderId, int fragmentShaderId){
		final int programObjId = glCreateProgram();
		
		if(programObjId == 0){
			logShaderHelper(Log.WARN, "Could not create new program");
			return 0;
		}
		
		glAttachShader(programObjId, vertexShaderId);
		glAttachShader(programObjId, fragmentShaderId);
		
		glLinkProgram(programObjId);
		
		final int[] linkStatus = new int[1];
		/**
		 * Tells openGL to read status associated with this object.
		 */
		glGetProgramiv(programObjId,GL_LINK_STATUS,linkStatus,0);
		
		logShaderHelper(Log.INFO, "Results of linking program:\n" + glGetProgramInfoLog(programObjId));
	
		if(linkStatus[0] == 0){
			glDeleteProgram(programObjId);
			logShaderHelper(Log.WARN, "Linking of program failed.");
			
			return 0;
		}
		
		return programObjId;
	}
	
	/**
	 * Validates program if it's correctly loaded, compiled, if it runs
	 * slowly or something similar.
	 * 
	 * @param programObjId program ID
	 * @return true if validation is successful, false otherwise.
	 */
	public static boolean validateProgram(int programObjId){
		glValidateProgram(programObjId);
		
		final int[] validateStatus = new int[1];
		
		glGetProgramiv(programObjId, GL_VALIDATE_STATUS, validateStatus,0);

		logShaderHelper(Log.INFO, "Results of validating program: "+validateStatus[0]
				+ "|-> " + glGetProgramInfoLog(programObjId));
		
		return validateStatus[0] != 0;
	}
	
	
	/**
	 * Helper to build program based on shader resources.
	 * 
	 * @param vertexShaderSource
	 * @param fragmentShaderSource
	 * @return programId
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
		int program;
		
		int vertexShader = compileVertexShader(vertexShaderSource);
		int fragmentShader = compileFragmentShader(fragmentShaderSource);
		
		program = linkShaderPrograms(vertexShader, fragmentShader);
		
		validateProgram(program);
		
		return program;
	}
}

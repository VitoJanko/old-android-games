package com.airhockey.androd.util;

import static android.opengl.GLES20.*;
import android.util.Log;

public class ShaderHelper {
	private static final String TAG = "ShaderHelper";
	
	public static int compileVertexShader(String shaderCode){
		return compileShader(GL_VERTEX_SHADER,shaderCode);
	}
	
	public static int compileFragmentShader(String shaderCode){
		return compileShader(GL_FRAGMENT_SHADER, shaderCode);
	}
	
	private static int compileShader(int type, String shaderCode){
		final int shaderObjectId = glCreateShader(type); //returns ID of this object on GPU
														//whenever we want to reference this object we use this ID
														// if 0, object wasn't created
		
		if(shaderObjectId == 0){
			if(LoggerConfig.ON){
				Log.w(TAG,"Could not create new shader.");
			}
			
			return 0;
		}
		
		glShaderSource(shaderObjectId, shaderCode);
		glCompileShader(shaderObjectId);//compiles shader on openGL to shader object
		
		final int[] compileStatus = new int[1];
		glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);//tells openGL to read compile status associated with shaderObjectId and write it to
																			//0th element in compileStatus
		
		if(LoggerConfig.ON){
			Log.v(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:"
					+glGetShaderInfoLog(shaderObjectId));
		}
		
		if(compileStatus[0] == 0){
			//if failed
			glDeleteShader(shaderObjectId);
			
			if(LoggerConfig.ON){
				Log.w(TAG,"Compilation of shader failed.");
			}
			
			return 0;
		}
		
		return shaderObjectId;
	}
	
	public static int linkProgram(int vertexShaderId, int fragmentShaderId){
		final int programObjectId = glCreateProgram();//creates new program id on openGL
		
		if(programObjectId == 0){
			if(LoggerConfig.ON)
				Log.w(TAG,"Could not create new program");
			
			return 0;
		}
		
		//we attach both shaders to program object
		glAttachShader(programObjectId, vertexShaderId);
		glAttachShader(programObjectId, fragmentShaderId);
		
		
		glLinkProgram(programObjectId); //links shaders in program together
		
		final int[] linkStatus = new int[1];
		glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus,0);
		
		if(LoggerConfig.ON){
			Log.v(TAG, "Results of linking program:\n" + glGetProgramInfoLog(programObjectId));
		}
		
		if(linkStatus[0] == 0){
			//if failed, delete object
			glDeleteProgram(programObjectId);
			
			if(LoggerConfig.ON){
				Log.w(TAG,"Linking of program failed.");
			}
			
			return 0;
		}
		
		return programObjectId;
	}
	
	/**
	 * Validates program if it's correctly loaded, compiled,
	 *  if it runs slowly or something like that.
	 * @param programObjectId
	 * @return
	 */
	public static boolean validateProgram(int programObjectId){
		glValidateProgram(programObjectId);
		
		final int[] validateStatus = new int[1];
		
		glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
		
		Log.v(TAG,"Results of validating program: "+validateStatus[0]
				+ "|nLog:" + glGetProgramInfoLog(programObjectId));
		
		
		return validateStatus[0] != 0;
	}
}

package com.hunted_seas.game.programs;

import static android.opengl.GLES20.glUseProgram;
import static com.hunted_seas.game.util.FileReader.readTextFromRaw;
import android.content.Context;

import com.hunted_seas.game.util.ShaderHelper;

/**
 * All shader progarms are suppose to extend this class.
 * It reads shaders from files and links them together.
 * 
 * <br />
 * It also holds variables names that are used in shaders.
 * 
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class SimpleShaderProgram {
	// Uniform constants
	protected static final String U_MATRIX = "u_Matrix";
	protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
	protected static final String U_COLOR = "u_Color";
	protected static final String U_MODEL_MATRIX = "u_ModelMatrix";
	protected static final String U_LIGHT_POSITION_VECTOR = "u_LightPositionVector";
	protected static final String U_ALPHA = "u_Alpha";
	
	// Attribute constants
	protected static final String A_POSITION = "a_Position";
	protected static final String A_COLOR = "a_Color";
	protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
	
	
	protected final int program;
	
	public SimpleShaderProgram(Context context, int vertexShader, int fragmentShader){
		program = ShaderHelper.buildProgram(readTextFromRaw(context, vertexShader), readTextFromRaw(context, fragmentShader));
	}
	
	public void useProgram(){
		glUseProgram(program);
	}
	
	public int getProgram(){
		return program;
	}
}

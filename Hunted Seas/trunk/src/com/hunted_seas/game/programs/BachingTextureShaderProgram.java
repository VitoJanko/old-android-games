package com.hunted_seas.game.programs;

import static android.opengl.GLES20.*;

import com.hunted_seas.game.R;

import android.content.Context;
import android.util.Log;

/**
 * Create program based on bached texture shader.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class BachingTextureShaderProgram extends SimpleShaderProgram implements ShaderProgramInterface {
	private final String A_MVP_INDEX = "a_mvpMatrixIndex";
	
	private final int uMatrixLocation;
	private final int uTextureUnitLocation;
	private final int uModelMatrixLocation;
	
	private final int uLightPositionVectorLocation;
	
	private final int aPositionLocation;
	private final int aTextureCoordinatesLocation;
	private final int aMVPIndexLocation;
	
	public BachingTextureShaderProgram(Context context, int fragment_shader) {
		super(context, R.raw.texture_optimized_vertex_shader, fragment_shader); //R.raw.texture_simple_fragment_shader
		
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
		
		uModelMatrixLocation = glGetUniformLocation(program, U_MODEL_MATRIX);
		uLightPositionVectorLocation = glGetUniformLocation(program, U_LIGHT_POSITION_VECTOR);
		
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
		aMVPIndexLocation = glGetAttribLocation(program, A_MVP_INDEX);
		
		Log.d("Locations","loc: inprogram "+program +"   " +aPositionLocation);
	}
	
	@Deprecated
	public void setUniforms(float[] matrix, int textureId){
		//Pass matrix into shader.
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
		
		//Set active texture unit. (Tell OpenGL which texture we are using)
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureId);
		//Tells OpenGL to use this texture in shader.
		glUniform1i(uTextureUnitLocation, 0);
	}
	
	/**
	 * Bind texture.
	 * 
	 * @param textureId
	 */
	public void bindTexture(int textureId){
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureId);
		glUniform1i(uTextureUnitLocation,0);
	}
	
	public int getLightPositionVectorLocation(){
		return uLightPositionVectorLocation;
	}
	
	public int getModelMatrixUniformLocation(){
		return uModelMatrixLocation;
	}
	
	public int getMatrixUniformLocation(){
		return uMatrixLocation;
	}
	
	public int getPositionAttributeLocation(){
		return aPositionLocation;
	}
	
	public int getTextureCoordinatesAttributeLocation(){
		return aTextureCoordinatesLocation;
	}
	
	public int getBachingIndexLocation(){
		return aMVPIndexLocation;
	}
}

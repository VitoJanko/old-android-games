package com.hunted_seas.game.programs;

import static android.opengl.GLES20.*;

import com.hunted_seas.game.R;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Program for shaders with textures.
 * 
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class TextureShaderProgram extends SimpleShaderProgram implements ShaderProgramInterface{
	private final int uMatrixLocation;
	private final int uTextureUnitLocation;
	
	private final int uModelMatrixLocation;
	
	private final int aPositionLocation;
	private final int aTextureCoordinatesLocation;
	
	private final int uLightPositionVector;
	
	private final int uAlphaLocation;
	
	public TextureShaderProgram(Context context, int fragment_shader) {
		//TODO zamenji to
		super(context, R.raw.texture_simple_vertex_shader, fragment_shader); //R.raw.texture_simple_fragment_shader
		
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
		uModelMatrixLocation = glGetUniformLocation(program, U_MODEL_MATRIX);
		
		uLightPositionVector = glGetUniformLocation(program, U_LIGHT_POSITION_VECTOR);
		
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
		
		uAlphaLocation = glGetUniformLocation(program, U_ALPHA);
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
	
	public int getLightPositionVector(){
		return uLightPositionVector;
	}
	
	public int getAlphaPosition(){
		return uAlphaLocation;
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
	
	public void bindTexture(int textureId){
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureId);
		glUniform1i(uTextureUnitLocation, 0);
	}
	
	/**
	 * This is something else, it doesn't work this way.
	 */
//	public void bindMultipleTextures(int[] textureIDs){
//		for(int i=0; i < textureIDs.length;i++){
//			glActiveTexture(getTextureConstant(i));
//			glBindTexture(GL_TEXTURE_2D, i);
//		}
//		
//		glUniform1i(uTextureUnitLocation, 0);
//	}
//	
//	public void setUniformTexture(int x){
//		glUniform1i(uTextureUnitLocation, GL_TEXTURE0);
//	}
//	
//	private int getTextureConstant(int i){
//		switch(i){
//			case 0:
//				return GL_TEXTURE0; 
//			case 1:
//				return GL_TEXTURE1;
//			case 2:
//				return GL_TEXTURE2;
//			case 3:
//				return GL_TEXTURE3;
//			case 4:
//				return GL_TEXTURE4;
//			case 5:
//				return GL_TEXTURE5;
//			case 6:
//				return GL_TEXTURE6;
//			case 7:
//				return GL_TEXTURE7;
//			default:
//				return GL_TEXTURE8;
//		}
//	}
}

package com.hunted_seas.game.programs;

import com.hunted_seas.game.R;

import android.content.Context;

import static android.opengl.GLES20.*;

/**
 * Creates program with color shader.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class ColorShaderProgram extends SimpleShaderProgram{
	
	private final int uMatrixLocation;
	private final int aPositionLocation;
	private final int aColorLocation;
	private final int aIndexLocation;
	
	final String A_INDEX = "a_mvpMatrixIndex";
	
	
	public ColorShaderProgram(Context context) {
		super(context, R.raw.optimized_vertex_shader, R.raw.simple_fragment_shader);
		
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		aColorLocation = glGetAttribLocation(program, A_COLOR);
		aIndexLocation = glGetAttribLocation(program, A_INDEX);
	}
	
	public void setUniformMatrix(float[] matrix){
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
	}
	
	public int getPositionAttributeLocation(){
		return aPositionLocation;
	}
	
	public int getColorAttributeLocation(){
		return aColorLocation;
	}
	
	public int getUniformMatrixLocation(){
		return uMatrixLocation;
	}
	
	public int getIndexLocation(){
		return aIndexLocation;
	}
	
}

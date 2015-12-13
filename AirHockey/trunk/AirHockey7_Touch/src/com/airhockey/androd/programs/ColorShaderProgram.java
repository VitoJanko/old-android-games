package com.airhockey.androd.programs;

import android.content.Context;

import com.airhockey.android.R;
import static android.opengl.GLES20.*;

public class ColorShaderProgram extends ShaderProgram {
	// Uniform locations
	private final int uMatrixLocation;
	
	// Attribute locations
	private final int aPositionLocation;
	private final int uColorLocation;
	
	public ColorShaderProgram(Context context) {
		super(context, R.raw.simple_vertex_shader,
		R.raw.simple_fragment_shader);
		// Retrieve uniform locations for the shader program.
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
		// Retrieve attribute locations for the shader program.
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		uColorLocation = glGetUniformLocation(program, U_COLOR);
	}
	
	public void setUniforms(float[] matrix) {
		// Pass the matrix into the shader program.
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
	}
	
	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}
	
	public void setUniforms(float[] matrix, float r, float g, float b) {
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
		glUniform4f(uColorLocation, r, g, b, 1f);
	}
}

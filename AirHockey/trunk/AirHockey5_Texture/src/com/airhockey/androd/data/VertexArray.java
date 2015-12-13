package com.airhockey.androd.data;

import static android.opengl.GLES20.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.airhockey.android.Constants;

public class VertexArray {
	private final FloatBuffer floatBuffer;
	
	
	public VertexArray(float[] vertexData){
		floatBuffer = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer()
				.put(vertexData);
	}
	
	public void setVertexAttribPointer(int dataOffset, int attibuteLocation, int componentCount, int stride){
		floatBuffer.position(dataOffset);
		glVertexAttribPointer(attibuteLocation, componentCount, GL_FLOAT, false, stride, floatBuffer);
		
		glEnableVertexAttribArray(attibuteLocation);
		
		floatBuffer.position(0);
	}
}

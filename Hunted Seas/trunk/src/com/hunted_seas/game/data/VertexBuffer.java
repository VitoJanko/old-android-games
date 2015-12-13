package com.hunted_seas.game.data;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.hunted_seas.game.world.acommon.CommonVariables.BYTES_PER_FLOAT;
import static com.hunted_seas.game.world.acommon.CommonVariables.androidVersion;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.annotation.TargetApi;
import android.os.Build;

import com.badlogic.gdx.backends.android.AndroidGL20;

/**
 * Buffer for FLOAT type. Used for various attribute variables in shaders.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class VertexBuffer {
	private final int bufferId;
	private AndroidGL20 repairedGL20;
	
	public VertexBuffer(float[] vertexData){
		//Allocate buffers
		final int buffers[] = new int[1];
		glGenBuffers(buffers.length,buffers,0);
		
		if(buffers[0] == 0){
			throw new RuntimeException("Could not create a new vertex buffer object.");
		}
		
		bufferId = buffers[0];
		
		//Bind to the buffer.
		glBindBuffer(GL_ARRAY_BUFFER, bufferId);
		
		//Transfer data to native memory
		
		FloatBuffer vertexArray = ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer()
				.put(vertexData);
		
		vertexArray.position(0);
		
		//Transfer data from native memory to the GPU buffer.
		glBufferData(GL_ARRAY_BUFFER, vertexArray.capacity() * BYTES_PER_FLOAT, vertexArray, GL_STATIC_DRAW);
	
		//IMPORTANT! Unbind from the buffer when we're done with it!
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * 
	 * @param dataOffset in number of elements
	 * @param attributeLocation
	 * @param componentCout in number of elements
	 * @param stride
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCout, int stride){
		glBindBuffer(GL_ARRAY_BUFFER, bufferId);
		
		if(androidVersion > Build.VERSION_CODES.FROYO)
			glVertexAttribPointer(attributeLocation, componentCout, GL_FLOAT, false, stride, dataOffset * BYTES_PER_FLOAT);
		else{
			if(repairedGL20 == null){
				repairedGL20 = new AndroidGL20();
			}
			
			repairedGL20.glVertexAttribPointer(attributeLocation, componentCout, GL_FLOAT, false, stride, dataOffset * BYTES_PER_FLOAT);
		}
		
		glEnableVertexAttribArray(attributeLocation);
		glBindBuffer(GL_ARRAY_BUFFER,  0);
	}
	
	/**
	 * @return buffer id
	 */
	public int getBufferId(){
		return bufferId;
	}
}

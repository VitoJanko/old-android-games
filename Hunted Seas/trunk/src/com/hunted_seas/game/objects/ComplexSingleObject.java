package com.hunted_seas.game.objects;

import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.multiplyMM;
import static com.hunted_seas.game.world.acommon.CommonVariables.BYTES_PER_FLOAT;
import static com.hunted_seas.game.world.acommon.CommonVariables.androidVersion;

import java.util.LinkedList;

import android.annotation.TargetApi;
import android.content.Context;
import android.opengl.GLES20;
import android.os.Build;

import com.badlogic.gdx.backends.android.AndroidGL20;
import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.data.IndexBuffer;
import com.hunted_seas.game.data.LineSegment;
import com.hunted_seas.game.data.MayaRawObjectData;
import com.hunted_seas.game.data.VertexBuffer;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.programs.TextureShaderProgram;
import com.hunted_seas.game.util.ObjectModelHelper;
import com.hunted_seas.game.util.TextureHelper;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Lights;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;

/**
 * This should be used for special objects like Player or boss.
 * There should be only 1 kind of this object present.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public abstract class ComplexSingleObject implements SpriteManagerInterface{
	private AndroidGL20 repairedGL20;
	
	protected LevelManager level;
	
	private float alpha = 1f;
	
	private int MVPLocation;
	private int positionLocation;
	private int textureLocation;
	
	private int[] numberOfVerticesToBeDrawn;	
	private int[] textureIDs;
	private VertexBuffer[] vertexBuffer;
	private IndexBuffer[] indexBuffer;
	
	protected LinkedList<LinkedList<LineSegment>> lineSegments;
	
	private TextureShaderProgram shaderProgram;
	
	private static final int POSITION_COMPONENT_COUNT = 3;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
	
	/**
	 * If you change this value you NEED TO CHANGE it in SHADER as well!!
	 */
	float[] MVP = new float[16];
	/**
	 *	boundingBox[0] = left;
	 *  boundingBox[1] = bottom;
	 *	boundingBox[2] = right;
	 *	boundingBox[3] = top;
	 */
	protected BoundingBox[] boundingBox;
	protected float[] offsetX;
	protected float[] offsetY;
	
	/**
	 * Model
	 */
	private final float[] modelMatrix = new float[16];
	
	protected final float[] tempModelMatrix = new float[16];
	protected float x = 0;
	protected float y = 0;
	protected float z = 0;
	protected int speed;
	protected int currentState = 0;
	
	
	protected void loadSprite(Context context, ShaderProgramInterface textureShaderProgram, String[] textureResources, int[] modelRawResources) {
		this.shaderProgram = (TextureShaderProgram) textureShaderProgram;
		
		MVPLocation = shaderProgram.getMatrixUniformLocation();
		positionLocation = shaderProgram.getPositionAttributeLocation();
		textureLocation = shaderProgram.getTextureCoordinatesAttributeLocation();
		
		textureIDs = new int[textureResources.length];
		indexBuffer = new IndexBuffer[textureResources.length];
		vertexBuffer = new VertexBuffer[textureResources.length];
		numberOfVerticesToBeDrawn = new int[textureResources.length];
		
		MayaRawObjectData data;
		float[] vertexArray;
		short[] indexArray;
		
		boundingBox = new BoundingBox[textureResources.length];
		offsetX = new float[textureResources.length];
		offsetY = new float[textureResources.length];
		lineSegments = new LinkedList<LinkedList<LineSegment>>();
		
		for(int i=0; i < textureResources.length;i++){			
			textureIDs[i] = TextureHelper.loadTexture(context, textureResources[i]);
	
			data = ObjectModelHelper.loadModel(context, modelRawResources[i]);
			vertexArray = data.getVertexDataArray();
			indexArray = data.getIndexDataArray();
			boundingBox[i] = data.getBoundingBox();
			lineSegments.addLast(data.getLineSegments());
			data = null;
		
			offsetX[i] = boundingBox[i].getWidth() / 2f;
			offsetY[i] = boundingBox[i].getHeight() / 2f;
			
			numberOfVerticesToBeDrawn[i] = indexArray.length;
			indexBuffer[i] = new IndexBuffer(indexArray);
			vertexBuffer[i] = new VertexBuffer(vertexArray);
			
			prepareShader(i);
		}
	}
	
	public abstract boolean step(float stepScale);

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public synchronized void draw(float[] viewMatrix, Lights lights) {	//TODO probably not good idea to synchronize this					
		prepareShader(currentState);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer[currentState].getBufferId());
		shaderProgram.bindTexture(textureIDs[currentState]);
		
		multiplyMM(MVP, 0, viewMatrix , 0, modelMatrix, 0);
		glUniformMatrix4fv(MVPLocation, 1, false, MVP, 0);
		glUniformMatrix4fv(shaderProgram.getModelMatrixUniformLocation(), 1, false, modelMatrix ,0);
		GLES20.glUniform4f(shaderProgram.getLightPositionVector(), lights.x , lights.y, lights.z, 0.0f);
		GLES20.glUniform1f(shaderProgram.getAlphaPosition(), alpha);
		
		
		if(androidVersion > Build.VERSION_CODES.FROYO)
			glDrawElements(GL_TRIANGLES,numberOfVerticesToBeDrawn[currentState], GL_UNSIGNED_SHORT, 0);
		else{
			if(repairedGL20 == null){
				repairedGL20 = new AndroidGL20();
			}
			repairedGL20.glDrawElements(GL_TRIANGLES,numberOfVerticesToBeDrawn[currentState], GL_UNSIGNED_SHORT, 0);
		}
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	@Override
	public abstract void pause();

	@Override
	public abstract void finish();

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	protected float[] setModelMatrix(){
		return modelMatrix;
	}
	
	public float[] getModelMatrix(){
		return modelMatrix;
	}
		
	private void prepareShader(int dataIndex){
		vertexBuffer[dataIndex].setVertexAttribPointer(0, positionLocation, POSITION_COMPONENT_COUNT, STRIDE);
		vertexBuffer[dataIndex].setVertexAttribPointer(POSITION_COMPONENT_COUNT, textureLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
	}
}

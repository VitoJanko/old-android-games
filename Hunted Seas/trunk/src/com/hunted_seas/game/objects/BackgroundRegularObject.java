package com.hunted_seas.game.objects;

import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setRotateM;
import static android.opengl.Matrix.translateM;
import static com.hunted_seas.game.world.acommon.CommonVariables.BYTES_PER_FLOAT;
import static com.hunted_seas.game.world.acommon.CommonVariables.androidVersion;

import java.util.LinkedList;
import java.util.Vector;

import android.annotation.TargetApi;
import android.content.Context;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidGL20;
import com.hunted_seas.game.collision.CollisionDetector_Manager;
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
 * This should be used for regular objects. This supports objects with multiple textures and shapes. Not recommended to use more than 100 of this object, for performance wise.
 * 
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public abstract class BackgroundRegularObject implements SpriteManagerInterface{
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
	
	protected LinkedList<LineSegment> lineSegments;
	
	private TextureShaderProgram shaderProgram;
	
	private static final int POSITION_COMPONENT_COUNT = 3;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
	
	/**
	 * If you change this value you NEED TO CHANGE it in SHADER as well!!
	 */
	float[] MVP = new float[16];
	
	protected Vector<float[]> backgroundModelMatrixes = new Vector<float[]>();
	
	protected BoundingBox[] boundingBox;
	protected float[] offsetX;
	protected float[] offsetY;
	
	protected float x = 0;
	protected float y = 0;
	protected float z = 0;
	
	public void addBackgroundObject(float x, float y, float z){
		addBackgroundObject(x, y, z,0,1);
	}
	
	protected void addBackgroundObject(float x, float y, float z, float rotation, float scale){
		this.x = x;
		this.y = y;
		this.z = z;
		
		float[] modelMatrix = new float[16];
		float[] tempModelMatrix = new float[16];
		float[] rotationModelMatrix = new float[16];
		
		setIdentityM(tempModelMatrix, 0);
		setRotateM(rotationModelMatrix, 0, rotation, 0.0f, 0.0f, -1.0f);
		translateM(tempModelMatrix, 0 , rotationModelMatrix , 0 , x , y , z );
		scaleM(modelMatrix,0,tempModelMatrix, 0, scale, scale, 0);
		
		backgroundModelMatrixes.add(modelMatrix);
	}
	
	/**
	 * 
	 * Order of texture and model array must be the same!
	 * 
	 * @param context
	 * @param textureShaderProgram !IMPORTANT! TextureShaderProgram
	 * @param textureResources array of texture files
	 * @param modelRawResources array of object files
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	protected void loadBackgroundResources(Context context, ShaderProgramInterface textureShaderProgram, String  textureResources, int modelRawResources) {
		this.shaderProgram = (TextureShaderProgram) textureShaderProgram;
		
		MVPLocation = shaderProgram.getMatrixUniformLocation();
		positionLocation = shaderProgram.getPositionAttributeLocation();
		textureLocation = shaderProgram.getTextureCoordinatesAttributeLocation();
		
		textureIDs = new int[1];
		indexBuffer = new IndexBuffer[1];
		vertexBuffer = new VertexBuffer[1];
		numberOfVerticesToBeDrawn = new int[1];
		
		MayaRawObjectData data;
		float[] vertexArray;
		short[] indexArray;
		
		offsetX = new float[1];
		offsetY = new float[1];
		
		
		textureIDs[0] = TextureHelper.loadTexture(context, textureResources);

		data = ObjectModelHelper.loadModel(context, modelRawResources,true);
		vertexArray = data.getVertexDataArray();
		indexArray = data.getIndexDataArray();
		boundingBox[0] = data.getBoundingBox();
		lineSegments = data.getLineSegments();
		data = null;
		
		offsetX[0] = boundingBox[0].getWidth() / 2f;
		offsetY[0] = boundingBox[0].getHeight() / 2f;
	
		numberOfVerticesToBeDrawn[0] = indexArray.length;
		indexBuffer[0] = new IndexBuffer(indexArray);
		vertexBuffer[0] = new VertexBuffer(vertexArray);
		
		prepareShader(0);
		
	}

	public void setUp_CollisionDetector(CollisionDetector_Manager collisionDetector){}
	
	public boolean step(float stepScale){
		return true;
	};

	/**
	 * Draws all objects. <br />
	 * Objects need to be organized in seperate arrays based on type of model and texture. This helps with optimization.
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public synchronized void draw(float[] viewMatrix, Lights lights) {	//TODO probably not good idea to synchronize this
		if(backgroundModelMatrixes.size() <= 0) return;
		
		prepareShader(0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer[0].getBufferId());
		shaderProgram.bindTexture(textureIDs[0]);
		
		for(float[] spriteModelMatirx : backgroundModelMatrixes){
			multiplyMM(MVP, 0, viewMatrix , 0, spriteModelMatirx, 0);
			glUniformMatrix4fv(MVPLocation, 1, false, MVP, 0);
			glUniformMatrix4fv(shaderProgram.getModelMatrixUniformLocation(), 1, false, spriteModelMatirx, 0);
			GLES20.glUniform4f(shaderProgram.getLightPositionVector(), lights.x, lights.y, lights.z, 0.0f);
			GLES20.glUniform1f(shaderProgram.getAlphaPosition(), alpha);
			
			
			if(androidVersion > Build.VERSION_CODES.FROYO)
				glDrawElements(GL_TRIANGLES,numberOfVerticesToBeDrawn[0], GL_UNSIGNED_SHORT, 0);
			else{
				if(repairedGL20 == null){
					repairedGL20 = new AndroidGL20();
				}
				repairedGL20.glDrawElements(GL_TRIANGLES,numberOfVerticesToBeDrawn[0], GL_UNSIGNED_SHORT, 0);
			}
		}

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	
	public void pause(){
		//TODO
	};

	
	public void finish(){
		//TODO
	};

	
	public void destroy() {
		//TODO
	}
		
	private void prepareShader(int dataIndex){
		vertexBuffer[dataIndex].setVertexAttribPointer(0, positionLocation, POSITION_COMPONENT_COUNT, STRIDE);
		vertexBuffer[dataIndex].setVertexAttribPointer(POSITION_COMPONENT_COUNT, textureLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
	}
	
	@Override
	public LevelManager getLvL(){
		return null;
	}

}

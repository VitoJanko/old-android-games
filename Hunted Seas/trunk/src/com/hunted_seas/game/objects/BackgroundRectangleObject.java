package com.hunted_seas.game.objects;
import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static com.hunted_seas.debugging.Logger.logBackgroundBox;
import static com.hunted_seas.game.world.acommon.CommonVariables.BYTES_PER_FLOAT;
import static com.hunted_seas.game.world.acommon.CommonVariables.androidVersion;
import android.annotation.TargetApi;
import android.content.Context;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidGL20;
import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.data.IndexBuffer;
import com.hunted_seas.game.data.VertexBuffer;
import com.hunted_seas.game.programs.TextureShaderProgram;
import com.hunted_seas.game.util.OnScreenOptimizer;
import com.hunted_seas.game.util.TextureHelper;
import com.hunted_seas.game.world.acommon.CommonVariables;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Lights;

/**
 * Background object, that is built out of mesh.
 * It is rectangular. Dimensions can be changed, mesh density as well.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public abstract class BackgroundRectangleObject{
	AndroidGL20 repairedGL20;

	public float alpha = 1f;
	
	private static final int POSITION_COMPONENT_COUNT = 3;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
	
	private short[] indexArray;
	private float[] vertexArray;
	
	private IndexBuffer indexBuffer;
	private VertexBuffer vertexBuffer;
	
	private int numberOfVerticesToBeDrawn;
	
	private TextureShaderProgram shaderProgram;
	
	protected LevelManager level;
	
	private int skyBoxtexture;
	
	private float[] tempTest = new float[16];
	
	/**
	 * leftX,rightX,topY,bottomY, Z
	 */
	protected BoundingBox backgroundDimensions;
	
	/**
	 * Creates background object. 
	 * 
	 * @param context 
	 * @param textureShaderProgram !IMPORTANT! TextureShaderProgram
	 * @param meshHorizontalColumns Number of horizontal columns in mesh (X)
	 * @param meshVerticalColumns Number of vertical columns in mesh (Y)
	 * @param textureResourceId
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	protected void loadObject(Context context,TextureShaderProgram textureShaderProgram,int meshHorizontalColumns, int meshVerticalColumns, String textureResourceId){
		this.shaderProgram = textureShaderProgram;
		
		skyBoxtexture = TextureHelper.loadTexture(context, textureResourceId);
		
		buildMesh(meshHorizontalColumns, meshVerticalColumns);
		
		indexBuffer = new IndexBuffer(indexArray);
		vertexBuffer = new VertexBuffer(vertexArray);
		
		numberOfVerticesToBeDrawn = indexArray.length;
		
		indexArray = null;
		vertexArray = null;
		
		bindData();
		
		setIdentityM(tempTest, 0);
		translateM(tempTest,0,tempTest,0,0.0f,0,0.0f);
	}
	
	/**
	 * Draws rectangular object.
	 * @param viewMatrix
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void draw(float[] viewMatrix, Lights lights){
		if(OnScreenOptimizer.visible(level.getCameraInfo(), new float[]{0,0,0,0}, backgroundDimensions,
				(CommonVariables.CAMERA_DEPTH - backgroundDimensions.z)/CommonVariables.CAMERA_DEPTH))
			return;
		

		
		bindData();
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.getBufferId());
		
		shaderProgram.bindTexture(skyBoxtexture);
		
		glUniformMatrix4fv(shaderProgram.getMatrixUniformLocation(), 1 , false, viewMatrix, 0);
		glUniformMatrix4fv(shaderProgram.getModelMatrixUniformLocation(), 1, false, viewMatrix ,0); //TEMP tole ni prou ker se luè premika s kamero èe tako napišeš
		GLES20.glUniform1f(shaderProgram.getAlphaPosition(), alpha);
		
		
		if(androidVersion > Build.VERSION_CODES.FROYO)
			glDrawElements(GL_TRIANGLES, numberOfVerticesToBeDrawn, GL_UNSIGNED_SHORT, 0);
		else{
			if(repairedGL20 == null){
				repairedGL20 = new AndroidGL20();
			}
			repairedGL20.glDrawElements(GL_TRIANGLES, numberOfVerticesToBeDrawn, GL_UNSIGNED_SHORT, 0);
		}
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	private void bindData(){
		vertexBuffer.setVertexAttribPointer(0, shaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
		vertexBuffer.setVertexAttribPointer(POSITION_COMPONENT_COUNT, shaderProgram.getTextureCoordinatesAttributeLocation(), TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
	}
	
	/**
	 * Creates rectangular mesh of density columns * rows.
	 * 
	 * @param numberOfColumns (x dimension)
	 * @param numberOfRows (y dimension)
	 */
	private void buildMesh(int numberOfColumns, int numberOfRows){
		float[] vertexArray = new float[numberOfColumns * numberOfRows * (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT)];
		float horizontalStep = (backgroundDimensions.getWidth())*1.0f / (numberOfColumns-1);
		float verticalStep = (backgroundDimensions.getHeight())*1.0f / (numberOfRows-1);
		
		float textureHorizontalStep = (float) (1.0 / (numberOfColumns-1));
		float textureVerticalStep = (float) (1.0 / (numberOfRows-1));
		
		logBackgroundBox(Log.DEBUG, "h: "+horizontalStep + "  :  v: "+verticalStep);
		
		int indexCounter = 0;
		for(int i=0; i < numberOfRows; i++){
			for(int j=0; j < numberOfColumns; j++){
				vertexArray[indexCounter] = backgroundDimensions.left + j * horizontalStep;
				vertexArray[indexCounter+1] = backgroundDimensions.top - i * verticalStep;
				vertexArray[indexCounter+2] = backgroundDimensions.z;
				vertexArray[indexCounter+3] = (j * textureHorizontalStep);
				vertexArray[indexCounter+4] = (i * textureVerticalStep);
				
				logBackgroundBox(Log.DEBUG, "i: "+i+"   "+vertexArray[indexCounter+3]+" : "+vertexArray[indexCounter+4]);
				
				indexCounter += 5;
			}
		}
		
		short[] gIndexArray = new short[2 * (numberOfColumns-1) * (numberOfRows-1)*3];
		
		indexCounter = 0;
		for(short i=0; i < numberOfRows -1; i++){
			for(short j=0; j < numberOfColumns -1; j++){
				gIndexArray[indexCounter] = j;
				gIndexArray[indexCounter+2] = (short) (j + 1);
				gIndexArray[indexCounter+1] = (short) (j+((i+1)*numberOfColumns));
				
				gIndexArray[indexCounter+3] = gIndexArray[indexCounter + 2];
				gIndexArray[indexCounter+4] = gIndexArray[indexCounter + 1];
				gIndexArray[indexCounter+5] = (short) (gIndexArray[indexCounter + 1] + 1);
				
				indexCounter += 6;
			}
		}
		
		indexArray = gIndexArray;
		this.vertexArray = vertexArray;
	}
	
	public abstract void finish();
	
}

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
import com.hunted_seas.game.util.OnScreenOptimizer;
import com.hunted_seas.game.util.TextureHelper;
import com.hunted_seas.game.world.acommon.CommonVariables;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Lights;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;

/**
 * This should be used for regular objects. This supports objects with multiple textures and shapes. Not recommended to use more than 100 of this object, for performance wise.
 * 
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public abstract class RegularObject implements SpriteManagerInterface{
	private AndroidGL20 repairedGL20;
	
	protected LevelManager level;
	
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
	
	protected Vector<Sprite> sprites = new Vector<Sprite>();
	private Vector<Vector<Sprite>> spriteStates = new Vector<Vector<Sprite>>();
	
	protected BoundingBox[] boundingBox;
	protected float[] offsetX;
	protected float[] offsetY;
	
	
	protected CollisionDetector_Manager collisionDetector;
	
	/**
	 * Is program currently drawing this element(s)?
	 * <p>
	 * If so, you shouldn't change (remove) number of sprites.
	 */
	private boolean drawing = false;
	
	public int[] getTextureIDs(){
		return textureIDs;
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
	public void loadSprite(Context context, ShaderProgramInterface textureShaderProgram, String[] textureResources, int[] modelRawResources, boolean center) {
		loadSprite(context, textureShaderProgram, textureResources, modelRawResources, center, false);
	}
	
	/**
	 * 
	 * This is used with atlas objects, if textures have already been loaded for this object.
	 * Texture id is provided as parameter. <p>
	 * 
	 * @param context 
	 * @param textureshaderprogram 
	 * @param textureID (loaded texture id)
	 * @param modelResourceId array of models for this sprite
	 * @param center, if model is going to be centered around 0,0, or not.
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public void loadSprite(Context context, ShaderProgramInterface textureShaderProgram, int textureResourceID, int[] modelRawResources, boolean center) {
		this.shaderProgram = (TextureShaderProgram) textureShaderProgram;
		
		MVPLocation = shaderProgram.getMatrixUniformLocation();
		positionLocation = shaderProgram.getPositionAttributeLocation();
		textureLocation = shaderProgram.getTextureCoordinatesAttributeLocation();
		
		textureIDs = new int[modelRawResources.length];
		indexBuffer = new IndexBuffer[modelRawResources.length];
		vertexBuffer = new VertexBuffer[modelRawResources.length];
		numberOfVerticesToBeDrawn = new int[modelRawResources.length];
		
		MayaRawObjectData data;
		float[] vertexArray;
		short[] indexArray;
		
		boundingBox = new BoundingBox[modelRawResources.length];
		offsetX = new float[modelRawResources.length];
		offsetY = new float[modelRawResources.length];
		lineSegments = new LinkedList<LinkedList<LineSegment>>();
		
		
		for(int i=0; i < modelRawResources.length;i++){
			spriteStates.add(new Vector<Sprite>());
			
			textureIDs[i] = textureResourceID;
			
			data = ObjectModelHelper.loadModel(context, modelRawResources[i], center);
			vertexArray = data.getVertexDataArray();
			indexArray = data.getIndexDataArray();
			boundingBox[i] = data.getBoundingBox();
			lineSegments.addLast(data.getLineSegments());
			data = null;
			
			offsetX[i] = (boundingBox[i].right - boundingBox[i].left) / 2f;
			offsetY[i] = (boundingBox[i].top - boundingBox[i].bottom) / 2f;
		
			numberOfVerticesToBeDrawn[i] = indexArray.length;
			indexBuffer[i] = new IndexBuffer(indexArray);
			vertexBuffer[i] = new VertexBuffer(vertexArray);
			
			prepareShader(i);
		}
	}
	
	/**
	 * 
	 * Order of texture and model array must be the same!
	 * 
	 * @param context
	 * @param textureShaderProgram !IMPORTANT! TextureShaderProgram
	 * @param textureResources array of texture files
	 * @param modelRawResources array of object files
	 * @param center if model is going to be centered around 0,0
	 * @param atlas, if there is ONLY ONE texture and it is going to be used as a atlas.
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public void loadSprite(Context context, ShaderProgramInterface textureShaderProgram, String[] textureResources, int[] modelRawResources, boolean center, boolean atlas) {
		this.shaderProgram = (TextureShaderProgram) textureShaderProgram;
		
		MVPLocation = shaderProgram.getMatrixUniformLocation();
		positionLocation = shaderProgram.getPositionAttributeLocation();
		textureLocation = shaderProgram.getTextureCoordinatesAttributeLocation();
		
		textureIDs = new int[modelRawResources.length];
		indexBuffer = new IndexBuffer[modelRawResources.length];
		vertexBuffer = new VertexBuffer[modelRawResources.length];
		numberOfVerticesToBeDrawn = new int[modelRawResources.length];
		
		MayaRawObjectData data;
		float[] vertexArray;
		short[] indexArray;
		
		boundingBox = new BoundingBox[modelRawResources.length];
		offsetX = new float[modelRawResources.length];
		offsetY = new float[modelRawResources.length];
		lineSegments = new LinkedList<LinkedList<LineSegment>>();
		
		
		for(int i=0; i < modelRawResources.length;i++){
			spriteStates.add(new Vector<Sprite>());
			
			if(atlas){
				if(i == 0)
					textureIDs[i] = TextureHelper.loadTexture(context, textureResources[i]);
				else
					textureIDs[i] = textureIDs[0];
			}else{
				textureIDs[i] = TextureHelper.loadTexture(context, textureResources[i]);
			}
			
			data = ObjectModelHelper.loadModel(context, modelRawResources[i], center);
			vertexArray = data.getVertexDataArray();
			indexArray = data.getIndexDataArray();
			boundingBox[i] = data.getBoundingBox();
			lineSegments.addLast(data.getLineSegments());
			data = null;
			
			offsetX[i] = (boundingBox[i].right - boundingBox[i].left) / 2f;
			offsetY[i] = (boundingBox[i].top - boundingBox[i].bottom) / 2f;
		
			numberOfVerticesToBeDrawn[i] = indexArray.length;
			indexBuffer[i] = new IndexBuffer(indexArray);
			vertexBuffer[i] = new VertexBuffer(vertexArray);
			
			prepareShader(i);
		}
	}

	public void setUp_CollisionDetector(CollisionDetector_Manager collisionDetector){
		this.collisionDetector = collisionDetector;
		collisionDetector.addColidableObjectSprite(sprites);
	}
	
	public abstract boolean step(float stepScale);

	/**
	 * Draws all objects. <br />
	 * Objects need to be organized in seperate arrays based on type of model and texture. This helps with optimization.
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public synchronized void draw(float[] viewMatrix, Lights lights) {	//TODO probably not good idea to synchronize this					
		int currentState = 0;
		syncStates();
		for(Vector<Sprite> spritesInState : spriteStates){
			if(spritesInState.size() != 0){

				
				prepareShader(currentState);
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer[currentState].getBufferId());
				shaderProgram.bindTexture(textureIDs[currentState]);
				
				for(Sprite sprite : spritesInState){
					if(!sprite.draw())
						continue;
					
					multiplyMM(MVP, 0, viewMatrix , 0, sprite.getModelMatrix(), 0);
					glUniformMatrix4fv(MVPLocation, 1, false, MVP, 0);
					GLES20.glUniform4f(shaderProgram.getLightPositionVector(), lights.x, lights.y, lights.z, 0.0f);
					GLES20.glUniform1f(shaderProgram.getAlphaPosition(), sprite.getAlpha());
					
					
					if(androidVersion > Build.VERSION_CODES.FROYO)
						glDrawElements(GL_TRIANGLES,numberOfVerticesToBeDrawn[currentState], GL_UNSIGNED_SHORT, 0);
					else{
						if(repairedGL20 == null){
							repairedGL20 = new AndroidGL20();
						}
						repairedGL20.glDrawElements(GL_TRIANGLES,numberOfVerticesToBeDrawn[currentState], GL_UNSIGNED_SHORT, 0);
					}
				}
				
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
			}
			GLES20.glUniform1f(shaderProgram.getAlphaPosition(), 1);
			currentState++;
		}
	}

	@Override
	public abstract void pause();

	@Override
	public abstract void finish();

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * TODO // optimize synchronization?
	 * 
	 * Reorganises sprites in to their Vector array, for better performance with drawing.
	 */
	private synchronized void syncStates(){
		for(Vector<Sprite> state : spriteStates){
			state.clear();
		}
		
		for(Sprite sprite : sprites){
			if(sprite.getBoundBox() != null && sprite.getPosition() != null && OnScreenOptimizer.visible(level.getCameraInfo(), sprite.getPosition(), sprite.getBoundBox()[sprite.getAnimationState()],
					(CommonVariables.CAMERA_DEPTH - sprite.getPosition()[2])/CommonVariables.CAMERA_DEPTH)){
				sprite.setVisible(false);
				continue;
			}
			
			sprite.setVisible(true);
			spriteStates.get(sprite.getAnimationState()).add(sprite);

		}
	}
	
	private void prepareShader(int dataIndex){
		vertexBuffer[dataIndex].setVertexAttribPointer(0, positionLocation, POSITION_COMPONENT_COUNT, STRIDE);
		vertexBuffer[dataIndex].setVertexAttribPointer(POSITION_COMPONENT_COUNT, textureLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
	}
	
	public synchronized boolean isDrawing(){
		return drawing;
	}
	
	private synchronized void setDrawing(){
		drawing = true;
	}
	
	private synchronized void cancelDrawing(){
		drawing = false;
	}
	
	protected synchronized void addSprite(Sprite sprite){
		sprites.add(sprite);
	}
	
	protected synchronized void removeSprite(int spriteLocation){
		sprites.remove(spriteLocation);
	}
	
	public synchronized int spritesSize(){
		return sprites.size();
	}
	
	public synchronized Sprite getSprite(int spriteLocation){
		return sprites.get(spriteLocation);
	}
	
	protected synchronized void clearSprites(){
		sprites.clear();
	}

}

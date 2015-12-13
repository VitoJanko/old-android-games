package com.hunted_seas.game.objects;

import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static com.hunted_seas.game.world.acommon.CommonVariables.BYTES_PER_FLOAT;
import static com.hunted_seas.game.world.acommon.CommonVariables.androidVersion;

import java.util.LinkedList;
import java.util.Vector;

import android.annotation.TargetApi;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;

import com.badlogic.gdx.backends.android.AndroidGL20;
import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.data.IndexBuffer;
import com.hunted_seas.game.data.LineSegment;
import com.hunted_seas.game.data.MayaRawObjectData;
import com.hunted_seas.game.data.VertexBuffer;
import com.hunted_seas.game.programs.BachingTextureShaderProgram;
import com.hunted_seas.game.programs.ShaderProgramInterface;
import com.hunted_seas.game.util.BachingHelper;
import com.hunted_seas.game.util.MatrixHelper;
import com.hunted_seas.game.util.ObjectModelHelper;
import com.hunted_seas.game.util.OnScreenOptimizer;
import com.hunted_seas.game.util.TextureHelper;
import com.hunted_seas.game.world.acommon.CommonVariables;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Lights;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;

/**
 * Similar to RegularObject. This class is meant to be used by sprites where sprites are static (don't change shape or texture) and there are a lot of them.
 * To optimize performance this should be used only when there are a lot of same sprites. It draws 16 sprites at the same time, so it is preferably (memory wise) to
 * have amount of sprites that is divided by 16.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public abstract class BachedObject implements SpriteManagerInterface{
	private AndroidGL20 repairedGL20;
	
	protected LevelManager level;
	
	private int MVPLocation;
	private int positionLocation;
	private int textureLocation;
	private int bachingIndexLocation;
	
	private BachingTextureShaderProgram shaderProgram;
	
	private static final int POSITION_COMPONENT_COUNT = 3;
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	private static final int BACH_INDEX_COMPONENT_COUNT = 1;
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT + BACH_INDEX_COMPONENT_COUNT) * BYTES_PER_FLOAT;
	
	protected CollisionDetector_Manager collisionDetector;
	
	/**
	 * Is program currently drawing this element(s)?
	 * <p>
	 * If so, you shouldn't change (remove) number of sprites.
	 */
	private boolean drawing = false;
	
	
	/**
	 * If you change this value you NEED TO CHANGE it in SHADER as well!!
	 */
	private static final int BACHED_INSTANCES = 16;
	float[] bachMVP = new float[16 * BACHED_INSTANCES];
	float[] bachMM = new float[16 * BACHED_INSTANCES];
	
	private int[] numberOfVerticesToBeDrawn;	
	private int[] textureIDs;
	private VertexBuffer[] vertexBuffer;
	private IndexBuffer[] indexBuffer;
	
	protected Vector<Sprite> sprites = new Vector<Sprite>();
	private Vector<Vector<Sprite>> spriteStates = new Vector<Vector<Sprite>>();
	
	protected LinkedList<LinkedList<LineSegment>> lineSegments;
	
	private int drawCalls = 1;
	
	protected BoundingBox[] boundingBox;
	protected float[] offsetX;
	protected float[] offsetY;
	/**
	 * Loads all essential data and creates sprite object.
	 * 
	 * @param context
	 * @param bachingShaderProgram !IMPORTANT! needs to be BachingTextureShaderProgram
	 * 
	 * @param spriteTextureResource Texture for sprite.
	 * @param spriteModelResource .ojb Maya object for sprite
	 * 
	 * @see BachingTextureShaderProgram
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 * 
	 */
	public void loadSprite(Context context, ShaderProgramInterface bachingShaderProgram, String textureResources, int modelResources) {
		loadSprite(context, bachingShaderProgram,new String[]{textureResources},new int[]{modelResources});
	}
	
	/**
	 * Loads all essential data and creates sprite object.
	 * 
	 * @param context
	 * @param bachingShaderProgram !IMPORTANT! needs to be BachingTextureShaderProgram
	 * 
	 * @param spriteTextureResource[] Texture for sprite.
	 * @param spriteModelResource[] .ojb Maya object for sprite
	 * 
	 * @see BachingTextureShaderProgram
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 * 
	 */
	
	public void loadSprite(Context context, ShaderProgramInterface bachingShaderProgram, String[] textureResources, int[] modelResources) {
		loadSprite(context, bachingShaderProgram, textureResources, modelResources, true);
	}
	
	public void loadSprite(Context context, ShaderProgramInterface bachingShaderProgram, String[] textureResources, int[] modelResources, boolean center) {
		this.shaderProgram = (BachingTextureShaderProgram) bachingShaderProgram;

		MVPLocation = shaderProgram.getMatrixUniformLocation();
		positionLocation = shaderProgram.getPositionAttributeLocation();
		textureLocation = shaderProgram.getTextureCoordinatesAttributeLocation();
		bachingIndexLocation = shaderProgram.getBachingIndexLocation();
					
		MayaRawObjectData data;
		float[] vertexArray;
		short[] indexArray;
		
		textureIDs = new int[textureResources.length];
		indexBuffer = new IndexBuffer[textureResources.length];
		vertexBuffer = new VertexBuffer[textureResources.length];
		numberOfVerticesToBeDrawn = new int[textureResources.length];
		
		boundingBox = new BoundingBox[textureResources.length];
		offsetX = new float[textureResources.length];
		offsetY = new float[textureResources.length];
		lineSegments = new LinkedList<LinkedList<LineSegment>>();
		
		
		for(int i=0; i < textureResources.length;i++){
			spriteStates.add(new Vector<Sprite>());
			
			textureIDs[i] = TextureHelper.loadTexture(context, textureResources[i]);
	
			data = ObjectModelHelper.loadBachedModel(context, modelResources[i], center);
			vertexArray = data.getVertexDataArray();
			indexArray = data.getIndexDataArray();
			boundingBox[i] = data.getBoundingBox();
			lineSegments.addLast(data.getLineSegments());
			data = null;
			
			offsetX[i] = boundingBox[i].getWidth() / 2f;
			offsetY[i] = boundingBox[i].getHeight() / 2f;
		
			numberOfVerticesToBeDrawn[i] = indexArray.length * BACHED_INSTANCES;
			indexBuffer[i] = new IndexBuffer(BachingHelper.batchIndexData(BACHED_INSTANCES, vertexArray.length/6, indexArray));
			vertexBuffer[i] = new VertexBuffer(BachingHelper.batchVertexData(BACHED_INSTANCES, 5, vertexArray));
			
			prepareShader(i);
		}
	}
	
	public synchronized void setUp_CollisionDetector(CollisionDetector_Manager collisionDetector){
		this.collisionDetector = collisionDetector;
		collisionDetector.addColidableObjectSprite(sprites);
	}


	public abstract boolean step(float stepScale);

	/**
	 * Draws all sprites. It draws 16 of them at the time to save time.
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void draw(float[] viewMatrix, Lights lights) {							
		int currentState = 0;
		syncStates();//TODO tole more it stran ker ni optimalno
		for(Vector<Sprite> spritesInState : spriteStates){
			if(spritesInState.size() != 0){

				prepareShader(currentState);
				glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer[currentState].getBufferId());
				shaderProgram.bindTexture(textureIDs[currentState]);
				
				drawCalls = (int) Math.ceil(1.0 * spritesInState.size() / BACHED_INSTANCES);
				
				for(int i=0; i< drawCalls; i++){
					for(int j = 0; j < bachMVP.length ; j++){
						bachMVP[j] = 0;
						bachMM[j] = 0;
					}
					
					float[] tmpI = new float[16];
					Matrix.setIdentityM(tmpI,0);
					
					if(BACHED_INSTANCES * (i+1) >= spritesInState.size()){
						MatrixHelper.calculateBachMVP(bachMVP,viewMatrix, spritesInState,BACHED_INSTANCES * i, spritesInState.size());
						MatrixHelper.calculateBachMVP(bachMM,tmpI, spritesInState,BACHED_INSTANCES * i, spritesInState.size());
					}else{ //more than BACHED_INSTANCES  sprites
						MatrixHelper.calculateBachMVP(bachMVP,viewMatrix, spritesInState,BACHED_INSTANCES * i, BACHED_INSTANCES * (i+1));
						MatrixHelper.calculateBachMVP(bachMM,tmpI, spritesInState,BACHED_INSTANCES * i, BACHED_INSTANCES * (i+1));
					}
					
					glUniformMatrix4fv(MVPLocation, bachMVP.length/16, false, bachMVP, 0);
					glUniformMatrix4fv(shaderProgram.getModelMatrixUniformLocation(), bachMVP.length/16, false, bachMM, 0);
					GLES20.glUniform4f(shaderProgram.getLightPositionVectorLocation(), lights.x, lights.y, lights.z, 0.0f);
					
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
			
			currentState++;
		}
	}

	@Override
	public abstract void pause();

	@Override
	public abstract void finish();

	@Override
	public void destroy(){
		//TODO
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
			try{
			if(sprite.getBoundBox() != null && sprite.getPosition() != null && OnScreenOptimizer.visible(level.getCameraInfo(), sprite.getPosition(), sprite.getBoundBox()[sprite.getAnimationState()],
					(CommonVariables.CAMERA_DEPTH - sprite.getPosition()[2])/CommonVariables.CAMERA_DEPTH))
				continue;
			}catch(NullPointerException e){
				e.printStackTrace();
			};
			
			spriteStates.get(sprite.getAnimationState()).add(sprite);

		}
	}
	
	/**
	 * Prepares shader for drawing of this object.
	 */
	private void prepareShader(int dataIndex){
		vertexBuffer[dataIndex].setVertexAttribPointer(0, positionLocation, POSITION_COMPONENT_COUNT, STRIDE);
		vertexBuffer[dataIndex].setVertexAttribPointer(POSITION_COMPONENT_COUNT, textureLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
		vertexBuffer[dataIndex].setVertexAttribPointer(POSITION_COMPONENT_COUNT+TEXTURE_COORDINATES_COMPONENT_COUNT, bachingIndexLocation, BACH_INDEX_COMPONENT_COUNT, STRIDE);
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
	
	protected synchronized int spritesSize(){
		return sprites.size();
	}
	
	protected synchronized Sprite getSprite(int spriteLocation){
		return sprites.get(spriteLocation);
	}
	
	protected synchronized void clearSprites(){
		sprites.clear();
	}
}

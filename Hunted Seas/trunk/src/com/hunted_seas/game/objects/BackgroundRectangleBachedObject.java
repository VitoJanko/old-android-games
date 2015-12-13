package com.hunted_seas.game.objects;

import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static com.hunted_seas.debugging.Logger.logBackgroundBox;
import static com.hunted_seas.game.world.acommon.CommonVariables.BYTES_PER_FLOAT;
import static com.hunted_seas.game.world.acommon.CommonVariables.androidVersion;

import java.util.LinkedList;
import java.util.Vector;

import android.annotation.TargetApi;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidGL20;
import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.data.IndexBuffer;
import com.hunted_seas.game.data.LineSegment;
import com.hunted_seas.game.data.VertexBuffer;
import com.hunted_seas.game.programs.BachingTextureShaderProgram;
import com.hunted_seas.game.util.BachingHelper;
import com.hunted_seas.game.util.MatrixHelper;
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
public abstract class BackgroundRectangleBachedObject implements SpriteManagerInterface{
	private AndroidGL20 repairedGL20;
	
	protected LevelManager level;
	
	protected int onScreenSprites = 0;
	
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
	 * leftX,rightX,topY,bottomY, Z
	 */
	protected float[] backgroundDimensions;
	float[] vertexArray;
	short[] indexArray;
	
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
	protected void loadObject(Context context, BachingTextureShaderProgram bachingShaderProgram, String textureResources){
		loadObject(context, bachingShaderProgram, new String[]{textureResources});
	}
	
	protected void loadObject(Context context, BachingTextureShaderProgram bachingShaderProgram, String[] textureResources){
		this.shaderProgram = (BachingTextureShaderProgram) bachingShaderProgram;

		MVPLocation = shaderProgram.getMatrixUniformLocation();
		positionLocation = shaderProgram.getPositionAttributeLocation();
		textureLocation = shaderProgram.getTextureCoordinatesAttributeLocation();
		bachingIndexLocation = shaderProgram.getBachingIndexLocation();
					

		
		textureIDs = new int[textureResources.length];
		indexBuffer = new IndexBuffer[textureResources.length];
		vertexBuffer = new VertexBuffer[textureResources.length];
		numberOfVerticesToBeDrawn = new int[textureResources.length];
		
		boundingBox = new BoundingBox[textureResources.length];
		offsetX = new float[textureResources.length];
		offsetY = new float[textureResources.length];
		lineSegments = new LinkedList<LinkedList<LineSegment>>();
		
		buildMesh(2,2);
		
		for(int i=0; i < textureResources.length;i++){
			spriteStates.add(new Vector<Sprite>());
			
			textureIDs[i] = TextureHelper.loadTexture(context, textureResources[i]);
			
			boundingBox[i] = new BoundingBox();
			boundingBox[i].left = backgroundDimensions[0]; boundingBox[i].right = backgroundDimensions[2];
			boundingBox[i].bottom = backgroundDimensions[1]; boundingBox[i].top = backgroundDimensions[3];
			
			offsetX[i] = boundingBox[i].getWidth() / 2f;
			offsetY[i] = boundingBox[i].getHeight() / 2f;
					
			numberOfVerticesToBeDrawn[i] = indexArray.length * BACHED_INSTANCES;
			indexBuffer[i] = new IndexBuffer(BachingHelper.batchIndexData(BACHED_INSTANCES, vertexArray.length/5, indexArray));
			vertexBuffer[i] = new VertexBuffer(BachingHelper.batchVertexData(BACHED_INSTANCES, 5, vertexArray));
			
			prepareShader(i);
		}
		
//		prepareShader();
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
		
		short spriteBuffer = 0;
		
		for(Sprite sprite : sprites){
			if(OnScreenOptimizer.visible(level.getCameraInfo(), sprite.getPosition(), boundingBox[sprite.getAnimationState()],
					(CommonVariables.CAMERA_DEPTH - sprite.getPosition()[2])/CommonVariables.CAMERA_DEPTH))
				continue;

			spriteStates.get(sprite.getAnimationState()).add(sprite);
			spriteBuffer++;
		}
		
		onScreenSprites = spriteBuffer;
	}
	
	public synchronized int getOnScreenSprites(){
		return onScreenSprites;
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
	
	/**
	 * Creates rectangular mesh of density columns * rows.
	 * 
	 * @param numberOfColumns (x dimension)
	 * @param numberOfRows (y dimension)
	 */
	private void buildMesh(int numberOfColumns, int numberOfRows){
		int componentCount = POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT + BACH_INDEX_COMPONENT_COUNT;
		float[] vertexArray = new float[numberOfColumns * numberOfRows * (componentCount)];
		float horizontalStep = (backgroundDimensions[1] - backgroundDimensions[0])*1.0f / (numberOfColumns-1);
		float verticalStep = (backgroundDimensions[2] - backgroundDimensions[3])*1.0f / (numberOfRows-1);
		
		float textureHorizontalStep = (float) (1.0 / (numberOfColumns-1));
		float textureVerticalStep = (float) (1.0 / (numberOfRows-1));
		
		logBackgroundBox(Log.DEBUG, "h: "+horizontalStep + "  :  v: "+verticalStep);
		
		int indexCounter = 0;
		for(int i=0; i < numberOfRows; i++){
			for(int j=0; j < numberOfColumns; j++){
				vertexArray[indexCounter] = backgroundDimensions[0] + j * horizontalStep;
				vertexArray[indexCounter+1] = backgroundDimensions[2] - i * verticalStep;
				vertexArray[indexCounter+2] = backgroundDimensions[4];
				vertexArray[indexCounter+3] = (1-j * textureHorizontalStep);
				vertexArray[indexCounter+4] = (1-i * textureVerticalStep);
				
				logBackgroundBox(Log.DEBUG, "i: "+i+"   "+vertexArray[indexCounter+3]+" : "+vertexArray[indexCounter+4]);
				
				indexCounter += componentCount;
			}
		}
		
		short[] gIndexArray = new short[2 * (numberOfColumns-1) * (numberOfRows-1)*3];
		
		indexCounter = 0;
		for(short i=0; i < numberOfRows -1; i++){
			for(short j=0; j < numberOfColumns -1; j++){
				gIndexArray[indexCounter] = j;
				gIndexArray[indexCounter+1] = (short) (j + 1);
				gIndexArray[indexCounter+2] = (short) (j+((i+1)*numberOfColumns));
				
				gIndexArray[indexCounter+3] = gIndexArray[indexCounter + 2];
				gIndexArray[indexCounter+4] = gIndexArray[indexCounter + 1];
				gIndexArray[indexCounter+5] = (short) (gIndexArray[indexCounter + 2] + 1);
				
				indexCounter += 6;
			}
		}
		
		this.indexArray = gIndexArray;
//		short[] bb = {0,1,2,2,1,3};
//		this.indexArray = bb;
		this.vertexArray = vertexArray;
	}
}

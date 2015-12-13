package com.hunted_seas.game.util;

import android.content.Context;

import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.data.MayaRawObjectData;

/**
 * Helps build up model objects from .obj files.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class ObjectModelHelper {
	
	/**
	 * Creates simple object.
	 * 
	 * Creates object
	 * @param context
	 * @param resourceId
	 * @return
	 */
	
	public static MayaRawObjectData loadModel(Context context, int resourceId){
		return loadModel(context,resourceId,true);
	}
	
	public static MayaRawObjectData loadModel(Context context, int resourceId, boolean center){
		MayaRawObjectData obj = FileReader.readMayaObjectFile(context, resourceId, center);
		
		int cordSize = obj.getCoordinateObject().size();
		
//		float[] boundingBox = new float[4];
//		boundingBox[0] = Integer.MAX_VALUE;
//		boundingBox[1] = Integer.MAX_VALUE;
//		boundingBox[2] = Integer.MIN_VALUE;
//		boundingBox[3] = Integer.MIN_VALUE;
		BoundingBox boundingBox = new BoundingBox();

		
		float[] vertexDataHolderTemp = new float[cordSize+obj.getTextureObject().size()];
		short[] indexDataHolderTep = new short[obj.getIndexObject().size()];
		
		int counter = 0;
		for(short index : obj.getIndexObject()){
			indexDataHolderTep[counter] = index;
			counter++;
		}
		
		counter = 0;
		int textureCounter = 0;
		for(int i=0; i < cordSize; i=i+3){
			vertexDataHolderTemp[counter] = obj.getCoordinateObject().get(i);
			vertexDataHolderTemp[counter+1] = obj.getCoordinateObject().get(i+1);
			vertexDataHolderTemp[counter+2] = obj.getCoordinateObject().get(i+2);
			
			vertexDataHolderTemp[counter+3] = obj.getTextureObject().get(textureCounter);
			vertexDataHolderTemp[counter+4] = obj.getTextureObject().get(textureCounter+1);
			
			checkBounds(vertexDataHolderTemp[counter], vertexDataHolderTemp[counter+1],boundingBox);
			
			counter += 5;
			textureCounter += 2;
		}
		
		obj.clearHolders();
		obj.setIndex(indexDataHolderTep);
		obj.setVertexData(vertexDataHolderTemp);
		obj.setBoundingBox(boundingBox);
		
		vertexDataHolderTemp = null;
		indexDataHolderTep = null;
		
		return obj;
	}
	
	/**
	 * Creates bached object. Object that can be used for baching.
	 * 
	 * @param context
	 * @param resourceId
	 * @return
	 */
	public static MayaRawObjectData loadBachedModel(Context context, int resourceId){
		return loadBachedModel(context,resourceId,true);
	}
	
	public static MayaRawObjectData loadBachedModel(Context context, int resourceId, boolean center){
		MayaRawObjectData obj = FileReader.readMayaObjectFile(context, resourceId, center);
		
		int cordSize = obj.getCoordinateObject().size();
		
		BoundingBox boundingBox = new BoundingBox();	
		
		float[] vertexDataHolderTemp = new float[cordSize/3+cordSize+obj.getTextureObject().size()];
		short[] indexDataHolderTep = new short[obj.getIndexObject().size()];
		
		int counter = 0;
		for(short index : obj.getIndexObject()){
			indexDataHolderTep[counter] = index;
			counter++;
		}
		
		counter = 0;
		int textureCounter = 0;
		for(int i=0; i < cordSize; i=i+3){
			vertexDataHolderTemp[counter] = obj.getCoordinateObject().get(i);
			vertexDataHolderTemp[counter+1] = obj.getCoordinateObject().get(i+1);
			vertexDataHolderTemp[counter+2] = obj.getCoordinateObject().get(i+2);
			
			vertexDataHolderTemp[counter+3] = obj.getTextureObject().get(textureCounter);
			vertexDataHolderTemp[counter+4] = obj.getTextureObject().get(textureCounter+1);
			/**
			 * Baching index!
			 */
			vertexDataHolderTemp[counter+5] = 0;
			
			checkBounds(vertexDataHolderTemp[counter], vertexDataHolderTemp[counter+1],boundingBox);
			
			counter += 6;
			textureCounter += 2;
		}
		
		obj.clearHolders();
		obj.setIndex(indexDataHolderTep);
		obj.setVertexData(vertexDataHolderTemp);
		obj.setBoundingBox(boundingBox);
		
		vertexDataHolderTemp = null;
		indexDataHolderTep = null;
		
		return obj;
		
		
	}
	
	/**
	 * Looks for bounds of object.
	 * 
	 * @param x
	 * @param y
	 * @param boundingBox
	 */
	private synchronized static void checkBounds(float x, float y, BoundingBox boundingBox){
		if(x < boundingBox.left)
			boundingBox.left = x;
		if(x > boundingBox.right)
			boundingBox.right = x;
		
		if(y < boundingBox.bottom)
			boundingBox.bottom = y;
		if(y > boundingBox.top)
			boundingBox.top = y;
	}
}

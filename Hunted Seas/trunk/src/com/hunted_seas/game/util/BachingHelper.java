package com.hunted_seas.game.util;

/**
 * Changes vertex and index array so they can be used for baching. <br />
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class BachingHelper {

	/**
	 * Changes vertex data so it can be used for bachng.
	 * 
	 * @param BACHED_INSTANCES How many items do we want to draw at the same time? (16 default)
	 * @param BACHING_STRIDE Stride for baching index!
	 * @param vertexArray
	 * @return modified vertex array
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public static float[] batchVertexData(int BACHED_INSTANCES,int BACHING_STRIDE,float[] vertexArray){
		float[] bachedVertex = new float[vertexArray.length * BACHED_INSTANCES];
		
		int bached_stripe_counter = 0;
		int indexCounter = 0;
		
		for(int i=0; i < BACHED_INSTANCES; i++){
			bached_stripe_counter = 0;
			
			for(int j=0; j < vertexArray.length; j++){
				if(bached_stripe_counter == BACHING_STRIDE){
					bachedVertex[indexCounter] = i;
					bached_stripe_counter = 0;
				}else{
					bachedVertex[indexCounter] = vertexArray[j];
					bached_stripe_counter++;
				}
				
				indexCounter++;
			}
		}
		
		return bachedVertex;
	}
	
	/**
	 * Changes index data so it can be used for baching.
	 * 
	 * @param BACHED_INSTANCES How many items are we going to draw at the same time? (default 16)
	 * @param NUMBER_OF_VERTICES_PER_OBJECT How many vertices does 1 (normal) object have?
	 * @param indexArray
	 * @return modified array of index data that can be used for baching
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public static short[] batchIndexData(int BACHED_INSTANCES,int NUMBER_OF_VERTICES_PER_OBJECT, short[] indexArray){
		short[] bachedIndex = new short[indexArray.length * BACHED_INSTANCES];
		
		int indexCounter = 0;
		
		for(int i=0; i < BACHED_INSTANCES; i++){
			for(int j=0; j < indexArray.length; j++){
				bachedIndex[indexCounter] = (short) (indexArray[j] + (i * NUMBER_OF_VERTICES_PER_OBJECT));
				indexCounter++;
			}
		}
		
		return bachedIndex;
	}
}

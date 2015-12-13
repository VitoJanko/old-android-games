package com.hunted_seas.game.util;

import static android.opengl.Matrix.multiplyMM;

import java.util.Vector;

import com.hunted_seas.game.world.acommon.Sprite;

/**
 * Matirx operations that are regulary used.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class MatrixHelper {
	
	/**
	 * Creates perspective matrix. (mvP)
	 * 
	 * @param empty matrix
	 * @param yFovInDegrees
	 * @param aspect (width/height)
	 * @param near cliping
	 * @param far cliping
	 */
	public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f){
		final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);
		final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));
		
		
		m[0] = a / aspect; //first 4 values are 1st column
		m[1] = 0f;
		m[2] = 0f;
		m[3] = 0f;
		m[4] = 0f; // second column
		m[5] = a;
		m[6] = 0f;
		m[7] = 0f;
		m[8] = 0f;
		m[9] = 0f;
		m[10] = -((f + n) / (f - n));
		m[11] = -1f;
		m[12] = 0f;
		m[13] = 0f;
		m[14] = -((2f * f * n) / (f - n));
		m[15] = 0f;
	}
	
	@Deprecated
	public static float[] concatenateMatrix(float[] leftM, float[] rightM){
		float[] concatenated = new float[leftM.length+rightM.length];
		
		int fullLength = concatenated.length;
		int leftMLenght = leftM.length;
		
		for(int i=0; i < fullLength; i++){
			if(i < leftMLenght)
				concatenated[i] = leftM[i];
			else
				concatenated[i] = rightM[i-leftMLenght];
		}
		
		return concatenated;
	}
	
	@Deprecated
	public static float[] concatenateAll(Vector<float[]> matrixes){
		float[] concatenated = new float[16*matrixes.size()];
		
		int counter = 0;
		
		for(float[] mat : matrixes){
			for(float val : mat){
				concatenated[counter] = val;
				counter++;
			}
		}
		
		return concatenated;
	}
	
	/**
	 * multiplyMM for bached matrices.
	 * 
	 * @param concatenated
	 * @param viewM
	 * @param sprites SpriteInterface from whom we get model matrix.
	 * @param beginningOffset from where in sprites vector do we start?
	 * @param endOffset at what element do do we end?
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public static void calculateBachMVP(float[] concatenated,float[] viewM, Vector<Sprite> sprites,int beginningOffset,int endOffset){
		int counter = 0;
		
		for(int i=beginningOffset; i < endOffset; i++){
			try{
				multiplyMM(concatenated, counter*16, viewM , 0, sprites.get(i).getModelMatrix(), 0);
			}catch(ArrayIndexOutOfBoundsException e){
				e.printStackTrace();
				break;
			}
			counter++;
		}
	}
}

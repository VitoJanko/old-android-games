package com.igrargti;

/**
 * TODO Put here a description of what this class does.
 *
 * @author Jani.
 *         Created 17. jan. 2012.
 */
public class Zid extends Cube {

	/**
	 * TODO Put here a description of what this constructor does.
	 *
	 * @param sirina
	 * @param visina
	 * @param dolzina
	 * @param x
	 * @param y
	 * @param z
	 * @param move
	 * @param mx
	 * @param my
	 * @param mz
	 */
	public Zid(int textura,float sirina, float visina, float dolzina, float x, float y,
			float z, boolean[] move, float[] mx, float[] my, float[] mz) {
		super(textura,sirina, visina, dolzina, x, y, z, move, mx, my, mz);
		
	}
	
	public boolean killed(float X,float Z){
		if(X > (moveX-sirina) && X < (moveX+sirina)){
			if(Z > (moveZ-sirina) && Z < (moveZ+sirina)){
				return true;
			}
		}
		return false;
	}

}

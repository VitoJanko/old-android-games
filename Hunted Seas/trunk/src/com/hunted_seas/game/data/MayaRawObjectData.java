package com.hunted_seas.game.data;

import java.util.LinkedList;
import java.util.Vector;

import android.util.Log;

/**
 * Helper object that holds data from reading .obj resource from Maya model.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class MayaRawObjectData {
	private Vector<Float> objectCoordinatesHolder;
	private Vector<Float> textureCoordinatesHolder;
	private Vector<Short> vertexIndexHolder;
	
	private float[] vertexDataArray;
	private short[] vertexIndexArray;
	private LinkedList<LineSegment> lineSegments = new LinkedList<LineSegment>();
	
	/**
	 * Rectangle that holds object. (ocrtan pravokotnik)
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	private BoundingBox boundingBox = new BoundingBox();
	
	
	public Vector<Float> getCoordinateObject(){
		if(objectCoordinatesHolder == null)
			objectCoordinatesHolder = new Vector<Float>();
		return objectCoordinatesHolder;
	}
	
	public Vector<Float> getTextureObject(){
		if(textureCoordinatesHolder == null)
			textureCoordinatesHolder = new Vector<Float>();
		return textureCoordinatesHolder;
	}
	
	public Vector<Short> getIndexObject(){
		if(vertexIndexHolder == null)
			vertexIndexHolder = new Vector<Short>();
		return vertexIndexHolder;
	}
	
	public void setVertexData(float[] vertexDataArray){
		this.vertexDataArray = vertexDataArray;
	}
	
	public void setIndex(short[] indexDataArray){
		this.vertexIndexArray = indexDataArray;
	}
	
	public float[] getVertexDataArray(){
		return vertexDataArray;
	}
	
	public short[] getIndexDataArray(){
		return vertexIndexArray;
	}
	
	public LinkedList<LineSegment> getLineSegments(){
		return lineSegments;
	}
	
	public void addLineSegment(LineSegment segment){
		lineSegments.add(segment);
	}
	
	/**
	 * Clears data in vectors. This should only be called after 
	 * data is copied in to arrays!
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public void clearHolders(){
		objectCoordinatesHolder.clear();
		objectCoordinatesHolder = null;
		vertexIndexHolder.clear();
		vertexIndexHolder = null;
		textureCoordinatesHolder.clear();
		textureCoordinatesHolder = null;
	}
	
	public BoundingBox getBoundingBox(){
		return boundingBox;
	}
	
	public void setBoundingBox(BoundingBox boundingBox){
		this.boundingBox = boundingBox;
	}
	
	public void setBoundingBox(float left, float bottom, float right, float top){
		boundingBox.left = left;
		boundingBox.bottom = bottom;
		boundingBox.right = right;
		boundingBox.top = top;
	}

}

package com.hunted_seas.game.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;

import com.hunted_seas.game.data.LineSegment;
import com.hunted_seas.game.data.MayaRawObjectData;

public class FileReader {

	/**
	 * Reads raw resource and returns it as string.
	 * <br />
	 * Throws exception if file not found.
	 * 
	 * @param context
	 * @param resourceId
	 * @return String of raw resource
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public static String readTextFromRaw(Context context, int resourceId){
		StringBuilder body = new StringBuilder();
		
		try{
			InputStream inputStream = context.getResources().openRawResource(resourceId);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String nextLine;
			
			while((nextLine = bufferedReader.readLine()) != null){
				body.append(nextLine+"\n");
			}
			
			bufferedReader.close();
			
		}catch(IOException e){
			throw new RuntimeException("Could not open resource: "+resourceId,e);
		}catch(Resources.NotFoundException nfe){
			throw new RuntimeException("Resource not found: "+resourceId,nfe);
		}
		
		return body.toString();
	}
	
	/**
	 * Reads and parses Maya .obj file.
	 * 
	 * @param context
	 * @param resourceId
	 * @return MayaRawObjectData Only Vectors are filled!
	 * 
	 * @see MayaRawObjectData
	 * 
	 * @author Jani Bizjak <janibizjak@gmail.com>
	 */
	public static MayaRawObjectData readMayaObjectFile(Context context, int resourceId, boolean center){
		MayaRawObjectData obj = new MayaRawObjectData();
		
		InputStream inputStream = context.getResources().openRawResource(resourceId);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		try{
			String line; 
			
			Vector<Float> vertexCoordinates = obj.getCoordinateObject();
			Vector<Float> textureCoordinates = obj.getTextureObject();
			Vector<Short> indexArray = obj.getIndexObject();
			
			while((line = reader.readLine()) != null){
				if(line.startsWith("f")){//vertex order
					String[] groups = line.split(" ");
					indexArray.add((short) (Short.valueOf(groups[1].split("/")[0])-1));
					indexArray.add((short) (Short.valueOf(groups[2].split("/")[0])-1));
					indexArray.add((short) (Short.valueOf(groups[3].split("/")[0])-1));
				}else if(line.startsWith("vt")){//texture coords
					String[] groups = line.split(" ");
					textureCoordinates.add(Float.valueOf(groups[1]));
					textureCoordinates.add(1 - Float.valueOf(groups[2]));
				}else if(line.startsWith("v") && !line.startsWith("vn")){//vertex coords
					String[] groups = line.split(" ");
					vertexCoordinates.add(Float.valueOf(groups[1]));
					vertexCoordinates.add(Float.valueOf(groups[2]));
					vertexCoordinates.add(Float.valueOf(groups[3])); //z is always zero in .ojb file
				}
			}
			
			reader.close();
			
			float offsetX = 0;
			float offsetY = 0;
			
			if(center){
				float[] boundingBox = new float[4];
				boundingBox[0] = Integer.MAX_VALUE;
				boundingBox[1] = Integer.MAX_VALUE;
				boundingBox[2] = Integer.MIN_VALUE;
				boundingBox[3] = Integer.MIN_VALUE;
				
				for(int i=0; i < vertexCoordinates.size(); i += 3){
					checkBounds(vertexCoordinates.get(i), vertexCoordinates.get(i+1), boundingBox);
				}
				
				offsetX = (boundingBox[0] + boundingBox[2]) / 2f;
				offsetY = (boundingBox[1] + boundingBox[3]) / 2f;
				
				for(int i=0; i < vertexCoordinates.size(); i += 3){
					vertexCoordinates.set(i, vertexCoordinates.get(i) - offsetX);
					vertexCoordinates.set(i+1, vertexCoordinates.get(i+1) - offsetY);
				}
			}
			
			if(vertexCoordinates.size() > 3){
				for(int i = 0; i < vertexCoordinates.size(); i += 3){
					if(i == vertexCoordinates.size()-3){
						LineSegment segment = new LineSegment();
						segment.setStartPoint(vertexCoordinates.get(i),vertexCoordinates.get(i+1), vertexCoordinates.get(i+2));
						segment.setEndPoint(vertexCoordinates.get(0),vertexCoordinates.get(1), vertexCoordinates.get(2));
						segment.setOffset(offsetX, offsetY);
						obj.addLineSegment(segment);
					}else{
						LineSegment segment = new LineSegment();
						segment.setStartPoint(vertexCoordinates.get(i),vertexCoordinates.get(i+1), vertexCoordinates.get(i+2));
						segment.setEndPoint(vertexCoordinates.get(i+3),vertexCoordinates.get(i+4), vertexCoordinates.get(i+5));
						segment.setOffset(offsetX, offsetY);
						obj.addLineSegment(segment);
					}
				}
			}
			
		}catch(IOException e){
			throw new RuntimeException("Could not open resource: "+resourceId,e);
		}catch(Resources.NotFoundException nfe){
			throw new RuntimeException("Resource not found: "+resourceId,nfe);
		}
		
		
		return obj;
	}
	
	/**
	 * Looks for bounds of object.
	 * 
	 * @param x
	 * @param y
	 * @param boundingBox
	 */
	private synchronized static void checkBounds(float x, float y, float[] boundingBox){
		if(x < boundingBox[0])
			boundingBox[0] = x;
		if(x > boundingBox[2])
			boundingBox[2] = x;
		
		if(y < boundingBox[1])
			boundingBox[1] = y;
		if(y > boundingBox[3])
			boundingBox[3] = y;
	}
}

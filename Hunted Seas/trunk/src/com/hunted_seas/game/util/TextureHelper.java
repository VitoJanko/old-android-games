package com.hunted_seas.game.util;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;
import static com.hunted_seas.debugging.Logger.logTextureHelper;
import static com.hunted_seas.game.world.acommon.CommonVariables.ATI;
import static com.hunted_seas.game.world.acommon.CommonVariables.DEBUG;
import static com.hunted_seas.game.world.acommon.CommonVariables.NVIDIA;
import static com.hunted_seas.game.world.acommon.CommonVariables.PVRTC;
import static com.hunted_seas.game.world.acommon.CommonVariables.RENDERER_TYPE;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

/**
 * Creates textures.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class TextureHelper {
	// PowerVR Texture compression constants
	public static final int GL_COMPRESSED_RGB_PVRTC_4BPPV1_IMG = 0x8C00;
	public static final int GL_COMPRESSED_RGB_PVRTC_2BPPV1_IMG = 0x8C01;
	public static final int GL_COMPRESSED_RGBA_PVRTC_4BPPV1_IMG = 0x8C02;
	public static final int GL_COMPRESSED_RGBA_PVRTC_2BPPV1_IMG = 0x8C03;
	public static final int GL_COMPRESSED_PVRTC_BGRA8888 = 0x80E1;
	
    public static final int ATC_RGB_AMD = 0x8C92;
    public static final int ATC_RGBA_EXPLICIT_ALPHA_AMD = 0x8C93;
    public static final int ATC_RGBA_INTERPOLATED_ALPHA_AMD = 0x87EE;
    
    public static final int COMPRESSED_RGB_S3TC_DXT1_EXT = 0x83F0;
    public static final int COMPRESSED_RGBA_S3TC_DXT1_EXT = 0x83F1;
    public static final int COMPRESSED_RGBA_S3TC_DXT3_EXT = 0x83F2;
    public static final int COMPRESSED_RGBA_S3TC_DXT5_EXT = 0x83F3;
	
    public static int loadTexture(Context context, int resourceId){
    	return 0;
    }
    public static int loadTexture(Context context, String resourceId){
    	switch(RENDERER_TYPE){
    	case PVRTC:
    		try {
				return loadCompressedTexture_PVR(context, resourceId);
			} catch (IOException e) {
				e.printStackTrace();
				if(DEBUG)
					return 0;
			}
    	case ATI:
    		try {
				return loadCompressedTextureAMD(context, resourceId);
			} catch (IOException e) {
				e.printStackTrace();
				if(DEBUG)
					return 0;
			}
    	case NVIDIA:
    		try {
				return loadCompressedTextureNvidia(context, resourceId);
			} catch (IOException e) {
				if(DEBUG)
					return 0;
			}
    	default: //PNG
    		try {
				return loadTexturePNG(context, resourceId);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	return 0;
    }
    
	/**
	 * 
	 * @param context
	 * @param resourceId
	 * @return texture object id
	 * @throws IOException 
	 */
	public static int loadTexturePNG(Context context, String resourceId) throws IOException{
		final int[] textureObjectIds = new int[1];
		
		glGenTextures(1, textureObjectIds, 0);
		
		if(textureObjectIds[0] == 0){
			logTextureHelper(Log.WARN, "Could not generate a new OpenGL texture object");
			return 0;
		}
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false; //don't scale data
		
		final Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(resourceId+".png"));
		
		if(bitmap == null){
			logTextureHelper(Log.WARN, "Resource ID " + resourceId + " could not be decoded.");
			glDeleteTextures(1, textureObjectIds, 0);
			return 0;
		}
		
		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);//binds texture to texture object
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);//minimization filter
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);//send texture data to OpenGL to the CURRENTLY BOUND object
		
		bitmap.recycle();
		
		glGenerateMipmap(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0); //unbind texture
		
		return textureObjectIds[0];
	}
	
	
	/**
	 * 
	 * @param context
	 * @param resourceId
	 * @return texture object id
	 * @throws IOException 
	 */
	public static int loadCompressedTexture_PVR(Context context, String resourceId) throws IOException{
		final int[] textureObjectIds = new int[1];
		
		glGenTextures(1, textureObjectIds, 0);
		
		if(textureObjectIds[0] == 0){
			logTextureHelper(Log.WARN, "Could not generate a new OpenGL texture object");
			return 0;
		}
		
		final InputStream bitmap = context.getAssets().open(resourceId+".pvr");
		byte[] buffer;
		ByteBuffer bf;
		
		try {
			buffer = new byte[bitmap.available()];
			bitmap.read(buffer);

			int offset = 67; // 52 bit = header, 15 bit = metadata
			bf = ByteBuffer.wrap(buffer, offset, buffer.length-offset);
			bf.order(ByteOrder.LITTLE_ENDIAN);
			
			int height 	     = bf.getInt(24);
			int width   	 = bf.getInt(28);
			
	
			glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
			
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	        glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
			
	       
			GLES20.glCompressedTexImage2D(GL_TEXTURE_2D, 0,GL_COMPRESSED_RGBA_PVRTC_4BPPV1_IMG, width, height, 0, bf.capacity()-offset, bf);

			glBindTexture(GL_TEXTURE_2D, 0); //unbind texture
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return textureObjectIds[0];
	}
	
	/**
	 * 
	 * @param context
	 * @param resourceId
	 * @return texture object id
	 * @throws IOException 
	 */
	public static int loadCompressedTextureAMD(Context context, String resourceId) throws IOException{
		final int[] textureObjectIds = new int[1];
		
		glGenTextures(1, textureObjectIds, 0);
		
		if(textureObjectIds[0] == 0){
			logTextureHelper(Log.WARN, "Could not generate a new OpenGL texture object");
			return 0;
		}
		
		final InputStream bitmap = context.getAssets().open(resourceId+".DDS");
		byte[] buffer;
		ByteBuffer bf;
		
		try {
			buffer = new byte[bitmap.available()];
			bitmap.read(buffer);

			int offset = 128; // 64 bit = header, 15 bit = metadata
			bf = ByteBuffer.wrap(buffer, offset, buffer.length-offset);
			bf.order(ByteOrder.LITTLE_ENDIAN);
			
			int height = bf.getInt(16);
			int width = bf.getInt(12);
			
		
			int size = ((height + 3) / 4) * ((width + 3) / 4) * 16;
			
			bf.compact();
			bf.position(0);
			
			
			glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
			
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	        glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
			
	       
			GLES20.glCompressedTexImage2D(GL_TEXTURE_2D, 0,ATC_RGBA_EXPLICIT_ALPHA_AMD, width, height, 0, size, bf);

			glBindTexture(GL_TEXTURE_2D, 0); //unbind texture
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return textureObjectIds[0];
	}
	
	
	/**
	 * 
	 * @param context
	 * @param resourceId
	 * @return texture object id
	 * @throws IOException 
	 */
	public static int loadCompressedTextureNvidia(Context context, String resourceId) throws IOException{
		final int[] textureObjectIds = new int[1];
		
		glGenTextures(1, textureObjectIds, 0);
		
		if(textureObjectIds[0] == 0){
			logTextureHelper(Log.WARN, "Could not generate a new OpenGL texture object");
			return 0;
		}
		
		final InputStream bitmap = context.getAssets().open(resourceId+".s3tc");
		byte[] buffer;
		ByteBuffer bf;
		
		try {
			buffer = new byte[bitmap.available()];
			bitmap.read(buffer);

			int offset = 128; // 64 bit = header, 15 bit = metadata
			bf = ByteBuffer.wrap(buffer, offset, buffer.length-offset);
			bf.order(ByteOrder.LITTLE_ENDIAN);
			
			int height = bf.getInt(16);
			int width = bf.getInt(12);
			
		
			int size = ((height + 3) / 4) * ((width + 3) / 4) * 16;

			bf.compact();
			bf.position(0);
			
			
			glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
			
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	        glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
			
	       
			GLES20.glCompressedTexImage2D(GL_TEXTURE_2D, 0,COMPRESSED_RGBA_S3TC_DXT5_EXT, width, height, 0, size, bf);

			glBindTexture(GL_TEXTURE_2D, 0); //unbind texture
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return textureObjectIds[0];
	}
}

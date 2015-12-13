package com.airhockey.androd.util;

import static android.opengl.GLES20.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import static android.opengl.GLUtils.*;
import android.util.Log;

public class TextureHelper {
	private static final String TAG = "TextureHelper";
	
	
	public static int loadTexture(Context context, int resourceId){
		final int[] textureObjectIds = new int[1];
		glGenTextures(1, textureObjectIds, 0);//generate texture object in openGL and returns ID of generated object
		
		if(textureObjectIds[0] == 0){
			if(LoggerConfig.ON){
				Log.w(TAG, "Could not generate a new OpenGL texture object.");
			}
			
			return 0;
		}
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;//don't scale data
		
		/**
		 * Decodes image to bitmap or returns null.
		 */
		final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resourceId,options);
		
		
		if(bitmap == null){
			if(LoggerConfig.ON){
				Log.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
			}
			
			glDeleteTextures(1, textureObjectIds, 0);
			return 0;
		}
		
		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]); //binds texture to texture object in OpenGL (doesn't load it!)
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); // minification, linear_mipmap -> trilinear filtering 
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);// magnification -> bilinear filtering
	
		texImage2D(GL_TEXTURE_2D, 0 , bitmap, 0);//send texture data to openGL to the CURRENTLY BOUND object
		
		
		bitmap.recycle();
		
		glGenerateMipmap(GL_TEXTURE_2D); // generates mipmaps
		
		glBindTexture(GL_TEXTURE_2D,0); //unbind texture so we prevent changing it in future by mistake
		
		
		return textureObjectIds[0];
	}
}

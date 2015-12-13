package com.huntedseas.live.wallpaper;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * TODO Put here a description of what this class does.
 *
 * @author Jani.
 *         Created 2. dec. 2012.
 */
public abstract class GLWallpaperService extends WallpaperService {
	public static Context context;
	
	public class GLEngine extends Engine{
		private static final String TAG = "GLEngine";
		 
		private WallpaperGLSurfaceView glSurfaceView;
		private boolean rendererHasBeenSet;
		 
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
		    super.onCreate(surfaceHolder);
		    glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);
		}
		
		public void onTouchEvent(MotionEvent event){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				float x = event.getX();
				float y = event.getY();
							
				SquareGL.TouchedEvent(x, y);
			}
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
		    super.onVisibilityChanged(visible);
		 
		    if (rendererHasBeenSet) {
		        if (visible) {
		            glSurfaceView.onResume();
		        } else {
		            if (!isPreview()) {
		                glSurfaceView.onPause();
		            }
		        }
		    }
		}
		
		@Override
		public void onDestroy() {
		    super.onDestroy();
		    glSurfaceView.onDestroy();
		}
		
		protected void setRenderer(Renderer renderer) {
		    glSurfaceView.setRenderer(renderer);
		    rendererHasBeenSet = true;
		}
		 
		protected void setEGLContextClientVersion(int version) {
		    glSurfaceView.setEGLContextClientVersion(version);
		}
		
		class WallpaperGLSurfaceView extends GLSurfaceView {
		    private static final String TAG = "WallpaperGLSurfaceView";
		    
		    
		    
		    WallpaperGLSurfaceView(Context context) {
		        super(context);
		        GLWallpaperService.context = context;
		    }
		 
		    @Override
		    public SurfaceHolder getHolder() {
		        return getSurfaceHolder();
		    }
		 
		    public void onDestroy() {
		        super.onDetachedFromWindow();
		    }
		}
	}

}

package com.huntedseas;

import android.content.Context;
import android.opengl.GLSurfaceView;


public class GameView extends GLSurfaceView {
	public static Context context;
	
	private final GameRenderer renderer;
	
	public GameView(Context context){
		super(context);
		GameView.context = context;
		
		setEGLContextClientVersion(2);
		
		renderer = new GameRenderer();
		setRenderer(renderer);
	}
	
	protected void destroy(){
		renderer.destroy();
	}
}

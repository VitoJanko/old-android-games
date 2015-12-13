package com.huntedseas.live.wallpaper;

import android.opengl.GLSurfaceView.Renderer;

import com.learnopengles.android.lesson3.*;


public class LessonThreeWallpaperService extends OpenGLES2WallpaperService {
    @Override
    Renderer getNewRenderer() {
        return new GameRenderer();
    }
}
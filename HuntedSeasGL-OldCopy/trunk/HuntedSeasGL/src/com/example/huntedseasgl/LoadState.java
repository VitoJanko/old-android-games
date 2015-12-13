package com.example.huntedseasgl;

public class LoadState {
	int image;
	int model;
	int[] texture;
	float coordinates[];
	float cubeTextureCoordinateData[];
	short order[];
	
	public LoadState(Sea host, int image, int model){
		this.image = image;
		this.model=model;
		texture=new int[1];
		texture[0] = host.renderer.loadTexture(host.context, image);
		host.renderer.loadModel(model,this);
	}
}

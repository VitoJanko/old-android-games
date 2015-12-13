package org.bizjak.android.GPU;

public class LoadState {
	int image;
	int model;
	int[] texture;
	float coordinates[];
	float cubeTextureCoordinateData[];
	short order[];
	float xStart;
	float xEnd;
	float yStart;
	float yEnd;
	
	public LoadState(Sea host, int image, int model){
		this.image = image;
		this.model=model;
		texture=new int[1];
		texture[0] = host.renderer.loadTexture(host.context, image);
		host.renderer.loadModel(model,this);
	}
	
	public LoadState(Sea host, int[] image, int model){
		this.image = image[0];
		this.model=model;
		texture=new int[image.length];
		for (int i=0; i<image.length; i++)
			texture[i] = host.renderer.loadTexture(host.context, image[i]);
		host.renderer.loadModel(model,this);
	}
}

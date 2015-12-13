package com.igrargti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;


public class World implements OnKeyListener, OnTouchListener,SensorEventListener {
	public Hashtable<Integer,CollisionElement> vsaTla = new Hashtable<Integer,CollisionElement>();
	public Hashtable<Integer,int[]> sosedje = new Hashtable<Integer,int[]>();
	public int trenutniTrikotnik = 2;
	
	public ArrayList<Zid> zidovi = new ArrayList<Zid>();
	public ArrayList<Pyramid> piramide = new ArrayList<Pyramid>();
	private int stPiramid = 0;
	public ArrayList<Stikalo> stikala = new ArrayList<Stikalo>();
	public ArrayList<Vrata> vrata = new ArrayList<Vrata>();
	
	public MiscSound sound;
	
	/** Our texture pointer */
	private int[] textures = new int[3];
	
	/** The Activity Context */
	private Context context;
	private Lesson10 father ;
	private boolean resetTexture = false;
	
	/** The World sector */
	private Sector sector1;
	

	private final float piover180 = 0.0174532925f;
	private float heading = 180 * piover180;
	private float xpos;
	private float zpos;
	private float yrot;	 				//Y Rotation
	private float walkbias = 0;
	private float walkbiasangle = 0;
	private float lookupdown = 0.0f;
	
	private float posX = 0;
	private float posZ = 0;
	private int saveTrikotnik = trenutniTrikotnik;
	
	/* Variables and factor for the input handler */
	private float oldX;
    private float oldY;
	private final float TOUCH_SCALE = 0.1f;			//Proved to be good for normal rotation
	
	//stikala za vrata
	public boolean[] vrata1 = {false,false,false};
	
	//
	private float zadnjiCas = System.nanoTime();
	//
	Boss boss;
	
	/*
	 * Angles for gyroscope 
	 */
	private float angleX = 0;
	private float angleY = 0;
	private float angleZ = 0;
	
	/*
	 * Sensor
	 */
	private SensorManager sm = null;
	private Sensor mAccelerometer;
	
	
	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	private FloatBuffer textureBuffer;
	
	/** The initial vertex definition */
	private float[] vertices;
	
	/** The texture coordinates (u, v) */	
	private float[] texture;
	

	public World(Context context,Lesson10 father) {
		this.context = context;
		this.father = father;
		
        sm = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        //TODO
        sm.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        sound = new MiscSound(context);
	}

	
	public void loadWorld(String fileName) {
		try {
			//Some temporary variables
			int numtriangles = 0;
			int counter = 0;
			sector1 = new Sector();
			
			List<String> lines = null;
			StringTokenizer tokenizer;
			
			//Quick Reader for the input file
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.context.getAssets().open(fileName)));
			
			//Iterate over all lines
			String line = null;
			while((line = reader.readLine()) != null) {
				//Skip comments and empty lines
				if(line.startsWith("//") || line.trim().equals("")) {
					continue;
				}
				
				//Read how many polygons this file contains
				if(line.startsWith("NUMPOLLIES")) {
					numtriangles = Integer.valueOf(line.split(" ")[1]);					
					sector1.numtriangles = numtriangles;
					sector1.triangle = new Triangle[sector1.numtriangles];
				
				//Read every other line
				}else if(line.startsWith("id") ){
					String id = line;
					String sosedje3 = reader.readLine();
					String rob = reader.readLine();

					id = id.split(" ")[1];
					String[] sosedje1 = sosedje3.split(" ");
					String[] rob1 = rob.split(" ");
					int[] sosedje2 = new int[sosedje1.length];
					
					vsaTla.put(Integer.valueOf(id), new CollisionElement(Integer.valueOf(id),Float.valueOf(rob1[0]),Float.valueOf(rob1[1]),Float.valueOf(rob1[2]),Float.valueOf(rob1[3]),Float.valueOf(rob1[4]),Float.valueOf(rob1[5])));
					for(int j=0;j<sosedje1.length;j++){
						sosedje2[j] = Integer.valueOf(sosedje1[j]);
					}
					
					sosedje.put(Integer.valueOf(id), sosedje2);
					
				}else {
					if(lines == null) {
						lines = new ArrayList<String>();
					}
					
					lines.add(line);
				}
			}
			
			//Clean up!
			reader.close();
			
			//Now iterate over all read lines...
			for(int loop = 0; loop < numtriangles; loop++) {
				//...define triangles...
				Triangle triangle = new Triangle();
				
				//...and fill these triangles with the five read data 
				for(int vert = 0; vert < 3; vert++) {
					//
					line = lines.get(loop * 3 + vert);
					tokenizer = new StringTokenizer(line);
					
					//
					triangle.vertex[vert] = new Vertex();
					//
					triangle.vertex[vert].x = Float.valueOf(tokenizer.nextToken());
					triangle.vertex[vert].y = Float.valueOf(tokenizer.nextToken());
					triangle.vertex[vert].z = Float.valueOf(tokenizer.nextToken());
					triangle.vertex[vert].u = Float.valueOf(tokenizer.nextToken());
					triangle.vertex[vert].v = Float.valueOf(tokenizer.nextToken());
				}
				
				//Finally, add the triangle to the sector
				sector1.triangle[counter++] = triangle;
			}
			
		//If anything should happen, write a log and return
		} catch(Exception e) {
			Log.e("World", "Could not load the World file!", e);
			return;
		}
		

		vertices = new float[sector1.numtriangles * 3 * 3];
		texture = new float[sector1.numtriangles * 3 * 2];
		
		int vertCounter = 0;
		int texCounter = 0;
				
		//
		for(Triangle triangle : sector1.triangle) {
			//
			for(Vertex vertex : triangle.vertex) {
				//
				vertices[vertCounter++] = vertex.x;
				vertices[vertCounter++] = vertex.y;
				vertices[vertCounter++] = vertex.z;
				//
				texture[texCounter++] = vertex.u;
				texture[texCounter++] = vertex.v;
			}
		}		
		
		//Build the buffers
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		//
		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
		
		
		
		RESETLVL();
	}
		
	public void addPiramid(){
		if(stPiramid < 20){
			boolean[] premikanje = {false,false,false};
			float[] poX={-0.5f,0f,0.1f};
			float[] poY={-0.5f,0.5f,0.01f};
			float[] poZ={0,0,0};
			
			float tmx = xpos+ (float)Math.sin(heading * piover180) * (-0.35f);	//x
			float tmz = zpos + (float)Math.cos(heading * piover180) * (-0.35f);
			
			piramide.add(new Pyramid(R.drawable.aluminijozadje,0.2f,0.2f,0.2f,tmx,0, tmz, premikanje,poX,poY,poZ));
			posX = xpos;
			posZ = zpos;
			saveTrikotnik = trenutniTrikotnik;
			stPiramid ++;
			try{
				String hud = "Save points left: "+String.valueOf(20-stPiramid);
				father.setHudText(hud);
			}catch(Exception e){
				Log.e("HUD: ","V dodaj piramido!");
			}
		}
	}
	
	
	public void RESETLVL(){
		trenutniTrikotnik = 2;
		zidovi.clear();
		piramide.clear();
		stikala.clear();
		vrata.clear();
		boss = null;
		stPiramid = 0;
		createMovingWalls();
		createSwitch();
		xpos = 0;
		zpos = 0;
		resetTexture = true;
		
		try{
			String hud = "Save points left: "+String.valueOf(20-stPiramid);
			father.setHudText(hud);
		}catch(Exception e){
			Log.e("HUD ", "V resetLvL!");
		}
	}
	
	
	
	public void createSwitch(){
		vrata.add(new Vrata(R.drawable.crate,0.5f,0.3f,0.1f,11.0f,0.20f,6.5f,new boolean[]{false,false,false},new float[]{-0.5f,0f,0.1f},new float[]{-0.2f,0f,0.1f},new float[]{-0.5f,0f,0.1f},new boolean[]{false,false,false}));
		
		
		stikala.add(new Stikalo(R.drawable.stone,0.1f,0.05f,0.1f,7.36f,0.05f,10.24f,new boolean[]{false,false,false},new float[]{-0.5f,0f,0.1f},new float[]{-0.2f,0f,0.1f},new float[]{-0.5f,0f,0.1f},0,vrata.get(0)));
		stikala.add(new Stikalo(R.drawable.stone,0.1f,0.05f,0.1f,9.3f,0.05f,11.9f,new boolean[]{false,false,false},new float[]{-0.5f,0f,0.1f},new float[]{-0.2f,0f,0.1f},new float[]{-0.5f,0f,0.1f},1,vrata.get(0)));
		stikala.add(new Stikalo(R.drawable.stone,0.1f,0.05f,0.1f,7.6f,0.05f,13.75f,new boolean[]{false,false,false},new float[]{-0.5f,0f,0.1f},new float[]{-0.2f,0f,0.1f},new float[]{-0.5f,0f,0.1f},2,vrata.get(0)));
	}
	
	public void createMovingWalls(){
		//zid {sirina/2, visina/2, dolzina/2, startX,startY,startZ, premikanje[],poX,poY,poZ}
		
				
		zidovi.add(new Zid(R.drawable.aluminijozadje,0.5f,0.3f,0.2f,0.5f, 0.2f, 13f, new boolean[]{true,false,false},new float[]{-0.5f,0.5f,0.01f},new float[]{-0.5f,0.5f,0.01f},new float[]{-0.5f,0.5f,0.01f}));
		zidovi.add(new Zid(R.drawable.aluminijozadje,0.5f,0.3f,1.0f,0.5f, 0.2f, 15f, new boolean[]{true,false,false},new float[]{-0.5f,0.5f,0.05f},new float[]{-0.5f,0.5f,0.01f},new float[]{-0.5f,0.5f,0.01f}));
		zidovi.add(new Zid(R.drawable.aluminijozadje,1.0f,0.3f,0.15f,1f, 0.2f, 17f, new boolean[]{true,false,false},new float[]{-0.5f,1.5f,0.01f},new float[]{-0.5f,0.5f,0.01f},new float[]{-0.5f,0.5f,0.01f}));
		


		zidovi.add(new Zid(R.drawable.spikes,0.5f,0.3f,0.50f,18.5f, 0.2f, 3.0f, new boolean[]{true,false,false},new float[]{18.5f,36.5f,0.1f},new float[]{-0.5f,0.5f,0.01f},new float[]{-0.5f,0.5f,0.01f}));
		
		
		//winner
		zidovi.add(new Zid(R.drawable.winner,0.5f,0.3f,0.02f,45.0f, 0.2f, 42.0f, new boolean[]{false,false,false},new float[]{18.5f,36.5f,0.08f},new float[]{-0.5f,0.5f,0.01f},new float[]{-0.5f,0.5f,0.01f}));
	
		
		//boss
		boss = new Boss(R.drawable.kdoek,0.2f,0.2f,0.2f,35.0f, 0.2f, 20.0f, new boolean[]{false,false,false},new float[]{18.5f,36.5f,0.08f},new float[]{-0.5f,0.5f,0.01f},new float[]{-0.5f,0.5f,0.01f},0.045f);
	}
		//TODO
	

	public void draw(GL10 gl, int filter) {
		if(resetTexture){
			loadGLTexture(gl,context);
			resetTexture = false;
		}
		filter = 2;
		
		//Bind the texture based on the given filter
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[filter]);

		float xtrans = -xpos;						
		float ztrans = -zpos;						
		float ytrans = -walkbias - 0.25f;			
		float sceneroty = 360.0f - yrot;			
		
		//View
		gl.glRotatef(lookupdown, 1.0f, 0, 0);		//pogled
		gl.glRotatef(sceneroty, 0, 1.0f, 0);		//glava
		gl.glTranslatef(xtrans, ytrans, ztrans);	//gor dol
					
		//Enable the vertex, texture and normal state
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				
		//Point to our buffers
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		//Draw the vertices as triangles
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertices.length / 3);
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		
		
	    //GL11.glEnable(GL11.GL_LIGHTING);
	    gl.glLightfv(gl.GL_LIGHT1, gl.GL_SPECULAR, allocFloats(new float[] { 0.99f, 0.99f, 0.95f, 1.0f}));
	    gl.glLightfv(gl.GL_LIGHT1, gl.GL_POSITION, allocFloats(new float[] { xpos,0.45f, zpos+2, 1}));
	    	    
	    
	    gl.glLightfv(gl.GL_LIGHT2, gl.GL_DIFFUSE , allocFloats(new float[] { 1.0f,0.0f, 0.0f, 1.0f}));
	    gl.glLightfv(gl.GL_LIGHT2, gl.GL_POSITION, allocFloats(new float[] { 45.0f,0.4f, 39.0f, 1}));
	    
	    gl.glLightfv(gl.GL_LIGHT3, gl.GL_DIFFUSE , allocFloats(new float[] { 0.6f, 0.3f,0.3f, 1.0f}));
	    gl.glLightfv(gl.GL_LIGHT3, gl.GL_POSITION, allocFloats(new float[] { 0.0f,0.39f, 12.0f, 1}));
	    
	    gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, allocFloats(new float[] { 0.5f, 0.5f, 0.5f, 1.0f}));
	    gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_SPECULAR, allocFloats(new float[] { 0.5f, 0.5f,0.5f, 1.0f}));
	    
	    gl.glFogf(gl.GL_FOG_MODE, gl.GL_EXP2);

	    gl.glEnable(gl.GL_COLOR_MATERIAL);
	    //gl.glEnable(gl.GL_LIGHT3);
	    //gl.glEnable(gl.GL_LIGHT2);
	    gl.glEnable(gl.GL_LIGHT1);
	    
	    
	    /////////////////////////////////////
		//gl.glScalef(0.8f, 0.8f, 0.8f); 			
		//gl.glRotatef(rquad, 1.0f, 1.0f, 1.0f);	//Rotate The Square On The X axis 
		for(int i=0;i<stikala.size();i++){
			stikala.get(i).draw(gl,filter);
			if(stikala.get(i).playerContact(xpos,zpos)) sound.playDoor();
		}
	    
	    
		for(int i=0;i<zidovi.size();i++){
			zidovi.get(i).draw(gl,filter);
		}

	    for(int i=0;i<piramide.size();i++){
	    	piramide.get(i).draw(gl,filter);
	    }
	    
		for(int i=0;i<zidovi.size();i++){
			if(zidovi.get(i).killed(xpos,zpos)){
				xpos = posX;
				zpos = posZ;
				trenutniTrikotnik = saveTrikotnik;
				father.vibrate(200);
			}
		}
		
		for(int i=0;i<vrata.size();i++){
			vrata.get(i).draw(gl,filter);
		}
		
		/////////////////////////////////////
		try{
		if(boss.player(xpos,zpos,gl,filter)){
			father.vibrate(200);
			xpos = posX;
			zpos = posZ;
			trenutniTrikotnik = saveTrikotnik;
		}
		}catch(NullPointerException e){
			Log.e("Boss: ","Zgleda da po resetu je nastavljen na null");
		}
	}
		
	
	
	public static FloatBuffer allocFloats(float[] floatarray)
	  {
	    FloatBuffer fb = ByteBuffer.allocateDirect(floatarray.length * 4).order(
	        ByteOrder.nativeOrder()).asFloatBuffer();
	    fb.put(floatarray).flip();
	    return fb;
	  }	
	
	public void drawText(GL10 gl){
		// Create an empty, mutable bitmap
		Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_4444);
		// get a canvas to paint over the bitmap
		Canvas canvas = new Canvas(bitmap);
		bitmap.eraseColor(0);

		// get a background image from resources
		// note the image format must match the bitmap format
		Drawable background = context.getResources().getDrawable(R.drawable.sivkamen);
		background.setBounds(0, 0, 256, 256);
		background.draw(canvas); // draw the background to our bitmap

		// Draw the text
		Paint textPaint = new Paint();
		textPaint.setTextSize(32);
		textPaint.setAntiAlias(true);
		textPaint.setARGB(0xff, 0x00, 0x00, 0x00);
		// draw the text centered
		String bb = "X: "+String.valueOf(xpos)+"  Z: "+String.valueOf(zpos) + " Y: "+String.valueOf(yrot);
		canvas.drawText(bb, 20,312, textPaint);

		//Generate one texture pointer...
		gl.glGenTextures(1, textures, 0);
		//...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		//Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

		//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		//Clean up
		bitmap.recycle();	
	}
	

	public void loadGLTexture(GL10 gl,Context context) {
		boss.loadGLTexture(gl,context);
		
		for(int i=0;i<zidovi.size();i++){
			zidovi.get(i).loadGLTexture(gl,context);
		}
		
		for(int i=0;i<stikala.size();i++){
			stikala.get(i).loadGLTexture(gl,context);
		}
		
		for(int i=0;i<vrata.size();i++){
			vrata.get(i).loadGLTexture(gl,context);
		}
		
		
		//Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(R.drawable.sivkamen);
		Bitmap bitmap = null;
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}

		//Generate there texture pointer
		gl.glGenTextures(3, textures, 0);

		//Create Nearest Filtered Texture and bind it to texture 0
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		//Create Linear Filtered Texture and bind it to texture 1
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		//Create mipmapped textures and bind it to texture 2
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
		gl.glTexParameterf(gl.GL_TEXTURE_2D,gl.GL_TEXTURE_WRAP_S,gl.GL_REPEAT);
		gl.glTexParameterf(gl.GL_TEXTURE_2D,gl.GL_TEXTURE_WRAP_T,gl.GL_REPEAT);
		/*
		 * This is a change to the original tutorial, as buildMipMap does not exist anymore
		 * in the Android SDK.
		 * 
		 * We check if the GL context is version 1.1 and generate MipMaps by flag.
		 * Otherwise we call our own buildMipMap implementation
		 */
		if(gl instanceof GL11) {
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			
		//
		} else {
			buildMipmap(gl, bitmap);
		}		
		
		//Clean up
		bitmap.recycle();
	}
	

	private void buildMipmap(GL10 gl, Bitmap bitmap) {
		//
		int level = 0;
		//
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();

		//
		while(height >= 1 || width >= 1) {
			//First of all, generate the texture from our bitmap and set it to the according level
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
			
			//
			if(height == 1 || width == 1) {
				break;
			}

			//Increase the mipmap level
			level++;

			//
			height /= 2;
			width /= 2;
			Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
			
			//Clean up
			bitmap.recycle();
			bitmap = bitmap2;
		}
	}
	
/* ***** Structure classes for the "World" ***** */	
	/**
	 * A classic Vertex definition with
	 * texture coordinates.
	 */
	public class Vertex {
		//
		public float x, y, z;
		//
		public float u, v;
	}
	
	/**
	 * The Triangle class, containing
	 * all Vertices for the Triangle
	 */
	public class Triangle {
		//
		public Vertex[] vertex = new Vertex[3];
	}

	/**
	 * The Sector class holding the number and
	 * all Triangles.
	 */
	public class Sector {
		//
		public int numtriangles;
		//
		public Triangle[] triangle;
	}

/* ***** Listener Events ***** */	
	/**
	 * Override the key listener to receive onKey events.
	 *  
	 */
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		//Handle key down events
		if(event.getAction() == KeyEvent.ACTION_DOWN) {
			return onKeyDown(keyCode, event);
		}
		
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			heading += 2.0f;	
			yrot = heading;					//Rotira v levo
			
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			heading -= 2.0f;
			yrot = heading;					//Rotira v desno
			
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			xpos -= (float)Math.sin(heading * piover180) * 0.1f;	
			zpos -= (float)Math.cos(heading * piover180) * 0.1f;	
			
			if(walkbiasangle >= 359.0f) {							
				walkbiasangle = 0.0f;								
			} else {
				walkbiasangle += 10;								
			}
			walkbias = (float)Math.sin(walkbiasangle * piover180) / 20.0f;

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			xpos += (float)Math.sin(heading * piover180) * 0.1f;	
			zpos += (float)Math.cos(heading * piover180) * 0.1f;	
			
			if(walkbiasangle <= 1.0f) {								
				walkbiasangle = 359.0f;								
			} else {
				walkbiasangle -= 10;								
			}
			walkbias = (float)Math.sin(walkbiasangle * piover180) / 20.0f;	
		} else if (keyCode == KeyEvent.KEYCODE_BACK){
			//TODO
			//sm.unregisterListener(this);
			xpos = 0f;
			zpos = 0f;
			trenutniTrikotnik = 1;
		}

		//We handled the event
		return true;
	}

	/**
	 * React to moves on the touchscreen.
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//
		boolean handled = false;
		
		//
		float x = event.getX();
        float y = event.getY();
        
        //If a touch is moved on the screen
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
        	//Calculate the change
        	float dx = x - oldX;
	        float dy = y - oldY;
        	        		
	        //Up and down looking through touch
    	    lookupdown += dy * TOUCH_SCALE;
    	    //Look left and right through moving on screen
    	    heading = (heading - dx * TOUCH_SCALE) % 360;
    	    yrot = heading;

    	    //We handled the event
            handled = true;
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
        	zadnjiCas = System.nanoTime();
        }else if(event.getAction() == MotionEvent.ACTION_UP){
        	float tCas = System.nanoTime();
        	Log.i("CAS: ",String.valueOf(tCas-zadnjiCas));
        	if(tCas-zadnjiCas  < 120000000){
        		addPiramid();
        	}
        }
        
        //Remember the values
        oldX = x;
        oldY = y;
        
        //
		return handled;
	}
	
	public void moveAround(float x,float y, float z){
		float hitrost = 180;
		
		if(collisionDetection(x,y,z,hitrost)){
			//LEVO DESNO
			if ( Math.abs(x) < 9){
				
				y = (float) ((y-1)/hitrost);
				zpos -= (float)Math.sin(heading * piover180) * y;
				xpos += (float)Math.cos(heading * piover180) * y;
			}
			
			if (Math.abs(z) > 3){
				if (x < 5) x = 5;
				x = (float) ((13-x*8/9)/hitrost);
				if(z > 0){ // NAPREJ
					xpos -= (float)Math.sin(heading * piover180) * x;	//x
					zpos -= (float)Math.cos(heading * piover180) * x;	//z
					
					if(walkbiasangle <= 1.0f) {								
						walkbiasangle = 359.0f;								
					} else {
						walkbiasangle -= 10;								
					}
					walkbias = (float)Math.sin(walkbiasangle * piover180) / 20.0f;			
				}else{ // NAZAJ
					x = Math.abs(x);
					xpos += (float)Math.sin(heading * piover180) * x;	
					zpos += (float)Math.cos(heading * piover180) * x;	
					
					if(walkbiasangle <= 1.0f) {								
						walkbiasangle = 359.0f;								
					} else {
						walkbiasangle -= 10;								
					}
					walkbias = (float)Math.sin(walkbiasangle * piover180) / 20.0f;			
				}
			}
			
			Log.i("Position: "," X ="+String.valueOf(xpos)+"  Z ="+String.valueOf(zpos)+" Trikotnik: "+String.valueOf(trenutniTrikotnik));
		}
	}
	
	public boolean collisionDetection(float x,float y,float z,float hitrost){
		float Tzpos = zpos;
		float Txpos = xpos;
		
		
		//LEVO DESNO
		if ( Math.abs(x) < 9){
			
			y = (float) ((y-1)/hitrost);
			Tzpos -= (float)Math.sin(heading * piover180) * y;
			Txpos += (float)Math.cos(heading * piover180) * y;
		}
		
		if (Math.abs(z) > 3){
			if (x < 5) x = 5;
			x = (float) ((13-x*8/9)/hitrost);
			if(z > 0){ // NAPREJ
				Txpos -= (float)Math.sin(heading * piover180) * x;	//x
				Tzpos -= (float)Math.cos(heading * piover180) * x;	//z
				
				if(walkbiasangle <= 1.0f) {								
					walkbiasangle = 359.0f;								
				} else {
					walkbiasangle -= 10;								
				}
				walkbias = (float)Math.sin(walkbiasangle * piover180) / 20.0f;	//gor dol v korak		
			}else{ // NAZAJ
				x = Math.abs(x);
				Txpos += (float)Math.sin(heading * piover180) * x;	//premakne po x
				Tzpos += (float)Math.cos(heading * piover180) * x;	//premakne po z
				
				if(walkbiasangle <= 1.0f) {								
					walkbiasangle = 359.0f;								
				} else {
					walkbiasangle -= 10;								//Gor dol v koraku
				}
				walkbias = (float)Math.sin(walkbiasangle * piover180) / 20.0f;			
			}
		}
		
		for(int i=0;i<zidovi.size();i++){ //kontakt z zidom
			if(zidovi.get(i).playerContact(Txpos,Tzpos)) return false;
		}
		for(int i=0;i<vrata.size();i++){
			if(vrata.get(i).playerContact(Txpos,Tzpos)) return false;
		}
		
		if(vsaTla.get(trenutniTrikotnik).isNotCollision(Txpos,Tzpos)) return true;
		else{
			int[] sosed = sosedje.get(trenutniTrikotnik);
			
			for(int i=0;i<sosed.length;i++){
				if(vsaTla.get(sosed[i]).isNotCollision(Txpos, Tzpos)){
					trenutniTrikotnik = sosed[i];
					return true;
				}
			}
			
			return false;
		}
	}
		
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
    
	@Override
	public void onSensorChanged(SensorEvent event) {
		angleX=event.values[0];
		angleY=event.values[1];
		angleZ=event.values[2];
		
		try{
		moveAround(angleX,angleY,angleZ);
		}catch(NullPointerException e){
			Log.e("Null pointer Exception","V move around + collision detection");
		}
	}
	
	
}

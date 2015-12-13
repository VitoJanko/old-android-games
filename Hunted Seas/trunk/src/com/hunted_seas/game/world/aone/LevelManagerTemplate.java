package com.hunted_seas.game.world.aone;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.Activity;
import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.objects.masters.BachedBackgroundMaster;
import com.hunted_seas.game.objects.masters.BachedSpriteMaster;
import com.hunted_seas.game.objects.masters.SpriteMaster;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.SkyBox;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;
import com.hunted_seas.game.world.acommon.SurfaceCircle;
import com.hunted_seas.game.world.player.Player;


@SuppressLint("UseSparseArrays")
abstract class LevelManagerTemplate extends LevelManager{

	protected GameObjects objects;
	
	
	public LevelManagerTemplate(Activity activity, String levelName){
		super(activity);
		waypoints = new HashMap<Integer,Float[]>();
		objects = (new LevelLoader()).loadLevel(levelName, this, activity);
	}
	
	@Override
	public boolean loadResources() {
		
		objects.backgroundMasters.add(new BachedBackgroundMaster(this,SurfaceCircle.texture,SurfaceCircle.SIZE));
		createSky();
		
		TEXTURE_SHADER_PROGRAM.useProgram();
		
		setPlayer(new Player(this));
		getPlayer().loadSprite(activity, TEXTURE_SHADER_PROGRAM);
		getPlayer().setLocation(objects.player_x, objects.player_y);
		
		for(SpriteMaster master: objects.regularMasters){
			master.loadSprite(activity, TEXTURE_SHADER_PROGRAM);
		}
		for(SpriteManagerInterface master: objects.multipartMasters){
			master.loadSprite(activity, TEXTURE_SHADER_PROGRAM);
		}
		
		for(SkyBox s : objects.backgrounds)
			s.loadBackgroundBox(activity, TEXTURE_SHADER_PROGRAM);
		for(SkyBox s : objects.foregrounds)
			s.loadBackgroundBox(activity, TEXTURE_SHADER_PROGRAM);
		
		BACHING_TEXTURE_SHADER_PROGRAM.useProgram();
			
		for(SpriteManagerInterface master: objects.multipartBachedMasters){
			master.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM);
		}
		
		for(BachedBackgroundMaster master: objects.backgroundMasters){
			master.loadSprite(activity, BACHING_TEXTURE_SHADER_PROGRAM);
		}
		
		for(BachedSpriteMaster master : objects.bachedMasters){
			master.loadSprite(activity,BACHING_TEXTURE_SHADER_PROGRAM);
			master.updateBoundingBox();
		}
		
		BACHING_TEXTURE_DEPTH_SHADER_PROGRAM.useProgram();
		
		for(BachedSpriteMaster master : objects.darkBachedMasters){
//			master.loadSprite(activity,BACHING_TEXTURE_DEPTH_SHADER_PROGRAM, master.getTexture(), master.getModel());
			master.updateBoundingBox();
		}
		
		return true;
	}

	@Override
	public void finish() {
		setFinishLevel(true);
		getPlayer().finish();
		for(SpriteManagerInterface master: objects.allMasters)
			master.finish();
		activity.finish();
	}

	@Override
	public void destroy() {
		setPlayer(null);
		objects.bachedMasters.clear();
		objects.darkBachedMasters.clear();
		objects.backgroundMasters.clear();
		objects.regularMasters.clear();
		objects.allMasters.clear();
		objects.multipartBachedMasters.clear();
	}

	
	@Override
	public void step(float stepScale) {
		for(SpriteManagerInterface master: objects.allMasters)
			master.step(stepScale);
	}

	@Override
	public boolean setUp_CollisionDetector(CollisionDetector_Manager collisionDetector) {
		collisionDetector.setPlayer(getPlayer());
		for(BachedSpriteMaster master : objects.bachedMasters){
			if(master.collision)
				master.setUp_CollisionDetector(collisionDetector);
		}
		for(SpriteMaster master : objects.regularMasters){
			if(master.collision)
				master.setUp_CollisionDetector(collisionDetector);
		}
		for(SpriteManagerInterface master : objects.multipartMasters){
			master.setUp_CollisionDetector(collisionDetector);
		}
		for(SpriteManagerInterface master : objects.multipartBachedMasters){
			master.setUp_CollisionDetector(collisionDetector);
		}
		return true;
	}

	
	public void createSky(){
		float x = leftBound-500;
		float y = topBound-20;
		while(x<rightBound+500){
			SurfaceCircle sc = new SurfaceCircle((float) (x+Math.random()*20),(float) (y+Math.random()*10),0,0);
			sc.setScale((float)(0.9+Math.random()*0.2));
			objects.backgroundMasters.get(objects.backgroundMasters.size()-1).addElement(sc);
			x+=166;
		}
		objects.allMasters.add(objects.backgroundMasters.get(objects.backgroundMasters.size()-1));
	}

}

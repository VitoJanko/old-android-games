package com.hunted_seas.game.world.aone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;

import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.objects.masters.BachedBackgroundMaster;
import com.hunted_seas.game.objects.masters.BachedSpriteMaster;
import com.hunted_seas.game.objects.masters.SpriteMaster;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.SkyBox;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.acommon.SpriteManagerInterface;
import com.hunted_seas.game.world.acommon.SurfaceCircle;
import com.hunted_seas.game.world.aone.background.Rock1;
import com.hunted_seas.game.world.aone.background.Rock2;
import com.hunted_seas.game.world.aone.background.Rock3;
import com.hunted_seas.game.world.awone.blowfish.BlowFishMaster;
import com.hunted_seas.game.world.awone.blowfish.BlowfishBody;
import com.hunted_seas.game.world.cthree.Chili;
import com.hunted_seas.game.world.cthree.Coin;
import com.hunted_seas.game.world.cthree.Key;

public class LevelLoader {

	HashMap<Integer,BachedSpriteMaster> map;
	HashMap<Integer,BachedSpriteMaster> darkMap;
	HashMap<Integer,BachedBackgroundMaster> bachedBox;
	HashMap<Integer,SpriteMaster> regularMap;
	HashMap<Integer,SpriteManagerInterface> extras;
//	HashMap<Integer,SkyBox> foregroundMap;
//	HashMap<Integer,SkyBox> backgroundMap;
	GameObjects objects;
	
	
	@SuppressLint("UseSparseArrays")
	GameObjects loadLevel(String name, LevelManager level, Context contex){
		map = new HashMap<Integer,BachedSpriteMaster>();
		darkMap = new HashMap<Integer,BachedSpriteMaster>();
		bachedBox = new HashMap<Integer,BachedBackgroundMaster>();
		regularMap = new HashMap<Integer,SpriteMaster>();
		extras = new HashMap<Integer,SpriteManagerInterface>();
		objects = new GameObjects();
		LevelLoaderTypes types = new  LevelLoaderTypes();
		AssetManager am = contex.getAssets();
		try {
			InputStream is = am.open(name);
			InputStreamReader inputStreamReader = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String w = bufferedReader.readLine();
			String h = bufferedReader.readLine();
			float width = Float.valueOf(w);
			float height = Float.valueOf(h);
			level.setLeftBound(-width/2);
			level.setRightBound(width/2);
			level.setBottomBound(-height/2);
			level.setTopBound(height/2);
			String line = bufferedReader.readLine();
			while(line!=null){
				String[] tokens = line.split(" ");
				int type = Integer.valueOf(tokens[0]);
				if(type==-1){
					float x1 = Float.valueOf(tokens[1]);
					float y1 = Float.valueOf(tokens[2]);
					float x2 = Float.valueOf(tokens[3]);
					float y2 = Float.valueOf(tokens[4]);
					int index = Integer.valueOf(tokens[5]);
					level.waypoints.put(index, new Float[]{x1,y1,x2,y2});
				}
				else{
					int index = 0;
					if(tokens.length>14)
						index = Integer.valueOf(tokens[14]);
					float x = Float.valueOf(tokens[1]);
					float y = Float.valueOf(tokens[2]);
					float z = Float.valueOf(tokens[3]);
					float scale = Float.valueOf(tokens[4]);
					float rotation = Float.valueOf(tokens[5]);
					boolean reflectedH = Boolean.valueOf(tokens[6]);
					boolean reflectedV = Boolean.valueOf(tokens[7]);
					boolean hasModel = Boolean.valueOf(tokens[9]);
					if(type==35){
						objects.player_x=x;
						objects.player_y=y;
					}
					else if(hasModel){
						if(type==0){
							BlowfishBody b = new BlowfishBody(0,0,6,0,level);
							b.setAttributes(x, y, -999, scale, rotation, reflectedH, reflectedV, index);
							if(extras.containsKey(type))
								((BlowFishMaster)(extras.get(type))).addNewFish(b);
							else{
								BlowFishMaster bm = new BlowFishMaster(level,true,0);
								bm.addNewFish(b);
								extras.put(type, bm);
								objects.multipartMasters.add(bm);
							}
						}
						else if (type==36){
							GreenFishMaster gm;
							if(extras.containsKey(type))
								gm = (GreenFishMaster)(extras.get(type));
							else{
								gm = new GreenFishMaster(level,true,0);
								extras.put(type, gm);
								objects.multipartBachedMasters.add(gm);
							}
							GreenFish f = new GreenFish(x,y,(float)(Math.random()*7+5),-1,gm);
	//						f.setAttributes(x, y, -999 , scale, rotation, reflectedH, reflectedV);
							f.respawn = true;
							gm.addFish(f);
						}
						else if (type==37){
							GreenFishMaster gm;
							if(extras.containsKey(type))
								gm = (GreenFishMaster)(extras.get(type));
							else{
								gm = new GreenFishMaster(level,true,2);
								extras.put(type, gm);
								objects.multipartBachedMasters.add(gm);
							}
							GreenFish f = new GreenFish(x,y,20,-1,gm);
	//						f.setAttributes(x, y, -999 , scale, rotation, reflectedH, reflectedV);
							f.wait = true;
							f.level = level;
							gm.addFish(f);
						}
						else{
							ObjectInfo oi = returnSpriteByType(type,level);
							if(oi!=null){
								oi.sprite.setAttributes(x, y, z, scale, rotation, reflectedH, reflectedV, index);
								boolean isDark = Boolean.valueOf(tokens[8]);
								addElement(oi,type,level,isDark, types.bachedObjects.contains(type));
							}
						}
	
					}
					else{
						float x1 = Float.valueOf(tokens[10]);
						float y1 = Float.valueOf(tokens[11]);
						float x2 = Float.valueOf(tokens[12]);
						float y2 = Float.valueOf(tokens[13]);
						if(types.bachedObjects.contains(type)){
							float sizeX = x2-x1;
							float sizeY = y2-y1;
							float centerX = (x2+x1)/2;
							float centerY = (y2+y1)/2;
							float[] bounds = {-sizeX/2,sizeX/2,sizeY/2,-sizeY/2};
							ObjectInfo oi = returnSpriteByType(type,level);
							oi.sprite.setAttributes(centerX, centerY, z, 1, 0, false, false, index);
							if(bachedBox.containsKey(type)){
								bachedBox.get(type).addElement(oi.sprite);
								float[] oldBounds = bachedBox.get(type).bounds;
								float oldSize = oldBounds[1]-oldBounds[0];
								float newScale = sizeX/oldSize;
								oi.sprite.setScale(newScale);
							}
							else{
								BachedBackgroundMaster target = new BachedBackgroundMaster(level,oi.texture[0],bounds);
								bachedBox.put(type, target);
								objects.backgroundMasters.add(target);
								target.addElement(oi.sprite);
							}
						}
						else if(types.singleSquare.contains(type)){
							
						}
						else{
							String texture = types.textures.get(type);
							if(texture!=null){
								SkyBox s = new SkyBox(level,0,0);
								s.setBounds(new BoundingBox(x1,x2,y1,y2,z));
								s.setTexture(texture);
								if(z<50)
									objects.backgrounds.add(s);
								else
									objects.foregrounds.add(s);
							}
						}
					}
				}
				line = bufferedReader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		objects.unify();
		return objects;
	}
	
	ObjectInfo returnSpriteByType(int type, LevelManager level){
		if(type==25){
			return new ObjectInfo(SurfaceCircle.texture, new SurfaceCircle(0,0,0,0));
		}
//		if(type==0)
////			return new Blowfish(0,0,0);
		if(type==12)
			return new ObjectInfo(Urchin.model,Urchin.texture, new Urchin(0,0,0,level));
		if(type==27)
			return new ObjectInfo(Coin.model,Coin.texture,new Coin(0,0,0));
		if(type==26)
			return new ObjectInfo(Chili.model,Chili.texture,new Chili(0,0,0,level));
		if(type==38)
			return new ObjectInfo(Key.model,Key.texture,new Key(0,0,0,level));
		if(type==1)
			return new ObjectInfo(Food.model,Food.TEXTURE,new Food(0,0,1, 1));
		if(type==2)
			return new ObjectInfo(Rock1.model, Rock1.texture, new Rock1());
		if(type==4)
			return new ObjectInfo(Rock2.model, Rock2.texture, new Rock2());
		if(type==3)
			return new ObjectInfo(Rock3.model, Rock3.texture, new Rock3());;
//		if(type==4
//			return new RockTwoSprite();
//		if(type==3)
//			return new RockThreeSprite();
//		if(type==11)
//			return new Sign();
//		else return null;
		return null;
	}
	
	
	void addElement(ObjectInfo oi, int type, LevelManager level, boolean isDark, boolean isBatched){
		if(isBatched){
			if(map.containsKey(type))
				map.get(type).addElement(oi.sprite);
			else{
				BachedSpriteMaster target = new BachedSpriteMaster(oi.model,oi.texture,level,oi.sprite.isCollision());
				map.put(type, target);
				objects.bachedMasters.add(target);
				target.addElement(oi.sprite);
			}
		}
		else{
			if(regularMap.containsKey(type))
				regularMap.get(type).addElement(oi.sprite);
			else{
				SpriteMaster target = new SpriteMaster(oi.model,oi.texture,level,oi.sprite.isCollision());
				regularMap.put(type, target);
				objects.regularMasters.add(target);
				target.addElement(oi.sprite);
			}
		}
//		else{
//			if(darkMap.containsKey(type))
//				darkMap.get(type).addElement(s);
//			else{
//				BachedSpriteMaster target = new BachedSpriteMaster(s.getModel(),s.getTexture(),level,s.isCollision());
//				darkMap.put(type, target);
//				objects.darkBachedMasters.add(target);
//				target.addElement(s);
//			}
//		}
	}
	
}

class ObjectInfo{
	
	String[] texture;
	int[] model;
	Sprite sprite;
	
	
	public ObjectInfo(String texture, Sprite sprite) {
		super();
		this.texture = new String[]{texture};
		this.sprite = sprite;
	}
	
	public ObjectInfo(int model, String texture, Sprite sprite) {
		super();
		this.model = new int[]{model};
		this.texture = new String[]{texture};
		this.sprite = sprite;
	}
	
	public ObjectInfo(int[] model, String[] texture, Sprite sprite) {
		super();
		this.model = model;
		this.texture = texture;
		this.sprite = sprite;
	}
	
}

class GameObjects{
	
	ArrayList<BachedSpriteMaster> bachedMasters;
	ArrayList<BachedSpriteMaster> darkBachedMasters;
	ArrayList<BachedBackgroundMaster> backgroundMasters;
	ArrayList<SpriteMaster> regularMasters;
	ArrayList<SpriteManagerInterface> multipartMasters;
	ArrayList<SpriteManagerInterface> multipartBachedMasters;
	ArrayList<SkyBox> backgrounds;
	ArrayList<SkyBox> foregrounds;
	ArrayList<SpriteManagerInterface> allMasters;
	float player_x;
	float player_y;
	
	public void unify(){
		allMasters = new ArrayList<SpriteManagerInterface>();
		allMasters.addAll(bachedMasters);
		allMasters.addAll(darkBachedMasters);
		allMasters.addAll(backgroundMasters);
		allMasters.addAll(regularMasters);
		allMasters.addAll(multipartMasters);
		allMasters.addAll(multipartBachedMasters);
	}
	
	GameObjects(){
		bachedMasters = new ArrayList<BachedSpriteMaster>();
		darkBachedMasters = new ArrayList<BachedSpriteMaster>();
		foregrounds = new ArrayList<SkyBox>();
		backgrounds = new ArrayList<SkyBox>();
		backgroundMasters = new ArrayList<BachedBackgroundMaster>();
		regularMasters = new ArrayList<SpriteMaster>();
		multipartMasters = new ArrayList<SpriteManagerInterface>();
		multipartBachedMasters = new ArrayList<SpriteManagerInterface>();
	}
	
}
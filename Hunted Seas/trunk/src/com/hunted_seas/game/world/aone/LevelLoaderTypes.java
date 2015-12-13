package com.hunted_seas.game.world.aone;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressLint("UseSparseArrays")
public class LevelLoaderTypes {

	Set<Integer> bachedObjects;
	Set<Integer> model;
	Set<Integer> singleSquare;
	Set<Integer> multipart;
	
	HashMap<Integer,String> textures;
	
	String sand1 = "world_one/background/sand/sand1";
	String sand2 = "world_one/background/sand/sand2";
	String sand3 = "world_one/background/sand/sand3";
	String sandhill1 = "world_one/background/sand/sandhill1";
	String sandhill2 = "world_one/background/sand/sandhill2";
	String sandhill3 = "world_one/background/sand/sandhill3";
	String sandhill4 = "world_one/background/sand/sandhill4";
	String sandhill5 = "world_one/background/sand/sandhill5";
	String seabed = "world_one/background/sand/seabed";
	
	String light1 = "world_one/background/light/light1";
	String light2 = "world_one/background/light/light4";
	
	String sign = "world_one/background/other/sign";
	String sign1 = "world_one/background/other/sign1";
	
	String seaweed1 = "world_one/background/seaweed/seaweed1";
	String seaweed2 = "world_one/background/seaweed/seaweed2";
	String seaweed3 = "world_one/background/seaweed/seaweed3";
	String seaweed4 = "world_one/background/seaweed/seaweed4";
	String seaweed1b = "world_one/background/seaweed/seaweed1b";
	String seaweed2b = "world_one/background/seaweed/seaweed2b";
	String seaweed3b = "world_one/background/seaweed/seaweed3b";
	String seaweed4b = "world_one/background/seaweed/seaweed4b";
	
	LevelLoaderTypes(){
		singleSquare = new HashSet<Integer>();
		multipart = new HashSet<Integer>();
		multipart.add(0);
		bachedObjects = new HashSet<Integer>();
		bachedObjects.add(25);
		bachedObjects.add(1);
		model = new HashSet<Integer>();
		model.add(26);
		model.add(2);
		model.add(3);
		model.add(4);
		model.add(0);
		model.add(27);
		model.add(12);
		textures = new HashMap<Integer,String>();
		textures.put(5,sand1);
		textures.put(6,sand3);
		textures.put(7,sand2);
		textures.put(14,sandhill1);
		textures.put(15,sandhill2);
		textures.put(16,sandhill3);
		textures.put(17,sandhill4);
		textures.put(18,sandhill5);
		textures.put(28, light1);
		textures.put(29, light2);
		textures.put(11, sign);
		textures.put(34, sign1);
		textures.put(19, seaweed1);
		textures.put(20, seaweed2);
		textures.put(21, seaweed3);
		textures.put(22, seaweed4);
		textures.put(30, seaweed1b);
		textures.put(31, seaweed2b);
		textures.put(32, seaweed3b);
		textures.put(33, seaweed4b);
		
	}
	
}

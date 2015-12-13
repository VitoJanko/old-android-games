package com.hunted_seas.game.objects.masters;

import com.hunted_seas.game.collision.CollisionDetector_Manager;
import com.hunted_seas.game.world.acommon.Lights;

public interface Master {

	public void setUp_CollisionDetector(CollisionDetector_Manager collisionDetector);	
	public boolean step(float stepScale);
	public void draw(float[] viewMatrix, Lights lights); 
	public void pause();
	public void finish();
	public void destroy();
	
}

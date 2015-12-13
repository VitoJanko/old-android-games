package com.hunted_seas.game.world.aone;

import com.hunted_seas.game.R;
import com.hunted_seas.game.collision.ColidableObjectInterface;
import com.hunted_seas.game.data.BoundingBox;
import com.hunted_seas.game.objects.masters.SpriteMaster;
import com.hunted_seas.game.world.acommon.LevelManager;
import com.hunted_seas.game.world.acommon.Sprite;
import com.hunted_seas.game.world.player.Player;

/**
 * Bubble.
 * 
 * @see Sprite
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public class Urchin extends Sprite {
	
	public static String texture = "temp/urchin";
	public static int model = R.raw.urchin;
	
	LevelManager level;
	float circumference;
		
	public Urchin(float x, float y, float speed, LevelManager level){
		super(x, y, 0, speed,1);
		collision = true;
		if(speed==0)
			this.speed = (float) (Math.random()*2+2);
		angle = 0;
		if(Math.random()<0.5)
			direction = -1;
		setScale(2);
		
		this.level = level;
	}

	@Override
	public boolean step(float stepScale) {
		float move = stepScale * speed * direction;
		circumference = (float) (Math.PI*(boundingBox[0].getWidth()))*getScale();
		x += move;
		angle += (2*Math.PI*move)/circumference;
		if(x>=master.getLvL().getRightBound())
			direction = -1;
		if(x<=master.getLvL().getLeftBound())
			direction = 1;
		
		for(SpriteMaster master : ((LevelManagerTemplate)level).objects.regularMasters){
			if(master.collision){
				for(int i=0; i<master.spritesSize(); i++){
					try{
						Sprite bub = master.getSprite(i);
						BoundingBox bbO = bub.getBoundBox()[0];
						BoundingBox bb = getBoundBox()[0];
						if(bb.left<bbO.right)
							direction = 1;
						if(bb.right>bbO.left)
							direction = -1;
					}catch(Exception e){}
				}
			}
		}
		return true;
	}


	@Override
	public void resolveCollision(ColidableObjectInterface sprite, float[] pointOfCollision) {
		Player player = (Player)sprite; 
		player.changeHealth(-15);
		float alpha = getDirectionToPoint(sprite.getPosition(),getPosition());
		player.graduallyMovePlayer(alpha, 40, 4f);
	}

}

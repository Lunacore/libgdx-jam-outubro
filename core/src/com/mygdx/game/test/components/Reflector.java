package com.mygdx.game.test.components;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;

public class Reflector extends Platform{

	
	public Reflector(ObjectInfo info, MapProperties properties) {
		super(info, properties);
	
	}
	
	public void resizeBox(Rectangle2D oldRect, Rectangle2D newRect) {
		//Posição
		float relativeX =
				(float) ((body.getWorldCenter().x - oldRect.getX()/State.PHYS_SCALE) / (oldRect.getWidth() / State.PHYS_SCALE));		
		float newX = (float) (newRect.getX() / State.PHYS_SCALE + (newRect.getWidth() / State.PHYS_SCALE * relativeX));
				
		float relativeY =
				(float) ((body.getWorldCenter().y - oldRect.getY()/State.PHYS_SCALE) / (oldRect.getHeight() / State.PHYS_SCALE));		
		float newY = (float) (newRect.getY() / State.PHYS_SCALE + (newRect.getHeight() / State.PHYS_SCALE * relativeY));

		body.setTransform(new Vector2(newX, newY), 0);
	}
	
}

package com.mygdx.game.test.components;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;

public class Reflector extends Platform{

	boolean posicao;
	
	public Reflector(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		posicao = get("posicao") != null ? get("posicao", Boolean.class) : true;
		
		
	}
	
	@Override
	public void resizeBox(Rectangle2D oldRect, Rectangle2D newRect) {
		if(posicao) {
			//Posição
			float relativeX =
					(float) ((body.getWorldCenter().x - oldRect.getX()/State.PHYS_SCALE) / (oldRect.getWidth() / State.PHYS_SCALE));		
			float newX = (float) (newRect.getX() / State.PHYS_SCALE + (newRect.getWidth() / State.PHYS_SCALE * relativeX));
					
			float relativeY =
					(float) ((body.getWorldCenter().y - oldRect.getY()/State.PHYS_SCALE) / (oldRect.getHeight() / State.PHYS_SCALE));		
			float newY = (float) (newRect.getY() / State.PHYS_SCALE + (newRect.getHeight() / State.PHYS_SCALE * relativeY));
	
			body.setTransform(new Vector2(newX, newY), body.getAngle());
		}
	}
	
}

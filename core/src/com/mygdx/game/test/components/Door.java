package com.mygdx.game.test.components;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateOne;

public class Door extends Platform {

	boolean open;
	
	float originalHeight;
	float currentHeight;
	float targetHeight;
	
	float timeOpen;
	float openTimer;
	
	public Door(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		//tamanho = true;
		originalHeight = get("height", Float.class) / State.PHYS_SCALE;
		currentHeight = originalHeight;
		body.setUserData(this);
		
		timeOpen = get("timeOpen", Float.class);
	}
	
	public void toggle() {
		open = !open;
		
		if(open) {
			openTimer = timeOpen;
		}
	}
	
	public boolean update(float delta) {
		super.update(delta);
		
		float oldOp = openTimer;
		openTimer -= delta * ((StateOne)getState()).getWorldSpeed();
		
		if(oldOp > 0 && openTimer < 0) {
			toggle();
		}
		
		PolygonShape shape = (PolygonShape) body.getFixtureList().get(0).getShape();
		shape.setAsBox(
				get("width", Float.class) / State.PHYS_SCALE / 2f, currentHeight / 2f,
				new Vector2(get("width", Float.class) / State.PHYS_SCALE / 2f, currentHeight / 2f),
				0);
		
		if(open)
			targetHeight = 0;
		else
			targetHeight = originalHeight;
		
		currentHeight += (targetHeight - currentHeight)/5f;
		
		return false;
	}
	
	public boolean isOpen() {
		return open;
	}

}

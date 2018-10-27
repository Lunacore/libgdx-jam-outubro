package com.mygdx.game.test.components;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateOne;

public class LaserEmitter extends Platform{
	
	float frequency;
	float laserTimer = 0;
	float laserSpeed = 1;
	
	StateOne state;
	Vector2 emitDirection;
	
	Body body;

	public LaserEmitter(ObjectInfo info, MapProperties properties) {
		super(info, properties);

		body = get("body", Body.class);
		body.setUserData(this);
		state = (StateOne) getState();
		
		
		laserSpeed = get("laserSpeed", Float.class);
		emitDirection = Helper.newPolarVector(get("emitDirection", Float.class), laserSpeed);
		frequency = get("frequency", Float.class);
		
	}
	
	public boolean update(float delta) {
		super.update(delta);
	
		laserTimer += delta * ((StateOne)getState()).getWorldSpeed();
		
		if(laserTimer > frequency / state.getWorldSpeed()) {
			laserTimer -= frequency / state.getWorldSpeed();
			Vector2 sz = new Vector2(get("width", Float.class) / 2f / State.PHYS_SCALE, get("height", Float.class) / 2f / State.PHYS_SCALE);
			Laser laser = new Laser(info, body.getWorldCenter().cpy().add(sz).scl(State.PHYS_SCALE), emitDirection);
			getState().putInScene(laser);
		}
		
		return false;
	}

}
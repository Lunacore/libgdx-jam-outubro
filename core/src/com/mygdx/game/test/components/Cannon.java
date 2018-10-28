package com.mygdx.game.test.components;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateOne;

public class Cannon extends Platform{
	
	float frequency;
	Vector2 direction;
	float strength;
	StateOne state;
	
	Body body;
	
	float timer;

	public Cannon(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		body = get("body", Body.class);
		body.setUserData(this);
		state = (StateOne) getState();
		
		strength = get("strength", Float.class);
		direction = Helper.newPolarVector(get("direction", Float.class), 1);
		frequency = get("frequency", Float.class);
	}
	
	public boolean update(float delta) {
		super.update(delta);
	
		timer += delta * ((StateOne)getState()).getWorldSpeed();
		
		if(timer > frequency / state.getWorldSpeed()) {
			timer -= frequency / state.getWorldSpeed();
			Vector2 sz = new Vector2(get("width", Float.class) / 2f / State.PHYS_SCALE, get("height", Float.class) / 2f / State.PHYS_SCALE);
			CannonBall ball = new CannonBall(info, body.getWorldCenter().cpy().add(sz).scl(State.PHYS_SCALE), direction.cpy().scl(strength));
			getState().putInScene(ball);
		}
		
		return false;
	}

}

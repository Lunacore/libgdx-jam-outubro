package com.mygdx.game.test.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateOne;

public class Cannon extends Platform{
	
	float frequency;
	Vector2 direction;
	float strength;
	StateOne state;
	float ballSize;
	float timer = -1;
	Sound fire;

	public Cannon(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		body.setUserData(this);
		state = (StateOne) getState();
		
		strength = get("strength", Float.class, 10f);
		direction = Helper.newPolarVector((float) Math.toDegrees(body.getAngle()), 1);
		frequency = get("frequency", Float.class, 1f);
		ballSize = get("ballSize", Float.class, 10f);
		fire = Gdx.audio.newSound(Gdx.files.internal("audio/cannon.ogg"));
	}
	
	public boolean update(float delta) {
		super.update(delta);
	
		timer += delta * ((StateOne)getState()).getWorldSpeed();
		
		if(timer > frequency / state.getWorldSpeed()) {
			timer -= frequency / state.getWorldSpeed();
			Vector2 sz = new Vector2(get("width", Float.class) / 2f / State.PHYS_SCALE, get("height", Float.class) / 2f / State.PHYS_SCALE);
			sz.rotate((float) Math.toDegrees(body.getAngle()));
			CannonBall ball = new CannonBall(info, body.getWorldCenter().cpy().add(sz).scl(State.PHYS_SCALE), direction.cpy().scl(strength), ballSize);
			getState().putInScene(ball);
			fire.play(0.3f);
		}
		
		return false;
	}

}

package com.mygdx.game.test.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
	Vector2 emitCenter;

	public LaserEmitter(ObjectInfo info, MapProperties properties) {
		super(info, properties);

		tamanho = true;
		body.setUserData(this);
		state = (StateOne) getState();
		
		laserSpeed = get("laserSpeed", Float.class);
		emitDirection = Helper.newPolarVector(-get("rotation", Float.class), 1);
		frequency = get("frequency", Float.class);
		
		emitCenter = new Vector2();
		
		try {
			emitCenter = ((CircleShape)body.getFixtureList().get(1).getShape()).getPosition().cpy();
		}
		catch(Exception e) {
			System.err.println("Objeto Laser Emitter deve ter como seu SEGUNDO fixture, um circulo para definir de onde sai o tiro");
			Gdx.app.exit();
		}
		emitCenter.rotate((float) Math.toDegrees(body.getAngle()));
		emitCenter.add(body.getWorldCenter()).scl(State.PHYS_SCALE);
		
	}
	
	public boolean update(float delta) {
		super.update(delta);
	
		laserTimer += delta * ((StateOne)getState()).getWorldSpeed();
		
		if(laserTimer > frequency / state.getWorldSpeed()) {
			laserTimer -= frequency / state.getWorldSpeed();
			Laser laser = new Laser(info, emitCenter, emitDirection, laserSpeed);
			getState().putInScene(laser);
		}

		
		return false;
	}

}

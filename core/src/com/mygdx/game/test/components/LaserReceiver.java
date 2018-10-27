package com.mygdx.game.test.components;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;

public class LaserReceiver extends Platform{

	Body body;
	
	float capacity;
	float targetCapacity;
	float currentCapacity;
	float fillStep;
	float decaySpeed;
	
	ArrayList<Door> connections;
	
	public LaserReceiver(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		this.body = get("body", Body.class);
		body.setUserData(this);
		
		capacity = get("capacity", Float.class);
		fillStep = get("fillStep", Float.class);
		decaySpeed = get("decaySpeed", Float.class);
		currentCapacity = 0;
		targetCapacity = 0;
		connections  = new ArrayList<Door>();
	}
	
	public void create() {
		super.create();
		
		int i = 0;
		while(get("conn" + i) != null) {
			connections.add((Door)get("conn" + i, Body.class).getUserData());
			i ++;
		}
	}
	
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		super.render(sb, sr, camera);
		
		sr.setAutoShapeType(true);
		sr.begin();
		
		sr.set(ShapeType.Line);
		sr.rect(body.getWorldCenter().x + 30/State.PHYS_SCALE, body.getWorldCenter().y, 30/State.PHYS_SCALE, 50 / State.PHYS_SCALE);
		
		sr.set(ShapeType.Filled);
		sr.rect(body.getWorldCenter().x + 30/State.PHYS_SCALE, body.getWorldCenter().y, 30/State.PHYS_SCALE, (currentCapacity / capacity) * 50 / State.PHYS_SCALE);
		
		sr.end();
		
		sr.setAutoShapeType(false);
	}
	
	public void activate() {
		for(Door d : connections) {
			d.toggle();
		}
	}
	
	@Override
	public boolean update(float delta) {
		super.update(delta);
		targetCapacity -= delta * decaySpeed;
		if(targetCapacity < 0) {
			targetCapacity = 0;
		}
		
		if(targetCapacity > capacity) {
			targetCapacity = 0;
			activate();
		}
		currentCapacity += (targetCapacity - currentCapacity)/5f;
		return false;
	}

	public void fill() {
		targetCapacity += fillStep;
	}

}
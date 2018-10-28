package com.mygdx.game.test.components;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;

public class Button extends Platform{
	
	boolean bounded;
	ArrayList<Door> connections;
	BitmapFont font;
	boolean pressed = false;

	float timerToPressAgain = 0;
	
	public Button(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		connections = new ArrayList<Door>();
		//body.getFixtureList().get(0).setSensor(true);
		font = Helper.newFont("Allan-Bold.ttf", 18);
	}
	
	public void create() {
		super.create();
		
		int i = 0;
		while(get("conn" + i) != null) {
			Door d = (Door)get("conn" + i, Body.class).getUserData();
			d.setParent(this);
			connections.add(d);
			i ++;
		}
	}
	
	
	public void activate() {
		for(Door d : connections) {
			d.toggle();
		}
	}
	
	@Override
	public boolean update(float delta) {
		super.update(delta);
		timerToPressAgain -= delta;
		return super.update(delta);
	}

	public boolean isBounded() {
		return bounded;
	}

	public void setBounded(boolean bounded) {
		this.bounded = bounded;
	}
	
	public boolean keyDown(int keycode) {
		if(keycode == Keys.SPACE && bounded) {
			activate();
		}
		return super.keyDown(keycode);
	}

	public void unPress() {
		if(pressed) {
			PolygonShape shape = (PolygonShape) body.getFixtureList().get(0).getShape();
			
			shape.setAsBox(
					get("width", Float.class) /2f / State.PHYS_SCALE,
					get("height", Float.class) *2f /2f / State.PHYS_SCALE,
					
					new Vector2(get("width", Float.class) /2f / State.PHYS_SCALE,
					get("height", Float.class) *2f /2f / State.PHYS_SCALE), 0);
			
			properties.put("height", get("height", Float.class) *2f);
			pressed = false;
			timerToPressAgain = 0.4f;
		}
	}
	
	public void press() {

		if(!pressed && timerToPressAgain <= 0) {
		PolygonShape shape = (PolygonShape) body.getFixtureList().get(0).getShape();
		
		shape.setAsBox(
				get("width", Float.class) /2f / State.PHYS_SCALE,
				get("height", Float.class) /2f /2f / State.PHYS_SCALE,
				
				new Vector2(get("width", Float.class) /2f / State.PHYS_SCALE,
				get("height", Float.class) /2f /2f / State.PHYS_SCALE), 0);
		
		properties.put("height", get("height", Float.class) /2f);
		pressed = true;
		activate();
		
		}
	}

}

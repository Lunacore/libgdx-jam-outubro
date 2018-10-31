package com.mygdx.game.test.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameParticle;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateOne;

public class Door extends Platform {

	boolean open;
	
	float currentWidth;
	float targetWidth;
	
	float timeOpen;
	float openTimer;
	
	Button parent;
	
	public Door(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		//tamanho = true;
		currentWidth = original_width;
		body.setUserData(this);
		
		timeOpen = get("timeOpen", Float.class);
	}
	
	public void setParent(Button button) {
		parent = button;
	}
	
	public void toggle() {
		if(!open) {
			open();
		}
		else {
			open = false;
			body.getFixtureList().get(0).setSensor(false);
		}
	}
	
	public void open() {
		if(!open) {
			open = true;
			openTimer = timeOpen;
			body.getFixtureList().get(0).setSensor(true);
		}
	}

	
	public boolean update(float delta) {
		super.update(delta);
		
		float oldOp = openTimer;
		openTimer -= delta * ((StateOne)getState()).getWorldSpeed();
		
		if(oldOp > 0 && openTimer < 0) {
			toggle();
			if(parent != null) {
				parent.unPress();
			}
		}
		
		
		if(open)
			targetWidth = 54;
		else
			targetWidth = original_width;
		
		PolygonShape shape = (PolygonShape) body.getFixtureList().get(0).getShape();
		shape.setAsBox(
				currentWidth / State.PHYS_SCALE / 2f, get("height", Float.class) / 2f / State.PHYS_SCALE,
				new Vector2(currentWidth / State.PHYS_SCALE / 2f, get("height", Float.class) / 2f / State.PHYS_SCALE),
				0);
		
		
		currentWidth += (targetWidth - currentWidth)/5f;
		
		transform.setScale(new Vector2(
				imgObj.getScaleX() * (currentWidth / original_width),
				imgObj.getScaleY()
				));
		
		return false;
	}
	
	public boolean isOpen() {
		return open;
	}

}

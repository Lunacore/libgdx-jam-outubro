package com.mygdx.game.test.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.helper.Helper;
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
	
	Button parent;
	
	public Door(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		//tamanho = true;
		originalHeight = get("height", Float.class) / State.PHYS_SCALE;
		currentHeight = originalHeight;
		body.setUserData(this);
		
		timeOpen = get("timeOpen", Float.class);
	}
	
	public void setParent(Button button) {
		parent = button;
	}
	
	public void toggle() {
		open = !open;
		
		if(open) {
			openTimer = timeOpen;
		}
	}
	
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {

		Helper.enableBlend();
		
		sb.setColor(1, 1, 1, alpha);
		sb.draw(bloco, body.getWorldCenter().x, body.getWorldCenter().y, get("width", Float.class) / State.PHYS_SCALE, currentHeight);
		sb.setColor(Color.WHITE);
		
		Helper.disableBlend();
		
		sb.flush();
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

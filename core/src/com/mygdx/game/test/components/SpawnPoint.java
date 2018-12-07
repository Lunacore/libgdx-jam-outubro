package com.mygdx.game.test.components;

import java.util.function.Consumer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;

public class SpawnPoint extends GameObject{

	Vector2 position;
	String name;
	
	public SpawnPoint(ObjectInfo info, final MapProperties properties) {
		super(info, properties);
		
		this.name = get("name", String.class);
		this.position = get("position", Vector2.class).scl(1/State.PHYS_SCALE);
	}
	
	public String getName() {
		return name;
	}
	
	public Vector2 getPosition() {
		return position;
	}

	public void create() {
		
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
	}

	public boolean update(float delta) {
		return false;
	}

}

package com.mygdx.game.test.components;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.ObjectInfo;

public class LaserReceiver extends Platform{
	
	float capacity;
	float targetCapacity;
	float currentCapacity;
	float fillStep;
	float decaySpeed;
	boolean reversed;
	
	ArrayList<Door> connections;
	
	Vector2 gaugeMin;
	Vector2 gaugeMax;
	Vector2 gaugeOriginalSize;
	
	static Sound receive;
	
	public LaserReceiver(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		body.setUserData(this);
		tamanho = true;
		capacity = get("capacity", Float.class);
		fillStep = get("fillStep", Float.class);
		decaySpeed = get("decaySpeed", Float.class);
		currentCapacity = 0;
		targetCapacity = 0;
		connections  = new ArrayList<Door>();
		reversed = get("reversed") == null ? false : get("reversed", Boolean.class);
	
		gaugeMin = new Vector2();
		gaugeMax = new Vector2();
		
		gaugeOriginalSize = Helper.PhysHelp.getFixtureRelativeSize(body.getFixtureList().get(1));

		receive = Gdx.audio.newSound(Gdx.files.internal("audio/receive.ogg"));
	}
	
	public void create() {
		super.create();
		
		int i = 0;
		while(get("conn" + i) != null) {
			connections.add((Door)get("conn" + i, Body.class).getUserData());
			i ++;
		}
	}

	
	public void activate() {
		for(Door d : connections) {
			d.open();
		}
	}
	
	@Override
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
		
		sb.end();
		
		sr.begin(ShapeType.Filled);
		sr.setProjectionMatrix(camera.combined);
		
		float factor = get("height", Float.class) / original_height;

		sr.setColor(Color.RED);
		sr.rect(
				gaugeMin.x,
				gaugeMin.y,
				body.getWorldCenter().x - gaugeMin.x,
				body.getWorldCenter().y - gaugeMin.y,
				(gaugeMax.x - gaugeMin.x), (gaugeMax.y - gaugeMin.y),
				1, 1, (float)Math.toDegrees(body.getAngle()));
		
				
		sr.setColor(Color.GREEN);
		sr.rect(
				gaugeMin.x,
				gaugeMin.y,
				body.getWorldCenter().x - gaugeMin.x,
				body.getWorldCenter().y - gaugeMin.y,
				(gaugeMax.x - gaugeMin.x), (gaugeMax.y - gaugeMin.y) * (currentCapacity / capacity),
				1, 1, (float)Math.toDegrees(body.getAngle()));
		
		sr.end();
		
		sb.begin();
		super.render(sb, sr, camera);
		
	}
	
	
	@Override
	public boolean update(float delta) {
		super.update(delta);
		
		PolygonShape sh = (PolygonShape) body.getFixtureList().get(1).getShape();
		
		gaugeMin = new Vector2();
		sh.getVertex(0, gaugeMin);
		gaugeMin.scl(get("width", Float.class) / original_width, get("height", Float.class) / original_height);
		gaugeMax = new Vector2();
		sh.getVertex(2, gaugeMax);
		gaugeMax.scl(get("width", Float.class) / original_width, get("height", Float.class) / original_height);

		
		gaugeMin.add(body.getWorldCenter());
		gaugeMax.add(body.getWorldCenter());
		
		if(!reversed)
			targetCapacity -= delta * decaySpeed;
		else
			targetCapacity += delta * decaySpeed;
			
		if(targetCapacity < 0) {
			targetCapacity = 0;
			currentCapacity = 0;
		}
		
		if(targetCapacity > capacity) {
			targetCapacity = 0;
			currentCapacity = 0;
			activate();
		}

		currentCapacity += (targetCapacity - currentCapacity)/5f;
		return false;
	}

	public void fill() {
		if(!reversed) {
			targetCapacity += fillStep;
		}
		else {
			targetCapacity -= fillStep;
		}
		receive.play(0.5f);
	}
	
}

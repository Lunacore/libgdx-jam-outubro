package com.mygdx.game.test.components;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;
import com.mygdx.game.structs.Transform;

public class LaserReceiver extends Platform{
	
	float capacity;
	float targetCapacity;
	float currentCapacity;
	float fillStep;
	float decaySpeed;
	boolean reversed;
	
	ArrayList<Door> connections;
	
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
	public boolean update(float delta) {
		super.update(delta);
		
		if(!reversed)
			targetCapacity -= delta * decaySpeed;
		else
			targetCapacity += delta * decaySpeed;
			
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
		if(!reversed) {
			targetCapacity += fillStep;
		}
		else {
			targetCapacity -= fillStep;
		}
	}
	
	protected void renderBodyTexture2(SpriteBatch sb, Texture texture, Body body, Transform customTransform, boolean flipX, boolean flipY) {
		renderTex(sb, texture, body.getWorldCenter().add(customTransform.getPosition()), (float)Math.toDegrees(body.getAngle()) + customTransform.getAngle(), customTransform.getScale().cpy().scl(1/State.PHYS_SCALE), flipX, flipY);
	}
	
	public void renderTex(SpriteBatch sb, Texture tex, Vector2 position, float angle, Vector2 size, boolean flipX, boolean flipY) {
		sb.draw(
				tex,
				position.x - tex.getWidth()/2f,
				position.y - (tex.getHeight() * (currentCapacity / capacity))/2f,
				tex.getWidth()/2f,//originx
				(tex.getHeight() * (currentCapacity / capacity))/2f,//originy
				tex.getWidth(),//width
				(tex.getHeight() * (currentCapacity / capacity)),//height
				size.x,//scalex
				size.y,//scaley
				angle,//rotation
				0,//srcx
				0,//srcy
				tex.getWidth(),//srcwidth
				(int)(tex.getHeight() * (currentCapacity / capacity)),//srcheight
				flipX,
				flipY
				);
	}

}

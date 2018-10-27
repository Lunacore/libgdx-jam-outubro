package com.mygdx.game.test.components;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.helper.Helper.Position;
import com.mygdx.game.objects.GameParticle;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;

public class Button extends Platform{
	
	boolean bounded;
	
	ArrayList<Door> connections;
	
	BitmapFont font;

	public Button(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		connections = new ArrayList<Door>();
		body.getFixtureList().get(0).setSensor(true);
		body.setUserData(this);
		font = Helper.newFont("Allan-Bold.ttf", 18);

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
			d.toggle();
		}
	}
	
	@Override
	public boolean update(float delta) {
		super.update(delta);
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

}

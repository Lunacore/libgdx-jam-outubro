package com.mygdx.game.test.components;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.objects.ObjectInfo;

public class EndLevel extends Platform{

	Body body;
	
	public EndLevel(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		body = get("body", Body.class);
		body.getFixtureList().get(0).setSensor(true);
		body.setUserData(this);
	}

}

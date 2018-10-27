package com.mygdx.game.test.components;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.objects.ObjectInfo;

public class DeathCompo extends Platform{
	Body body;
	public DeathCompo(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		body = get("body", Body.class);
		body.setUserData(this);
	}
}

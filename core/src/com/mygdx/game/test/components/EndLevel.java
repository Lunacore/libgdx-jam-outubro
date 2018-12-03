package com.mygdx.game.test.components;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.TransitionState;

public class EndLevel extends Platform{

	Body body;
	
	public String nextLevel;
	public int direction;
	
	public EndLevel(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		body = get("body", Body.class);
		body.getFixtureList().get(0).setSensor(true);
		body.setUserData(this);
		nextLevel = get("next", String.class);
		String dir = get("direction", String.class, "LEFT");
		
		if(dir.equals("RIGHT")) {
			direction = TransitionState.RIGHT;
		}
		else if(dir.equals("UP")) {
			direction = TransitionState.UP;
		}
		else if(dir.equals("DOWN")) {
			direction = TransitionState.DOWN;
		}
		else {
			direction = TransitionState.LEFT;
		}
		 
	}

}

package com.mygdx.game.test;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.PlatformPlayer;
import com.mygdx.game.states.StateOne;
import com.mygdx.game.states.TransitionState;

public class MyPlayer extends PlatformPlayer{

	public MyPlayer(ObjectInfo info, Vector2 position, Vector2 size) {
		super(info, position, size);
	}

	public MyPlayer(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		nextLevel = false;
		
		setTotalJumps(get("jumps", Integer.class));
		setSpeed(get("speed", Float.class));
		setJumpStrength(get("strength", Float.class));
		
		setKeyLeft(Keys.A);
		setKeyRight(Keys.D);
		setKeyJump(Keys.W);
	}
	
	public void create() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		if(nextLevel) {
			TransitionState.lastPrint = ScreenUtils.getFrameBufferTexture();
			getState().manager.changeState(1);
			nextLevel = false;
		}
	}

	public void kill() {
		((StateOne)getState()).kill();
	}

	public void dispose() {
		getState().deleteBody(body);
	}
	
	boolean nextLevel;

	public void nextLevel() {
		nextLevel = true;
		
		
	}

}

package com.mygdx.game.test;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.helper.Helper.Game;
import com.mygdx.game.objects.AnimationLoader;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.PlatformPlayer;
import com.mygdx.game.states.StateOne;
import com.mygdx.game.states.TransitionState;

public class MyPlayer extends PlatformPlayer{

	Animation<TextureRegion> current;
	Animation<TextureRegion> walk;
	Animation<TextureRegion> idle;
	
	public MyPlayer(ObjectInfo info, Vector2 position, Vector2 size) {
		super(info, position, size);

	}

	public MyPlayer(ObjectInfo info, MapProperties properties) {
		super(info, properties);
				
		float ratio = get("width", Float.class) / 30f;
		
		transform.setScale(new Vector2(0.4f * ratio , 0.4f * ratio));
		transform.setPosition(new Vector2(0, 5));
		
		setTotalJumps(get("jumps", Integer.class));
		setSpeed(get("speed", Float.class));
		setJumpStrength(get("strength", Float.class));
		
		idle = AnimationLoader.load("animations/luna_idle.png", 147, 176, 0, 10, 1/24f);
		idle.setPlayMode(PlayMode.LOOP);
		
		walk = AnimationLoader.load("animations/luna_walk.png", 147, 176, 0, 9, 1/24f);
		walk.setPlayMode(PlayMode.LOOP);

		current = idle;
	}
	
	public void create() {
		
	}
	
	float timer = 0;

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
		TextureRegion reg = current.getKeyFrame(timer);
		renderBodyRegion(sb, reg, body, direction == -1, false);
	}
	
	@Override
	public boolean update(float delta) {
		timer += delta * ((StateOne)getState()).getWorldSpeed() / 2f;
		
		if(Math.abs(body.getLinearVelocity().x) > 1){
			current = walk;
		}
		else {
			current = idle;
		}
		
		return super.update(delta);
	}

	public void kill() {
		((StateOne)getState()).kill();
	}

	public void dispose() {
		getState().deleteBody(body);
	}
	

}

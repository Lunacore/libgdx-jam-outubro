package com.mygdx.game.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.brashmonkey.spriter.PlayerTweener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.PlatformPlayer;
import com.mygdx.game.objects.SpriterAnimation;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateOne;

public class MyPlayer extends PlatformPlayer{

	SpriterAnimation animation;
	float endVelocityX = 0;
	PlayerTweener idle_run;
	Sound death;
	
	public MyPlayer(ObjectInfo info, Vector2 position, Vector2 size) {
		super(info, position, size.cpy().scl(1/2f));
	}

	public MyPlayer(ObjectInfo info, MapProperties properties) {
		super(info, properties);
				
		float ratio = get("width", Float.class) / 60f;
		
		transform.setScale(new Vector2(0.4f * ratio , 0.4f * ratio));
		transform.setPosition(new Vector2(0, 5));
		
		setTotalJumps(get("jumps", Integer.class));
		setSpeed(get("speed", Float.class));
		setJumpStrength(get("strength", Float.class));
		
		animation = new SpriterAnimation(info, "spriter/luna/luna.scml", new Vector2(get("x", Float.class) / State.PHYS_SCALE, get("y", Float.class) / State.PHYS_SCALE));
		getState().putInScene(animation);
		idle_run = animation.createInterpolatedAnimation("Idle", "Run", 0);
		animation.setPlayer(idle_run);
		animation.setToRender(false);
		
		death = Gdx.audio.newSound(Gdx.files.internal("audio/luna_die.ogg"));
	}
	
	public void create() {
		
	}
	
	float timer = 0;

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		animation.render(sb, sr, camera);
	}
	
	@Override
	public boolean update(float delta) {
		timer += delta * ((StateOne)getState()).getWorldSpeed() / 2f;
		
		animation.getTransform().setPosition(
				body.getWorldCenter().x,
				body.getWorldCenter().y - 0.656f);
		animation.setScale(new Vector2(1/14f, 1/14f));
		animation.flip(direction == -1, false);
		
		endVelocityX += (Math.abs(body.getLinearVelocity().x) - endVelocityX) / 15f;
		idle_run.setWeight(endVelocityX / speed);
		
		animation.getPlayer().speed = (int) (15f * ((StateOne)getState()).getWorldSpeed());
		
		return super.update(delta);
	}

	public void kill() {
		((StateOne)getState()).kill();
		MyGdxGame.sendEvent("player_killed");
		death.play(0.6f);
	}

	public void dispose() {
		getState().deleteBody(body);
	}
	

}

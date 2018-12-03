package com.mygdx.game.test.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.GameParticle;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateOne;

public class CannonBall extends GameObject{

	private Body body;
	static Texture ball;
	static Texture smoke;
	float timer = 5;
	
	public CannonBall(ObjectInfo info, Vector2 position, Vector2 velocity, float ballSize) {
		super(info, new MapProperties());
		transform.setPosition(Vector2.Zero.cpy());
		
		if(ball == null) {
			ball = new Texture("cannon ball.png");
			smoke = new Texture("particles/smoke.png");
		}
		
		transform.setScale(new Vector2(ballSize / State.PHYS_SCALE, ballSize / State.PHYS_SCALE));
		
		body = (Helper.PhysHelp.createDynamicCircleBody(getState().getWorld(), position.cpy(), ballSize));
		body.setLinearVelocity(velocity.cpy());
		body.setUserData(this);
		
		setToRender(false);
		
		StateOne st = (StateOne) getState();
		st.getCanvas().addPlatform(this);
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		getState().deleteBody(body);
	}

	@Override
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		renderBodyTexture(sb, ball, body);
		
	}

	@Override
	public boolean update(float delta) {
		timer -= delta;
		if(timer <= 0) {
			
			for(int i = 0; i < 10; i ++) {
				GameParticle gp = new GameParticle(info, body.getWorldCenter(), smoke, 0.07f);
				gp.setVelocity(new Vector2((float)Math.random() -0.5f, (float)Math.random() -0.5f).scl(0.3f));
				gp.setDrag((float)Math.random() + 0.5f);
				gp.setGravity(Vector2.Y.cpy().scl(0.01f));
				getState().putInScene(gp);
			}
			
			return true;
		}
		return false;
	}
	
}

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
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;

public class CannonBall extends GameObject{

	private Body body;
	static Texture ball;
	
	public CannonBall(ObjectInfo info, Vector2 position, Vector2 velocity, float ballSize) {
		super(info, new MapProperties());
		transform.setPosition(Vector2.Zero.cpy());
		
		if(ball == null) {
			ball = new Texture("cannon ball.png");
		}
		
		transform.setScale(new Vector2(ballSize / State.PHYS_SCALE, ballSize / State.PHYS_SCALE));
		
		body = (Helper.PhysHelp.createDynamicCircleBody(getState().getWorld(), position.cpy(), ballSize));
		body.setLinearVelocity(velocity.cpy());
		body.setUserData(this);
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
		// TODO Auto-generated method stub
		return false;
	}
	
}

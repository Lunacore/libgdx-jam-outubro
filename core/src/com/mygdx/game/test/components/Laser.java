package com.mygdx.game.test.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.StateOne;

public class Laser extends GameObject{

	static Texture ball;
	
	static {
		ball = new Texture("laser.png");
	}
	
	private Body body;
	Vector2 velocity;
	
	public Laser(ObjectInfo info, Vector2 position, Vector2 velocity) {
		super(info, new MapProperties());
		transform.setPosition(Vector2.Zero.cpy());
		setBody(Helper.PhysHelp.createDynamicCircleBody(getState().getWorld(), position.cpy(), 5));
		body.setGravityScale(0);
		this.velocity = velocity.cpy();
		getBody().setUserData(this);
		
	}

	public void create() {
		
	}

	public void dispose() {
		getState().deleteBody(body);
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		renderBodyTexture(sb, ball, getBody());
	}

	public boolean update(float delta) {
		StateOne state = (StateOne) getState();
		getBody().setLinearVelocity(velocity.cpy().scl(state.getWorldSpeed()));
		return false;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

}

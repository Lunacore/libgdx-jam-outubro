package com.mygdx.game.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.KeyMapper.Device;

public class TransitionState extends State{

	public static TextureRegion lastPrint;
	
	Texture print;
	
	float offset = 0;
	float timer = 0;

	public TransitionState(StateManager manager) {
		super(manager);
		
		print = new Texture("print.png");
		
	}

	public void create() {
		offset  = 0;
		timer = 0;
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		
		sb.begin();
		sb.draw(lastPrint, -offset, 0, 1280, 720);
		sb.draw(print, 1280 - offset, 0, 1280, 720);
		sb.end();
		
		sb.setProjectionMatrix(camera.combined);
	}

	public void update(float delta) {
		timer += delta;
		
		timer = Math.min(timer, 2);
		
		
		offset = easeInOut(timer/2f, 0, 1280, 1);
		
		if(timer >= 2) {
			manager.changeState(0);
		}
		
	}
	
	public float easeInOut(float t,float b , float c, float d) {
		if ((t/=d/2) < 1) return c/2*t*t + b;
		return -c/2 * ((--t)*(t-2) - 1) + b;
	}
}

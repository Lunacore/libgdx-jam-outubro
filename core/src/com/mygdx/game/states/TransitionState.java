package com.mygdx.game.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.test.Canvas;
import com.mygdx.game.utils.ScreenSize;

public class TransitionState extends State{

	public static TextureRegion lastPrint;
	
	Texture print;
	
	float offset = 0;
	float timer = 0;
	
	Texture nextPhaseBG;
	
	public static String nextPhase;

	public TransitionState(StateManager manager) {
		super(manager);		
		print = new Texture("wall_bg.png");
	}
	
	public String proximo() {
		TiledMap t = new TmxMapLoader().load(Canvas.levelToLoad);
		String ret = t.getProperties().get("fundo", String.class);
		t.dispose();
		return ret;
	}

	public void create() {
		offset  = 0;
		timer = 0;
		nextPhaseBG = new Texture(proximo());
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		
		sb.begin();
		sb.draw(lastPrint, -offset, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		sb.draw(print, ScreenSize.getWidth() - offset, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		sb.draw(nextPhaseBG, ScreenSize.getWidth() - offset + (ScreenSize.getWidth() - 800)/2f, (ScreenSize.getHeight() - 600)/2f, 800, 600);
		sb.end();
		
		sb.setProjectionMatrix(camera.combined);
	}

	public void update(float delta) {
		timer += delta;
		
		timer = Math.min(timer, 2);
		
		
		offset = easeInOut(timer/2f, 0, ScreenSize.getWidth(), 1);
		
		if(timer >= 2) {
			manager.changeState(0);
		}
		
	}
	
	public float easeInOut(float t,float b , float c, float d) {
		if ((t/=d/2) < 1) return c/2*t*t + b;
		return -c/2 * ((--t)*(t-2) - 1) + b;
	}
}

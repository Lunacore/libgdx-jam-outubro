package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.helper.Helper.Game;
import com.mygdx.game.helper.Helper.Position;
import com.mygdx.game.objects.KeyMapper.Device;

public class IntroState extends State{

	float alpha1 = 0;
	float alpha2 = 0;
	
	Texture logo;
	Texture gear;
	
	float fadeout = 6;
	
	public IntroState(StateManager manager) {
		super(manager);
		logo = new Texture("lunacore/logo.png");
		gear = new Texture("lunacore/gear.png");
	}

	public void create() {
		
	}

	public void render(SpriteBatch sb) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		
		sb.begin();
		sb.setColor(1, 1, 1, alpha2);
		sb.draw(logo, (1280 - logo.getWidth()* 0.5f)/2f , (720 - logo.getHeight()* 0.5f)/2f - 20, logo.getWidth()* 0.5f, logo.getHeight()* 0.5f);
				
		sb.setColor(Color.WHITE);
		Helper.renderTex(sb, gear, Position.CENTER.cpy().add(135, -10), Game.globalTimer * 50, new Vector2(0.5f, 0.5f), false, false);

		sb.end();
		
		sb.setProjectionMatrix(camera.combined);
		
		Helper.enableBlend();
		sr.begin(ShapeType.Filled);
		
		sr.setColor(0, 0, 0, 1-alpha1);
		sr.rect(0, 0, 1280, 720);
		
		
		sr.end();
		Helper.disableBlend();
	}

	public void update(float delta) {
		alpha1 += delta;
		alpha1 = Math.min(1, alpha1);
		
		if(alpha1 >= 1) {
			alpha2 += delta;
			alpha2 = Math.min(1, alpha2);
		}
		
		fadeout -= delta;
		
		if(fadeout <= 1) {
			alpha1 = fadeout;
			
			if(alpha1 <= 0) {
				manager.changeState(0);
			}
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(fadeout > 1)
		fadeout = 1;
		return super.keyDown(keycode);
	}
}

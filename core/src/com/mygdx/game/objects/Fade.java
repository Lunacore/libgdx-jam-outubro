package com.mygdx.game.objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.states.State;

public class Fade extends GameObject{

	float alpha;
	boolean intro;
	boolean outro;
	
	OrthographicCamera camera;
	
	//User props
	Color color;
	ActionListener action;
	float timeToFade = 1;
	
	public Fade(State state) {
		super(new ObjectInfo(state, 1000, 1f), new MapProperties());
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		color = new Color(0, 0, 0, 0);
	}

	public void setFadeFinishListener(ActionListener listener) {
		action = listener;
	}
	
	public void create() {
		intro = true;
		outro = false;
		alpha = 1;
	}

	public boolean isIntro() {
		return intro;
	}

	public boolean isOutro() {
		return outro;
	}	
	
	public void dispose() {
		
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	public void begin() {
		intro = true;
		outro = false;
	}
	
	public void end() {
		intro = false;
		outro = true;
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setTimeToFade(float timeToFade) {
		this.timeToFade = timeToFade;
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		Helper.enableBlend();
		
		sr.begin(ShapeType.Filled);
		sr.setProjectionMatrix(camera.combined);
		sr.setColor(color.r, color.g, color.b, alpha);
		sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sr.end();
		
		Helper.disableBlend();
	}

	public boolean update(float delta) {
		camera.update();
		
		if(intro) {
			alpha -= delta / timeToFade;
			if(alpha <= 0) {
				alpha = 0;
				intro = false;
			}
		}
		
		if(outro) {
			alpha += delta / timeToFade;
			if(alpha >= 1) {
				alpha = 1;
				outro = false;
				if(action != null)
				action.actionPerformed(new ActionEvent(this, 0, null));
			}
		}
		
		return false;
	}

}

package com.mygdx.game.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OpeninCutsceneState extends State{

	int sceneIndex = 0;
	
	Texture scenes[] = new Texture[6];
	
	public OpeninCutsceneState(StateManager manager) {
		super(manager);
	}

	public void create() {
		sceneIndex = 0;
	}

	public void render(SpriteBatch sb) {
		
	}

	public void update(float delta) {
		
	}

}

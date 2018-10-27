package com.mygdx.game.states;


import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.test.components.Platform;

public class StateOne extends State{

	Rectangle2D canvasBox;
	Rectangle2D targetCanvasBox;
	ArrayList<GameObject> platforms;
	float tween = 15f;
	
	Sound musicTest;
	long musicID;
	
	boolean playerDead;
	
	public StateOne(StateManager manager) {
		super(manager);
	}
	
	public void create() {
		dispose();
		playerDead = false;
		enablePhysics(new StateOneListener(this));
		enableDebugDraw();
		setGravity(new Vector2(0, -20));
		platforms = new ArrayList<GameObject>();
		canvasBox = new Rectangle2D.Double(0, 0, 800, 600);
		targetCanvasBox = (Rectangle2D) canvasBox.clone();
		setTmxMap("maps/fase1.tmx", 1);
				
		camera.position.set(400 / State.PHYS_SCALE, 300 / State.PHYS_SCALE, 0);
		platforms = getByClass(Platform.class);
		
		resizeBox(new Vector2(800, 350));
		
		musicTest = Gdx.audio.newSound(Gdx.files.internal("music/badvibes.wav"));
		musicID = musicTest.play();
		musicTest.setLooping(musicID, true);
	}

	public void render(SpriteBatch sb) {
		
		sr.begin(ShapeType.Line);
			sr.rect(
					(float)canvasBox.getX() / State.PHYS_SCALE,
					(float)canvasBox.getY() / State.PHYS_SCALE,
					(float)canvasBox.getWidth() / State.PHYS_SCALE,
					(float)canvasBox.getHeight() / State.PHYS_SCALE);
		sr.end();
		
		Rectangle2D oldRect = (Rectangle2D) canvasBox.clone();

		canvasBox.setRect(
				canvasBox.getX() + (targetCanvasBox.getX() - canvasBox.getX())/tween,
				canvasBox.getY() + (targetCanvasBox.getY() - canvasBox.getY())/tween,
				canvasBox.getWidth() + (targetCanvasBox.getWidth() - canvasBox.getWidth())/tween,
				canvasBox.getHeight() + (targetCanvasBox.getHeight() - canvasBox.getHeight())/tween
				);
		for(GameObject g : platforms) {
			((Platform)g).resizeBox(oldRect, canvasBox);
		}
		
		float a = (float) ((canvasBox.getHeight() - 100f) / 500f);
		
		if(!playerDead)
			worldStepFPS = Helper.lerp(30, 100, a);
		else
			worldStepFPS = 100000000f;
		
		musicTest.setPitch(musicID, Helper.lerp(0.5f, 1.7f, 1-a));

	}
	
	public void resizeBox(Vector2 newSize) {
		newSize.set(Helper.clamp(newSize.x, 300, 1000), Helper.clamp(newSize.y, 100, 600));
		targetCanvasBox.setFrame((800 - newSize.x)/2f, (600 - newSize.y)/2f, newSize.x, newSize.y);
	}

	public void update(float delta) {		
	
	}
	
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
			float nw = (float)Math.abs((screenX - Gdx.graphics.getWidth()/2f)) * 2f;
			float nh = (float)Math.abs((screenY - Gdx.graphics.getHeight()/2f)) * 2f;
			resizeBox(new Vector2(nw, nh));
		}
		return super.touchDragged(screenX, screenY, pointer);
	}

	public float getWorldSpeed() {
		float a = (float) ((canvasBox.getHeight() - 100f) / 500f);
		return Helper.lerp(0.5f, 1.7f, 1-a);
	}

	public void kill() {
		playerDead = true;
		manager.changeState(0);
	}
}

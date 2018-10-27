package com.mygdx.game.states;


import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.test.Platform;

public class StateOne extends State{

	Rectangle2D canvasBox;
	Rectangle2D targetCanvasBox;
	ArrayList<GameObject> platforms;
	float tween = 15f;
	
	public StateOne(StateManager manager) {
		super(manager);
	}
	
	public void create() {
		enablePhysics(new StateOneListener(this));
		enableDebugDraw();
		setGravity(new Vector2(0, -20));
		platforms = new ArrayList<GameObject>();
		canvasBox = new Rectangle2D.Double(0, 0, 800, 600);
		targetCanvasBox = (Rectangle2D) canvasBox.clone();
		setTmxMap("maps/fase1.tmx", 1);
				
		camera.position.set(400 / State.PHYS_SCALE, 300 / State.PHYS_SCALE, 0);
		platforms = getByClass(Platform.class);
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
	}
	
	public void resizeBox(Vector2 newSize) {
		newSize.set(Helper.clamp(newSize.x, 300, 1000), Helper.clamp(newSize.y, 100, 600));
		targetCanvasBox.setFrame((800 - newSize.x)/2f, (600 - newSize.y)/2f, newSize.x, newSize.y);
	}

	public void update(float delta) {		
	
	}

	public void dispose() {
		
	}
	
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
			float nw = (float)Math.abs((screenX - Gdx.graphics.getWidth()/2f)) * 2f;
			float nh = (float)Math.abs((screenY - Gdx.graphics.getHeight()/2f)) * 2f;
			resizeBox(new Vector2(nw, nh));
		}
		return super.touchDragged(screenX, screenY, pointer);
	}
	

}

package com.mygdx.game.test.components;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;

public class Platform extends GameObject{

	Body body;
	boolean tamanho;
	
	public Platform(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		body = get("body", Body.class);
	
		tamanho = get("tamanho") != null ? get("tamanho", Boolean.class) : false;
	}

	public void create() {
		
	}

	public void dispose() {
		getState().deleteBody(body);
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
	}

	public boolean update(float delta) {
		return false;
	}

	public void resizeBox(Rectangle2D oldRect, Rectangle2D newRect) {
	
		//Tamanho
		
		if(tamanho) {
			float relativeWidth = (float) (get("width", Float.class) / oldRect.getWidth());
			float newWidth = (float) (relativeWidth * newRect.getWidth());
			
			float relativeHeight = (float) (get("height", Float.class) / oldRect.getHeight());
			float newHeight = (float) (relativeHeight * newRect.getHeight());
			
			PolygonShape shape = (PolygonShape)body.getFixtureList().get(0).getShape();
	
			shape.setAsBox(
					newWidth / State.PHYS_SCALE / 2f,
					newHeight / State.PHYS_SCALE / 2f,
					new Vector2(
							newWidth / State.PHYS_SCALE / 2f,
							newHeight / State.PHYS_SCALE / 2f),
					0);
			
			properties.put("width", newWidth);
			properties.put("height", newHeight);
		}
		
		//Posição
		float relativeX =
				(float) ((body.getWorldCenter().x - oldRect.getX()/State.PHYS_SCALE) / (oldRect.getWidth() / State.PHYS_SCALE));		
		float newX = (float) (newRect.getX() / State.PHYS_SCALE + (newRect.getWidth() / State.PHYS_SCALE * relativeX));
		
		float relativeY =
				(float) ((body.getWorldCenter().y - oldRect.getY()/State.PHYS_SCALE) / (oldRect.getHeight() / State.PHYS_SCALE));		
		float newY = (float) (newRect.getY() / State.PHYS_SCALE + (newRect.getHeight() / State.PHYS_SCALE * relativeY));
		
		
		body.setTransform(new Vector2(newX, newY), 0);
		
	}

}

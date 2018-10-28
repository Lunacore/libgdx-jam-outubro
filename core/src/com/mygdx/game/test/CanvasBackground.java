package com.mygdx.game.test;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;

public class CanvasBackground extends GameObject{

	Texture image;
	Rectangle2D rect;
	
	public CanvasBackground(ObjectInfo info, Rectangle2D rect, String imagem) {
		super(info, new MapProperties());
		this.rect = rect;
		image = new Texture(imagem);
		//image = new Texture("maps/quadros/o-grito-de-luna.jpg");
	}

	public void create() {
		
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		
		sb.draw(
				image,
				(float)rect.getX() + 240,
				(float)rect.getY() + 60,
				(float)rect.getWidth(),
				(float)rect.getHeight()
				);
		
		sb.setProjectionMatrix(camera.combined);
		
	}

	@Override
	public boolean update(float delta) {
		// TODO Auto-generated method stub
		return false;
	}

}

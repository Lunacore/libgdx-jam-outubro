package com.mygdx.game.test;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;

public class CanvasFrame extends GameObject{

	Rectangle2D rect;
	
	Texture quina;
	Texture cima;
	Texture esq;
	
	public CanvasFrame(ObjectInfo info, Rectangle2D rect) {
		super(info, new MapProperties());
		this.rect = rect;
		
		quina = new Texture("canvas_frame/quina.png");
		cima = new Texture("canvas_frame/cima.png");
		cima.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		esq = new Texture("canvas_frame/esq.png");
		esq.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	public void create() {
		
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
		float r = 10;
		//sb.flush();
		//sb.begin();
		Helper.renderTex(sb, quina, new Vector2(
				(float)(rect.getX() + r) / State.PHYS_SCALE,
				(float)(rect.getY() + rect.getHeight() - r) / State.PHYS_SCALE),
				0, new Vector2(0.5f/State.PHYS_SCALE, 0.5f/State.PHYS_SCALE) , false, false);
		Helper.renderTex(sb, quina, new Vector2((float)(rect.getX() + rect.getWidth() - r) / State.PHYS_SCALE, (float)(rect.getY() + rect.getHeight() - r) / State.PHYS_SCALE), 0, new Vector2(0.5f/State.PHYS_SCALE, 0.5f/State.PHYS_SCALE) , true, false);

		Helper.renderTex(sb, quina, new Vector2((float)(rect.getX() + r) / State.PHYS_SCALE, (float)(rect.getY() + r) / State.PHYS_SCALE), 0, new Vector2(0.5f/State.PHYS_SCALE, 0.5f/State.PHYS_SCALE) , false, true);
		Helper.renderTex(sb, quina, new Vector2((float)(rect.getX() + rect.getWidth() - r) / State.PHYS_SCALE, (float)(rect.getY() + r) / State.PHYS_SCALE), 0, new Vector2(0.5f/State.PHYS_SCALE, 0.5f/State.PHYS_SCALE) , true, true);

		float a = (float)(rect.getX() + r) / State.PHYS_SCALE + quina.getWidth()*0.5f/State.PHYS_SCALE/2f;
		float b = (float)(rect.getX() + rect.getWidth() - r) / State.PHYS_SCALE - quina.getWidth()*0.5f/State.PHYS_SCALE/2f;
		
		float c = (float)(rect.getY() + r) / State.PHYS_SCALE + quina.getHeight()*0.5f/State.PHYS_SCALE/2f;
		float d = (float)(rect.getY() + rect.getHeight() - r) / State.PHYS_SCALE - quina.getHeight()*0.5f/State.PHYS_SCALE/2f;
		
		//Cima
		sb.draw(cima,
				(float)(rect.getX() + r) / State.PHYS_SCALE + cima.getWidth()*0.5f/State.PHYS_SCALE/2f,
				(float)(rect.getY() + rect.getHeight() - r) / State.PHYS_SCALE - cima.getHeight()*0.5f/State.PHYS_SCALE/2f,
				(b-a), cima.getHeight()*0.5f/State.PHYS_SCALE);
		
		//Baixo
		sb.draw(cima,
				(float)(rect.getX() + r) / State.PHYS_SCALE + cima.getWidth()*0.5f/State.PHYS_SCALE/2f,
				(float)(rect.getY() + r) / State.PHYS_SCALE + cima.getHeight()*0.5f/State.PHYS_SCALE/2f,
				(b-a),  - (cima.getHeight()*0.5f/State.PHYS_SCALE));
		
		
		
		//Esquerda
		sb.draw(esq, 
				(float)(rect.getX() + r) / State.PHYS_SCALE - cima.getWidth()*0.5f/State.PHYS_SCALE/2f,
				(float)(rect.getY() + r) / State.PHYS_SCALE + cima.getHeight()*0.5f/State.PHYS_SCALE/2f,
				cima.getWidth()*0.5f/State.PHYS_SCALE,  (d-c));

		//Direita
		sb.draw(esq, 
				(float)(rect.getX() + rect.getWidth() - r) / State.PHYS_SCALE + cima.getWidth()*0.5f/State.PHYS_SCALE/2f,
				(float)(rect.getY() + r) / State.PHYS_SCALE + cima.getHeight()*0.5f/State.PHYS_SCALE/2f,
				-(cima.getWidth()*0.5f/State.PHYS_SCALE), (d-c));

		sb.flush();
	}

	public boolean update(float delta) {
		return false;
	}

}

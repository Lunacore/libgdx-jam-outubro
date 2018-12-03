package com.mygdx.game.test;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.Gdx;
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

	Texture canto_inf;
	Texture canto_sup;
	Texture sup;
	Texture lado;
	
	Texture sup_simple;
	Texture lado_simple;
	
	public CanvasFrame(ObjectInfo info, Rectangle2D rect) {
		super(info, new MapProperties());
		this.rect = rect;
		
		quina = new Texture("canvas_frame/quina.png");

		canto_inf = new Texture("canvas_frame/new/canto_inf.png");
		canto_sup = new Texture("canvas_frame/new/canto_sup.png");
		sup = new Texture("canvas_frame/new/sup.png");
		lado = new Texture("canvas_frame/new/lateral.png");
		sup_simple = new Texture("canvas_frame/new/sup_simple.png");
		lado_simple = new Texture("canvas_frame/new/lat_simple.png");
		
	}

	public void create() {
		
	}

	public void dispose() {
		
	}
	
	public void drawQuina(SpriteBatch sb, Texture tex, float dx, float dy) {
		Helper.renderTex(sb, tex, new Vector2(
				(float)(rect.getX() + dx) / State.PHYS_SCALE,
				(float)(rect.getY() + rect.getHeight() - dy) / State.PHYS_SCALE),
				0, new Vector2(0.5f/State.PHYS_SCALE, 0.5f/State.PHYS_SCALE) , false, false);
		Helper.renderTex(sb, tex, new Vector2((float)(rect.getX() + rect.getWidth() - dx) / State.PHYS_SCALE, (float)(rect.getY() + rect.getHeight() - dy) / State.PHYS_SCALE), 0, new Vector2(0.5f/State.PHYS_SCALE, 0.5f/State.PHYS_SCALE) , true, false);

		Helper.renderTex(sb, tex, new Vector2((float)(rect.getX() + dx) / State.PHYS_SCALE, (float)(rect.getY() + dy) / State.PHYS_SCALE), 0, new Vector2(0.5f/State.PHYS_SCALE, 0.5f/State.PHYS_SCALE) , false, true);
		Helper.renderTex(sb, tex, new Vector2((float)(rect.getX() + rect.getWidth() - dx) / State.PHYS_SCALE, (float)(rect.getY() + dy) / State.PHYS_SCALE), 0, new Vector2(0.5f/State.PHYS_SCALE, 0.5f/State.PHYS_SCALE) , true, true);

	}
	
	public void drawLados(SpriteBatch sb, float dx, float dy, Texture cima, Texture esq) {
		float a = (float)(rect.getX() + dx) / State.PHYS_SCALE + quina.getWidth()*0.5f/State.PHYS_SCALE/2f;
		float b = (float)(rect.getX() + rect.getWidth() - dx) / State.PHYS_SCALE - quina.getWidth()*0.5f/State.PHYS_SCALE/2f;
		
		float c = (float)(rect.getY() + dy) / State.PHYS_SCALE + quina.getHeight()*0.5f/State.PHYS_SCALE/2f;
		float d = (float)(rect.getY() + rect.getHeight() - dy) / State.PHYS_SCALE - quina.getHeight()*0.5f/State.PHYS_SCALE/2f;
		
		//Cima
		sb.draw(cima,
				(float)(rect.getX() + dx) / State.PHYS_SCALE + cima.getWidth()*0.5f/State.PHYS_SCALE/2f,
				(float)(rect.getY() + rect.getHeight() - dy) / State.PHYS_SCALE - cima.getHeight()*0.5f/State.PHYS_SCALE/2f,
				(b-a), cima.getHeight()*0.5f/State.PHYS_SCALE);
		
		//Baixo
		sb.draw(cima,
				(float)(rect.getX() + dx) / State.PHYS_SCALE + cima.getWidth()*0.5f/State.PHYS_SCALE/2f,
				(float)(rect.getY() + dy) / State.PHYS_SCALE + cima.getHeight()*0.5f/State.PHYS_SCALE/2f,
				(b-a),  - (cima.getHeight()*0.5f/State.PHYS_SCALE));
		
		
		
		//Esquerda
		sb.draw(esq, 
				(float)(rect.getX() + dx) / State.PHYS_SCALE - cima.getWidth()*0.5f/State.PHYS_SCALE/2f,
				(float)(rect.getY() + dy) / State.PHYS_SCALE + cima.getHeight()*0.5f/State.PHYS_SCALE/2f,
				cima.getWidth()*0.32f/State.PHYS_SCALE,  (d-c));

		//Direita
		sb.draw(esq, 
				(float)(rect.getX() + rect.getWidth() - dx) / State.PHYS_SCALE + cima.getWidth()*0.5f/State.PHYS_SCALE/2f,
				(float)(rect.getY() + dy) / State.PHYS_SCALE + cima.getHeight()*0.5f/State.PHYS_SCALE/2f,
				-(cima.getWidth()*0.32f/State.PHYS_SCALE), (d-c));
	}
	
	public void drawMolduras(SpriteBatch sb, Texture cima, Texture esq, float dx, float dy) {
		
		sb.draw(cima,
				(float)(rect.getX() + rect.getWidth()/2f - cima.getWidth()/4f) / State.PHYS_SCALE,
				(float)(rect.getY() + rect.getHeight() + dy) / State.PHYS_SCALE,
				cima.getWidth()/2f / State.PHYS_SCALE,
				cima.getHeight()/2f / State.PHYS_SCALE);
		
		sb.draw(cima,
				(float)(rect.getX() + rect.getWidth()/2f - cima.getWidth()/4f) / State.PHYS_SCALE,
				(float)(rect.getY() - dy) / State.PHYS_SCALE,
				cima.getWidth()/2f / State.PHYS_SCALE,
				cima.getHeight()/2f / State.PHYS_SCALE * -1);
		
		sb.draw(esq,
				(float)(rect.getX() + dx) / State.PHYS_SCALE,
				(float)(rect.getY() + rect.getHeight()/2f - esq.getHeight()/4f) / State.PHYS_SCALE,
				esq.getWidth() / 2f / State.PHYS_SCALE,
				esq.getHeight() / 2f / State.PHYS_SCALE);
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
		drawLados(sb, 3, -2, sup_simple, lado_simple);
		drawQuina(sb, canto_sup, 10, 40);
		drawMolduras(sb, sup, lado, -68, -120);

		sb.flush();
	}

	public boolean update(float delta) {
		return false;
	}

}

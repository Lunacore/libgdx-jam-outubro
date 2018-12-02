package com.mygdx.game.test.components;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameParticle;
import com.mygdx.game.objects.ObjectInfo;

public class Button extends Platform{
	
	boolean bounded;
	ArrayList<Door> connections;
	BitmapFont font;
	boolean pressed = false;

	float timerToPressAgain = 0;
	
	Vector2 normalScale;
	
	static Texture fumacinha = new Texture("fumacinha.png");
	
	public Button(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		connections = new ArrayList<Door>();
		font = Helper.newFont("Allan-Bold.ttf", 18);
		
	}
	
	public void create() {
		super.create();
		
		int i = 0;
		while(get("conn" + i) != null) {
			Door d = (Door)get("conn" + i, Body.class).getUserData();
			d.setParent(this);
			connections.add(d);
			i ++;
		}
	}
	
	@Override
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
		if(imgObj.getTile() instanceof AnimatedTiledMapTile) {
			AnimatedTiledMapTile tile = (AnimatedTiledMapTile) imgObj.getTile();
			TextureRegion region = tile.getFrameTiles()[0].getTextureRegion(); //This gets the first frame (not pressed)
			
			if(pressed) {
				region = tile.getFrameTiles()[1].getTextureRegion(); //This gets the second frame (pressed)
			}
	
			renderBodyRegionNoCenter(sb, region, body, imgObj.isFlipHorizontally(), imgObj.isFlipVertically());
		}
	}
	
	
	public void activate() {
		for(Door d : connections) {
			d.open();
		}
	}

	
	@Override
	public boolean update(float delta) {
		super.update(delta);
		timerToPressAgain -= delta;
		
		return super.update(delta);
	}

	public boolean isBounded() {
		return bounded;
	}

	public void setBounded(boolean bounded) {
		this.bounded = bounded;
	}
	
	public boolean keyDown(int keycode) {
		if(keycode == Keys.SPACE && bounded) {
			activate();
		}
		return super.keyDown(keycode);
	}

	public void unPress() {
		if(pressed) {
			Fixture f = body.getFixtureList().get(1);
			f.setSensor(false);
			pressed = false;
			timerToPressAgain = 0.4f;
		}
	}
	
	public void press() {
		
		if(timerToPressAgain < 0) {
			pressed = true;
			Fixture f = body.getFixtureList().get(1);
			f.setSensor(true);
			
			activate();
			timerToPressAgain = 0.4f;
		}

	}

}

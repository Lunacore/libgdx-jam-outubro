package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.utils.ScreenSize;

public class OpeningCutsceneState extends State{

	int sceneIndex = 0;
	float sceneLerp = 0;
	
	Texture scenes[] = new Texture[6];
	
	Texture s2Black;
	Texture teddy;
	Texture olhos;
	
	float blackAlpha = -5f;
	
	float timer = 0;

	
	Music music;
	
	float introAlpha = 1;
	boolean intro;
	boolean outro;
	
	BitmapFont font;
	
	public OpeningCutsceneState(StateManager manager) {
		super(manager);
		
		scenes[0] = new Texture("cutscenes/op/01.jpg");
		scenes[1] = new Texture("cutscenes/op/02_bc.png");
		scenes[2] = new Texture("cutscenes/op/03.jpg");
		scenes[3] = new Texture("cutscenes/op/04.jpg");
		scenes[4] = new Texture("cutscenes/op/05.jpg");
		scenes[5] = new Texture("cutscenes/op/06.jpg");
		
		s2Black = new Texture("cutscenes/op/02_bc2.png");
		teddy = new Texture("cutscenes/op/02_td.png");
		olhos = new Texture("cutscenes/op/02_lz.png");
		
		font = Helper.newFont("Allan-Bold.ttf", 72, Color.BROWN.cpy().mul(0.3f));

	}
	
	boolean finished = false;

	public void create() {
		sceneIndex = 0;
		intro = true;
		outro = false;

		camera.position.set(ScreenSize.getWidth() /2f + ScreenSize.getWidth()*sceneLerp, ScreenSize.getHeight()/2f, 0);
	
		music = Gdx.audio.newMusic(Gdx.files.internal("music/intro.ogg"));
		music.play();
		music.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(Music music) {
				finished = true;
			}
		});
	}

	public void render(SpriteBatch sb) {
		Helper.enableBlend();
		
		camera.position.set(ScreenSize.getWidth() /2f + ScreenSize.getWidth()*sceneLerp, ScreenSize.getHeight()/2f, 0);

		sb.begin();
		//cena 1
		sb.draw(scenes[0], 0, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		//cena 2
		sb.draw(scenes[1], ScreenSize.getWidth(), 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		sb.setColor(1, 1, 1, Helper.clamp(blackAlpha, 0, 1));
		sb.draw(s2Black, ScreenSize.getWidth(), 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		sb.setColor(1, 1, 1, 1);
		sb.draw(teddy, ScreenSize.getWidth(), 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		sb.setColor(1, 1, 1, Helper.clamp(blackAlpha, 0, 1));
		sb.draw(olhos, ScreenSize.getWidth(), 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		sb.setColor(1, 1, 1, 1);
		//cena 3
		sb.draw(scenes[2], ScreenSize.getWidth()*2, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		//cena 4
		sb.draw(scenes[3], ScreenSize.getWidth()*3, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		//cena 5
		sb.draw(scenes[4], ScreenSize.getWidth()*4, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		//cena 6
		sb.draw(scenes[5], ScreenSize.getWidth()*5, 0, ScreenSize.getWidth(), ScreenSize.getHeight());

		sb.end();
		
		Helper.disableBlend();
		
		Helper.enableBlend();
				
		sr.setProjectionMatrix(Helper.getDefaultProjection());
		sr.begin(ShapeType.Filled);
		sr.setColor(new Color(0, 0, 0, introAlpha));
		sr.rect(0, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		sr.end();

		Helper.disableBlend();
	}

	public void update(float delta) {
		if(delta > 1/15f) delta = 1/60f;
		
		blackAlpha = Helper.clamp((music.getPosition() - 15)/10f, 0, 1);
		
		if(music.getPosition() < 11) sceneIndex = 0;
		else if(music.getPosition() < 28) sceneIndex = 1;
		else if(music.getPosition() < 34.9) sceneIndex = 2;
		else if(music.getPosition() < 41.5) sceneIndex = 3;
		else if(music.getPosition() < 55) sceneIndex = 4;
		else sceneIndex = 5;
		
		if(finished) {
			sceneIndex = 5;
			outro = true;
		}
		
		sceneLerp += (sceneIndex - sceneLerp)/15f;
		
		if(sceneIndex > 5) {
			outro = true;
		}
		
		if(intro) {
			introAlpha -= 1/60f;
			if(introAlpha < 0) {
				introAlpha = 0;
				intro = false;
			}
		}
		
		if(outro) {
			introAlpha += 1/60f;
			music.setVolume(1 - introAlpha);
			if(introAlpha >= 1) {
				introAlpha = 1;
				manager.changeState(0);
				music.stop();
				outro = false;
			}
		}
	}
	@Override
	public boolean keyDown(int keycode) {
		outro = true;
		intro = false;
				
		return super.keyDown(keycode);
	}

}

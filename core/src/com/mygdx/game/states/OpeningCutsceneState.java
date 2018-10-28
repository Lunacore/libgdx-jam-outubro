package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	}

	public void create() {
		sceneIndex = 0;
		
		
		camera.position.set(ScreenSize.getWidth() /2f + ScreenSize.getWidth()*sceneLerp, ScreenSize.getHeight()/2f, 0);
	
		music = Gdx.audio.newMusic(Gdx.files.internal("music/intro.ogg"));
		music.play();
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
	}

	public void update(float delta) {
		blackAlpha += 1/60f/2f;
		
		timer += 1/60f;
		
		sceneIndex = (int)(timer/9f);
		
		sceneLerp += (sceneIndex - sceneLerp)/15f;
		
		if(sceneIndex > 5) {
			music.stop();
			manager.changeState(0);
		}
	}

}
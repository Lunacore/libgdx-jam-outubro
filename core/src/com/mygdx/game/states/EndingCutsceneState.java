package com.mygdx.game.states;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.objects.Fade;
import com.mygdx.game.utils.ScreenSize;

public class EndingCutsceneState extends State{

	Texture background;

	Music music;
	Texture scenes[];
	OrthographicCamera camera;
	int intIndex;
	float timer = 0;
	Fade fade;
	
	public EndingCutsceneState(final StateManager manager) {
		super(manager);
		music = Gdx.audio.newMusic(Gdx.files.internal("music/Outro.ogg"));
		background = new Texture("fundo.jpg");
		scenes = new Texture[] {
				new Texture("cutscenes/end/01.jpg"),
				new Texture("cutscenes/end/02.jpg"),
				new Texture("cutscenes/end/03.jpg"),
				new Texture("cutscenes/end/04.jpg")
		};
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, 0);
		
		fade = new Fade(this);
		
		fade.setFadeFinishListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				music.stop();
				manager.changeState(5);
			}
		});
	}

	public void create() {
		System.out.println("End game step 4");
		music.setVolume(0);
		music.play();
		fade.create();
		intIndex = 0;
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(background, 0, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		
		sb.setColor(1, 1, 1, 1-fade.getAlpha());
		for(int i = 0; i < 4; i ++) {
			sb.draw(scenes[i], ScreenSize.getWidth() * i, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		}
		sb.setColor(1, 1, 1, 1);

		sb.end();
		
		if(!fade.isIntro())
		fade.render(sb, sr, camera);
	}
	int temp = 0;
	public void update(float delta) {
		camera.update();
		music.setVolume(1-fade.getAlpha());
		
		timer += delta;
		
		if(timer > 6) {
			timer -= 6;
			intIndex ++;
			if(intIndex >= 4) {
				intIndex = 3;
				temp ++;
				if(temp >= 1) {
					fade.end();
				}
			}
		}
		
		camera.position.add(
				((intIndex*Gdx.graphics.getWidth() + Gdx.graphics.getWidth()/2f) - camera.position.x)/15f,
				0, 0);
		
		fade.update(delta);

	}
	
	@Override
	public boolean keyDown(int keycode) {
		fade.end();
		return super.keyDown(keycode);
	}

}

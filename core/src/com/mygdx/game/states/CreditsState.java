package com.mygdx.game.states;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.Fade;
import com.mygdx.game.utils.ScreenSize;

public class CreditsState extends State{

	Texture background;
	BitmapFont font;
	float offset = 0;
	String creditos;
	Sound music;
	Fade fade;
	float speed = 1;
	
	long soundID;
	
	public CreditsState(StateManager manager) {
		super(manager);
		
		background = new Texture("fundo.jpg");
		
		font = Helper.newFont("Allan-Bold.ttf", 72, Color.BROWN.cpy().mul(0.3f));
		
		creditos = "LUNACORE TEAM \n\n " + 
				"Rotciv - Game Design, Level Design, Programming\r\n" + 
				"Alex Roda- Art, Animation\r\n" + 
				"Caio Gagliardi - Music and SFX\r\n" + 
				"Geovanna Votto- UX Design\r\n" + 
				"Titoncio - Level Design, Marketing\n\n" + 
				"Brownie - Emotional support\r\n" + 
				"Game made for the LibGDX Jam 5\n\n\n" + 
				"Thanks for playing!";
		fade = new Fade(this);
		
		music = Gdx.audio.newSound(Gdx.files.internal("music/ingame-slowed1.ogg"));
		fade.setFadeFinishListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				music.stop();
				Gdx.app.exit();
			}
		});
	}

	@Override
	public void create() {
		fade.create();
		soundID = music.play(0);
		music.setLooping(soundID, true);
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(background, 0, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		font.draw(sb, creditos, 0, 0 + offset, ScreenSize.getWidth(), 1, true);
		sb.end();
		
		fade.render(sb, sr, camera);
		
	}

	@Override
	public void update(float delta) {
		offset += delta * 50 * speed;
		music.setVolume(soundID, 1 - fade.getAlpha());
		fade.update(delta);
		
		if(offset > 2300) {
			fade.end();
		}
	}
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		music.setPitch(soundID, 2f);
		speed = 5;
		return super.touchDown(screenX, screenY, pointer, button);
	}
	
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		music.setPitch(soundID, 1f);
		speed = 1;
		return super.touchUp(screenX, screenY, pointer, button);
	}

}

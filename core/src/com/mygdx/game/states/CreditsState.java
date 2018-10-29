package com.mygdx.game.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.utils.ScreenSize;

public class CreditsState extends State{

	Texture background;
	BitmapFont font;
	
	float offset = 0;
	
	String creditos;
	
	public CreditsState(StateManager manager) {
		super(manager);
		
		background = new Texture("fundo.jpg");
		
		font = Helper.newFont("Allan-Bold.ttf", 72, Color.BROWN.cpy().mul(0.3f));
		
		creditos = "LUNACORE TEAM \n\n " + 
				"Rotciv - Game Design, Level Design, Programming\r\n" + 
				"Alex - Art, Animation\r\n" + 
				"Brownie - Emotional support\r\n" + 
				"Caio Gagliardi - Music and SFX\r\n" + 
				"Geovanna - UX Design\r\n" + 
				"Titoncio - Level Design, Marketing\n\n" + 
				"Game made for the LibGDX Jam 5\n\n\n" + 
				"Thanks for playing!";
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(background, 0, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		font.draw(sb, creditos, 0, 0 + offset, ScreenSize.getWidth(), 1, true);
		sb.end();
		
	}

	@Override
	public void update(float delta) {
		offset += delta * 50;
		
	}

}

package com.mygdx.game;

import javax.swing.JOptionPane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.states.StateManager;
import com.mygdx.game.utils.ScreenSize;

import de.golfgl.gdxgameanalytics.GameAnalytics;
import de.golfgl.gdxgameanalytics.GameAnalytics.ErrorType;
import io.anuke.gif.GifRecorder;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	
	StateManager manager;
	GifRecorder gifRecorder;
	
	static GameAnalytics gameAnalytics;
	
	@Override
	public void create () {
		try {
			initGameAnalytics();		
			batch = new SpriteBatch();
			manager = new StateManager();
			manager.create();
			gifRecorder = new GifRecorder(batch);
			ScreenSize.setDynamicScreen(true);
		}
		catch(Exception e) {
			printError(e);
		}
		
	}
	
	public void printError(Exception e) {
		String message = e.toString() + " -> " + e.getStackTrace()[0];
		e.printStackTrace();
		gameAnalytics.submitErrorEvent(ErrorType.error, message);
		Gdx.app.exit();
		
		//TODO: escrever erros num TXT
		
	}
	
	public static void sendEvent(String event, float value) {
		gameAnalytics.submitDesignEvent(event, value);
		
	}
	
	public static void setCustom1(String custom) {
		gameAnalytics.setCustom1(custom);
	}
	
	public static void sendEvent(String event) {
		gameAnalytics.submitDesignEvent(event);
	}

	@Override
	public void render () {
		try {
			Gdx.gl.glClearColor(17/255f, 26/255f, 36/255f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			manager.update(Gdx.graphics.getDeltaTime());
			manager.render(batch);
			
			gifRecorder.update();
			Helper.Game.globalTimer += Gdx.graphics.getDeltaTime();
		}
		catch(Exception e) {
			printError(e);
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
		gameAnalytics.closeSession();
	}
	
	protected void initGameAnalytics() {
		String appKey = "b8f40b6c58b4740231ca69e422a5e944";
		String secret = "a2b58d22b1a0c5b362a405bfc990e120c7e9b61c";
				
	    gameAnalytics = new GameAnalytics();
	    gameAnalytics.setPlatformVersionString("1");

	    gameAnalytics.setGameBuildNumber("debug");

	    gameAnalytics.setPrefs(Gdx.app.getPreferences("preferences"));
	    gameAnalytics.setGameKey(appKey);
	    gameAnalytics.setGameSecretKey(secret);
	    gameAnalytics.startSession();
	}
}

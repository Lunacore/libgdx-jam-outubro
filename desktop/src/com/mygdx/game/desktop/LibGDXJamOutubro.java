package com.mygdx.game.desktop;

import java.awt.Toolkit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class LibGDXJamOutubro {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		config.height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();;
		config.fullscreen = true;
		new LwjglApplication(new MyGdxGame(), config);
	}
}

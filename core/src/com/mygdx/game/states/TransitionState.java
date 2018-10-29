package com.mygdx.game.states;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.test.Canvas;
import com.mygdx.game.test.CanvasFrame;
import com.mygdx.game.utils.ScreenSize;

public class TransitionState extends State{

	public static TextureRegion lastPrint;
	
	Texture print;
	
	float offset = 0;
	float timer = 0;
	
	Texture nextPhaseBG;
	CanvasFrame frame;
	
	public static String nextPhase;

	public TransitionState(StateManager manager) {
		super(manager);	
		
		enablePhysics(null);
		print = new Texture("fundo.jpg");
		
		frame = new CanvasFrame(new ObjectInfo(this, 0, 1f),
				new Rectangle2D.Double(
						ScreenSize.getWidth() - (ScreenSize.getWidth() - 800) / 2f,
						0, 800, 600));
		camera.position.set(400 / State.PHYS_SCALE, 300 / State.PHYS_SCALE, 0);

	}
	
	public String proximo() {
		if(Canvas.levelToLoad != null) {
			TiledMap t = new TmxMapLoader().load(Canvas.levelToLoad);
			String ret = t.getProperties().get("fundo", String.class);
			t.dispose();
			return ret;
		}
		return null;
		
	}

	public void create() {
		offset  = 0;
		timer = 0;
		String prox = proximo();
		if(prox != null) {
			nextPhaseBG = new Texture(proximo());
		}
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		
		sb.begin();
		sb.draw(lastPrint, -offset, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		sb.draw(print, ScreenSize.getWidth() - offset, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		
		if(nextPhaseBG != null) {
			sb.draw(nextPhaseBG, ScreenSize.getWidth() - offset + (ScreenSize.getWidth() - 800)/2f, (ScreenSize.getHeight() - 600)/2f, 800, 600);
			
			sb.setProjectionMatrix(camera.combined);
			frame.render(sb, sr, camera);
		}
		sb.end();
		
		
	}

	public void update(float delta) {
		timer += delta;
		camera.position.set((400 + offset) / State.PHYS_SCALE, 300 / State.PHYS_SCALE, 0);
		camera.update();
		timer = Math.min(timer, 2);
		
		frame.update(delta);
		offset = easeInOut(timer/2f, 0, ScreenSize.getWidth(), 1);
		
		if(timer >= 2) {
			if(nextPhaseBG != null) {
				manager.changeState(0);
			}
			else {
				manager.changeState(4);
			}
		}
		
	}
	
	public float easeInOut(float t,float b , float c, float d) {
		if ((t/=d/2) < 1) return c/2*t*t + b;
		return -c/2 * ((--t)*(t-2) - 1) + b;
	}
}

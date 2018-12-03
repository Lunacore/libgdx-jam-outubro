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
	
	Texture parede;
	
	float offset = 0;
	float timer = 0;
	
	Texture nextPhaseBG;
	CanvasFrame frame;
	
	public static String nextPhase;
	
	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	
	public static int direction = UP;


	public TransitionState(StateManager manager) {
		super(manager);	
		
		enablePhysics(null);
		parede = new Texture("fundo.jpg");
		
		
		camera.position.set(400 / State.PHYS_SCALE, 300 / State.PHYS_SCALE, 0);

	}
	
	public String proximo() {
		if(Canvas.levelToLoad != null && !Canvas.levelToLoad.equals("")) {
			System.out.println("[" + Canvas.levelToLoad + "]");
			TiledMap t = new TmxMapLoader().load(Canvas.levelToLoad);
			String ret = t.getProperties().get("fundo", String.class);
			t.dispose();
			return ret;
		}
		return null;
		
	}

	public void create() {
		
		float stX = 0;
		float stY = 0;
		
		switch(direction) {
			case LEFT:
				stX = ScreenSize.getWidth();
				break;
			case RIGHT:
				stX = -ScreenSize.getWidth();
				break;
			case UP:
				stY = ScreenSize.getHeight();
				break;
			case DOWN:
				stY = - ScreenSize.getHeight();
				break;
		}
		
		frame = new CanvasFrame(new ObjectInfo(this, 0, 1f),
				new Rectangle2D.Double(
						stX - (ScreenSize.getWidth() - 800) / 2f,
						stY, 800, 600));
		
		offset  = 0;
		timer = 0;
		String prox = proximo();
		if(prox != null) {
			nextPhaseBG = new Texture(proximo());
		}
		else {
			manager.changeState(4);
		}
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		
		sb.begin();
		
		if(direction == LEFT || direction == RIGHT) {
			sb.draw(lastPrint, -offset, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		}
		else {
			sb.draw(lastPrint, 0, -offset, ScreenSize.getWidth(), ScreenSize.getHeight());
		}
		
		switch(direction) {
			case LEFT:
				sb.draw(parede, ScreenSize.getWidth() - offset, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
				if(nextPhaseBG != null) {
					sb.draw(nextPhaseBG, ScreenSize.getWidth() - offset + (ScreenSize.getWidth() - 800)/2f, (ScreenSize.getHeight() - 600)/2f, 800, 600);
				}
				break;
			case RIGHT:
				sb.draw(parede, - ScreenSize.getWidth() - offset, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
				if(nextPhaseBG != null) {
					sb.draw(nextPhaseBG, - ScreenSize.getWidth() - offset + (ScreenSize.getWidth() - 800)/2f, (ScreenSize.getHeight() - 600)/2f, 800, 600);
				}
				break;
			case UP:
				sb.draw(parede, 0, ScreenSize.getHeight() - offset, ScreenSize.getWidth(), ScreenSize.getHeight());
				if(nextPhaseBG != null) {
					sb.draw(nextPhaseBG,(ScreenSize.getWidth() - 800)/2f, ScreenSize.getHeight() + (ScreenSize.getHeight() - 600)/2f - offset, 800, 600);
				}
				break;
			case DOWN:
				sb.draw(parede, 0, - ScreenSize.getHeight() - offset, ScreenSize.getWidth(), ScreenSize.getHeight());
				if(nextPhaseBG != null) {
					sb.draw(nextPhaseBG, (ScreenSize.getWidth() - 800)/2f, - ScreenSize.getHeight() + (ScreenSize.getHeight() - 600)/2f - offset, 800, 600);
				}
				break;
		}
		
		
		if(nextPhaseBG != null) {			
			sb.setProjectionMatrix(camera.combined);
			frame.render(sb, sr, camera);
		}
		sb.end();
		
		
	}

	public void update(float delta) {
		timer += delta;
		
		if(direction == LEFT || direction == RIGHT) {
			camera.position.set((-150 + offset) / State.PHYS_SCALE, 300 / State.PHYS_SCALE, 0);
		}
		else {
			camera.position.set(-150 / State.PHYS_SCALE, (300 + offset) / State.PHYS_SCALE, 0);
		}
		
		timer = Math.min(timer, 2);
		
		frame.update(delta);
		
		switch(direction) {
			case LEFT:
				offset = easeInOut(timer/2f, 0, ScreenSize.getWidth(), 1);
				break;
			case RIGHT:
				offset = easeInOut(timer/2f, 0, -ScreenSize.getWidth(), 1);
				break;
			case UP:
				offset = easeInOut(timer/2f, 0, ScreenSize.getHeight(), 1);
				break;
			case DOWN:
				offset = easeInOut(timer/2f, 0, - ScreenSize.getHeight(), 1);
				break;
		}
		
		if(timer >= 2) {
			if(nextPhaseBG != null) {
				manager.changeState(0);
			}
			else {
				manager.changeState(4);
			}
		}
		camera.update();
	}
	
	public float easeInOut(float t,float b , float c, float d) {
		if ((t/=d/2) < 1) return c/2*t*t + b;
		return -c/2 * ((--t)*(t-2) - 1) + b;
	}
}

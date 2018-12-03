package com.mygdx.game.states;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.objects.KeyMapper.Device;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.test.Canvas;
import com.mygdx.game.utils.ScreenSize;

public class StateOne extends State{

	Canvas canvas;
	
	public StateOne(StateManager manager) {
		super(manager);
	}
	
	public void create() {
		dispose();
		enablePhysics(new StateOneListener(this));
		//enableDebugDraw();
		setGravity(new Vector2(0, -40));
		
		MyGdxGame.setCustom1(Canvas.levelToLoad);
		MyGdxGame.sendEvent("state_begin_StateOne");
		
		
		setTmxMap(Canvas.levelToLoad, 1);
		camera.position.set(400 / State.PHYS_SCALE, 300 / State.PHYS_SCALE, 0);
		
		canvas = new Canvas(new ObjectInfo(this, 0,1f),tmxRenderer.getTiledMap().getProperties().get("fundo", String.class));
		putInScene(canvas);
		
	}
	
	@Override
	public void postRender(SpriteBatch sb) {
		super.postRender(sb);
		if(nextLevel) {
			TransitionState.lastPrint = ScreenUtils.getFrameBufferTexture(0, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
			manager.changeState(1);
			nextLevel = false;
		}
		
	}

	public void render(SpriteBatch sb) {
		
	}

	public void update(float delta) {		
		
	}

	public void kill() {
		canvas.kill();
	}

	public float getWorldSpeed() {
		return canvas.getWorldSpeed();
	}
	
	boolean nextLevel = false;

	public void nextLevel() {
		nextLevel = true;
		if(Canvas.levelToLoad.equals("")) {
			canvas.stopMusic();
		}
	}

}

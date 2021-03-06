package com.mygdx.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.Fade;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.test.Canvas;
import com.mygdx.game.test.MyPlayer;
import com.mygdx.game.test.components.SpawnPoint;
import com.mygdx.game.utils.FrameBufferStack;
import com.mygdx.game.utils.ScreenSize;

public class StateOne extends State{

	Canvas canvas;
	
	FrameBuffer wholeScene;
	ShaderProgram shader;
	Texture abs;
	float intensity = 0;
	public static String spawnPoint = null;
	
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
		
		System.out.println("Trying to load: [" + Canvas.levelToLoad + "]");
		
		setTmxMap(Canvas.levelToLoad, 1);
		camera.position.set(400 / State.PHYS_SCALE, 300 / State.PHYS_SCALE, 0);
		
		canvas = new Canvas(new ObjectInfo(this, 0,1f),tmxRenderer.getTiledMap().getProperties().get("fundo", String.class));
		putInScene(canvas);
		
		wholeScene = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		shader = new ShaderProgram(Gdx.files.internal("shaders/default.vs"), Gdx.files.internal("shaders/scene.fs"));
		abs = new Texture("shaders/abstract.jpg");
		if(shader.getLog().length() > 0) {
			System.err.println(shader.getLog());
			Gdx.app.exit();
		}
		
		if(spawnPoint != null) {
			boolean found = false;
			for(GameObject go : getByClass(SpawnPoint.class)){
				SpawnPoint sp = (SpawnPoint) go;
				if(sp.getName() != null && sp.getName().equals(spawnPoint)) {
					MyPlayer luna = (MyPlayer) getByClass(MyPlayer.class).get(0);
					luna.setBodyPosition(sp.getPosition());
					found = true;
					break;
				}
			}
			
			if(!found) {
				System.err.println("Spawn point " + spawnPoint + " not located on map " + TransitionState.nextPhase);
				Gdx.app.exit();
			}
			
			spawnPoint = null;
		}
		
	}
	
	
	@Override
	public void preRender(SpriteBatch sb) {
		FrameBufferStack.begin(wholeScene);
		// TODO Auto-generated method stub
		super.preRender(sb);
	}

	public void render(SpriteBatch sb) {
		
	}
	

	@Override
	public void postRender(SpriteBatch sb) {
		super.postRender(sb);
		
		
		FrameBufferStack.end();
		
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		
		
		
		shader.begin();
		shader.setUniformf("intensity", intensity);
		
		Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE1);
		abs.bind(1);
		shader.setUniformi("v_texture", 1);		

		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		sb.setShader(shader);
		
		sb.begin();
		sb.draw(FrameBufferStack.getTexture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
		sb.end();
		
		shader.end();
		sb.setShader(null);
		
		
		if(nextLevel) {
			System.out.println("End game step 2");
			TransitionState.lastPrint = ScreenUtils.getFrameBufferTexture(0, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
			nextLevel = false;
			manager.changeState(1);
			return;
		}
	}
	
	
	public void update(float delta) {		
		intensity += (0 - intensity) / 10f;
	}

	public void kill() {
		canvas.kill();
		intensity = 0.5f;

	}

	public float getWorldSpeed() {
		return canvas.getWorldSpeed();
	}
	
	boolean nextLevel = false;

	public void nextLevel() {
		nextLevel = true;
		if(Canvas.levelToLoad.equals("")) {
			System.out.println("End game step 1");
			canvas.stopMusic();
		}
	}

	public Canvas getCanvas() {
		return canvas;
	}

}

package com.mygdx.game.test;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.helper.Helper.Position;
import com.mygdx.game.helper.Helper.Size;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.ParallaxBackground;
import com.mygdx.game.objects.XBoxController;
import com.mygdx.game.states.State;
import com.mygdx.game.test.components.Platform;
import com.mygdx.game.utils.FrameBufferStack;
import com.mygdx.game.utils.ScreenSize;

import de.quippy.jflac.frame.Frame;

public class Canvas extends GameObject{

	Rectangle2D canvasBox;
	Rectangle2D targetCanvasBox;
	ArrayList<GameObject> platforms;
	float tween = 15f;
	
	static float musicCurrentPitch;
	
	public static String levelToLoad = "tiled/maps/EMPTY.tmx";
	
	static Sound musicTest;
	long musicID;
	
	boolean playerDead;
	
	CanvasFrame frame;
	//ParallaxBackground bg;
	Texture wall_bg;
	CanvasBackground canvas_bg;
	
	FrameBuffer fbo;
	FrameBuffer fbo2;
	FrameBuffer fboplats;
	
	ShaderProgram shader;
	
	boolean xLocked;
	boolean yLocked;
	
	Arm armX;
	Arm armY;
	
	Vector2 armXcurrent;
	Vector2 armYcurrent;
	
	boolean intro;
	boolean outro;
	
	float introTimer = 0;
	float outroTimer = 0;

	Texture bear_canto;
	float ursoPos = 0;
	
	public Canvas(ObjectInfo info, String imagem) {
		super(info, new MapProperties());
		
		
		intro = true;
		outro = false;
		
		playerDead = false;
		platforms = new ArrayList<GameObject>();
		canvasBox = new Rectangle2D.Double(0, 0, 800, 600);
		targetCanvasBox = (Rectangle2D) canvasBox.clone();
		
		platforms.addAll(getState().getByClass(Platform.class));
		platforms.addAll(getState().getByClass(MyPlayer.class));
		
		for(int i = platforms.size() -1 ; i >= 0; i --) {
			platforms.get(i).setToRender(false);
		}
		
		Object xl = getState().getTmxRenderer().getTiledMap().getProperties().get("xLocked");
		xLocked = xl == null ? false : (Boolean) xl;
		Object yl = getState().getTmxRenderer().getTiledMap().getProperties().get("yLocked");
		yLocked = yl == null ? false : (Boolean) yl;
		
		Object hob = getState().getTmxRenderer().getTiledMap().getProperties().get("startHeight");
		Object wob = getState().getTmxRenderer().getTiledMap().getProperties().get("startWidth");
		
		if(hob != null && wob != null) {
			resizeBox(new Vector2((Float)wob, (Float) hob));
		}
		
		bear_canto = new Texture("arm/bear.png");
		ursoPos = -bear_canto.getWidth() * 0.35f;
		//resizeBox(new Vector2(800, 350));
		
		if(musicTest == null) {
			musicTest = Gdx.audio.newSound(Gdx.files.internal("music/ingame-normal.ogg"));
			musicID = musicTest.play();
			musicTest.setLooping(musicID, true);
		}
		
		frame = new CanvasFrame(new ObjectInfo(getState(), 4, 1f), canvasBox);
		
		wall_bg = new Texture("fundo.jpg");
		//bg = new ParallaxBackground(new ObjectInfo(getState(), -3, 1f), "wall_bg.png");
		
		canvas_bg = new CanvasBackground(new ObjectInfo(getState(), 2, 1f), canvasBox, imagem);
	
		fbo = new FrameBuffer(Format.RGBA8888, ScreenSize.getWidth(), ScreenSize.getHeight(), false);
		fbo2 = new FrameBuffer(Format.RGBA8888, ScreenSize.getWidth(), ScreenSize.getHeight(), false);
		fboplats = new FrameBuffer(Format.RGBA8888, ScreenSize.getWidth(), ScreenSize.getHeight(), false);

		shader = new ShaderProgram(Gdx.files.internal("shaders/default.vs"), Gdx.files.internal("shaders/blur.fs"));
		ShaderProgram.pedantic = false;
		
		if(shader.getLog().length() > 0) {
			System.err.println(shader.getLog());
		}
		
		armX = new Arm(info, Vector2.Zero.cpy(), Size.SCREEN.cpy());
		armY = new Arm(info, Vector2.Zero.cpy().add(Size.HEIGHT), Size.WIDTH);
		
		armXcurrent = new Vector2(Position.CENTERY.cpy());
		armYcurrent = new Vector2(Position.CENTERX.cpy().add(0, ScreenSize.getHeight()));

	}

	public void create() {
		
		
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		//bg.render(sb, sr, camera);
		sb.draw(wall_bg, 0, 0, ScreenSize.getWidth(), ScreenSize.getHeight());
		
		sb.setProjectionMatrix(camera.combined);
		FrameBufferStack.begin(fbo);
		Gdx.gl.glClearColor(17/255f, 26/255f, 36/255f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		canvas_bg.render(sb, sr, camera);
		frame.render(sb, sr, camera);
		
		FrameBufferStack.end();
		
		Texture tex = FrameBufferStack.getTexture();
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		sb.flush();
		
		FrameBufferStack.begin(fbo2);
		Gdx.gl.glClearColor(17/255f, 26/255f, 36/255f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		sb.setShader(shader);
		shader.setUniformf("dir", 1f, 0f);
		shader.setUniformf("radius", 5f);
		shader.setUniformf("resolution", 1920);
		
		
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		sb.draw(tex, 0, ScreenSize.getHeight(), ScreenSize.getWidth(), -ScreenSize.getHeight());
		sb.setShader(null);
		sb.setProjectionMatrix(camera.combined);
		
		FrameBufferStack.end();
		
		sb.flush();
		
		Texture tex2 = FrameBufferStack.getTexture();
		tex2.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		sb.setShader(shader);
		shader.setUniformf("dir", 0f, 1f);
		shader.setUniformf("radius", 5f);
		shader.setUniformf("resolution", 1920);
		
		
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		sb.setColor(Color.BLACK);
			sb.draw(tex2, 10, ScreenSize.getHeight() - 10, ScreenSize.getWidth(), -ScreenSize.getHeight());
		sb.setColor(Color.WHITE);
		sb.setShader(null);

		sb.draw(tex, 0, ScreenSize.getHeight(), ScreenSize.getWidth(), -ScreenSize.getHeight());
		
		sb.setProjectionMatrix(camera.combined);
		//Desenha os blocos recortados
		
		FrameBufferStack.begin(fboplats);
		Gdx.gl.glClearColor(17/255f, 26/255f, 36/255f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		for(int i = platforms.size() -1 ; i >= 0; i --) {
			platforms.get(i).render(sb, sr, camera);
		}
		
		FrameBufferStack.end();
		
		Texture plats = FrameBufferStack.getTexture();

		TextureRegion partialplats = new TextureRegion(plats,
				(int)(canvasBox.getX() + (ScreenSize.getWidth() - 800)/2f),
				(int)(canvasBox.getY() + (ScreenSize.getHeight() - 600)/2f),
				(int)canvasBox.getWidth(),
				(int)canvasBox.getHeight()
				);

		sb.end();
		
		sr.setProjectionMatrix(Helper.getDefaultProjection());
		sr.begin(ShapeType.Line);
			sr.rect(
					(int)canvasBox.getX() + (ScreenSize.getWidth() - 800)/2f,
					(int)canvasBox.getY() + (ScreenSize.getHeight() - 600)/2f,
					(int)canvasBox.getWidth(),
					(int)canvasBox.getHeight());
			
		sr.end();
		
		
		sb.begin();
		
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		
		sb.draw(partialplats,
				(int)canvasBox.getX() + (ScreenSize.getWidth() - 800)/2f,
				ScreenSize.getHeight() - (int)canvasBox.getY() - (ScreenSize.getHeight() - 600)/2f,
				(int)canvasBox.getWidth(),
				-(int)canvasBox.getHeight());
		
		ursoPos += (0 - ursoPos)/5f;
		
		sb.draw(bear_canto,
				ursoPos,
				ScreenSize.getHeight() - bear_canto.getHeight() * 0.35f,
				bear_canto.getWidth() * 0.35f,
				bear_canto.getHeight() * 0.35f);
		
		if(!xLocked)
		armX.render(sb, sr, camera);
		if(!yLocked)
		armY.render(sb, sr, camera);
		
		sb.setProjectionMatrix(camera.combined);
}
	
	
	public void resizeBox(Vector2 newSize) {
		newSize.set(Helper.clamp(newSize.x, 300, 1000), Helper.clamp(newSize.y, 200, 600));
		targetCanvasBox.setFrame((800 - newSize.x)/2f, (600 - newSize.y)/2f, newSize.x, newSize.y);
	}
	
	public boolean update(float delta) {
		introTimer += delta;
		if(introTimer < 0.5f) {
			intro = true;
		}
		if(outro) {
			outroTimer += delta;
			if(outroTimer > 0.5f) {
				outro = false;
				getState().manager.changeState(0);
			}
		}
		//bg.update(delta);
		canvas_bg.update(delta);
		frame.update(delta);
		
		Rectangle2D oldRect = (Rectangle2D) canvasBox.clone();
		
		armX.update(delta);
		armY.update(delta);		
		
		float ax = ((ScreenSize.getHeight() - Gdx.input.getY()));
		float ay  = Gdx.input.getX();
		
		ax = Helper.clamp(ax, (float)canvasBox.getY() + (ScreenSize.getHeight() - 600)/2f, (float)(canvasBox.getY() + (ScreenSize.getHeight() - 600)/2f + canvasBox.getHeight()));
		ay = Helper.clamp(ay, (float)canvasBox.getX() + (ScreenSize.getWidth() - 800)/2f, (float)(canvasBox.getX() + (ScreenSize.getWidth() - ScreenSize.getHeight())/2f + canvasBox.getWidth() - 50));
		
		//ArmX
		
		
		if(intro) {
			armXcurrent.add(
				(((float)(canvasBox.getX() + (ScreenSize.getWidth() - 800)/2f - 10) / State.PHYS_SCALE) - armXcurrent.x)/5f,
				(ax / State.PHYS_SCALE - armXcurrent.y) / 5f
				);
		}
		if(outro) {
			armXcurrent.add(
					(0 - armXcurrent.x)/5f,
					(ax / State.PHYS_SCALE - armXcurrent.y) / 5f
					);
		}
		
		if(!intro && !outro) {
			armX.setArmStart(new Vector2(
					0, 
					ax / State.PHYS_SCALE
					));
			armX.setArmEnd(new Vector2(
				(float)(canvasBox.getX() + (ScreenSize.getWidth() - 800)/2f - 10) / State.PHYS_SCALE, 
				ax / State.PHYS_SCALE
				));
		}
		else {
			armX.setArmEnd(armXcurrent);
			armX.setArmStart(new Vector2((-50) / State.PHYS_SCALE, (ScreenSize.getHeight() - 300) / State.PHYS_SCALE));
		}
		
		
		//ArmY
		if(intro) {
		armYcurrent.add(
				((ay / State.PHYS_SCALE) - armYcurrent.x)/5f,
				(((float)(ScreenSize.getHeight() - canvasBox.getY() - (ScreenSize.getHeight() - 600)/2f) / State.PHYS_SCALE) - armYcurrent.y) / 5f
				);
		}
		if(outro) {
			armYcurrent.add(
				((ay / State.PHYS_SCALE) - armYcurrent.x)/5f,
				((ScreenSize.getHeight() / State.PHYS_SCALE) - armYcurrent.y) / 5f
					);
		}
		
		if(!intro && !outro) {
			armY.setArmStart(new Vector2(
					ay / State.PHYS_SCALE, 
					ScreenSize.getHeight() / State.PHYS_SCALE
					));
			
			armY.setArmEnd(new Vector2(
				ay / State.PHYS_SCALE, 
				(float)(ScreenSize.getHeight() - canvasBox.getY() - (ScreenSize.getHeight() - 600)/2f) / State.PHYS_SCALE
				));
		}
		else {
			armY.setArmEnd(armYcurrent);
			armY.setArmStart(new Vector2((200) / State.PHYS_SCALE, (ScreenSize.getHeight() + 100) / State.PHYS_SCALE));
			//armY.setArmStart(armYcurrent.cpy().scl(1, 0).add(0, ScreenSize.getHeight() / State.PHYS_SCALE));
		}
		
		canvasBox.setRect(
				canvasBox.getX() + (targetCanvasBox.getX() - canvasBox.getX())/tween,
				canvasBox.getY() + (targetCanvasBox.getY() - canvasBox.getY())/tween,
				canvasBox.getWidth() + (targetCanvasBox.getWidth() - canvasBox.getWidth())/tween,
				canvasBox.getHeight() + (targetCanvasBox.getHeight() - canvasBox.getHeight())/tween
				);
		for(GameObject g : platforms) {
			if(g instanceof Platform)
			((Platform)g).resizeBox(oldRect, canvasBox);
		}
		
		float a = (float) ((canvasBox.getHeight() - 100f) / 500f);
		
		if(!playerDead)
			getState().worldStepFPS = Helper.lerp(30, 100, a);
		else
			getState().worldStepFPS = 100000000f;
		
		musicCurrentPitch += (Helper.lerp(0.5f, 1.7f, 1-a) - musicCurrentPitch)/15f;
		
		musicTest.setPitch(musicID, musicCurrentPitch);
		return false;
	}
	
	float contScale = 15;
	
	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		
		float nw = (float) canvasBox.getWidth();
		float nh = (float) canvasBox.getHeight();
		
		if(axisCode == XBoxController.AXIS_RIGHT_X) {
			nw = (float) (canvasBox.getWidth() - value*contScale);
		}
		if(axisCode == XBoxController.AXIS_RIGHT_Y) {
			nh = (float) (canvasBox.getHeight() - value*contScale);
		}
		
		if(xLocked) nw = 800;
		if(yLocked) nh = 600;
		
		resizeBox(new Vector2(nw, nh));
		
		return super.axisMoved(controller, axisCode, value);
	}
	
	float lastScreenX;
	float lastScreenY;

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
			
			int fx = 1;
			int fy = 1;
			
			if(screenX > ScreenSize.getWidth()/2f) fx = -1;
			if(screenY > ScreenSize.getHeight()/2f) fy = -1;
			
			float nw = (float) (canvasBox.getWidth() - (screenX - lastScreenX)*contScale*fx);
			float nh = (float) (canvasBox.getHeight() - (screenY - lastScreenY)*contScale*fy);

//			float nw = (float)Math.abs((screenX - Gdx.graphics.getWidth()/2f)) * 2f;
//			float nh = (float)Math.abs((screenY - Gdx.graphics.getHeight()/2f)) * 2f;
//			
			if(xLocked) nw = 800;
			if(yLocked) nh = 600;
			
			resizeBox(new Vector2(nw, nh));
			
			lastScreenX = screenX;
			lastScreenY = screenY;
		}
		return super.touchDragged(screenX, screenY, pointer);
	}

	public float getWorldSpeed() {
		float a = (float) ((canvasBox.getHeight() - 100f) / 500f);
		return Helper.lerp(0.5f, 1.7f, 1-a);
	}

	public void kill() {
		playerDead = true;
		intro = false;
		outro = true;
		//Espera pelo fim do outro
		getState().manager.changeState(0);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		return super.keyDown(keycode);
	}
}

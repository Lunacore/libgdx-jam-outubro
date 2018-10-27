package com.mygdx.game.test;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.ParallaxBackground;
import com.mygdx.game.test.components.Platform;
import com.mygdx.game.utils.FrameBufferStack;

public class Canvas extends GameObject{

	Rectangle2D canvasBox;
	Rectangle2D targetCanvasBox;
	ArrayList<GameObject> platforms;
	float tween = 15f;
	
	public static String levelToLoad = "maps/fase1.tmx";
	
	Sound musicTest;
	long musicID;
	
	boolean playerDead;
	
	CanvasFrame frame;
	ParallaxBackground bg;
	CanvasBackground canvas_bg;
	
	FrameBuffer fbo;
	
	ShaderProgram shader;
	
	
	public Canvas(ObjectInfo info) {
		super(info, new MapProperties());
		
		playerDead = false;
		platforms = new ArrayList<GameObject>();
		canvasBox = new Rectangle2D.Double(0, 0, 800, 600);
		targetCanvasBox = (Rectangle2D) canvasBox.clone();
		
		platforms = getState().getByClass(Platform.class);
		
		resizeBox(new Vector2(800, 350));
		
		musicTest = Gdx.audio.newSound(Gdx.files.internal("music/badvibes.wav"));
		musicID = musicTest.play();
		musicTest.setLooping(musicID, true);
		
		frame = new CanvasFrame(new ObjectInfo(getState(), 3, 1f), canvasBox);
		
		bg = new ParallaxBackground(new ObjectInfo(getState(), -3, 1f), "wall_bg.png");
		
		canvas_bg = new CanvasBackground(new ObjectInfo(getState(), 2, 1f), canvasBox);
	
		fbo = new FrameBuffer(Format.RGBA8888, 1280, 720, false);
		
		shader = new ShaderProgram(Gdx.files.internal("shaders/default.vs"), Gdx.files.internal("shaders/blacken.fs"));
		ShaderProgram.pedantic = false;
	}

	public void create() {
		

	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
		bg.render(sb, sr, camera);
		
		FrameBufferStack.begin(fbo);
		Gdx.gl.glClearColor(17/255f, 26/255f, 36/255f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		canvas_bg.render(sb, sr, camera);
		frame.render(sb, sr, camera);
		
		FrameBufferStack.end();
		
		Texture tex = FrameBufferStack.getTexture();
		
		sb.flush();
		
		shader.begin();
		shader.setUniformf("pass", 4);
		shader.setUniformf("strength", 0.003f);
		
		sb.setShader(shader);
		sb.setProjectionMatrix(Helper.getDefaultProjection());

			sb.draw(tex, 0, 720, 1280, -720);
			
		sb.setShader(null);
		shader.end();
		sb.setProjectionMatrix(camera.combined);
		
	}
	
	
	public void resizeBox(Vector2 newSize) {
		newSize.set(Helper.clamp(newSize.x, 300, 1000), Helper.clamp(newSize.y, 200, 600));
		targetCanvasBox.setFrame((800 - newSize.x)/2f, (600 - newSize.y)/2f, newSize.x, newSize.y);
	}

	public boolean update(float delta) {
		bg.update(delta);
		canvas_bg.update(delta);
		frame.update(delta);
		
		Rectangle2D oldRect = (Rectangle2D) canvasBox.clone();

		canvasBox.setRect(
				canvasBox.getX() + (targetCanvasBox.getX() - canvasBox.getX())/tween,
				canvasBox.getY() + (targetCanvasBox.getY() - canvasBox.getY())/tween,
				canvasBox.getWidth() + (targetCanvasBox.getWidth() - canvasBox.getWidth())/tween,
				canvasBox.getHeight() + (targetCanvasBox.getHeight() - canvasBox.getHeight())/tween
				);
		for(GameObject g : platforms) {
			((Platform)g).resizeBox(oldRect, canvasBox);
		}
		
		float a = (float) ((canvasBox.getHeight() - 100f) / 500f);
		
		if(!playerDead)
			getState().worldStepFPS = Helper.lerp(30, 100, a);
		else
			getState().worldStepFPS = 100000000f;
		
		musicTest.setPitch(musicID, Helper.lerp(0.5f, 1.7f, 1-a));
		return false;
	}
	

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
			float nw = (float)Math.abs((screenX - Gdx.graphics.getWidth()/2f)) * 2f;
			float nh = (float)Math.abs((screenY - Gdx.graphics.getHeight()/2f)) * 2f;
			resizeBox(new Vector2(nw, nh));
		}
		return super.touchDragged(screenX, screenY, pointer);
	}

	public float getWorldSpeed() {
		float a = (float) ((canvasBox.getHeight() - 100f) / 500f);
		return Helper.lerp(0.5f, 1.7f, 1-a);
	}

	public void kill() {
		playerDead = true;
		getState().manager.changeState(0);
	}
}

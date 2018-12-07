package com.mygdx.game.test.components;


import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;

public class Reflector extends Platform{

	boolean once = false;
	
	public Reflector(ObjectInfo info, MapProperties properties) {
		super(info, properties);	
		
		if(body.getType() == BodyType.DynamicBody) {
			body.getFixtureList().get(0).setFriction(1);
			getTransform().getPosition().add(6, 8);
			Vector2 prots = body.getTransform().getPosition().cpy().sub(body.getWorldCenter());		
			body.setTransform(
					body.getTransform().getPosition().add(prots.scl(2)),
					body.getTransform().getRotation()
				);
			body.setBullet(true);
		}
		
	
	}
	
	@Override
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
		if(body.getType() == BodyType.DynamicBody) {
			getTransform().getPosition().set(
					(float)(6 * Math.cos(body.getAngle()) - 8 * Math.sin(body.getAngle())),
					(float)(6 * Math.sin(body.getAngle()) + 8 * Math.cos(body.getAngle()))
					);
			
			TextureRegion region = imgObj.getTile().getTextureRegion();
			renderBodyRegion(sb, region, body);
		}
		else {
			super.render(sb, sr, camera);
		}
	}
	
	@Override
	public void resizeBox(Rectangle2D oldRect, Rectangle2D newRect) {
		if(!once) {
			changePosition(oldRect, newRect);
			once = true;
		}
	}

}

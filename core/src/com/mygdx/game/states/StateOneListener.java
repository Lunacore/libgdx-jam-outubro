package com.mygdx.game.states;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.objects.EmptyContact;
import com.mygdx.game.objects.PlatformPlayer;
import com.mygdx.game.test.MyPlayer;
import com.mygdx.game.test.components.Button;
import com.mygdx.game.test.components.DeathCompo;
import com.mygdx.game.test.components.EndLevel;
import com.mygdx.game.test.components.Laser;
import com.mygdx.game.test.components.LaserEmitter;
import com.mygdx.game.test.components.LaserReceiver;
import com.mygdx.game.test.components.Platform;
import com.mygdx.game.test.components.Reflector;

public class StateOneListener extends EmptyContact{

	
	public StateOneListener(State state) {
		super(state);
	}

	public void beginContact(Contact contact) {
		PlatformPlayer.beginContact(contact, this);
		
		if(compareCollision(contact, MyPlayer.class, EndLevel.class)) {
			MyPlayer player = getInstanceFromContact(contact, MyPlayer.class);
			player.nextLevel();
		}
		
		if(compareCollision(contact, Laser.class, Reflector.class)) {
			Laser laser = getInstanceFromContact(contact, Laser.class);
			float ang  = laser.getVelocity().scl(-1).nor().angle(contact.getWorldManifold().getNormal());
			Vector2 result= contact.getWorldManifold().getNormal().rotate(ang);
			
			laser.setVelocity(result);
		}
		
		if(compareCollision(contact, MyPlayer.class, DeathCompo.class)) {
			MyPlayer player = getInstanceFromContact(contact, MyPlayer.class);
			player.kill();
		}
		
		if(compareCollision(contact, Button.class, MyPlayer.class)){
			Button button = getInstanceFromContact(contact, Button.class);
			button.setBounded(true);
		}
		
		if(compareCollision(contact, Laser.class, LaserReceiver.class)) {
			Laser laser = getInstanceFromContact(contact, Laser.class);
			LaserReceiver receiver = getInstanceFromContact(contact, LaserReceiver.class);
			state.removeObject(laser);
			state.deleteBody(laser.getBody());
			receiver.fill();
		}
		
		if(compareCollision(contact, Laser.class, LaserEmitter.class)) {
			contact.setEnabled(false);
		}
		
		if(compareCollision(contact, Laser.class, Platform.class)) {
			if(!
				(getInstanceFromContact(contact, Platform.class) instanceof LaserEmitter)
				&& !(getInstanceFromContact(contact, Platform.class) instanceof Reflector)) {
				Laser laser = getInstanceFromContact(contact, Laser.class);
				state.removeObject(laser);
				state.deleteBody(laser.getBody());
			}
		}
	}

	public void endContact(Contact contact) {
		PlatformPlayer.endContact(contact, this);
		
		if(compareCollision(contact, Button.class, MyPlayer.class)){
			Button button = getInstanceFromContact(contact, Button.class);
			button.setBounded(false);
		}
	}

	public void preSolve(Contact contact, Manifold oldManifold) {
		if(compareCollision(contact, Laser.class, LaserEmitter.class)) {
			contact.setEnabled(false);
		}
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}

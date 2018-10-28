package com.mygdx.game.states;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.objects.EmptyContact;
import com.mygdx.game.objects.PlatformPlayer;
import com.mygdx.game.test.Canvas;
import com.mygdx.game.test.MyPlayer;
import com.mygdx.game.test.components.Button;
import com.mygdx.game.test.components.Cannon;
import com.mygdx.game.test.components.CannonBall;
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
		
		//Player termina a fase
		if(compareCollision(contact, MyPlayer.class, EndLevel.class)) {
			MyPlayer player = getInstanceFromContact(contact, MyPlayer.class);
			EndLevel endLevel = getInstanceFromContact(contact, EndLevel.class);
			TransitionState.nextPhase = endLevel.nextLevel;
			Canvas.levelToLoad = endLevel.nextLevel;
			player.nextLevel();
		}
		
		//Laser reflete
		if(compareCollision(contact, Laser.class, Reflector.class)) {
			Laser laser = getInstanceFromContact(contact, Laser.class);
			float ang  = laser.getVelocity().scl(-1).nor().angle(contact.getWorldManifold().getNormal());
			Vector2 result = contact.getWorldManifold().getNormal().rotate(ang);
			
			laser.setVelocity(result);
		}
		
		//Player morre
		if(compareCollision(contact, MyPlayer.class, DeathCompo.class)) {
			MyPlayer player = getInstanceFromContact(contact, MyPlayer.class);
			player.kill();
		}
		//Player morre
		if(compareCollision(contact, MyPlayer.class, Laser.class)) {
			MyPlayer player = getInstanceFromContact(contact, MyPlayer.class);
			player.kill();
		}
		
		//Laser é absorvido pelo receptor
		if(compareCollision(contact, Laser.class, LaserReceiver.class)) {
			Laser laser = getInstanceFromContact(contact, Laser.class);
			LaserReceiver receiver = getInstanceFromContact(contact, LaserReceiver.class);
			state.removeObject(laser);
			state.deleteBody(laser.getBody());
			receiver.fill();
		}
		
		//Laser é destruido quando encontra plataforma
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

	}

	public void preSolve(Contact contact, Manifold oldManifold) {
		//Laser atravessa emissor
		if(compareCollision(contact, Laser.class, LaserEmitter.class)) {
			contact.setEnabled(false);
		}
		
		//bola de canhão atravessa canhão
		if(compareCollision(contact, Cannon.class, CannonBall.class)) {
			contact.setEnabled(false);
		}
		
		//Player aperta o botão
		if(compareCollision(contact, Button.class, MyPlayer.class)){
			float angCos = contact.getWorldManifold().getNormal().angle();
			if(angCos > 85 && angCos < 95) {
				Button button = getInstanceFromContact(contact, Button.class);
				button.press();
			}
		}
		//Bola de canhão aperta o botão
		if(compareCollision(contact, Button.class, CannonBall.class)){
			float angCos = contact.getWorldManifold().getNormal().angle();
			if(angCos > 85 && angCos < 95) {
				Button button = getInstanceFromContact(contact, Button.class);
				button.press();
			}
		}
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}

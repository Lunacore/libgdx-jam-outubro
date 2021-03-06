package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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

	Sound reflect;
	
	public StateOneListener(State state) {
		super(state);
		reflect = Gdx.audio.newSound(Gdx.files.internal("audio/reflect.ogg"));
	}


	public void beginContact(Contact contact) {
		PlatformPlayer.beginContact(contact, this);
		
		//Player termina a fase
		if(compareCollision(contact, MyPlayer.class, EndLevel.class)) {
			EndLevel endLevel = getInstanceFromContact(contact, EndLevel.class);
			TransitionState.nextPhase = endLevel.nextLevel;
			TransitionState.direction = endLevel.direction;
			StateOne.spawnPoint = endLevel.spawnPoint;
			Canvas.levelToLoad = endLevel.nextLevel;
			((StateOne)state).nextLevel();
		}
		
		//Laser reflete
		if(compareCollision(contact, Laser.class, Reflector.class)) {
			Laser laser = getInstanceFromContact(contact, Laser.class);
			float ang  = laser.getVelocity().scl(-1).nor().angle(contact.getWorldManifold().getNormal());
			Vector2 result = contact.getWorldManifold().getNormal().rotate(ang);
			
			laser.setVelocity(result);
			reflect.play(0.1f);
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
		
		//Laser � absorvido pelo receptor
		if(compareCollision(contact, Laser.class, LaserReceiver.class)) {
			Laser laser = getInstanceFromContact(contact, Laser.class);
			LaserReceiver receiver = getInstanceFromContact(contact, LaserReceiver.class);
			state.removeObject(laser);
			state.deleteBody(laser.getBody());
			receiver.fill();
		}
		
		//Laser � destruido quando encontra plataforma
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
		
		//bola de canh�o atravessa canh�o
		if(compareCollision(contact, Cannon.class, CannonBall.class)) {
			contact.setEnabled(false);
		}
		
		//Player aperta o bot�o
		if(compareCollision(contact, Button.class, MyPlayer.class)){
			float angCos = contact.getWorldManifold().getNormal().angle();
			if(angCos > 85 && angCos < 95) {
				Button button = getInstanceFromContact(contact, Button.class);
				button.press();
			}
		}
		//Bola de canh�o aperta o bot�o
		if(compareCollision(contact, Button.class, CannonBall.class)){
			Button button = getInstanceFromContact(contact, Button.class);
			float bang = normalizeAngle((float) Math.toDegrees(button.getBody().getAngle()));
		
			float angCos = contact.getWorldManifold().getNormal().angle();
			System.out.println("Angle: " + bang + ", contact: " + angCos);

			if(angCos > bang + 85 && angCos < bang + 95) {
				button.press();
			}
		}
	}
	
	public float normalizeAngle(float angle) {
		
		while(angle < 0)
			angle += 360;
		
		angle = angle % 360;
		return angle;
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}

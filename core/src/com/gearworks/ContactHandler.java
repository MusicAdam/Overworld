package com.gearworks;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactHandler implements ContactListener{

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		if(fixA == null || fixB == null || fixA.getUserData() == null || fixB.getUserData() == null) return;
		
		Entity entA = (Entity)fixA.getUserData();
		Entity entB = (Entity)fixB.getUserData();
		
		entA.beginContactWith(fixA, fixB, contact);
		entB.beginContactWith(fixB, fixA, contact);
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		contact.setTangentSpeed(0);
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}

package com.gearworks.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gearworks.Entity;
import com.gearworks.Game;

public class LevelBounds extends Entity {

	public LevelBounds(Game cRef) {
		super(cRef);
	}
	
	@Override
	public void spawn(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		
		body = game.world().createBody(bodyDef);
		
		float screenWidth = Gdx.graphics.getWidth() * Game.WORLD_TO_BOX;
		float screenHeight = Gdx.graphics.getHeight() * Game.WORLD_TO_BOX;
		
		FixtureDef lineDef = new FixtureDef();
		lineDef.density = 1.0f;
		lineDef.friction = 0.1f;
		lineDef.restitution = 0.0f;
		
		EdgeShape topLine = new EdgeShape();
		topLine.set(0, screenHeight, screenWidth,screenHeight);
		
		lineDef.shape = topLine;
		body.createFixture(lineDef);
		
		EdgeShape leftLine = new EdgeShape();
		leftLine.set(0, screenHeight, 0, 0);
		
		lineDef.shape = leftLine;
		body.createFixture(lineDef);
		
		EdgeShape bottomLine = new EdgeShape();
		bottomLine.set(0, 0, screenWidth, 0);
		
		lineDef.shape = bottomLine;
		body.createFixture(lineDef);
		
		EdgeShape rightLine = new EdgeShape();
		rightLine.set(screenWidth, 0, screenWidth, screenHeight);
		
		lineDef.shape = rightLine;
		body.createFixture(lineDef);
		
		
		
	}

}

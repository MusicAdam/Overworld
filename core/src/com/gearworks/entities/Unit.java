package com.gearworks.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gearworks.Entity;
import com.gearworks.Game;
import com.gearworks.Player;
import com.gearworks.Utils;

public class Unit extends Entity {
	private Player owner;
	private Texture texture;
	private Vector2 destination;
	
	public Unit(Player owner, Game cRef) {
		super(cRef);
		
		this.owner = owner;
		selectable(true);
	}
	
	
		
	@Override
	public void render(SpriteBatch batch, ShapeRenderer r){	
		if(debug){
			if(destination != null){
				Utils.drawLine(r, Color.GREEN, position().x, position().y, destination.x, destination.y);
			}
		}
		
		batch.begin();
		sprite.draw(batch);
		batch.end();
		
		super.render(batch, r);
	}
	
	@Override
	public void update(){
		super.update();
		
		if(destination != null){
			for(Fixture f : body.getFixtureList()){
				f.setFriction(0.1f);
			}
			
			float speed = 1;
			Vector2 deltaNorm = destination.cpy().sub(position()).nor();
			float newX = position().x + speed * deltaNorm.x;
			float newY = position().y + speed * deltaNorm.y;
			position(newX, newY);
			
			if(position().epsilonEquals(destination, speed)){
				destination = null;
			}
		}else{
			//Give the fixtures an obscene amount of friction so they don't move. Resetting could be a problem if a different friction is set initially
			for(Fixture f : body.getFixtureList()){
				f.setFriction(10);
			}
		}
	}
	
	public void moveTo(Vector2 point){
		destination = point;
		float angle = Utils.radToDeg((float)Math.atan2(point.y - position().y, point.x - position().x));
		rotation(angle);
	}
	
	@Override
	public void spawn(){
		sprite = new Sprite(new Texture(Gdx.files.internal("debug_square.png")));
		sprite.setOriginCenter();
		
		//Create body def
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(100 * Game.WORLD_TO_BOX, 100 * Game.WORLD_TO_BOX);
		
		//Create body
		body = game.world().createBody(bodyDef);
		
		//Create Fixture
		CircleShape unitShape = new CircleShape();
		unitShape.setRadius((sprite.getWidth()/2) * Game.WORLD_TO_BOX );
		
		FixtureDef fix = new FixtureDef();
		fix.shape = unitShape;
		fix.density = 1.0f;
		fix.friction = 0.1f;
		fix.restitution = 0.0f;
		
		Fixture fixture = body.createFixture(fix);
		fixture.setUserData(this);
		
		unitShape.dispose();
	}
}

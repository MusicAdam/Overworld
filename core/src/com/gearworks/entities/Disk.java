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

public class Disk extends Entity {
	private Player owner;
	private Vector2 destination;
	
	private float impulseDelay			= .5f; //Delay between impulses in seconds;
	private float timeSinceLastImpulse 	= impulseDelay; //Initialize with the delay so that an impulse can be applied immediately.
	private Vector2 impulseDirection	= null;		//Impulse is applied in this direction when this is not null (requried to sync IO & box2d)
	private float impulseMagnitude		= .2f;	//Magnitude of the impulse
	private float drag					= 10f;
	private float turnDelay				= .5f; //Delay between turns in seconds
	private float timeSinceLastTurn		= turnDelay; 
	private Vector2 turnToDirection		= null;
	private float angularDamping 		= .2f; //Angular damping;
	private float bumperAngle			= 80; //Bumper half angle in degrees
	
	//Collision information
	private float bumperForceRatio		= .2f; //The fraction of the force applied to bumper collision
	private float nonBumperForceRatio   = 1 - bumperForceRatio;
	private float bumperDamageRatio     = 0f; //The fraction of the damage applied when bumper is hit
	private float nonBumperDamageRatio 	= 1 - bumperDamageRatio;
	private boolean calculatePostsolve	= false; //When true the force ratio will be calculated
	
	private float baseHp				= 100; //Base helath
	private float hp					= baseHp;
	private float baseDamageMod			= 20; //Multiplies base damage
	
	
	public Disk(Player owner, Game cRef) {
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
		
		timeSinceLastImpulse += Game.STEP;
		timeSinceLastTurn    += Game.STEP;
		
		//Apply impulse after impulse time step to provide more accurate delta time
		if(impulseDirection != null){
			body().setLinearVelocity(new Vector2()); //Zero out the velocity first so disk moves in correct direction.
			body().applyLinearImpulse(impulseMagnitude * impulseDirection.x, impulseMagnitude * impulseDirection.y, body().getPosition().x, body().getPosition().y, true);
			timeSinceLastImpulse = 0;
			impulseDirection = null;
		}
		
		//Apply drag if moving
		if(body().getLinearVelocity().len() > 0){
			body().applyForceToCenter(body().getLinearVelocity().scl(-1/drag), true);
		}
		
		//Apply turn after time step
		if(turnToDirection != null){
			body().setAngularVelocity(0); //Zero angular velocity 
			rotation(turnToDirection.angle());
			turnToDirection = null;
			timeSinceLastTurn = 0;
		}
	}
	
	public void applyImpulse(Vector2 dir){
		if(!canApplyImpulse()) return;
		
		impulseDirection = dir;
	}
	
	public boolean canApplyImpulse(){
		return (timeSinceLastImpulse >= impulseDelay);
	}
	
	public void turnTo(Vector2 dir){
		if(!canTurnTo()) return;
		
		turnToDirection = dir;
	}
	
	public boolean canTurnTo(){
		return (timeSinceLastTurn >= turnDelay);
	}
	
	@Override
	public void spawn(){
		sprite = new Sprite(new Texture(Gdx.files.internal("disk.png")));
		sprite.setOriginCenter();
		
		//Create body def
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(100 * Game.WORLD_TO_BOX, 100 * Game.WORLD_TO_BOX);
		
		//Create body
		body = game.world().createBody(bodyDef);
		body.setAngularDamping(angularDamping);
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
	
	//This function only checks the angle of the point relative to the position of the body. It does not check that the point is contained within a bodies' fixture
	public boolean worldPointIsOnBumper(Vector2 point){
		Vector2 dir = point.cpy().sub(position()).nor();
		float deltaAngle = rotation() - dir.angle();
		if(deltaAngle >= -bumperAngle && deltaAngle <= bumperAngle){
			return true;
		}
		return false;
	}
	
	@Override
	public void beginContactWith(Fixture myFix, Fixture otherFix, Contact contact){
		float damageRatio = nonBumperDamageRatio;
		
		for(Vector2 point : contact.getWorldManifold().getPoints()){
			if(myFix.testPoint(point)){
				if(worldPointIsOnBumper(point.scl(Game.BOX_TO_WORLD))){
					damageRatio = bumperDamageRatio;
					break;
				}
			}
		}
		
		float baseDamage = body().getLinearVelocity().len() + otherFix.getBody().getLinearVelocity().len();
		baseDamage *= Game.BOX_TO_WORLD * baseDamageMod;
		baseDamage /= baseHp;
		
		applyDamage(baseDamage * damageRatio);
		calculatePostsolve = true;
	}
	
	@Override 
	public void postsolveContactWith(Fixture myFix, Fixture otherFix, Contact contact){
		if(!calculatePostsolve) return;
		
		float forceRatio = nonBumperForceRatio;
		
		for(Vector2 point : contact.getWorldManifold().getPoints()){
			if(myFix.testPoint(point)){
				if(worldPointIsOnBumper(point.scl(Game.BOX_TO_WORLD))){
					forceRatio = bumperForceRatio;
					break;
				}
			}
		}
		
		body().setLinearVelocity(body().getLinearVelocity().scl(forceRatio));
		calculatePostsolve = false;
	}
	
	public void applyDamage(float dmg){
		hp -= dmg;

		if(hp < 0)
			hp = 0;
	}
}

package com.gearworks;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

//Should be shared 
public class Entity {
	public static boolean debug = false;
	
	public static final int COLLISION_NONE = 0;
	public static final int COLLISION_UNIT = 1;
	public static final int COLLISION_WALL = 2;
	
	public Game game;
	
	private boolean 	selectable = true;
	private boolean 	selected;
	protected Sprite 	sprite;
	protected float 	selectPadding = 5;
	protected Vector2 	direction;
	protected Body body;
	
	
	public Entity(Game cRef){
		game = cRef;
		direction = new Vector2(0, 1);
	}
	
	public Vector2 position(){
		return body.getPosition().scl(Game.BOX_TO_WORLD);
	}
	
	public void position(Vector2 p){ 
		body.setTransform(p.scl(Game.WORLD_TO_BOX), body.getAngle()); 
		body.setAwake(true); //This is to register collisions
	}
	
	public float rotation(){
		return Utils.radToDeg(body.getAngle());
	}
	
	public void rotation(float angle){
		body.setTransform(body.getPosition(), Utils.degToRad(angle));
		body.setAwake(true); //This is to register collisiosn
	}
	
	public void position(float x, float y){
		position(new Vector2(x, y));
	}
	
	public Rectangle getBounds(){
		return sprite.getBoundingRectangle();
	}
	
	public void render(SpriteBatch batch, ShapeRenderer r){		
		if(debug){
			float length = 10;
			Utils.drawRect(r, Color.GREEN, getBounds().x, getBounds().y, getBounds().width, getBounds().height);
			Utils.drawLine(r, Color.CYAN, position().x, position().y, position().x + length * direction.x, position().y + length * direction.y);
		}
		
		if(selected()){
			float aabbMinPaddedX = getBounds().x - selectPadding;
			float aabbMinPaddedY = getBounds().y - selectPadding;
			float aabbPaddedW =   getBounds().width + selectPadding * 2;
			float aabbPaddedH =  getBounds().height + selectPadding * 2;
			//Draw a box around the entity based on the aabb. Add padding to make the box bigger than the entity itself.
			Utils.drawRect(r, Color.GREEN, aabbMinPaddedX, aabbMinPaddedY, aabbPaddedW, aabbPaddedH);			
		}
	}
	public void update(){
		followPhysicsBody();
	}
	
	public void dispose(){}
	
	public void beginContactWith(Fixture myFix, Fixture otherFix, Contact contact){}
	public void presolveContactWith(Fixture myFix, Fixture otherFix, Contact contact){}
	public void postsolveContactWith(Fixture myFix, Fixture otherFix, Contact contact){}
	
	protected void followPhysicsBody(){
		if(sprite == null) return;
		if(body == null) return; 
		
		Vector2 position = position();
		sprite.setPosition(position.x - sprite.getWidth()/2, position.y - sprite.getHeight()/2);
		sprite.setRotation(rotation());
	}
	
	//Spawn is responsible for creating the physics body and sprite associated with this entity
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
		PolygonShape testBox = new PolygonShape();
		testBox.setAsBox((sprite.getWidth()/2) * Game.WORLD_TO_BOX, (sprite.getHeight()/2) * Game.WORLD_TO_BOX);
		
		FixtureDef fix = new FixtureDef();
		fix.shape = testBox;
		fix.density = 1.0f;
		fix.friction = 0.1f;
		fix.restitution = 0.0f;
		
		Fixture fixture = body.createFixture(fix);
		fixture.setUserData(this);
		
		testBox.dispose();
	}
	
	public Vector2 size(){ return new Vector2(sprite.getWidth(), sprite.getHeight()); }
	public float width(){ return sprite.getWidth(); }
	public float height(){ return sprite.getHeight(); }
	public void selectable(boolean s){ selectable = s; }
	public boolean selectable(){ return selectable; }
	public void selected(boolean s){ selected = s; }
	public boolean selected(){ return selected; }
	public Body body(){ return body; }
}

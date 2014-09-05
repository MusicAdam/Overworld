package com.gearworks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gearworks.entities.Unit;
import com.gearworks.game.Level;
import com.gearworks.state.GameState;
import com.gearworks.state.State;
import com.gearworks.state.StateManager;

public class Game implements ApplicationListener {
	public static final String 	TITLE = "Overworld";
	public static final int 	V_WIDTH = 800;
	public static final	int 	V_HEIGHT = 800;
	public static final float 	ASPECT_RATIO = (float)V_WIDTH/(float)V_HEIGHT;
	public static final int 	SCALE = 1;
	public static final float 	ZOOM = 5;
	public static final float 	WORLD_TO_BOX = 0.01f;
	public static final float   BOX_TO_WORLD = 100f;
	
	
	public static final float STEP = 1 / 60f;
	private float accum;
	
	public BitmapFont font;
	
	protected StateManager sm;
	
	private ScreenViewport viewport;
	private OrthographicCamera camera;
	private InputMultiplexer inputMultiplexer;
	private UserInterface ui;
	private ArrayList<Entity> entities;

	private SpriteBatch batch;
	private ShapeRenderer renderer;
	
	//Box2d stuff
	private World world;
	private Box2DDebugRenderer b2ddbgRenderer;
	
	@Override
	public void create() {	
		
		entities = new ArrayList<Entity>();
		
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		ui = new UserInterface(this);
		
		//Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		camera.update();
		
		viewport = new ScreenViewport(camera);
		
		//State Manager
		sm = new StateManager(this);
		
		font = new BitmapFont();
		font.setScale(.8f);
		font.setColor(Color.WHITE);
		
		batch = new SpriteBatch();	
		renderer = new ShapeRenderer();
		inputMultiplexer.addProcessor(ui);
		
		sm.setState(new GameState());
		
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new ContactHandler());
		
		b2ddbgRenderer = new Box2DDebugRenderer();
		
		Entity ent = spawn(new Unit(new Player(), this));
		ent.position(150,  150);
		spawn(new Unit(new Player(), this));
		
		
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {		

		//Render
		Gdx.gl.glClearColor(.21f, .21f, .21f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		accum += Gdx.graphics.getDeltaTime();
		while(accum >= STEP) {
			accum -= STEP;
			
			sm.update();
		}


		//camera.update();
		batch().setProjectionMatrix(camera().combined);
		sm.render();
		ui.render(batch, renderer);
		
		Matrix4 dbgMatrix = camera.combined.cpy().scl(BOX_TO_WORLD);
		b2ddbgRenderer.render(world, dbgMatrix);
		
		world.step(STEP, 6, 8);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public Entity spawn(Entity ent){
		if(state() == null) return ent;
		
		ent.spawn();
		entities.add(ent);
		
		return ent;
	}
	
	public void destroy(Entity ent){
		if(state() == null) return;
		
		ent.dispose();
		entities.remove(ent);
	}
	
	public ArrayList<Entity> entities() {
		return entities;
	}
	
	public OrthographicCamera camera(){ return camera; }
	public State state(){ return sm.state(); }
	public SpriteBatch batch() { return batch; }	
	public ShapeRenderer renderer() { return renderer; }
	public UserInterface ui(){ return ui; }
	public World world(){ return world; }
}

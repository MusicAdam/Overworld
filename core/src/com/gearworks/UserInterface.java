package com.gearworks;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class UserInterface implements InputProcessor{
	public static final float MAX_ZOOM = 1.2f;
	public static final float MIN_ZOOM = .08f;
	private Game game;
	
	private Character activeCharacter;
	private InputMapper inputMapper;
	
	float selectionPadding = 0f;
	
	//Takes a mouse coordinate and returns screen coordinates, does not alter original vector
	public static Vector2 mouseToScreen(Vector2 coord, Camera camera){
		Vector3 screenCoord = new Vector3(coord.x, coord.y, 0);
		camera.unproject(screenCoord);
		
		return new Vector2(screenCoord.x, screenCoord.y);
	}
	
	public UserInterface(Game game){
		this.game = game;
		inputMapper = new InputMapper();
	}

	@Override
	public boolean keyDown(int keycode) {
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false; 
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false; 
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		float zoom = game.camera().zoom + .1f * amount;
		if(zoom <= MAX_ZOOM && zoom >= MIN_ZOOM)
			game.camera().zoom = zoom;
		return false;
	}
	
	public void render(SpriteBatch batch, ShapeRenderer renderer){
		renderer.setProjectionMatrix(game.camera().combined);
		renderer.identity();
	}
	
	public void activeCharacter(Character c){ activeCharacter = c; }
	public Character activeCharacter(){ return activeCharacter; }

}

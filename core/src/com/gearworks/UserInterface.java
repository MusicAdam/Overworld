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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.gearworks.entities.Disk;

public class UserInterface implements InputProcessor{
	private Game game;
	
	public boolean debug_showContacts = true;
	
	private ArrayList<Entity> selected;
	//private Array<Group> groups;
	private Vector2 dragStart;
	private Vector2 dragPos;
	private InputMapper inputMap;
	
	float selectionPadding = 0f;

	//Takes a mouse coordinate and returns screen coordinates, does not alter original vector
	public static Vector2 screenToWorld(Vector2 coord, Camera cam){
		Vector3 screenCoord = new Vector3(coord.x, coord.y, 0);
		cam.unproject(screenCoord);
		return new Vector2(screenCoord.x, screenCoord.y);
	}
	
	public UserInterface(Game game){
		this.game = game;
		//groups = new Array<Group>();
		inputMap = new InputMapper();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.SPACE){
			Vector2 mousePos = screenToWorld(new Vector2(Gdx.input.getX(), Gdx.input.getY()), game.camera());
			Vector2 dir = mousePos.sub(game.player().disk().position()).nor();
			game.player().disk().applyImpulse(dir);
		}
		return false;
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
		return true; //This could interfere with menus in the future, unless this class handles the clicks...
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(button == 0){
			Vector2 mousePos = screenToWorld(new Vector2(screenX, screenY), game.camera());
			
			Vector2 dir = mousePos.sub(game.player().disk().position()).nor();
			game.player().disk().turnTo(dir);
		}else if(button == 1){
		}
		
		return true; //This could interfere with menus in the future, unless this class handles the clicks...
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	public void render(SpriteBatch batch, ShapeRenderer renderer){
		renderer.setProjectionMatrix(game.camera().combined);
		renderer.identity();
	}

}

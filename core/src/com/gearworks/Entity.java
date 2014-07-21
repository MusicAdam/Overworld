package com.gearworks;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

//Should be shared 
public class Entity {	
	public Game game;
	
	private Vector2 size;
	private Vector2 position;
	private boolean selectable = true;
	
	public Entity(Game cRef){
		game = cRef;
		size = new Vector2();
		position = new Vector2();
	}
	
	public Vector2 position(){
		return position;
	}
	
	public void position(Vector2 p){ position = p; }
	
	public Vector2 rotation(){
		return new Vector2();
	}
	
	public void rotation(Vector2 r){
		//TODO: This should be used to indicate the direction this is facing
		//		(1, 0) 	- Right
		//		(-1, 0)	- Left
		//		(0, 1) 	- Up
		//		(0, -1) - Down
	}
	
	public void position(float x, float y){
		position = new Vector2(x,y);
	}
	
	public void render(SpriteBatch batch, ShapeRenderer r){}
	public void update(){}
	
	public void dispose(){}
	
	public Vector2 size(){ return size; }
	public void size(Vector2 s){ size = s; }
	public void size(float x, float y){ size.x = x; size.y = y; }
	public float width(){ return size.x; }
	public void width(float w){ size.x = w; }
	public float height(){ return size.y; }
	public void height(float h){ size.y = h; }
	public void selectable(boolean s){ selectable = s; }
	public boolean selectable(){ return selectable; }
}

package com.gearworks;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

//Should be shared 
public class Entity {
	public static boolean debug = false;
	
	public Game game;
	
	private Vector2 size;
	private Vector2 position;
	private BoundingBox aabb;
	private boolean selectable = true;
	private boolean shouldUpdateBounds = false; //When set to true, aabb will update next update loop
	
	public Entity(Game cRef){
		game = cRef;
		size = new Vector2();
		position = new Vector2();
		aabb = new BoundingBox();
	}
	
	public Vector2 position(){
		return position;
	}
	
	public void position(Vector2 p){ position = p; invalidateBounds(); }
	
	public Vector2 rotation(){
		return new Vector2();
	}
	
	public void rotation(Vector2 r){
		//TODO: This should be used to indicate the direction this is facing
		//		(1, 0) 	- Right
		//		(-1, 0)	- Left
		//		(0, 1) 	- Up
		//		(0, -1) - Down
		invalidateBounds();
	}
	
	public void position(float x, float y){
		position = new Vector2(x,y);
		invalidateBounds();
	}
	
	public void invalidateBounds(){
		shouldUpdateBounds = true;
	}
	
	private void updateBounds(){
		BoundingBox box = new BoundingBox();
		box.set(  new Vector3(	position().x,
							 	position().y,
							 	0f),
				  new Vector3( 	position().x + size().x,
								position().y + size().y,
								0f));
		aabb = box;
	}
	
	public void render(SpriteBatch batch, ShapeRenderer r){
		if(debug){
			Utils.drawRect(r, Color.GREEN, aabb.min.x, aabb.min.y, aabb.max.x - aabb.min.x, aabb.max.y - aabb.min.y);
		}
	}
	public void update(){
		if(shouldUpdateBounds){
			updateBounds();
			shouldUpdateBounds = false;
		}
	}
	
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

package com.gearworks.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gearworks.Entity;
import com.gearworks.Game;
import com.gearworks.Player;
import com.gearworks.Utils;

//A unit the base selectable/controllable entity in the game. It also belongs to a player. 
public class Unit extends Entity {
	private Player owner;
	private boolean selected;
	
	public Unit(Player owner, Game cRef) {
		super(cRef);
		
		this.owner = owner;
		selected = false;
		selectable(true);
		size(30, 30); //Default size;
		position(100, 100);
	}
	
	public void selected(boolean s){ selected = s; }
	public boolean selected(){ return selected; }
	
		
	@Override
	public void render(SpriteBatch batch, ShapeRenderer renderer){
		Utils.fillRect(renderer, Color.RED, position().x, position().y, size().x, size().y);
		
		super.render(batch, renderer);
	}
	
	@Override
	public void update(){
		super.update();
		
		position(position().x + .01f, position().y);
	}
}

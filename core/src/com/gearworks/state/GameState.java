package com.gearworks.state;

import com.gearworks.Entity;
import com.gearworks.Game;

public class GameState implements State {

	@Override
	public void render(Game game) {
		for(Entity e : game.entities()){
			e.render(game.batch(), game.renderer());
		}
	}

	@Override
	public void update(Game game) {
		for(Entity e : game.entities()){
			e.update();
		}
	}

	@Override
	public void onEnter(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExit(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canEnterState(Game game) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canExitState(Game game) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}

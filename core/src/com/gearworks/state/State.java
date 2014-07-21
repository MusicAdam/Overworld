package com.gearworks.state;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gearworks.Game;

public interface State {
	public void render(Game game);
	public void update(Game game);
	public void onEnter(Game game);
	public void onExit(Game game);
	public boolean canEnterState(Game game);
	public boolean canExitState(Game game);
	public int getId();
}

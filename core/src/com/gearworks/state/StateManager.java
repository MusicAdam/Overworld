package com.gearworks.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gearworks.Game;

public class StateManager {
	protected State state;
	protected Game game;
	
	//Compares state id's
	public static boolean statesEqual(State state, State cState){
		if(state == null || cState == null) return false;
		if(state.getId() == cState.getId()){
			return true;
		}
		
		return false;
	}
	
	public StateManager(Game clientRef){
		this.game = clientRef;
	}
	
	public void update(){
		if(state != null)
			state.update(game);
	}
	
	public void render(){
		if(state != null)
			state.render(game);
	}
	
	public boolean setState(State toState){
		//Don't change states if they are the same
		if(statesEqual(state, toState)) return false;
		
		if(state == null && toState.canEnterState(game)){
			state = toState;
		}else if(state.canExitState(game) && toState.canEnterState(game)){
			state.onExit(game);
			state = toState;
		}
		
		if(state !=null)
			state.onEnter(game);
		
		return true;
	}
	
	public State state(){ return state; }
	
}

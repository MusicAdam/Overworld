package com.gearworks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gearworks.Game;

public class Player {
	
	public enum Team{
		Humans,
		Demons
	}
	
	public enum Type{
		Human,
		AI
	}
	
	protected Team	team;				
	private int 	instanceId;			//Unique instace id assigned by the server once a connection has been made
	
	public Player(){
		instanceId = -1;
	}
	
	public int instanceId(){ return instanceId; }
	public void instanceId(int id){ instanceId = id; }
	public Team team(){ return team; }
	public void team(Team t){ team = t; }
}

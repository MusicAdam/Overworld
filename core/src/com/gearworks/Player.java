package com.gearworks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gearworks.Game;
import com.gearworks.entities.Disk;

public class Player {	
	public Game game;
	
	private int 	instanceId;			//Unique instace id assigned by the server once a connection has been made
	private Disk 	disk;
	
	public Player(Game game){
		instanceId = -1;
		this.game = game;
	}
	
	public void spawnDisk(){
		//Destroy old disk if it exists
		if(disk != null)
			game.destroy(disk);
		disk = (Disk)game.spawn(new Disk(this, game));
	}
	
	public int instanceId(){ return instanceId; }
	public void instanceId(int id){ instanceId = id; }
	public Disk disk(){ return disk; }
}

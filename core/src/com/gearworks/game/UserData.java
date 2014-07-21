package com.gearworks.game;

import java.util.HashMap;
import java.util.Map;

/** Used to store data to a fixture, will eventually be used to save state of game */
public class UserData {
	public EntityType 	type;
	private Map<String, Object> properties;
	
	public UserData(){
		properties = new HashMap<String, Object>();
	}
	
	public void property(String key, Object value){
		properties.put(key, value);
	}
	
	public Object property(String key){
		return properties.get(key);
	}
}
